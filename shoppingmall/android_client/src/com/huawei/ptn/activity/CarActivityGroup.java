package com.huawei.ptn.activity;

import com.huawei.ptn.MainActivity;
import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;
import com.huawei.ptn.common.Constants;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.widget.Button;
import android.view.KeyEvent;
import android.view.View;

public class CarActivityGroup extends ActivityGroup {
	private static final String TAG = CarActivityGroup.class.getSimpleName();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		setContentView(R.layout.car_empty);

		Button button = (Button) findViewById(R.id.btn_back_home);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				MainActivity.getInstance().setCurrentTab(
						Constants.TAB_HOME_INDEX);

				// 下面是跳到指定Tab中指定activity的方法，当然，前提是这个activity存在堆栈中
				// 否则可能导致跳转后的activity中没有数据
				// MainActivity.getInstance().setCurrentTab(Constants.TAB_CATEGORY_IDNEX);
				// Intent intent = new Intent(context, ImageList.class);
				// Window window =
				// TabCategoryActivityGroup.mActivityGroup.getLocalActivityManager()
				// .startActivity(ImageList.class.getSimpleName(), intent);
				// View view = window.getDecorView();
				// TabCategoryActivityGroup.mActivityGroup.setContentView(view);
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MainActivity.getInstance().setCurrentTab(Constants.TAB_HOME_INDEX);
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

}
