package com.bandary.dcnet.utils;

import com.bandary.dcnet.entity.EntityKTInfo;
import com.bandary.dcnet.ui.ConstantHelper;

/**
 * 该类为16进制和10进制的数据转换帮助类
 * */
public class HexConvertHelper {
	/**
	 * hex to decimal
	 * 
	 * @param hex
	 *            character
	 * */
	public static int hex2Decimal(String hex) {
		int i = 0;
		if ("".equals(hex) || hex == null) {
			return 0;
		}
		try {
			i = Integer.parseInt(hex, 16);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return i;
		}
		return i;
	}

	/**
	 * decimal to hex
	 * 
	 * @param decimal
	 *            data
	 * */
	public static String decimal2Hex(int decimal) {
		if (Integer.valueOf(decimal) == null) {
			return "0000";
		}
		return Integer.toHexString(decimal);
	}

	/**
	 * 服务器端室温转换为本地显示室温，为小数
	 * 
	 * @param hex
	 *            服务器的hex格式温度
	 * @return 客户端温度
	 * */
	public static float displayRoomTemp(String roomtemp) {
		float code = 0;
		if (roomtemp == null) {
			return code;
		}
		try {
			code = Float.valueOf(roomtemp);
			// code = Integer.valueOf(temp);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return code / 10;
	}
	
	public static int displaySettingTemp(String setttemp){
		int code = 0;
		if (setttemp == null) {
			return code;
		}
		try {
			code = Integer.valueOf(setttemp);
			// code = Integer.valueOf(temp);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return code / 10;
	}

	/**
	 * 显示设置温度，为整数
	 * */
	public static int stringToInteger(String val, int defaultValue) {
		int def = 0;
		if (val == null) {
			return defaultValue;
		} else {
			try {
				def = Integer.valueOf(val);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return def;
	}
	

	/**
	 * 将温度值组装成16进制数据进行下发，注意任何单变量占用2个字节，不够的补0
	 * **/
	public static String writeTemp(int temp) {
		String def = "FFFF";
		int t = temp * 10;
		def = addZero(decimal2Hex(t), 4);
		return def;
	}

	/**
	 * 该方法暂时封存，因为属性可以直接静态值 写控制属性，输入10进制数字，转成16进制双字节4位字符
	 * */
	/*
	 * public static String writeKTProperty(int decimal){ String def="FFFF";
	 * def=addZero(decimal2Hex(decimal), 4); return def; }
	 */
	/**
	 * 补0方法，专为16进制数据准备
	 * */
	public static String addZero(String num, int len) {
		int numlen = num.length(); // 得到num的长度
		String strChar = "0"; // 空缺位填充字符
		String str = num;
		for (int i = 0; i < len - numlen; i++) {
			str = strChar + str;
		}
		return str;
	}

	/**
	 * 解析服务器端的数字代号
	 * 
	 * @param dic
	 *            代码对应的字典数组
	 * @param serverData
	 *            服务器上的显示代码
	 * */
	public static String translateServerData(String[] dic, String serverData) {
		if ("".equals(serverData) || null == serverData) {
			return "";
		}
		int index = 0;
		try {
			index = Integer.parseInt(serverData);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "wrong";
		}
		if (index > dic.length - 1) {
			return "wrong";
		}
		return dic[index];
	}
	
	/**
	 *根据文字获得数组的
	 * */
	/*public static int getArrayIndex(String[] array,String val,int defaultValue){
		int valint=0;
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(val)){
				valint=i;
				break;
			}
		}
		return valint;
	}*/

	/***
	 * 解析服务器端的数值代码为客户文字
	 * */
	public static EntityKTInfo translate(int lang, EntityKTInfo info) {
		EntityKTInfo inf = info;
		if (lang == ConstantHelper.ZN_LANG) {
			inf = translate_Zn(info);
		} else if (lang == ConstantHelper.EN_LANG) {
			inf = translate_En(info);
		}
		return inf;
	}

	public static EntityKTInfo translateSimple(int lang, EntityKTInfo info) {
		EntityKTInfo inf = info;
		if (lang == ConstantHelper.ZN_LANG) {
			inf = translateSimple_Zn(info);
		} else if (lang == ConstantHelper.EN_LANG) {
			inf = translateSimple_En(info);
		}
		return inf;
	}

	/**
	 * 室温 设温 运行 模式 风速 通讯状态 锁定 故障
	 * */
	public static EntityKTInfo translate_Zn(EntityKTInfo sourceInfo) {
		// 室温 设温 运行 模式 风速 通讯状态 锁定 故障
		// EntityKTInfo newInfo=new EntityKTInfo();
		/*sourceInfo.setFname(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_fname,
				sourceInfo.getFname()));*/
		sourceInfo.setFroomtemp(displayRoomTemp(sourceInfo.getFroomtemp())
				+ ConstantHelper.DEFAULT_STRING_NULL);
		sourceInfo.setFsettemp(displaySettingTemp(sourceInfo.getFsettemp())
				+ ConstantHelper.DEFAULT_STRING_NULL);
		sourceInfo.setfState(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_fstate, sourceInfo.getfState()));
		sourceInfo.setfModel(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_fmode, sourceInfo.getfModel()));
		sourceInfo.setFspeed(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_fspeed, sourceInfo.getFspeed()));
		sourceInfo.setfCommstate(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_fcommstate,
				sourceInfo.getfCommstate()));
		sourceInfo.setFlock(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_flock, sourceInfo.getFlock()));
		sourceInfo.setFtrouble(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_ftrouble, sourceInfo.getFtrouble()));
		return sourceInfo;

	}

	/**
	 *  室温 设温  运行  通讯状态  故障
	 * */
	public static EntityKTInfo translateSimple_Zn(EntityKTInfo sourceInfo) {
		// 室温 设温  运行  通讯状态  故障
		sourceInfo.setFroomtemp(displayRoomTemp(sourceInfo.getFroomtemp())
				+ ConstantHelper.DEFAULT_STRING_NULL);
		sourceInfo.setFsettemp(displaySettingTemp(sourceInfo.getFsettemp())
				+ ConstantHelper.DEFAULT_STRING_NULL);
		sourceInfo.setfState(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_fstate, sourceInfo.getfState()));
		sourceInfo.setfCommstate(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_fcommstate,
				sourceInfo.getfCommstate()));
		sourceInfo.setFtrouble(HexConvertHelper.translateServerData(
				ConstantHelper.ZN_Array_KT_ftrouble, sourceInfo.getFtrouble()));
		sourceInfo.setfModel(sourceInfo.getfModel());
		sourceInfo.setFspeed(sourceInfo.getFspeed());
		return sourceInfo;

	}

	public static EntityKTInfo translate_En(EntityKTInfo sourceInfo) {
		sourceInfo.setFroomtemp(displayRoomTemp(sourceInfo.getFroomtemp()) + "");
		sourceInfo.setFsettemp(displaySettingTemp(sourceInfo.getFsettemp()) + "");
		sourceInfo.setfState(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_fstate, sourceInfo.getfState()));
		sourceInfo.setfModel(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_fmode, sourceInfo.getfModel()));
		sourceInfo.setFspeed(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_fspeed, sourceInfo.getFspeed()));
		sourceInfo.setfCommstate(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_fcommstate,
				sourceInfo.getfCommstate()));
		sourceInfo.setFlock(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_flock, sourceInfo.getFlock()));
		sourceInfo.setFtrouble(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_ftrouble, sourceInfo.getFtrouble()));
		return sourceInfo;
	}

	public static EntityKTInfo translateSimple_En(EntityKTInfo sourceInfo) {
		sourceInfo.setFroomtemp(displayRoomTemp(sourceInfo.getFroomtemp())
				+ ConstantHelper.DEFAULT_STRING_NULL);
		sourceInfo.setFsettemp(displaySettingTemp(sourceInfo.getFsettemp())
				+ ConstantHelper.DEFAULT_STRING_NULL);
		sourceInfo.setfState(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_fstate, sourceInfo.getfState()));
		sourceInfo.setfCommstate(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_fcommstate,
				sourceInfo.getfCommstate()));
		sourceInfo.setFtrouble(HexConvertHelper.translateServerData(
				ConstantHelper.EN_Array_KT_ftrouble, sourceInfo.getFtrouble()));
		sourceInfo.setfModel(sourceInfo.getfModel());
		sourceInfo.setFspeed(sourceInfo.getFspeed());
		return sourceInfo;
	}
}
