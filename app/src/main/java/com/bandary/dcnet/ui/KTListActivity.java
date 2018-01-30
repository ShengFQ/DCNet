package com.bandary.dcnet.ui;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.bandary.dcnet.R;
import com.bandary.dcnet.adapter.KTInfoListViewAdapter;
import com.bandary.dcnet.entity.EntityAbsDevice;
import com.bandary.dcnet.entity.EntityCtrlOrder;
import com.bandary.dcnet.entity.EntityKTCtrl;
import com.bandary.dcnet.entity.EntityKTInfo;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.service.KTService;
import com.bandary.dcnet.uihandler.UserSharePreferencesHandler;

/**
 * 空调设备列表显示界面UI
 * 
 * @author ShengFQ
 * @version 1.1 2014-3-10
 * 
 * */

public class KTListActivity extends BaseActivity {
	
	
	
	// 事件处理操作接口
	
	private static final String tag = "KTListActivity";

	private ImageButton btn_KTlist_refresh;
	private ImageButton btn_KTlist_addKT;
	private ImageButton btn_KTlist_back;
	private Button btn_ktlist_onekey_poweron;
	private Button btn_ktlist_onekey_poweroff;
	private AlertDialog mOneKeyDialog;
	private KTInfoListViewAdapter madapter;
	private KTCtrlAsyncTask ktCtrlAsyncTask;
	private ListView mlistView;
	private RemoteKTListAsyncTask remoteKTListAsyncTask;// 同步数据异步任务
	
	
	//字段
		private List<EntityKTInfo> mcurrentKTList;//当前显示的空调列表
		private int mcurrentListViewPosition=0;//当前选择的listview的position值
		private String mcurrentType;//当前的设备类型
		private String musid; // 登录用户ID
		private EntityKTCtrl mEntityKTCtrl;//一键控制命令对象
		private boolean misLoading = true; // 是否正在加载中
		private boolean misRefreshing = true; // 是否请求刷新
		private boolean misSubmit = false; // 延时提交意图
		private boolean misConnect=false;//网络是否正常
		private ArrayList<EntityCtrlOrder> mcodeItemList; // 控制命令包
		public boolean isMisLoading() {
			return misLoading;
		}
		public void setMisLoading(boolean misLoading) {
			this.misLoading = misLoading;
		}
		public boolean isMisRefreshing() {
			return misRefreshing;
		}
		public void setMisRefreshing(boolean misRefreshing) {
			this.misRefreshing = misRefreshing;
		}
		public boolean isMisSubmit() {
			return misSubmit;
		}
		public void setMisSubmit(boolean misSubmit) {
			this.misSubmit = misSubmit;
		}

		public List<EntityKTInfo> getMcurrentKTList() {
			return mcurrentKTList;
		}

		public void setMcurrentKTList(List<EntityKTInfo> mcurrentKTList) {
			this.mcurrentKTList = mcurrentKTList;
		}

		public int getMcurrentListViewPosition() {
			return mcurrentListViewPosition;
		}

		public void setMcurrentListViewPosition(int mcurrentListViewPosition) {
			this.mcurrentListViewPosition = mcurrentListViewPosition;
		}

		public String getMcurrentType() {
			return mcurrentType;
		}

		public void setMcurrentType(String mcurrentType) {
			this.mcurrentType = mcurrentType;
		}

		public String getMusid() {
			return musid;
		}

		public void setMusid(String musid) {
			this.musid = musid;
		}

		public EntityKTCtrl getEntityKTCtrl() {
			return mEntityKTCtrl;
		}

		public void setEntityKTCtrl(EntityKTCtrl entityKTCtrl) {
			mEntityKTCtrl = entityKTCtrl;
		}

		public boolean isMisConnect() {
			return misConnect;
		}

		public void setMisConnect(boolean misConnect) {
			this.misConnect = misConnect;
		}

		public ArrayList<EntityCtrlOrder> getMcodeItemList() {
			return mcodeItemList;
		}

		public void setMcodeItemList(ArrayList<EntityCtrlOrder> mcodeItemList) {
			this.mcodeItemList = mcodeItemList;
		}
		
	/**
	 * default construction
	 * */
	
	/**
	 * * 消息处理：根据消息读取服务器数据并进行刷新
	 * 
	 * */
	protected Handler autoRefreshHander = new Handler() {
		@Override
		public void handleMessage(Message message) {
			boolean value = message.getData().getBoolean("isRefreshing");
			if (value) {
				setMisRefreshing(true);
				getRemoteKTList(getMusid(), getMcurrentType());
			}
		}

	};

	/**
	 * 页面初始化方法
	 * */
	public void onCreate(Bundle savedInstance) {	
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_ktlist);
		initView();
		
		//handler.setMisLoading(true);
		//this.setHandler(KTListActivityHandler.getInstance(KTListActivity.this));
		setMisRefreshing(false);
		setMusid(UserSharePreferencesHandler
				.getUserIDFromLocal(getApplicationContext()));
		setMcurrentType(ConstantHelper.ZN_TYPES_NUM[0]);
		setMcurrentKTList(getCaCheCurrentList(getMusid()));
		initAdapterView(getMcurrentKTList());
		getRemoteKTList(getMusid(), getMcurrentType());
		registerViewListenerEvent();
	}

	/**
	 * 初始化界面控件
	 * */
	private void initView() {
		setMisRefreshing(false);
		setMisLoading(true);
		setEntityKTCtrl(new EntityKTCtrl());
		setMcodeItemList(new ArrayList<EntityCtrlOrder>());
		
		btn_KTlist_refresh = (ImageButton) findViewById(R.id.btn_KTlist_refresh);
		btn_KTlist_addKT = (ImageButton) findViewById(R.id.btn_KTlist_addKT);
		btn_KTlist_back = (ImageButton) findViewById(R.id.btn_KTlist_back);
		btn_ktlist_onekey_poweron = (Button) findViewById(R.id.btn_ktlist_onekey_poweron);
		btn_ktlist_onekey_poweroff = (Button) findViewById(R.id.btn_ktlist_onekey_poweroff);
		mlistView = (ListView) findViewById(R.id.kt_listview);
	}

	/**
	 * 读取数据库本地资源用于显示，也就是初始化空调列表
	 * 
	 * @param musid
	 *            用户id
	 * @return 该用户的空调列表
	 * */
	private List<EntityKTInfo> getCaCheCurrentList(String musid) {
		List<EntityKTInfo> list = KTService.getInstance().local_getKTInfoList(
				KTListActivity.this, musid);
		return list;
	}

	/**
	 * 初始化ListView,填充数据
	 * 
	 * @param ktlist
	 *            将用户的空调列表填充到ListView
	 * */
	private void initAdapterView(List<EntityKTInfo> ktlist) {
		madapter = new KTInfoListViewAdapter(KTListActivity.this, ktlist);
		mlistView.setAdapter(madapter);
		mlistView.setLongClickable(true);
	}

	/**
	 * 使用最新的空调列表数据，刷新listview的数据
	 * 
	 * @param templist
	 *            远程空调列表
	 * */
	private void refreshAdapterView(List<EntityKTInfo> templist) {
		if (!isMisConnect()) {
			MakeToast(ConstantHelper.msg_global_DisConnect);
		}
		madapter.setKTLists(templist);
		madapter.notifyDataSetChanged();
	}

	/**
	 * 注册按钮事件
	 * */
	public void registerViewListenerEvent() {

		// 单击进入详细界面
		mlistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(KTListActivity.this,
						DetailKTActivity.class);
				intent.putExtra(ConstantHelper.INDEX, position);
				startActivity(intent);
			}
		});

		/**
		 * 长按进入编辑菜单
		 * */
		mlistView.setOnItemLongClickListener(new OnItemLongClickListener() {
			// 长按显示编辑菜单，菜单项
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				// 使用AlterDialog弹出对话框
				mOneKeyDialog = null;
				mOneKeyDialog = new AlertDialog.Builder(KTListActivity.this)
						.setTitle(
								getMcurrentKTList().get(position)
										.getFname())
						.setItems(R.array.arrcontent,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										String[] PK = getResources()
												.getStringArray(
														R.array.arrcontent);
										if (PK[which]
												.equals(getString(R.string.CONTENT_ALTERDIALOG_ITEM_EDIT))) {
											// 编辑设备
											// TODO 单击事件,需要获得选择项的对象数据？？
											EntityAbsDevice device = new EntityAbsDevice();
											device.setfMac(getMcurrentKTList()
													.get(position).getfMac());
											device.setFname(getMcurrentKTList()
													.get(position).getFname());
											device.setFtype(getMcurrentKTList()
													.get(position).getFtype());
											editKTInfo(getApplicationContext(),
													device);
											dialog.dismiss();
										} else if (PK[which]
												.equals(getString(R.string.CONTENT_ALTERDIALOG_ITEM_DELETE))) {
											mOneKeyDialog = null;
											mOneKeyDialog = new AlertDialog.Builder(
													KTListActivity.this)
													.setTitle(
															R.string.TITLE_ALERTDIALOG_POSITIVE)
													.setMessage(
															R.string.CONTENT_ALERTDIALOG_MESSAGE_CONFIRM_DELETE)
													.setPositiveButton(
															R.string.MENU_POSITIVE_CONTEXT,
															new DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog1,
																		int which) {
																	EntityAbsDevice device = new EntityAbsDevice();
																	device.setfMac(getMcurrentKTList()
																			.get(position)
																			.getfMac());
																	device.setFname(getMcurrentKTList()
																			.get(position)
																			.getFname());
																	device.setFtype(getMcurrentKTList()
																			.get(position)
																			.getFtype());
																	deleteKTInfo(device);
																	dialog1.dismiss();
																}
															})
													.setNegativeButton(
															R.string.MENU_NEGATIVE_CONTEXT,
															new DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog,
																		int i) {

																}
															}).create();
											mOneKeyDialog.show();
										}

									}
								}).create();
				mOneKeyDialog.show();
				return true;
			}
		});

		// Asynchronous reFresh button
		btn_KTlist_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMisRefreshing(true);
				getRemoteKTList(getMusid(), getMcurrentType());
				// MakeToast(ConstantHelper.msg_global_refresh_success);
			}
		});

		// add kt register
		btn_KTlist_addKT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(KTListActivity.this,
						WifiPointWelcomeDiscActivity.class);
				/*// 我本打算将此id传到详细界面，在详细界面里面读取数据库信息，但是发现那样很耗内存
				// 不如写一个全局list，只传递一个index过去，在详细界面调用list内的对象
				intent.putExtra(ConstantHelper.REMOTE_ARGS_USID,
						getMusid());
				intent.putExtra(ConstantHelper.REMOTE_ARGS_FTYPE,
						ConstantHelper.ZN_TYPES_NUM[0]);*/
				startActivity(intent);
			}
		});

		// go back
		btn_KTlist_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// one key power off
		btn_ktlist_onekey_poweroff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isMisConnect()) {
					// 不允许任何操作,因为设备离线
					MakeToast(ConstantHelper.msg_global_DisConnect);
					return;
				}
				// a asyncTask
				// 1.get the data from db
				// 2.get the control code by
				mOneKeyDialog = null;
				mOneKeyDialog = new AlertDialog.Builder(KTListActivity.this)
						.setTitle(R.string.MENU_ONEKEY_POWEROFF)
						.setIcon(R.drawable.ic_launcher)
						.setMessage(R.string.MENU_AREYOUSURE_POWEROFF)
						.setPositiveButton(R.string.MENU_POSITIVE_CONTEXT,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										getEntityKTCtrl()
												.setWstate(
														ConstantHelper.code_write_ktinfo_wfstate_turnoff);
										doOneKeyOperate(
												getMcurrentKTList(),
												getEntityKTCtrl());
										dialog.dismiss();
									}
								})
						.setNegativeButton(R.string.MENU_NEGATIVE_CONTEXT,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialoginterface,
											int i) {

									}
								}).create();
				mOneKeyDialog.show();

			}
		});

		// one key power off
		btn_ktlist_onekey_poweron.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isMisConnect()) {
					// 不允许任何操作,因为设备离线
					MakeToast(ConstantHelper.msg_global_DisConnect);
					return;
				}
				mOneKeyDialog = null;
				mOneKeyDialog = new AlertDialog.Builder(KTListActivity.this)
						.setTitle(R.string.MENU_ONEKEY_POWERON)
						.setIcon(R.drawable.ic_launcher)
						.setMessage(R.string.MENU_AREYOUSURE_POWERON)
						.setPositiveButton(R.string.MENU_POSITIVE_CONTEXT,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										getEntityKTCtrl()
												.setWstate(
														ConstantHelper.code_write_ktinfo_wfstate_turnon);
										doOneKeyOperate(
												getMcurrentKTList(),
												getEntityKTCtrl());
										dialog.dismiss();
									}
								})
						.setNegativeButton(R.string.MENU_NEGATIVE_CONTEXT,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialoginterface,
											int i) {

									}
								}).create();
				mOneKeyDialog.show();
			}
		});
	}

	/**
	 * 一键开关机操作处理事件
	 * 
	 * @param list
	 *            控制目标对象
	 * @param propertys
	 *            属性名
	 * @param values
	 *            属性值
	 * */
	private void doOneKeyOperate(List<EntityKTInfo> list, EntityKTCtrl ctrl) {		
		for (int i = 0; i < list.size(); i++) {	
			EntityKTInfo info = list.get(i);	
			
			//if off line
			if (ConstantHelper.ZN_Array_KT_fcommstate[0].equals(info
					.getfCommstate())) {
				
				continue;
			}
			
			// if on line ,don't do it again
			//if request turn off,and the device was turned off
			if (ConstantHelper.code_write_ktinfo_wfstate_turnoff.equals(ctrl
					.getWstate())
					&& ConstantHelper.ZN_Array_KT_fstate[0].equals(info
							.getfState())) {				
				continue;
			}
			if (ConstantHelper.code_write_ktinfo_wfstate_turnon.equals(ctrl
					.getWstate())
					&& ConstantHelper.ZN_Array_KT_fstate[1].equals(info
							.getfState())) {
				continue;
			}
			EntityCtrlOrder codeItem =EntityCtrlOrder.getInstance()
					.getCtrlPostParam(info.getfNetSocketNo(), info.getfMac(),
							ctrl);
			Log.i(tag, "mac:"+codeItem.toString());
			//TODO 数据是重复的,被最后一个数据替代了。
			getMcodeItemList().add(codeItem);			
		}
		//list里面是最有一个加入进去的元素的x个副本。
		for (int j = 0; j < getMcodeItemList().size(); j++) {
			EntityCtrlOrder order=getMcodeItemList().get(j);
			Log.i(tag, "one key operate object is"+j+order.toString());
		}
		
		if (!isMisSubmit()) {
			Log.i(tag, "one key operate");
			setMisSubmit(true);// 意图提交
			Thread t = new Thread(new DelaySubmitThread());
			t.start();
		}
	}

	/**
	 * 内部类 开启新线程执行后台异步控制命令下发
	 * */
	private class DelaySubmitThread implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(ConstantHelper.delay_submit_second);// 延时
				writeRemoteCtrl(getMcodeItemList());
				setMisSubmit(false);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 异步任务下发控制任务
	 * */
	public void writeRemoteCtrl(List<EntityCtrlOrder> list) {
		ktCtrlAsyncTask = new KTCtrlAsyncTask();
		ktCtrlAsyncTask.execute(list);
	}

	/**
	 * 后台任务 删除空调
	 * */
	public void deleteKTInfo(EntityAbsDevice device) {
		DeleteEqRegInfoAsyncTask deleteTask = new DeleteEqRegInfoAsyncTask();
		deleteTask.execute(device.getfMac(), getMusid());
	}

	/**
	 * 
	 * 后台异步操作 删除设备(包括远程删除和本地缓存数据库删除)
	 * */
	class DeleteEqRegInfoAsyncTask extends
			AsyncTask<String, String, EntityMessage> {
		@Override
		protected EntityMessage doInBackground(String... params) {
			EntityMessage message = null;
			try {
				message = KTService.getInstance().remote_DeleteKTReg(params[0],
						params[1]);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (message != null
					&& ConstantHelper.MSGTYPE_T.equals(message.getMsgType())) {
				KTService.getInstance().local_deleteKTRegister(
						KTListActivity.this, params[0]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(EntityMessage message) {
			displayOperationResult(message);
		}
	}

	/**
	 * Worker Thread 下发控制命令
	 * */
	class KTCtrlAsyncTask extends
			AsyncTask<List<EntityCtrlOrder>, String, EntityMessage> {
		@Override
		protected EntityMessage doInBackground(List<EntityCtrlOrder>... params) {
			// Log.i(tag, "write to ctrl:"+params[0]);
			return KTService.getInstance().remote_writeContrl(params[0]);
		}

		@Override
		protected void onPostExecute(EntityMessage entityMessage) {
			displayOperationResult(entityMessage);
		}
	}

	/**
	 * Worker Thread 异步读取远程数据类
	 * */
	protected class RemoteKTListAsyncTask extends
			AsyncTask<String, String, EntityMessage> {
		/**
		 * 后台执行服务器访问
		 * 
		 * @param params0
		 *            usid
		 * @param params1
		 *            typeid
		 * @return 空调列表数据
		 * */
		@Override
		protected EntityMessage doInBackground(String... params) {
			EntityMessage message = null;
			try {
				// 如果脱网，则会出现异常，不会有返回值
				message = KTService.getInstance().remote_GetKTs(params[0],
						params[1]);
			} catch (UnknownHostException e) {
				Log.e(tag, e.getLocalizedMessage());
			} catch (Exception e) {
				Log.e(tag, e.getLocalizedMessage());
			}
			return message;
		}

		/**
		 * 后台返回结果回调函数，执行界面更新
		 * */
		@Override
		protected void onPostExecute(EntityMessage message) {
			List<EntityKTInfo> list = KTService.getInstance()
					.local_GetKTListFromEntityMessage(message);

			if (message == null) {
				setMisConnect(false);
				for (int i = 0; i < getMcurrentKTList().size(); i++) {

					getMcurrentKTList()
							.get(i)
							.setfCommstate(
									ConstantHelper.ZN_Array_KT_fcommstate[0]);
				}
				MakeToast(ConstantHelper.msg_global_refresh_failure);
			} else if (message != null
					&& ConstantHelper.MSGTYPE_F.equals(message.getMsgType())) {
				setMisConnect(true);
				getMcurrentKTList().clear();
			} else if (message != null
					&& ConstantHelper.MSGTYPE_T.equals(message.getMsgType())) {
				setMisConnect(true);
				setMcurrentKTList(list);// set current list
				if (!isMisRefreshing()) {
					/*Log.i(tag, "storage the ktlist to local db ");*/
					for (int i = 0; i < getMcurrentKTList().size(); i++) {
						getMcurrentKTList().get(i)
								.setUsid(getMusid());
						getMcurrentKTList().get(i).setFtype(ConstantHelper.ZN_TYPES_NUM[0]);
					}
					KTService.getInstance().local_saveKTList(
							KTListActivity.this, getMcurrentKTList());
				}else if(isMisRefreshing()){
					MakeToast(ConstantHelper.msg_global_refresh_success);
				}
			}
			// 保存到全局变量，在详细界面里面调用，通过索引
			ConstantHelper.setGlobal_ktInfolist(getMcurrentKTList());
			refreshAdapterView(getMcurrentKTList());
			setMisLoading(false);
		}
	}

	/**
	 * 执行一个异步获取远程空调列表并同步到本地数据库
	 * 
	 * @param musid
	 *            用户id
	 * @param typeid
	 *            用户的设备类型id
	 * */
	private void getRemoteKTList(String usid, String typeid) {		
		remoteKTListAsyncTask = new RemoteKTListAsyncTask();
		remoteKTListAsyncTask.execute(usid, typeid);
	}

	/**
	 * 后台任务 实现远程与本地编辑列表项名称
	 * */
	public void editKTInfo(Context context, EntityAbsDevice device) {
		Intent mIntent = new Intent(context, EditDCActivity.class);
		// 我本打算将此id传到详细界面，在详细界面里面读取数据库信息，但是发现那样很耗内存
		// 不如写一个全局list，只传递一个index过去，在详细界面调用list内的对象
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(ConstantHelper.KEY_ENTITY_ABSDEVICE, device);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

}