package com.bandary.dcnet.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectPLayer;
import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.uihandler.BaseHandler;
import com.bandary.dcnet.uihandler.ExitApplication;
import com.bandary.dcnet.uihandler.IBaseHandler;

@InjectPLayer(value = R.layout.activity_common, isFull = false, isTitle = false)
public class BaseActivity extends Activity {
	private IBaseHandler baseHander;
	// public static String tag="BaseActivity";//comment in
	// 2014-5-19,避免多余注释找不到出处
	private static final int MENU_EXIT = Menu.FIRST - 1;
	private static final int MENU_ABOUT = Menu.FIRST;
	
	@InjectInit
	public void init() {
		this.setBaseHander(new BaseHandler(BaseActivity.this));
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_EXIT, 0, getResources().getText(R.string.MENU_EXIT));
		menu.add(0, MENU_ABOUT, 0, getResources().getText(R.string.MENU_ABOUT));
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case MENU_EXIT:
			closeActivity();
			break;
		case MENU_ABOUT:
			alertAbout();
			break;
		}
		return true;
	}

	// close window
	private void closeActivity() {
		AlertDialog dailog = new AlertDialog.Builder(BaseActivity.this)
				.setTitle(R.string.MENU_EXIT)
				.setIcon(R.drawable.ic_launcher)
				.setMessage(R.string.MENU_AREYOUSURE)
				.setPositiveButton(R.string.MENU_POSITIVE_CONTEXT,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialoginterface, int i) {
								ExitApplication.getInstanse().exit();
							}
						})
				.setNegativeButton(R.string.MENU_NEGATIVE_CONTEXT,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						}).create();
		dailog.show();
	}

	/** 弹出关于对话框 */
	private void alertAbout() {
		AlertDialog dailog = new AlertDialog.Builder(BaseActivity.this)
				.setTitle(R.string.MENU_ABOUT)
				.setIcon(R.drawable.ic_launcher)
				.setMessage(R.string.MENU_COPYRIGHT_CONTEXT)
				.setPositiveButton(R.string.MENU_CLOSE_CONTEXT,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialoginterface, int i) {
							}
						}).create();
		dailog.show();
	}

	/**
	 * 消息提示
	 * */
	public void MakeToast(String title) {
		baseHander.MakeToast(title);
	}

	/**
	 * 反馈后台访问结果
	 * */
	public void displayOperationResult(EntityMessage entityMessage) {
		baseHander.displayOperationResult(entityMessage);
	}

	public IBaseHandler getBaseHander() {
		return baseHander;
	}

	public void setBaseHander(IBaseHandler baseHander) {
		this.baseHander = baseHander;
	}
}