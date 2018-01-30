package com.bandary.dcnet.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.bandary.dcnet.R;
import com.bandary.dcnet.upgrade.Config;
import com.bandary.dcnet.upgrade.VersionInfo;

public class UpgradeActivity extends Activity {
	private static final String tag = "UpgradeActivity";
	public ProgressDialog pBar;

	private Handler handler = new Handler();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade);
		UpgradeTask task=new UpgradeTask();
		task.execute();
		
	}
	
	public class UpgradeTask extends AsyncTask<Void, String, VersionInfo>{

		@Override
		protected VersionInfo doInBackground(Void... param) {
			VersionInfo sinfo=Config.getSVersionInfo();
			VersionInfo cinfo=Config.getCVersionInfo(getApplicationContext());
			sinfo.setCversionCode(cinfo.getCversionCode());
			sinfo.setCversionName(cinfo.getCversionName());
			return sinfo;
			
		}
		
		protected void onPostExecute(VersionInfo versioninfo){
			if(Config.doUpgrade(versioninfo)){
				//perform upgrade
				doNewVersionUpdate(versioninfo);
			}else{
				notNewVersionShow(versioninfo);
			}
		}		
	}

	

	private void notNewVersionShow(VersionInfo versioninfo) {
		int verCode = versioninfo.getCversionCode();
		String verName =versioninfo.getCversionName();
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);// 1.5
		sb.append(" Code:");
		sb.append(verCode);// 1
		sb.append(",\n已是最新版,无需更新!");
		Dialog dialog = new AlertDialog.Builder(UpgradeActivity.this)
				.setTitle("软件更新").setMessage(sb.toString())// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void doNewVersionUpdate(VersionInfo versioninfo) {		
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(versioninfo.getCversionName());// 1.5
		sb.append(" Code:");
		sb.append(versioninfo.getCversionCode());// 1
		sb.append(", 发现新版本:");
		sb.append(versioninfo.getSversionName());// 1.7
		sb.append(" Code:");
		sb.append(versioninfo.getSversionCode());// 2		
		sb.append("特征:");
		sb.append(versioninfo.getSapkNote());// 2		
		Dialog dialog = new AlertDialog.Builder(UpgradeActivity.this)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(UpgradeActivity.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(Config.UPDATE_SERVER
										+ Config.UPDATE_APKNAME);
							}

						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						File file = new File(
								Environment.getExternalStorageDirectory(),
								Config.UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}

	void update() {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}