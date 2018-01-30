package com.bandary.dcnet.dao;

import android.content.Context;
import android.database.sqlite.SQLiteAccessPermException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库建立
 * */
public class DataBaseHelper extends SQLiteOpenHelper {
	private static String tag = "DataBaseHelper";
	private static String dbName = "myDCNet.DB";
	public static String tbName_KTInfo = "tb_KTInfo";
	// 建立单例
	private static DataBaseHelper dataBaseHelper;

	private DataBaseHelper(Context context) {
		super(context, dbName, null, 1);
	}

	public static DataBaseHelper getInstance(Context context) {
		if (dataBaseHelper == null) {
			dataBaseHelper = new DataBaseHelper(context);
		}
		return dataBaseHelper;
	}

	// 创建数据表
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作
		// DDL 创建数据表等最好用execSQL
		//Log.i(tag, "onCreate() begin");
		// 以下语句竟然没有建立表
		try {
			// drop table
			// db.execSQL(
			// "DROP TABLE IF EXIST "+tbName_KTInfo);不能使用drop，否则无法建立表
			// create table
			db.execSQL("create table if not exists "
					+ tbName_KTInfo
					+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,fmac varchar(50),usid varchar(20)"
					+ ",ftype varchar(50),fname varchar(50),flocation varchar(50),fregtime varchar(50),fregNo varchar(50),"
					+ "fcommstate varchar(50),fregstate varchar(50),fnetsocketno varchar(50),"
					+ "fstate varchar(50),fmodel varchar(50),froomtemp varchar(50),fspeed varchar(50),fsetspeed varchar(50),"
					+ "fsettemp varchar(50),flock varchar(50),fsettempmin varchar(50),fsettempmax varchar(50),ftrouble varchar(50));");
			//******************test add data******************
			//db.execSQL("insert into KTInfo(fmac,usid)values('0022c0a54a00','80000001');");
		} catch (SQLiteAccessPermException e) {
			Log.i(tag, "onCreate() SQLiteAccessPermException:"  );
			
		} catch (SQLiteCantOpenDatabaseException e) {
			Log.i(tag,
					"onCreate() SQLiteCantOpenDatabaseException:"
							 );
			
		}
		//Log.i(tag, "onCreate() end");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作

		//Log.i(tag, "onUpgrade() end");

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO 每次成功打开数据库后首先被执行
		super.onOpen(db);
	}

}
