package com.huawei.ptn.activity.more;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.huawei.ptn.MainActivity;
import com.huawei.ptn.MyApplication;
import com.huawei.ptn.NewerGuideActivity;
import com.huawei.ptn.R;
import com.huawei.ptn.activity.MoreActivityGroup;
import com.huawei.ptn.common.Constants;
import com.huawei.ptn.util.ApkAutoUpdate;
import com.huawei.ptn.util.ViewUtils;

public class MoreActivity extends Activity implements View.OnClickListener {

	private RelativeLayout searchLayout;
	private RelativeLayout historyLayout;
	private RelativeLayout settingLayout;
	private RelativeLayout helpLayout;
	private RelativeLayout feedbackLayout;
	private RelativeLayout aboutLayout;
	private RelativeLayout versionLayout;
	private RelativeLayout exitLayout;

	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		setContentView(R.layout.app_more_activity);
		init();
	}

	private void init() {
		searchLayout = (RelativeLayout) findViewById(R.id.menu_search);
		historyLayout = (RelativeLayout) findViewById(R.id.menu_history);
		settingLayout = (RelativeLayout) findViewById(R.id.menu_setting);
		helpLayout = (RelativeLayout) findViewById(R.id.menu_help);
		feedbackLayout = (RelativeLayout) findViewById(R.id.menu_feedback);
		aboutLayout = (RelativeLayout) findViewById(R.id.menu_about);
		versionLayout = (RelativeLayout) findViewById(R.id.menu_version);
		exitLayout = (RelativeLayout) findViewById(R.id.menu_exit);
		searchLayout.setOnClickListener(this);
		historyLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
		helpLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);
		versionLayout.setOnClickListener(this);
		exitLayout.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		MainActivity.getInstance().setCurrentTab(Constants.TAB_HOME_INDEX);
	}

	private void setting() {
		Intent intent = new Intent(this, SettingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window window = MoreActivityGroup.getInstance()
				.getLocalActivityManager()
				.startActivity(SettingActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		MoreActivityGroup.getInstance().setContentView(view);
	}

	private void checkVerUpdate() {
		// 遗留问题：如何查询服务器端APK文件的版本号？(不采用服务器API接口查询版本号的方式)
		final ApkAutoUpdate aau = new ApkAutoUpdate(this.getParent());
		
		new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				boolean bUpdate = aau.update();// 下载并更新版本
				aau.delFile(); // 删除临时安装包
				Looper.loop();
			}
		}).start();
		
	}

	private void exitClient() {
		ViewUtils.showAlertDialog(this.getParent(),
				this.getResources().getString(R.string.logout_tips),
				this.getResources().getString(R.string.logout_content),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						MyApplication.getInstance().exit();
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	private void startAbout() {
		Intent intent = new Intent(this, AboutActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 把Activity转换成View
		Window window = MoreActivityGroup.getInstance()
				.getLocalActivityManager()
				.startActivity(AboutActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		// 把View添加到ActivityGroup中
		MoreActivityGroup.getInstance().setContentView(view);
	}

	private void startFeedback() {
		Intent intent = new Intent(this, FeedbackActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window window = MoreActivityGroup.getInstance()
				.getLocalActivityManager()
				.startActivity(FeedbackActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		MoreActivityGroup.getInstance().setContentView(view);
	}

	private void startGuideHelp() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, NewerGuideActivity.class);
		startActivity(intent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_search:
			break;
		case R.id.menu_history:
			break;
		case R.id.menu_setting:
			setting();
			break;
		case R.id.menu_help:
			startGuideHelp();
			break;
		case R.id.menu_feedback:
			startFeedback();
			break;
		case R.id.menu_about:
			startAbout();
			break;
		case R.id.menu_version:
			checkVerUpdate();
			break;
		case R.id.menu_exit:
			exitClient();
			break;
		default:
			break;
		}
	}
}
