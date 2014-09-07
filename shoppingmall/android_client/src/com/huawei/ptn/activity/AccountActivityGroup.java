package com.huawei.ptn.activity;

import com.huawei.ptn.MainActivity;
import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;
import com.huawei.ptn.common.Constants;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.KeyEvent;

public class AccountActivityGroup extends ActivityGroup {
	
	private static final String TAG = AccountActivityGroup.class.getSimpleName();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		setContentView(R.layout.user_login);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MainActivity.getInstance().setCurrentTab(Constants.TAB_HOME_INDEX);
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}
}
