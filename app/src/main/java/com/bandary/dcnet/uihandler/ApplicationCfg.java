package com.bandary.dcnet.uihandler;

import com.android.pc.ioc.app.ApplicationBean;
import com.bandary.dcnet.uihandler.CrashHandler;

/** 
 * 在开发应用时都会和Activity打交道，而Application使用的就相对较少了。 
 * Application是用来管理应用程序的全局状态的，比如载入资源文件。 
 * 在应用程序启动的时候Application会首先创建，然后才会根据情况(Intent)启动相应的Activity或者Service。 
 * 在本文将在Application中注册未捕获异常处理器。 
 */  
public class ApplicationCfg extends ApplicationBean {	
	public static ApplicationCfg app;
	@Override
    public void init() {
		app = this;
		//自定义数据库的路径
		setDbDirs("/sdcard/db");	
		//全局异常处理
        CrashHandler handler = CrashHandler.getInstance();  
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler); 
      //全局异常处理
    }
	
	
}
