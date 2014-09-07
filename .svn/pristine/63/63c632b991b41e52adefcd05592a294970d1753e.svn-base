package com.huawei.ptn.activity;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public abstract class TabHostActivity extends TabActivity {

	private TabHost mTabHost;
	private TabWidget mTabWidget;
	private LayoutInflater mLayoutInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		// 不需要标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set the theme because we do not want the shadow
		setTheme(R.style.Theme_Tabhost);
		setContentView(R.layout.api_tab_host);

		mLayoutInflater = getLayoutInflater();
		mTabHost = getTabHost();
		mTabWidget = getTabWidget();

		mTabWidget.setStripEnabled(false);

		prepare();
		// initTop();
		initTabSpec();
	}

	private void initTop() {
		View child = getTop();
		LinearLayout layout = (LinearLayout) findViewById(R.id.tab_top);
		layout.addView(child);
	}

	private void initTabSpec() {
		int count = getTabItemCount();

		for (int i = 0; i < count; i++) {
			View tabItem = mLayoutInflater.inflate(R.layout.api_tab_item, null);
			TextView tvTabItem = (TextView) tabItem
					.findViewById(R.id.tab_item_tv);
			setTabItemTextView(tvTabItem, i);

			String tabItemId = getTabItemId(i);

			TabSpec tabSpec = mTabHost.newTabSpec(tabItemId);
			tabSpec.setIndicator(tabItem);
			tabSpec.setContent(getTabItemIntent(i));

			mTabHost.addTab(tabSpec);
		}

	}

	/** 初始化用户界面之前调用 */
	protected void prepare() {
		return;
	}

	/** 自定义头部布局 */
	protected View getTop() {
		return null;
	}

	/** 获取选项的个数 */
	protected int getTabCount() {
		return mTabHost.getTabWidget().getTabCount();
	}

	protected void focusCurrentTab(int index) {
		mTabWidget.focusCurrentTab(index);
	}

	public void setCurrentTab(int index) {
		mTabHost.setCurrentTab(index);
	}

	/** 设置TabItem的图标和标题等 */
	abstract protected void setTabItemTextView(TextView textView, int position);

	abstract protected String getTabItemId(int position);

	abstract protected Intent getTabItemIntent(int position);

	abstract protected int getTabItemCount();

}
