package com.huawei.ptn.activity.more;

import com.huawei.ptn.R;
import com.huawei.ptn.activity.MoreActivityGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class AboutActivity extends Activity {

	private static final String TAG = AboutActivity.class.getSimpleName();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_aboutus);
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed");
		Intent intent = new Intent(this, MoreActivity.class);
		Window window = MoreActivityGroup.getInstance()
				.getLocalActivityManager()
				.startActivity(MoreActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		MoreActivityGroup.getInstance().setContentView(view);
		// super.onBackPressed();
	}

}
