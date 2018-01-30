package com.bandary.dcnet.ui;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityCtrlOrder;
import com.bandary.dcnet.entity.EntityKTCtrl;
import com.bandary.dcnet.entity.EntityKTInfo;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.service.KTService;
import com.bandary.dcnet.uihandler.ExitApplication;
import com.bandary.dcnet.uihandler.UserSharePreferencesHandler;
import com.bandary.dcnet.utils.GetPhonePriorityHelper;
import com.bandary.dcnet.utils.HexConvertHelper;

/**
 * 说明： 本界面作为空调控制的详细界面，主要组件和属性 1.要能显示当前选中的空调详细信息，并能及时更新界面，离线，开机，关机 三种状态，显示的模式不一样
 * 2.能随时控制，不过离线状态下要禁止控制数据下发 3.上一台/下一台的切换也要更新
 * 
 * 显示数据：名称 设温 室温 模式 风速 锁定 故障状态 开关 通讯状态 操作数据:开关 模式 风速 设温 锁定 隐藏数据：mac socket号
 * 声明所有变化字段
 * 
 * 名称，mac地址,通讯状态，室温，模式，风速，开关机状态，socket连接号,总共8个字段会传递过来
 */
@InjectLayer(value = R.layout.activity_detail_kt, parent = R.id.LLO_centerframe)
public class DetailKTActivity extends BaseActivity {
	private boolean isConnect;// 是否连接到主机
	private boolean isRefreshing;// 是否非第一次加载
	private boolean isSubmit; // 延时提交意图
	private int currentIndex;// 当前显示的设备对象在缓存中的索引
	private EntityKTInfo currentktInfo;// 远程服务器下来就是经过转换的本地语言
	private EntityKTCtrl entityKTCtrl;// 控制代码对象
	private EntityCtrlOrder entityCtrlOrder;// 上传的控制命令对象
	private int msettemp = ConstantHelper.DEFAULT_TEMPERATURE;// 默认的设置温度
	// 常量
	private final static String tag = "DetailKTActivity";
	//异步操作对象
	private GetRemoteKTInfoAsyncTask getRemoteKTInfoAsyncTask;
	private KTCtrlAsyncTask ktCtrlAsyncTask;
	
	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	public boolean isSubmit() {
		return isSubmit;
	}

	public void setSubmit(boolean isSubmit) {
		this.isSubmit = isSubmit;
	}

	public boolean isRefreshing() {
		return isRefreshing;
	}

	public void setRefreshing(boolean isRefreshing) {
		this.isRefreshing = isRefreshing;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public EntityKTInfo getCurrentktInfo() {
		return currentktInfo;
	}

	public void setCurrentktInfo(EntityKTInfo currentktInfo) {
		this.currentktInfo = currentktInfo;
	}

	public EntityKTCtrl getEntityKTCtrl() {
		return entityKTCtrl;
	}

	public void setEntityKTCtrl(EntityKTCtrl entityKTCtrl) {
		this.entityKTCtrl = entityKTCtrl;
	}

	public EntityCtrlOrder getEntityCtrlOrder() {
		return entityCtrlOrder;
	}

	public void setEntityCtrlOrder(EntityCtrlOrder entityCtrlOrder) {
		this.entityCtrlOrder = entityCtrlOrder;
	}

	public int getMsettemp() {
		return msettemp;
	}

	public void setMsettemp(int msettemp) {
		this.msettemp = msettemp;
	}
	
	private Button btn_pannel_up;// 上一个
	private Button btn_pannel_down;// 下一个
	private Button btn_refresh;// refresh button

	private TextView txt_kt_detail_fname;// 名称
	private TextView txt_kt_detail_fcommstate;// 通讯状态
	private TextView txt_kt_detail_fstate;// 运行状态
	private TextView txt_kt_detail_ftrouble;// 故障状态

	private TextView txt_kt_detail_froomtemp;// 室内温度
	private TextView txt_kt_detail_fsettemp;// 设置温度
	private Button btn_kt_detail_ftempup;// 温度自增
	private Button btn_kt_detail_ftempdown;// 温度自减

	private Button btn_kt_detail_fsetcool;// 制冷
	private Button btn_kt_detail_fsetheat;// 制热
	private Button btn_kt_detail_fsetfan;// 风扇
	private Button btn_kt_detail_fsetauto;// 自动

	private Button btn_kt_detail_fsetfanlow;// 低风
	private Button btn_kt_detail_fsetfanmid;// 中风
	private Button btn_kt_detail_fsetfanhigh;// 高风
	private Button btn_kt_detail_fsetfanauto;// 风扇自动
	private Button btn_kt_detail_power;
	
	
	/*-------------------------declare view-------------------------------------------------*/
	/**
	 * * 消息处理：根据消息读取服务器数据并进行刷新
	 * 
	 * */
	public Handler autoRefreshHander = new Handler() {
		@Override
		public void handleMessage(Message message) {
			boolean value = message.getData().getBoolean("isRefreshing");
			if (value) {
				// Log.i(tag, "this is loop thread:get data from server");
				setRefreshing(true);
				getRemoteKTInfo(getCurrentktInfo());
			}
		}

	};	

	@InjectInit
	public void init() {		
		initView();
		this.getBaseHander().setMusid(UserSharePreferencesHandler.getUserIDFromLocal(getApplicationContext()));
		setCurrentIndex(initIndex());
		setCurrentktInfo(getCaCheCurrentInfo(getCurrentIndex(),
				this.getBaseHander().getMusid()));
		displayCaCheCurrentInfo(getCurrentktInfo());
		// 初始化一个定时器
		// 读取后台数据，并显示到界面
		// 注册按钮事件
		// 定时器定时执行
		getRemoteKTInfo(getCurrentktInfo());
		registerViewListenerEvent();
	}

	/**
	 * 获取当前选择设备在list列表中的索引
	 * */
	private int initIndex() {		
		int index = getIntent().getIntExtra(ConstantHelper.INDEX, ConstantHelper.DEFAULT_INT_VALUE);
		//Log.i(tag, "initIndex:"+index);
		return index;
	}

	/**
	 * 从全局List里面获取缓存的空调详细信息，取出来的时候要做部分字段的转换，所以取方法公用一个。
	 * */
	private EntityKTInfo getCaCheCurrentInfo(int index, String usid) {
		EntityKTInfo info = ConstantHelper.getGlobal_ktInfolist().get(
				getCurrentIndex());
		info.setUsid(usid);
		// 模式和风速在列表界面还是文字，详细界面上使用的是数字代码
		for (int i = 0; i < ConstantHelper.ZN_Array_KT_fmode.length; i++) {
			if (ConstantHelper.ZN_Array_KT_fmode[i].equals(info.getfModel())) {
				info.setfModel(i + ConstantHelper.DEFAULT_STRING_NULL);
				break;
			}
		}
		info.setFsetspeed(ConstantHelper.DEFAULT_STRING_NULL);
		return info;
	}

	// 初始化控 件
	// 注册控件事件

	private void initView() {
		// 该方法确保只调用一次进行初始化
		ExitApplication.getInstanse().addActivity(this);
		btn_pannel_up = (Button) findViewById(R.id.btn_pannel_up);// 上一个
		btn_pannel_down = (Button) findViewById(R.id.btn_pannel_down);// 下一个
		btn_refresh = (Button) findViewById(R.id.btn_detail_kt_refresh);// 刷新
		txt_kt_detail_fname = (TextView) findViewById(R.id.txt_kt_detail_fname);// 名称
		txt_kt_detail_fcommstate = (TextView) findViewById(R.id.txt_kt_detail_fcommstate);// 通讯状态
		txt_kt_detail_fstate = (TextView) findViewById(R.id.txt_kt_detail_fstate);
		txt_kt_detail_ftrouble = (TextView) findViewById(R.id.txt_kt_detail_ftrouble);
		txt_kt_detail_froomtemp = (TextView) findViewById(R.id.txt_kt_detail_froomtemp);// 室内温度
		txt_kt_detail_fsettemp = (TextView) findViewById(R.id.txt_kt_detail_fsettemp);// 室内温度
		btn_kt_detail_ftempup = (Button) findViewById(R.id.btn_kt_detail_ftempup);
		btn_kt_detail_ftempdown = (Button) findViewById(R.id.btn_kt_detail_ftempdown);
		btn_kt_detail_fsetcool = (Button) findViewById(R.id.btn_kt_detail_fsetcool);
		btn_kt_detail_fsetheat = (Button) findViewById(R.id.btn_kt_detail_fsetheat);
		btn_kt_detail_fsetfan = (Button) findViewById(R.id.btn_kt_detail_fsetfan);
		btn_kt_detail_fsetauto = (Button) findViewById(R.id.btn_kt_detail_fsetauto);
		btn_kt_detail_fsetfanlow = (Button) findViewById(R.id.btn_kt_detail_fsetfanlow);
		btn_kt_detail_fsetfanmid = (Button) findViewById(R.id.btn_kt_detail_fsetfanmid);
		btn_kt_detail_fsetfanhigh = (Button) findViewById(R.id.btn_kt_detail_fsetfanhigh);
		btn_kt_detail_fsetfanauto = (Button) findViewById(R.id.btn_kt_detail_fsetfanauto);
		btn_kt_detail_power = (Button) findViewById(R.id.btn_kt_detail_power);
	}
	// 自动定时刷新数据
	// 上一台 下一台
	public void registerViewListenerEvent() {
		btn_pannel_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int pre = getCurrentIndex() - 1;
				if (pre >= 0
						&& ConstantHelper.getGlobal_ktInfolist().get(pre) != null) {
					setCurrentIndex(pre);
					setCurrentktInfo(getCaCheCurrentInfo(
							getCurrentIndex(), getBaseHander().getMusid()));
					setRefreshing(false);
					displayCaCheCurrentInfo(getCurrentktInfo());
					getRemoteKTInfo(getCurrentktInfo());
				} else {
					MakeToast(ConstantHelper.msg_detailkt_isFirstOne);
					// print currentIndex
					return;
				}
			}
		});
		btn_pannel_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int next = getCurrentIndex() + 1;
				if (next >= 0
						&& next < ConstantHelper.getGlobal_ktInfolist().size()
						&& ConstantHelper.getGlobal_ktInfolist().get(next) != null) {
					setCurrentIndex(next);
					setCurrentktInfo(getCaCheCurrentInfo(
							getCurrentIndex(), getBaseHander().getMusid()));
					setRefreshing(false);
					displayCaCheCurrentInfo(getCurrentktInfo());
					getRemoteKTInfo(getCurrentktInfo());
				} else {
					MakeToast(ConstantHelper.msg_detailkt_isLastOne);
					return;
				}
			}
		});

		// 手动刷新
		btn_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setRefreshing(true);
				getRemoteKTInfo(getCurrentktInfo());
				MakeToast(ConstantHelper.msg_global_refresh_success);
			}
		});

		btn_kt_detail_ftempup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});

		btn_kt_detail_ftempdown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetcool.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetheat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetfan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetauto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetfanlow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetfanmid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetfanhigh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_fsetfanauto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
		btn_kt_detail_power.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doOperate(v);
			}
		});
	}

	/*--------------------------全局事件开始----------------------------------------------*/
	/**
	 * 异步任务读取空调详细信息
	 * */
	public void getRemoteKTInfo(EntityKTInfo info) {
		// 异步读取该空调的网络服务器数据
		// 异步读远程服务器资料写到数据库
		// Log.i(tag, info.toString());
		getRemoteKTInfoAsyncTask = new GetRemoteKTInfoAsyncTask();
		getRemoteKTInfoAsyncTask.execute(info.getUsid(), info.getfMac());
	}

	/**
	 * 异步任务下发控制任务
	 * */
	public void writeRemoteCtrl(EntityCtrlOrder item) {
		ktCtrlAsyncTask = new KTCtrlAsyncTask();
		ktCtrlAsyncTask.execute(item);
	}

	/**
	 * 将缓存的空调信息显示到UI
	 * */
	public void displayCaCheCurrentInfo(EntityKTInfo ktInfo) {
		if (!isConnect()) {
			MakeToast(ConstantHelper.msg_global_DisConnect);
			changeUI_offline();
			return;
		}
		setMsettemp(HexConvertHelper.stringToInteger(
				ktInfo.getFsettemp(), ConstantHelper.DEFAULT_TEMPERATURE));
		// 第一次加载时显示
		txt_kt_detail_fname.setText(getCurrentktInfo().getFname());
		// 表示手机网络联通
		if (ConstantHelper.ZN_Array_KT_fcommstate[0].equals(ktInfo
				.getfCommstate())) {
			changeUI_offline();
			return;
		} else if (ConstantHelper.ZN_Array_KT_fcommstate[1].equals(ktInfo
				.getfCommstate())
				&& ConstantHelper.ZN_Array_KT_fstate[1].equals(ktInfo
						.getfState())) {
			changeUI_online_poweron(ktInfo);
			return;
		} else if (ConstantHelper.ZN_Array_KT_fcommstate[1].equals(ktInfo
				.getfCommstate())
				&& ConstantHelper.ZN_Array_KT_fstate[0].equals(ktInfo
						.getfState())) {
			changeUI_online_poweroff(ktInfo);
			return;
		} else {
			changeUI_offline();
			MakeToast(ConstantHelper.msg_global_unknown_data
					+ getCurrentktInfo().getfCommstate()
					+ ConstantHelper.space_key
					+ getCurrentktInfo().getfState());
			return;
		}
	}

	/**
	 * 操作按钮事件
	 * */
	public void doOperate(View view) {
		// 网络没连接或设备不在线都不能下发操作
		if (!isConnect()) {
			// 不允许任何操作,因为设备离线
			MakeToast(ConstantHelper.msg_global_DisConnect);
			return;
		} else if (ConstantHelper.ZN_Array_KT_fcommstate[0].equals(getCurrentktInfo().getfCommstate())) {
			MakeToast(ConstantHelper.msg_global_OffLine);
			return;
		} else if (isConnect()
				&& ConstantHelper.ZN_Array_KT_fcommstate[1].equals(getCurrentktInfo().getfCommstate())) {
			boolean isLongWarning = false;
			setEntityKTCtrl(new EntityKTCtrl());
			switch (view.getId()) {
			case R.id.btn_kt_detail_power:
				if (ConstantHelper.ZN_Array_KT_fstate[1].equals(getCurrentktInfo().getfState())) {
					// 修改全局变量，用于更新UI
					getCurrentktInfo().setfState(
							ConstantHelper.ZN_Array_KT_fstate[0]);
					// 设置控制参数
					getEntityKTCtrl().setWstate("0000");
					isLongWarning = true;
				} else {
					getCurrentktInfo().setfState(
							ConstantHelper.ZN_Array_KT_fstate[1]);
					getEntityKTCtrl().setWstate("0001");
				}
				break;
			case R.id.btn_kt_detail_ftempup:
				if (getMsettemp() >= ConstantHelper.SCOPE_KT_SETTEMP_MAX) {
					return;
				}
				setMsettemp(getMsettemp() + 1);// 温度自增
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setFsettemp(
						getMsettemp()
								+ ConstantHelper.DEFAULT_STRING_NULL);
				getEntityKTCtrl().setWsettemp(
						HexConvertHelper.writeTemp(getMsettemp()));
				break;
			case R.id.btn_kt_detail_ftempdown:
				// 最低不低于0
				if (getMsettemp() <= ConstantHelper.SCOPE_KT_SETTEMP_MIN) {
					break;
				}
				setMsettemp(getMsettemp() - 1);// 温度自增
				// 消息给出
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setFsettemp(
						getMsettemp()
								+ ConstantHelper.DEFAULT_STRING_NULL);
				getEntityKTCtrl().setWsettemp(
						HexConvertHelper.writeTemp(getMsettemp()));
				break;
			case R.id.btn_kt_detail_fsetcool:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setfModel(
						ConstantHelper.code_read_ktinfo_model_setcool);
				getEntityKTCtrl().setWmodel(
						ConstantHelper.code_write_ktinfo_wmodel_setcool);
				break;
			case R.id.btn_kt_detail_fsetheat:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setfModel(
						ConstantHelper.code_read_ktinfo_model_setheat);
				getEntityKTCtrl().setWmodel(
						ConstantHelper.code_write_ktinfo_wmodel_setheat);
				break;
			case R.id.btn_kt_detail_fsetfan:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setfModel(
						ConstantHelper.code_read_ktinfo_model_setfan);
				getEntityKTCtrl().setWmodel(
						ConstantHelper.code_write_ktinfo_wmodel_setfan);
				break;
			case R.id.btn_kt_detail_fsetauto:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setfModel(
						ConstantHelper.code_read_ktinfo_model_setauto);
				getEntityKTCtrl().setWmodel(
						ConstantHelper.code_write_ktinfo_wmodel_setauto);
				break;
			case R.id.btn_kt_detail_fsetfanlow:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setFsetspeed(
						ConstantHelper.code_read_ktinfo_fspeed_setlow);
				getEntityKTCtrl().setWsetspeed(
						ConstantHelper.code_write_ktinfo_wfspeed_setlow);
				break;
			case R.id.btn_kt_detail_fsetfanmid:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setFsetspeed(
						ConstantHelper.code_read_ktinfo_fspeed_setmid);
				getEntityKTCtrl().setWsetspeed(
						ConstantHelper.code_write_ktinfo_wfspeed_setmid);
				break;
			case R.id.btn_kt_detail_fsetfanhigh:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setFsetspeed(
						ConstantHelper.code_read_ktinfo_fspeed_sethigh);
				getEntityKTCtrl().setWsetspeed(
						ConstantHelper.code_write_ktinfo_wfspeed_sethigh);
				break;
			case R.id.btn_kt_detail_fsetfanauto:
				// 修改全局变量，用于更新UI
				getCurrentktInfo().setFsetspeed(
						ConstantHelper.code_read_ktinfo_fspeed_setauto);
				getEntityKTCtrl().setWsetspeed(
						ConstantHelper.code_write_ktinfo_wfspeed_setauto);
				break;
			default:
				break;
			}
			setEntityCtrlOrder(EntityCtrlOrder.getInstance()
					.getCtrlPostParam(
							getCurrentktInfo().getfNetSocketNo(),
							getCurrentktInfo().getfMac(),
							getEntityKTCtrl()));
			// 3.变更UI
			// 立即异步更新UI
			displayCaCheCurrentInfo(getCurrentktInfo());
			// 4.调用手机震动
			GetPhonePriorityHelper.useVibrator(GetPhonePriorityHelper.getPhoneVibrator(DetailKTActivity.this), isLongWarning);
			// 5.延时1秒提交到服务器			
			if (isSubmit() == false) {
				// 用一个全局变量保存控制命令，新的线程经过一段延时后，执行的永远是延时后最新的控制命令，逻辑原理就是最后一条保存在全局里面，经过一段时间后，执行的是全局变量里最新的记录
				setSubmit(true);// 意图提交
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(ConstantHelper.delay_submit_second);// 延时
							writeRemoteCtrl(getEntityCtrlOrder());
							setSubmit(false);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			}
		}
	}

	/**
	 * 根据数据值改变模式按钮的背景 互斥关系 model 1435分别代表制冷 制热 风扇 自动
	 * */
	public void changeUI_changeBG_fmodel(String model) {
		if (model.equals(ConstantHelper.code_read_ktinfo_model_setcool)) {
			btn_kt_detail_fsetcool
					.setBackgroundResource(R.drawable.btn_common_pressed);
			btn_kt_detail_fsetheat
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfan
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetauto
					.setBackgroundResource(R.drawable.btn_common_normal);
		} else if (model.equals(ConstantHelper.code_read_ktinfo_model_setheat)) {
			btn_kt_detail_fsetheat
					.setBackgroundResource(R.drawable.btn_common_pressed);
			btn_kt_detail_fsetcool
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfan
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetauto
					.setBackgroundResource(R.drawable.btn_common_normal);
		} else if (model.equals(ConstantHelper.code_read_ktinfo_model_setfan)) {
			btn_kt_detail_fsetfan
					.setBackgroundResource(R.drawable.btn_common_pressed);
			btn_kt_detail_fsetcool
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetheat
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetauto
					.setBackgroundResource(R.drawable.btn_common_normal);
		} else if (model.equals(ConstantHelper.code_read_ktinfo_model_setauto)) {
			btn_kt_detail_fsetauto
					.setBackgroundResource(R.drawable.btn_common_pressed);
			btn_kt_detail_fsetcool
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetheat
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfan
					.setBackgroundResource(R.drawable.btn_common_normal);
		} else {
			btn_kt_detail_fsetauto
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetcool
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetheat
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfan
					.setBackgroundResource(R.drawable.btn_common_normal);

		}
	}

	/**
	 * 根据数据值改变风速按钮的背景 互斥关系 fspeed 1234分别代表低速 中速 高速
	 * */
	public void changeUI_changeBG_fsetspeed(String fsetspeed) {

		if (fsetspeed.equals(ConstantHelper.code_read_ktinfo_fspeed_setlow)) {

			btn_kt_detail_fsetfanlow
					.setBackgroundResource(R.drawable.btn_common_pressed);
			btn_kt_detail_fsetfanmid
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanhigh
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanauto
					.setBackgroundResource(R.drawable.btn_common_normal);
		} else if (fsetspeed
				.equals(ConstantHelper.code_read_ktinfo_fspeed_setmid)) {
			btn_kt_detail_fsetfanlow
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanmid
					.setBackgroundResource(R.drawable.btn_common_pressed);
			btn_kt_detail_fsetfanhigh
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanauto
					.setBackgroundResource(R.drawable.btn_common_normal);
		} else if (fsetspeed
				.equals(ConstantHelper.code_read_ktinfo_fspeed_sethigh)) {
			btn_kt_detail_fsetfanlow
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanmid
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanhigh
					.setBackgroundResource(R.drawable.btn_common_pressed);
			btn_kt_detail_fsetfanauto
					.setBackgroundResource(R.drawable.btn_common_normal);
		} else if (fsetspeed
				.equals(ConstantHelper.code_read_ktinfo_fspeed_setauto)) {
			btn_kt_detail_fsetfanlow
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanmid
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanhigh
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanauto
					.setBackgroundResource(R.drawable.btn_common_pressed);
		} else {
			btn_kt_detail_fsetfanlow
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanmid
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanhigh
					.setBackgroundResource(R.drawable.btn_common_normal);
			btn_kt_detail_fsetfanauto
					.setBackgroundResource(R.drawable.btn_common_normal);
		}

	}

	/**
	 * 设置操作按钮的可操作性
	 * */
	public void changeUI_btn_isEnable(boolean isEnable) {
		int visible = View.VISIBLE;
		if (isEnable) {
			visible = View.VISIBLE;
		} else {
			visible = View.INVISIBLE;
		}
		btn_kt_detail_ftempup.setVisibility(visible);
		btn_kt_detail_ftempdown.setVisibility(visible);

		btn_kt_detail_fsetcool.setVisibility(visible);
		btn_kt_detail_fsetheat.setVisibility(visible);
		btn_kt_detail_fsetfan.setVisibility(visible);
		btn_kt_detail_fsetauto.setVisibility(visible);

		btn_kt_detail_fsetfanlow.setVisibility(visible);
		btn_kt_detail_fsetfanmid.setVisibility(visible);
		btn_kt_detail_fsetfanhigh.setVisibility(visible);
		btn_kt_detail_fsetfanauto.setVisibility(visible);
	}

	/**
	 * 离线状态下的UI变化
	 * */
	public void changeUI_offline() {
		txt_kt_detail_fcommstate
				.setText(ConstantHelper.ZN_Array_KT_fcommstate[0]);
		txt_kt_detail_fstate
				.setText(ConstantHelper.static_default_disable_text);
		txt_kt_detail_ftrouble
				.setText(ConstantHelper.static_default_disable_text);
		txt_kt_detail_froomtemp
				.setText(ConstantHelper.static_default_disable_text);
		txt_kt_detail_fsettemp
				.setText(ConstantHelper.static_default_disable_text);
		changeUI_btn_isEnable(false);
		changeUI_changeBG_fmodel(ConstantHelper.DEFAULT_STRING_NULL);
		changeUI_changeBG_fsetspeed(ConstantHelper.DEFAULT_STRING_NULL);
	}

	/**
	 * 在线状态下的开机UI变化
	 * */
	public void changeUI_online_poweron(EntityKTInfo ktInfo) {
		txt_kt_detail_fcommstate.setText(ktInfo.getfCommstate());
		txt_kt_detail_fstate.setText(ktInfo.getfState());
		txt_kt_detail_ftrouble.setText(ktInfo.getFtrouble());
		txt_kt_detail_froomtemp.setText(ktInfo.getFroomtemp());
		txt_kt_detail_fsettemp.setText(ktInfo.getFsettemp());
		changeUI_btn_isEnable(true);
		changeUI_changeBG_fmodel(ktInfo.getfModel());
		changeUI_changeBG_fsetspeed(ktInfo.getFsetspeed());
	}

	/**
	 * 在线状态下的关机UI变化
	 * */
	public void changeUI_online_poweroff(EntityKTInfo ktInfo) {
		txt_kt_detail_fcommstate.setText(ktInfo.getfCommstate());
		txt_kt_detail_fstate.setText(ktInfo.getfState());
		txt_kt_detail_ftrouble.setText(ktInfo.getFtrouble());
		txt_kt_detail_froomtemp
				.setText(ConstantHelper.static_default_disable_text);
		txt_kt_detail_fsettemp
				.setText(ConstantHelper.static_default_disable_text);
		changeUI_btn_isEnable(false);
		changeUI_changeBG_fmodel(ConstantHelper.DEFAULT_STRING_NULL);
		changeUI_changeBG_fsetspeed(ConstantHelper.DEFAULT_STRING_NULL);
	}

	/**
	 * Worker Thread 下发控制命令
	 * */
	class KTCtrlAsyncTask extends
			AsyncTask<EntityCtrlOrder, String, EntityMessage> {
		// 不能再非静态的内部类里面声明静态的变量 这条规则让我无法建立单例对象
		@Override
		protected EntityMessage doInBackground(EntityCtrlOrder... params) {
			// Log.i(tag, "write to ctrl:"+params[0]);
			return KTService.getInstance().remote_writeContrl(params[0]);
		}

		@Override
		protected void onPostExecute(EntityMessage entityMessage) {
			displayOperationResult(entityMessage);
		}
	}

	/**
	 * Worker Thread 查找服务器上该设备的详细信息，同步写入到本地数据
	 * */
	public class GetRemoteKTInfoAsyncTask extends
			AsyncTask<String, String, EntityMessage> {
		// 后台动作
		@Override
		protected EntityMessage doInBackground(String... params) {
			EntityMessage message = null;
			try {
				message = KTService.getInstance().remote_GetKT(params[0],
						params[1]);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				Log.e(tag, e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(tag, e.getMessage());
			}
			return message;
		}

		// 处理结果，更新UI
		@Override
		protected void onPostExecute(EntityMessage message) {
			EntityKTInfo info = KTService.getInstance()
					.local_GetKTFromEntityMessage(message);
			if (message == null) {
				setConnect(false);
				getCurrentktInfo().setfCommstate(
						ConstantHelper.ZN_Array_KT_fcommstate[0]);
			} else if (message != null && info.getfCommstate() != null) {
				// 手机通讯在线
				setConnect(true);
				getCurrentktInfo().setfNetSocketNo(
						info.getfNetSocketNo());
				getCurrentktInfo().setfCommstate(info.getfCommstate());
				getCurrentktInfo().setfState(info.getfState());
				getCurrentktInfo().setFtrouble(info.getFtrouble());
				getCurrentktInfo().setFroomtemp(info.getFroomtemp());
				getCurrentktInfo().setFsettemp(info.getFsettemp());
				getCurrentktInfo().setfModel(info.getfModel());
				getCurrentktInfo().setFspeed(info.getFspeed());
				getCurrentktInfo().setFsetspeed(info.getFsetspeed());// 设置风速
																				// add
																				// in
				// 2014-04-06
				if (!isRefreshing()) {
					// 没有解析的模式和风速 ，风速在本界面没有任何作用。
					info.setfModel(HexConvertHelper.translateServerData(
							ConstantHelper.ZN_Array_KT_fmode, getCurrentktInfo().getfModel()));
					info.setFspeed(HexConvertHelper.translateServerData(
							ConstantHelper.ZN_Array_KT_fspeed, getCurrentktInfo().getFspeed()));
					KTService.getInstance().local_saveKTDetailInfo(
							DetailKTActivity.this, info);
				}
			}
			displayCaCheCurrentInfo(getCurrentktInfo());
		}
	}

}
