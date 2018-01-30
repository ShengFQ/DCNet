package com.bandary.dcnet.ui;

import java.util.List;

import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityKTInfo;

public class ConstantHelper {
	/***************************全局常量定义****************************************/
	//网络访问超时时间
	public static final int internet_time=1000;
	//网络访问超时时间
	public static final int internet_time_out=3000;
	//网络访问默认字符
	public static final String charset_utf8="UTF-8";
	
	public static final String charset_gbk="GBK";
	
	public static final String DEFAULT_STRING_NULL = "";
	
	public static final String DEFAULT_STRING_Y="Y";
	
	public static final String DEFAULT_STRING_N="N";

	public static final String MSG = "msg";

	public static final String MSGTYPE = "msgType";	

	public static final String MSGTYPE_T = "T";

	public static final String MSGTYPE_F = "F";
	
	public static final String REMOTE_ARGS_USID="usid";
	
	public static final String REMOTE_ARGS_UNAME="uname";
	
	public static final String REMOTE_ARGS_UPASSWORD="upassword";
	
	public static final String REMOTE_ARGS_SESSIONID="sessionid";
	
	public static final String REMOTE_ARGS_FTYPE="ftype";
	/******************************************************************/
	//Handler对象传递消息变量
	public static final String signal_handler_isNetError="isNetError";
	//Handler对象传递消息变量
	public static final String signal_handler_isNotValidated="isNotValidated";
	/*************************SharePreferences Key****************************************/
	/** 用来操作SharePreferences的标识 */
	public static final String SHARE_LOGIN_TAG = "DCNET_SHARE_LOGIN_TAG";
	/** 如果登录成功后,用于保存编号到SharedPreferences,以便下次不再输入 */
	public static final String SHARE_LOGIN_USERID = "DCNET_LOGIN_USERID";
	/** 如果登录成功后,用于保存用户名到SharedPreferences,以便下次不再输入 */
	public static final String SHARE_LOGIN_USERNAME = "DCNET_LOGIN_USERNAME";
	/** 如果登录成功后,用于保存PASSWORD到SharedPreferences,以便下次不再输入 */
	public static final String SHARE_LOGIN_PASSWORD = "DCNET_LOGIN_PASSWORD";
	/** 保存各登录者自定义的设备类型，每个登录用户保存的格式为：userid.SHARE_CUSTOMIZING_TYPE */
	public static final String SHARE_CUSTOMIZING_TYPE = "SHARE_CUSTOMIZING_TYPE";
	
	/*****************************URL*******************************************/
	// 服务器静态地址
	//public static final String HostNameSpace = "http://hualingkeji1.vicp.cc:11363/bdy";
	/* begin 服务器端的数据请求服务接口*/
	public static final String URI_WRITETOKTCTRL="/writeToKTCtrl.php";
	
	public static final String URI_LOGIN="/login.php";
	
	public static final String URI_GETKTS="/getKTs.php";
	
	public static final String URI_GETKTREGS="/getKTRegs.php";
	
	public static final String URI_isRegPermission="/isRegPermission.php";
	
	public static final String URI_getKTInfo="/getKTInfo.php";
	
	public static final String URI_addEqRegInfo="/addEqRegInfo.php";
	
	public static final String URI_updateEqRegInfo="/updateEqRegInfo.php";
	
	public static final String URI_deleteEqRegInfo="/deleteEqRegInfo.php";
	//public static final String HostNameSpace = "http://bas.66ip.net:8090";
	
	public static final String HostNameSpace = "http://192.168.36.52:8090";	
	
	public static final String URI_addUserInfo="/addUserInfo.php";
	/* end 服务器端的数据请求服务接口*/
	// 设备功能码对应表
	// 这里只做单向转换，也就算从服务器转化到客户端显示，客户端发给服务器的控制指令另外做方法，因为那个可以组装出控制命令，方圆酒店mbus温控器的控制命令作为参考
	//public static final String[] ZN_Array_KT_fname = { "客厅", "卧室","厨房","小孩房","书房","健身房" };
	
	// 通讯状态
	public static final String[] ZN_Array_KT_fcommstate = { "离线", "在线" };
	public static final String[] EN_Array_KT_fcommstate = { "offline", "online" };
	// 模式
	public static final String[] ZN_Array_KT_fmode = { "空", "制冷", "除湿", "送风",
			"制热", "自动" };
	public static final String[] EN_Array_KT_fmode = { "null", "cool", "damp",
			"fan", "heat", "auto" };
	// 风速
	public static final String[] ZN_Array_KT_fspeed = { "停止", "低风", "中风", "高风" };
		
	public static final String[] EN_Array_KT_fspeed = { "stop", "slow",
			"middle", "high" };

	//风速设置
	public static final String[] ZN_Array_KT_fsetspeed={"空","低风","中风","高风","自动"};

	//风速设置
	public static final String[] EN_Array_KT_fsetspeed={"null","slow","midlle","high","auto"};
	// 运行状态
	public static final String[] ZN_Array_KT_fstate = { "关机", "开机" };
	public static final String[] EN_Array_KT_fstate = { "off", "on" };
	// 锁定状态
	public static final String[] ZN_Array_KT_flock = { "未锁", "开关", "模式", "风速",
			"温度", "全锁" };
	public static final String[] EN_Array_KT_flock = { "unlock", "power",
			"moderl", "fan", "temp", "lock" };
	// 故障状态
	public static final String[] ZN_Array_KT_ftrouble = { "无故障", "室温传感器故障",
			"进水温传感器故障", "存储器故障", "内外机通讯中断", "内盘传感器故障", "外盘传感器故障", "环境传感器故障",
			"内盘防冻报警", "内盘高温报警", "外盘高温报警", "冷媒泄漏报警", "压缩机过载报警", "压缩机排气高压报警",
			"压缩机吸气低压压报警", "风机过载报警", "流量开关报警", "进水温超高报警", "进水温过低报警" };
	public static final String[] EN_Array_KT_ftrouble = { "WELL", "sensor", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
	
	public static final int DEFAULT_INT_VALUE=0;
	// chinese
	public static final int ZN_LANG = 0;
	// english
	public static final int EN_LANG = 1;
	// your choise or default
	public static final int DEFAULT_LANG = 0;

	public static final int DEFAULT_TEMPERATURE=0;
	// 设置温度下限设置范围 低位
	public static final int SCOPE_KT_SETTEMP_MIN_BEGIN = 1;
	// 设置温度下限设置范围 高位
	public static final  int SCOPE_KT_SETTEMP_MIN_END = 50;
	// 设置温度上限设置范围 低位
	public static final  int SCOPE_KT_SETTEMP_MAX_BEGIN = 1;
	// 设置温度上限设置范围 高位
	public static final int SCOPE_KT_SETTEMP_MAX_END = 50;
	//设置温度最低值
	public static final int SCOPE_KT_SETTEMP_MIN=0;
	//设置温度最高值
	public static final  int SCOPE_KT_SETTEMP_MAX = 30;
	// 设备类别
	public static final String[] ZN_TYPES_TAG = { "空调控制", "新风控制", "地暖控制", "热水控制", "主机控制", "照明控制",
			"窗帘控制", "环境监测" };
	public static final String[] ZN_TYPES_NAME = { "空调", "新风", "地暖", "热水", "主机", "照明",
		"窗帘", "环境" };
	public static final String[] ZN_TYPES_NUM = { "01", "02", "03", "04", "05", "06",
			"07", "08" };
	public static final  int[] ZN_TYPES_PIC = { R.drawable.ic_nav_kt,
			R.drawable.ic_nav_xf, R.drawable.ic_nav_dn, R.drawable.ic_nav_rs,
			R.drawable.ic_nav_zj, R.drawable.ic_nav_zm, R.drawable.ic_nav_cl,
			R.drawable.ic_nav_hj };
	public static final  String ZN_TEMP_UNIT = "℃";
	
	public static final  String offLine_display="  --  ";
	
	public static final String msg_detailkt_isLastOne="已经是最后一台了";
	
	public static final String msg_detailkt_isFirstOne="已经是第一台了";
	
	public static final String msg_global_DisConnect="当前网络不可用,请检查网络设置";
	
	public static final String msg_global_OffLine="当前设备已离线";
	
	public static final String msg_global_success="成功";
	
	public static final  String msg_global_error="失败";
	
	public static final String msg_global_unknown_data="未知数据";
	
	public static final String msg_global_building="建设中..";
	
	public static final String msg_global_loading="加载中..";
	
	public static final String msg_global_connecting="连接中..";
	
	public static final String msg_global_waitting="请稍后";
	
	public static final String msg_global_login_failure="登陆失败:\n1.请检查您网络连接.\n2.请联系我们.!";
	
	public static final String msg_global_wrong_validate="登陆失败,请输入正确的用户名和密码!";
	
	public static final String msg_global_register_failure="注册失败:\n1.用户名或邮箱已存在!";
	
	public static final String space_key=" ";
	
	public static final String underline="_";
	
	public static final String msg_global_regdc_success="注册成功";
	public static final String msg_global_regdc_failure="注册失败：1.请检查空调在线\n 2.空调处于请求注册状态[功能码:666] \n 3.输入4位注册码";
	public static final String msg_global_regdc_failure_offline="注册失败-空调未联网";
	public static final String msg_global_regdc_failure_norequest="注册失败-空调未处于注册请求中";
	public static final String msg_global_regdc_failure_novalidate="注册失败-空调注册码不正确";
	
	public static final String singal_reg_failure_offline="1";
	public static final String singal_reg_failure_norequest="2";
	public static final String singal_reg_failure_novalidate="3";
	
	public static final String FORMAT_DATETIME="yyyy-MM-dd HH:mm:ss";
	
	
	public static final String msg_global_refresh_success="刷新成功";
	public static final String msg_global_refresh_failure="刷新失败";
	
	public static final String msg_global_verify_success="数据验证成功";
	public static final String msg_global_verify_failure="数据验证失败";
	
	
	public  static final long[] vibrator_pattern_long = { 100, 400 };// 震动器震动时间长
	public  static final long[] vibrator_pattern_short = { 100, 100 };// 震动器震动时间短
	public static final int delay_submit_second = 1500;// 延时提交控制请求的时间
	public static final int refresh_server_data_second = 10000;// 刷新本地数据的间隔时间
	public static final String static_default_disable_text = offLine_display;
	
	
	/*************************全局变量定义***************************************/
	// 存储KT信息列表对象，不能设为final
	private static List<EntityKTInfo> Global_ktInfolist;

	//是否能连接到服务器
	//private static boolean isConnect = true;
	
	//空调的状态代码
	public static final String code_read_ktinfo_model_setcool="1";
	public static final String code_read_ktinfo_model_setheat="4";
	public static final String code_read_ktinfo_model_setfan="3";
	public static final String code_read_ktinfo_model_setauto="5";
	
	public static final String code_read_ktinfo_fspeed_setlow="1";
	public static final String code_read_ktinfo_fspeed_setmid="2";
	public static final String code_read_ktinfo_fspeed_sethigh="3";
	public static final String code_read_ktinfo_fspeed_setauto="4";
	
	public static final String code_write_ktinfo_wmodel_setcool="0001";
	public static final String code_write_ktinfo_wmodel_setheat="0004";
	public static final String code_write_ktinfo_wmodel_setfan="0003";
	public static final String code_write_ktinfo_wmodel_setauto="0005";		
	
	public static final String code_write_ktinfo_wfspeed_setlow="0001";
	public static final String code_write_ktinfo_wfspeed_setmid="0002";
	public static final String code_write_ktinfo_wfspeed_sethigh="0003";
	public static final String code_write_ktinfo_wfspeed_setauto="0004";
	
	public static final String code_write_ktinfo_wfstate_turnon="0001";
	public static final String code_write_ktinfo_wfstate_turnoff="0000";
	

	//Intent of index
	public static final String INDEX="index";
	
	public static List<EntityKTInfo> getGlobal_ktInfolist() {
		return Global_ktInfolist;
	}

	public static void setGlobal_ktInfolist(List<EntityKTInfo> global_ktInfolist) {
		Global_ktInfolist = global_ktInfolist;
	}
	
	public static final String KEY_ENTITY_ABSDEVICE="com.bandary.dcnet.entity.EntityAbsDevice";
}
