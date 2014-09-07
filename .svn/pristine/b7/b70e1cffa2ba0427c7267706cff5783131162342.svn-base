package com.huawei.ptn.activity;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.activity.more.MoreActivity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MoreActivityGroup extends ActivityGroup {

	private static final String TAG = MoreActivityGroup.class.getSimpleName();

	// 静态变量，用于管理本组内的所有Activity
	private static MoreActivityGroup mActivityGroup;

	public static MoreActivityGroup getInstance() {
		return mActivityGroup;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		mActivityGroup = this;

		Intent intent = new Intent(this, MoreActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 将Activity转换成View
		Window window = mActivityGroup.getLocalActivityManager().startActivity(
				MoreActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		//把View添加到ActivityGroup中
		mActivityGroup.setContentView(view);
	}
	
	@Override
	public void onBackPressed() {
		// 后退事件留给子Activity进行处理
		mActivityGroup.getLocalActivityManager().getCurrentActivity()
				.onBackPressed();
	}

}
