package com.huawei.ptn.activity;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.activity.home.HomeActivity;
import com.huawei.ptn.activity.home.SearchActivity;

import android.app.ActivityGroup;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;


public class HomeActivityGroup extends ActivityGroup {

	private static final String TAG = HomeActivityGroup.class.getSimpleName();
	
	final static  int VOICE_RECOGNITION_REQUEST_CODE = 1234; //语音识别请求码，取值任意
	final static  int HOME_VOICE_RECOGNITION_REQUEST_CODE = 1235;
	
	
	// 静态变量，用于管理本组内的所有Activity
	private static HomeActivityGroup mActivityGroup;

	public static HomeActivityGroup getInstance() {
		return mActivityGroup;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		mActivityGroup = this;
		
		Intent intent = new Intent(this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 将Activity转换成View
		Window window = mActivityGroup.getLocalActivityManager().startActivity(
				HomeActivity.class.getSimpleName(), intent);
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
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if(requestCode==VOICE_RECOGNITION_REQUEST_CODE  && resultCode == RESULT_OK ){
        	SearchActivity act = (SearchActivity)(getLocalActivityManager().getCurrentActivity());
        			
        		act.handleActivityResult(requestCode, resultCode, data);
        }
        
        if(requestCode==HOME_VOICE_RECOGNITION_REQUEST_CODE  && resultCode == RESULT_OK ){
        	HomeActivity act = (HomeActivity)(getLocalActivityManager().getCurrentActivity());
        			
        		act.handleActivityResult(requestCode, resultCode, data);
        }
        
        
        super.onActivityResult(requestCode, resultCode, data);
    }



	
}
