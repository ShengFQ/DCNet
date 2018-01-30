package com.bandary.dcnet.utils.internet.test;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.test.AndroidTestCase;
import android.util.Log;

import com.bandary.dcnet.utils.GetNetWorkByApacheAPIs;

public class HttpTest extends AndroidTestCase{
	//框架调用应用程序类，通过IoC框架实现
	@Override
	protected void setUp() throws Exception{
		Log.e("HttpTest", "setUp");
	}
	
	@Override
	protected void tearDown() throws Exception{
		Log.e("Http Test","tearDown");
	}
	
	/*public void testExecuteHttpGet(){
		Log.e("HttpTest", "testExecuteHttpGet");
		GetNetWorkByJDK client=GetNetWorkByJDK.getInstance();
		String result=client.executeHttpGet();
		Log.e("HttpTest", result);
	}
	
	public void testExecuteHttpPost(){
		Log.e("HttpTest", "testExecuteHttpPost");
		GetNetWorkByJDK client=GetNetWorkByJDK.getInstance();
		String result=client.executeHttpPost();
		Log.e("HttpTest", result);
	}
	
	public void testExecuteApacheAPIsGet(){
		Log.e("HttpTest", "testExecuteApacheAPIsGet");
		GetNetWorkByApacheAPIs client=GetNetWorkByApacheAPIs.getInstance();
		String result=client.executeApacheAPIsGet();
		Log.e("HttpTest", result);
	}*/
	
	public void testPost(){
		Log.d("HttpTest", "testPost");
		NameValuePair param1=new BasicNameValuePair("uname", "test");
		NameValuePair param2=new BasicNameValuePair("upassword", "test");		
		//String jsonResult = GetNetWorkByApacheAPIs.post("http://192.168.0.120/login.php", param1,param2);
		//Log.d("HttpTest", jsonResult);
	}
}
