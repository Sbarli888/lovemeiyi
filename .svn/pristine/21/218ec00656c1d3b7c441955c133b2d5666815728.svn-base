package com.huawei.ptn;

import java.util.ArrayList;
import java.util.List;

import com.huawei.ptn.activity.AccountActivityGroup;
import com.huawei.ptn.activity.CarActivityGroup;
import com.huawei.ptn.activity.CategoryActivityGroup;
import com.huawei.ptn.activity.HomeActivityGroup;
import com.huawei.ptn.activity.TabHostActivity;
import com.huawei.ptn.activity.MoreActivityGroup;
import com.huawei.ptn.model.TabItem;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;

import com.huawei.ptn.R;

public class MainActivity extends TabHostActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static MainActivity instance;

	private List<TabItem> mItems;
	private LayoutInflater mLayoutInflater;

	public static MainActivity getInstance() {
		return instance;
	}

	@Override
	protected void prepare() {

		// 首页
		TabItem home = new TabItem(R.string.home, R.drawable.icon_home,
				R.drawable.example_tab_item_bg, new Intent(this,
						HomeActivityGroup.class));

		// 分类
		TabItem category = new TabItem(R.string.category,
				R.drawable.icon_selfinfo, R.drawable.example_tab_item_bg,
				new Intent(this, CategoryActivityGroup.class));

		// 购物车
		TabItem car = new TabItem(R.string.car, R.drawable.icon_meassage,
				R.drawable.example_tab_item_bg, new Intent(this,
						CarActivityGroup.class));

		// 我的账户
		TabItem account = new TabItem(R.string.account, R.drawable.icon_square,
				R.drawable.example_tab_item_bg, new Intent(this,
						AccountActivityGroup.class));

		// 更多
		TabItem more = new TabItem(R.string.more, R.drawable.icon_more,
				R.drawable.example_tab_item_bg, new Intent(this,
						MoreActivityGroup.class));

		mItems = new ArrayList<TabItem>();
		mItems.add(home);
		mItems.add(category);
		mItems.add(car);
		mItems.add(account);
		mItems.add(more);

		// 设置分割线
		TabWidget tabWidget = getTabWidget();
		tabWidget.setDividerDrawable(R.drawable.tab_divider);

		mLayoutInflater = getLayoutInflater();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		MyApplication.getInstance().setMainActivity(this);

		setCurrentTab(0);
		instance = this;
	}

	@Override
	protected void setTabItemTextView(TextView textView, int position) {
		textView.setPadding(3, 3, 3, 3);
		textView.setText(mItems.get(position).getTitle());
		textView.setBackgroundResource(mItems.get(position).getBackground());
		textView.setCompoundDrawablesWithIntrinsicBounds(0, mItems
				.get(position).getIcon(), 0, 0);

	}

	@Override
	protected String getTabItemId(int position) {
		return getResources().getString(mItems.get(position).getTitle());
	}

	@Override
	protected Intent getTabItemIntent(int position) {
		return mItems.get(position).getIntent();
	}

	@Override
	protected int getTabItemCount() {
		return mItems.size();
	}

	@Override
	protected View getTop() {
		return mLayoutInflater.inflate(R.layout.example_top, null);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.i(TAG, "onConfigurationChanged");
	}

}
