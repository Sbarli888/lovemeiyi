package com.huawei.ptn.activity;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.activity.category.FirstCategoryActivity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class CategoryActivityGroup extends ActivityGroup {

	private static final String TAG = CategoryActivityGroup.class
			.getSimpleName();

	// 静态变量，用于管理本组内的所有Activity
	private static CategoryActivityGroup mActivityGroup;
	
	public static CategoryActivityGroup getInstance() {
		return mActivityGroup;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		mActivityGroup = this;

		Intent intent = new Intent(this, FirstCategoryActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 把Activity转换成View
		Window window = mActivityGroup.getLocalActivityManager().startActivity(
				FirstCategoryActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		// 把view添加到ActivityGroup中
		mActivityGroup.setContentView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		// 后退事件留给子Activity进行处理
		mActivityGroup.getLocalActivityManager().getCurrentActivity()
				.onBackPressed();
	}

}
