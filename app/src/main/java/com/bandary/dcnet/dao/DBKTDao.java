package com.bandary.dcnet.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAccessPermException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bandary.dcnet.entity.EntityKTInfo;

/**
 * 任何添加方法都需要mac和usid
 * */
public class DBKTDao {
	private static String tag = "DBKTDao";
	private static DBKTDao instance;

	private DBKTDao() {

	}

	public static DBKTDao getInstance() {
		instance = new DBKTDao();
		return instance;
	}

	/**
	 * 取出KTInfo集合数据
	 * 
	 * @param context
	 * @return
	 */
	/**
	 * @param context
	 * @return
	 */
	public List<EntityKTInfo> getKTInfos(Context context, String usid) {
		// Log.i(tag, "getKTInfos() begin");
		List<EntityKTInfo> ktlist = new ArrayList<EntityKTInfo>();
		Cursor cursor = null;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = DataBaseHelper.getInstance(context)
					.getReadableDatabase();
			if (sqLiteDatabase != null) {
				cursor = sqLiteDatabase.query(DataBaseHelper.tbName_KTInfo,
						new String[] { "fname", "fcommstate", "fstate",
								"froomtemp", "fmodel", "fspeed" }, "usid=?",
						new String[] { usid }, null, null, "id asc");
				while (cursor.moveToNext()) {
					EntityKTInfo ktInfo = new EntityKTInfo();
					ktInfo.setFname(cursor.getString(cursor
							.getColumnIndex("fname")));
					ktInfo.setfCommstate(cursor.getString(cursor
							.getColumnIndex("fcommstate")));
					ktInfo.setfState(cursor.getString(cursor
							.getColumnIndex("fstate")));
					ktInfo.setFroomtemp(cursor.getString(cursor
							.getColumnIndex("froomtemp")));
					ktInfo.setfModel(cursor.getString(cursor
							.getColumnIndex("fmodel")));
					ktInfo.setFspeed(cursor.getString(cursor
							.getColumnIndex("fspeed")));
					ktlist.add(ktInfo);
					// cursor.moveToNext();
				}
			}
		} catch (SQLiteAccessPermException e) {
			 Log.e(tag, "getKTInfos() SQLiteAccessPermException:",e);
		} catch (SQLiteCantOpenDatabaseException e) {
			 Log.e(tag, "getKTInfos() SQLiteCantOpenDatabaseException:",e);
			
		} finally {

			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}		
		return ktlist;
	}
	
	public EntityKTInfo getKTInfo(Context context,String fmac){
		Cursor cursor=null;
		SQLiteDatabase sqliteDatabase=null;
		sqliteDatabase=DataBaseHelper.getInstance(context).getReadableDatabase();
		EntityKTInfo ktInfo=null;
		if(sqliteDatabase!=null){
			ktInfo = new EntityKTInfo();
			cursor=sqliteDatabase.query(DataBaseHelper.tbName_KTInfo,
					new String[]{"fmac","usid","ftype","fname","fregstate","flocation","fregtime"}, 
					"fmac=?", new String[]{fmac}, null, null, "id asc");
			while (cursor.moveToNext()) {
			ktInfo.setfMac(cursor.getString(cursor.getColumnIndex("fmac")));
			ktInfo.setUsid(cursor.getString(cursor.getColumnIndex("usid")));
			ktInfo.setFtype(cursor.getString(cursor.getColumnIndex("ftype")));
			ktInfo.setFname(cursor.getString(cursor.getColumnIndex("fname")));
			ktInfo.setFregstate(cursor.getString(cursor.getColumnIndex("fregstate")));
			ktInfo.setFlocation(cursor.getString(cursor.getColumnIndex("fregtime")));
			}
		}
		if (sqliteDatabase != null) {
			sqliteDatabase.close();
		}
		return ktInfo;
	}
	

	/**
	 * 持久化空调信息列表，空调只有简单的通讯状态
	 * 
	 * @param newss
	 * @param context
	 */
	public int saveKTList(Context context, List<EntityKTInfo> ktList) {		
		int count = -1;
		// 如果mac存在，则更新数据，如果不存在则添加数据
		for (EntityKTInfo ktInfo : ktList) {
			// 如果不存在该空调设备则增加
			if (getKTItemCount(context, ktInfo.getfMac()) > 0) {
				count = (int) updateKTInfo(context, ktInfo);// if has this mac
			} else {
				count = (int) addKTInfo(context, ktInfo);// has no this mac
			}
		}
		return count;
	}
	/**
	 * 【调试使用】 更新空调详细信息时，判断在数据库中是否存在，实际运行环境中，应该是已经存在的。 该方法处于调试时使用
	 * 
	 * @param ktInfo
	 * @param context
	 */
	public int saveKTDetailInfo(Context context, EntityKTInfo ktInfo) {
		// Log.i(tag, "saveKTDetailInfo() begin");
		int count = -1;
		// 如果mac存在，则更新数据，如果不存在则添加数据
		// 如果不存在该空调设备则增加
		if (getKTItemCount(context, ktInfo.getfMac()) > 0) {
			count = (int) updateKTDetailInfo(context, ktInfo);// if has this mac
		} else {
			count = (int) addKTInfo(context, ktInfo);// has no this mac
		}
		// Log.i(tag, "saveKTDetailInfo() end");
		return count;
	}
	/**
	 * 检查空调设备编号是否已经存在
	 * */
	public int getKTItemCount(Context context, String fmac) {
		// Log.i(tag, "getKTItemCount() begin fmac:" + fmac);
		int count = -1;
		SQLiteDatabase sqLiteDatabase = null;
		Cursor cursor = null;
		try {
			sqLiteDatabase = DataBaseHelper.getInstance(context)
					.getReadableDatabase();
			if (sqLiteDatabase != null) {
				String sql = "select fmac from " + DataBaseHelper.tbName_KTInfo
						+ " where fmac=?";
				cursor = sqLiteDatabase.rawQuery(sql, new String[] { fmac });
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					count = cursor.getCount();
					cursor.moveToNext();
				}
			}
		} catch (SQLiteAccessPermException e) {
			 Log.e(tag, "getKTItemCount() SQLiteAccessPermException:",e );
			// e.printStackTrace();
		} catch (SQLiteCantOpenDatabaseException e) {
			 Log.e(tag, "getKTItemCount() SQLiteCantOpenDatabaseException:",e );
			// e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
		// Log.i(tag, "getKTItemCount() end");
		return count;
	}

	/**
	 * 添加一个空调设备的通讯信息 注意，别忘记将空调的用户写进去，否则只有空调而没有用户了。 任何添加方法都需要mac和usid
	 * */
	public long addKTInfo(Context context, EntityKTInfo info) {
		// Log.i(tag, "addKTInfo() begin");
		long count = -1;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = DataBaseHelper.getInstance(context)
					.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("usid", info.getUsid());
			contentValues.put("ftype", info.getFtype());//add in 2014-6-25
			contentValues.put("fname", info.getFname());
			contentValues.put("fmac", info.getfMac());
			contentValues.put("fcommstate", info.getfCommstate());
			contentValues.put("froomtemp", info.getFroomtemp());
			contentValues.put("fmodel", info.getfModel());
			contentValues.put("fspeed", info.getFspeed());
			contentValues.put("fstate", info.getfState());
			contentValues.put("fnetsocketno", info.getfNetSocketNo());
			if (sqLiteDatabase != null) {
				count = sqLiteDatabase.insert(DataBaseHelper.tbName_KTInfo,
						null, contentValues);
				sqLiteDatabase.close();
			}
		} catch (SQLiteAccessPermException e) {
			// Log.i(tag, "addKTInfo() SQLiteAccessPermException:" );
			// e.printStackTrace();
		} catch (SQLiteCantOpenDatabaseException e) {
			// Log.i(tag, "addKTInfo() SQLiteCantOpenDatabaseException:");
			// e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
		// Log.i(tag, "addKTInfo() end");
		return count;
	}

	public long updateKTInfo(Context context, EntityKTInfo info) {
		// Log.i(tag, "updateKTInfo() begin");
		long count = -1;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = DataBaseHelper.getInstance(context)
					.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("fname", info.getFname());
			contentValues.put("fcommstate", info.getfCommstate());
			contentValues.put("froomtemp", info.getFroomtemp());
			contentValues.put("fmodel", info.getfModel());
			contentValues.put("fspeed", info.getFspeed());
			contentValues.put("fstate", info.getfState());
			contentValues.put("fnetsocketno", info.getfNetSocketNo());
			contentValues.put("ftype", info.getFtype());//add in 2014-6-25
			if (sqLiteDatabase != null) {
				count = sqLiteDatabase.update(DataBaseHelper.tbName_KTInfo,
						contentValues, "fmac=?",
						new String[] { info.getfMac() });
				sqLiteDatabase.close();
			}
		} catch (SQLiteAccessPermException e) {
			// Log.i(tag, "updateKTInfo() SQLiteAccessPermException:" );
			// e.printStackTrace();
		} catch (SQLiteCantOpenDatabaseException e) {
			// Log.i(tag, "updateKTInfo() SQLiteCantOpenDatabaseException:");
			// e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
		// Log.i(tag, "updateKTInfo() end");
		return count;
	}

	

	/**
	 * 空调操作界面上进行信息更新
	 * 
	 * */
	public long updateKTDetailInfo(Context context, EntityKTInfo info) {
		// Log.i(tag, "updateKTDetailInfo() begin");
		long count = -1;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = DataBaseHelper.getInstance(context)
					.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("fnetsocketno", info.getfNetSocketNo());
			contentValues.put("fcommstate", info.getfCommstate());
			contentValues.put("fmodel", info.getfModel());
			contentValues.put("froomtemp", info.getFroomtemp());
			contentValues.put("fsettemp", info.getFsettemp());
			contentValues.put("flock", info.getFlock());
			contentValues.put("fspeed", info.getFspeed());
			contentValues.put("fsetspeed", info.getFsetspeed());																
			contentValues.put("fstate", info.getfState());
			contentValues.put("ftrouble", info.getFtrouble());
			if (sqLiteDatabase != null) {
				count = sqLiteDatabase.update(DataBaseHelper.tbName_KTInfo,
						contentValues, "fmac=?",
						new String[] { info.getfMac() });
				sqLiteDatabase.close();
			}
		} catch (SQLiteAccessPermException e) {
			// Log.i(tag, "updateKTDetailInfo() SQLiteAccessPermException:" );
			// e.printStackTrace();
		} catch (SQLiteCantOpenDatabaseException e) {
			// Log.i(tag,
			// "updateKTDetailInfo() SQLiteCantOpenDatabaseException:" );
			// e.printStackTrace();
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}
		// Log.i(tag, "updateKTDetailInfo() end");
		return count;
	}

	/**
	 * 添加产品注册
	 * 添加字段有
	 * fname,fmac,flocation,usid,ftype
	 * */
	public long addKTRegister(Context context, EntityKTInfo info) {
		long count = -1;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = DataBaseHelper.getInstance(context)
					.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("fname", info.getFname());
			contentValues.put("fmac", info.getfMac());
			contentValues.put("flocation", info.getFlocation());
			contentValues.put("usid", info.getUsid());
			contentValues.put("ftype", info.getFtype());
			if (sqLiteDatabase != null) {
				count = sqLiteDatabase.insert(DataBaseHelper.tbName_KTInfo,
						null, contentValues);
				sqLiteDatabase.close();
			}
		} catch (SQLiteAccessPermException e) {
			Log.e(tag, "addKTRegister() SQLiteAccessPermException:", e);

		} catch (SQLiteCantOpenDatabaseException e) {
			Log.e(tag, "addKTRegister() SQLiteCantOpenDatabaseException:",e);
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}		
		return count;
	}
	
	/**
	 * 修改本地注册注销不用
	 * 修改产品注册
	 * update KTInfo set fname=?,flocation=? where fmac=?
	 * */
	@Deprecated
	public long updateKTRegister(Context context, EntityKTInfo info) {
		long count = -1;
		SQLiteDatabase sqLiteDatabase = null;
		String whereClause="fmac=?";
		String[] whereArgs={info.getfMac()};
		try {
			sqLiteDatabase = DataBaseHelper.getInstance(context)
					.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("fname", info.getFname());
			contentValues.put("flocation", info.getFlocation());
			if (sqLiteDatabase != null) {
				count = sqLiteDatabase.update(DataBaseHelper.tbName_KTInfo,contentValues,whereClause,whereArgs);
				sqLiteDatabase.close();
			}
		} catch (SQLiteAccessPermException e) {
			Log.e(tag, "updateKTRegister() SQLiteAccessPermException:", e);

		} catch (SQLiteCantOpenDatabaseException e) {
			Log.e(tag, "updateKTRegister() SQLiteCantOpenDatabaseException:",e);
		} finally {
			if (sqLiteDatabase != null) {
				sqLiteDatabase.close();
			}
		}		
		return count;
	}
	
	/**
	 * 删除空调注册
	 * */
	public long deleteKTRegister(Context context,String fmac){
		long count=-1;
		SQLiteDatabase sqLiteDatabase=null;
		String whereClause="fmac=?";
		String[] whereArgs={fmac};
		try{
			sqLiteDatabase=DataBaseHelper.getInstance(context).getWritableDatabase();
			if(sqLiteDatabase!=null){
				count=sqLiteDatabase.delete(DataBaseHelper.tbName_KTInfo, whereClause, whereArgs);
			}
		}catch(SQLiteAccessPermException exception){
			Log.e(tag,"deleteKTRegister() SQLiteAccessPermException",exception);
		}catch(SQLiteCantOpenDatabaseException exception2){
			Log.e(tag, "delteKTRegister() SQLiteCanOpenDatabaseException:",exception2);
		}finally{
			if(sqLiteDatabase!=null){
				sqLiteDatabase.close();
			}
		}
		return count;
	}
}
