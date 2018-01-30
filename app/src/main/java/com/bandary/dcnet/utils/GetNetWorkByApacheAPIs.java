package com.bandary.dcnet.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * **********************************
 * 该类暂停使用，原因是post方法执行不到服务器
 * 日期：2014-03-27
 * author:shengfq
 * 
 * *********************************
 * */
public class GetNetWorkByApacheAPIs {

	private static final String CHARSET = HTTP.UTF_8;

	private static HttpClient customerHttpClient;
	// 单例模式在并发访问的时候不适用
	// 单例模式
	private static GetNetWorkByApacheAPIs instance;

	// 定义为私有，则调用时不能使用new 一个对象出来。显示定义了构造函数则默认的被取代
	private GetNetWorkByApacheAPIs() {

	}

	public static GetNetWorkByApacheAPIs getInstance() {
		if (instance == null) {
			instance = new GetNetWorkByApacheAPIs();
		}
		return instance;
	}

	/**
	 * 构造了单例的HTTPClient对象
	 * */
	/*public static synchronized HttpClient getHttpClient() {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置
			 1.从连接池中取连接的超时时间 
			ConnManagerParams.setTimeout(params, 5000);
			 2.连接超时 
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			 3.请求超时 
			HttpConnectionParams.setSoTimeout(params, 4000);

			// support HTTP/HTTPS
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 8090));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}*/

	/*public String executeApacheAPIsGet() {
		String result = null;
		InputStreamReader reader = null;
		BufferedReader bfReader = null;
		// build URI
		try {
			java.net.URI uri = new URI(
					"http://10.0.2.2:8080/dcnetserver/index.jsp?name=doget");
			HttpGet get = new HttpGet(uri);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);
			reader = new InputStreamReader(response.getEntity().getContent());
			bfReader = new BufferedReader(reader);
			StringBuffer sb = new StringBuffer();
			String bfline = null;
			while ((bfline = bfReader.readLine()) != null) {
				sb.append(bfline);
			}
			result = sb.toString();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bfReader != null) {

				try {
					bfReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}*/

	/**
	 * apache apis 实现http网络请求和回复
	 * 
	 * @param strurl
	 *            访问的路径
	 * @param paramMap
	 *            上传参数集合
	 * @return JSON 格式的字符串
	 * **/

	private static final String TAG = "GetNetWorkByApacheAPIs";

	public static String post(String url,List<BasicNameValuePair> formparams) {
		URI uri;
		String result = null;
		HttpEntity resEntity=null;
		UrlEncodedFormEntity entity = null;
		try {
			uri = new URI(url);
			// 编码参数
			/*List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
			for (NameValuePair p : params) {
				formparams.add(p);
			}*/
			 entity = new UrlEncodedFormEntity(formparams,
					CHARSET);
			HttpPost post = new HttpPost(uri);
			post.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				//throw new RuntimeException("request error");
			}if(response.getStatusLine().getStatusCode()==200){
				resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity, "UTF-8");
			}		
		} catch (URISyntaxException e) {
			Log.e(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			return null;
		} 
		return result;
	}
}
