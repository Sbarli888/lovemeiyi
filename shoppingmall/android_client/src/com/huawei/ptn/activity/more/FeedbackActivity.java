package com.huawei.ptn.activity.more;

import com.huawei.ptn.R;
import com.huawei.ptn.activity.MoreActivityGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class FeedbackActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_user_suggestion);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MoreActivity.class);
		Window window = MoreActivityGroup.getInstance()
				.getLocalActivityManager()
				.startActivity(MoreActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		MoreActivityGroup.getInstance().setContentView(view);
	}

}
