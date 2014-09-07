package com.huawei.ptn.activity.category;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;
import com.huawei.ptn.activity.CategoryActivityGroup;
import com.huawei.ptn.adapter.CategoryAdapter;
import com.huawei.ptn.common.Constants;
import com.huawei.ptn.model.CategoryItem;
import com.huawei.ptn.util.CategoryManager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class SecondCategoryActivity extends ListActivity {

	private String title;

	private long parentid;

	private CategoryManager mCategoryManager;

	private ProgressDialog mProDialog;

	private MyDataSetObserver mObserver = new MyDataSetObserver();

	private class MyDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			if (!mCategoryManager.isLoading()) {
				mProDialog.cancel();
			}
		}

		@Override
		public void onInvalidated() {
			return;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.second_category_list);

		getDataFromIntent();
		initUi();
		initDialog();
		initManager();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mProDialog.dismiss();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				Intent intent = new Intent(this, FirstCategoryActivity.class);
				Window window = CategoryActivityGroup.getInstance()
						.getLocalActivityManager().startActivity(
								FirstCategoryActivity.class.getSimpleName(),
								intent);
				View view = window.getDecorView();
				CategoryActivityGroup.getInstance().setContentView(view);
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		CategoryItem gi = mCategoryManager.get(position);

		Intent intent = new Intent(this, ImageList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Constants.GOODS_ID, gi.getId());
		intent.putExtra(Constants.IMAGE_LIST_TITLE, gi.getName());
		intent.putExtra("request_code", Constants.CATEGORY_REQUEST); //区分“分类”与“搜索"对ImageList的请求。

		// 将Activity转换成view
		Window window = CategoryActivityGroup.getInstance()
				.getLocalActivityManager().startActivity(
						ImageList.class.getSimpleName(), intent);
		View view = window.getDecorView();
		CategoryActivityGroup.getInstance().setContentView(view);
	}

	private void getDataFromIntent() {
		Intent intent = getIntent();
		title = intent.getStringExtra(Constants.TITLE);
		parentid = intent.getLongExtra(Constants.FIRST_ID, 0);
	}
	
	private void initUi() {
		TextView textView = (TextView) findViewById(R.id.second_category_text_view);
		textView.setText(title);
	}
	
	private void initDialog() {
		mProDialog = new ProgressDialog(this.getParent());
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.setMessage(getResources().getString(R.string.loading));
		mProDialog.setIndeterminate(true);
	}
	
	private void initManager() {
		mCategoryManager = new CategoryManager(this);
		mCategoryManager.clear();
		mCategoryManager.load(Constants.CATEGORY_SECOND + parentid);
		
		setListAdapter(new CategoryAdapter(this, mCategoryManager));

		// 当CategoryManager还在下载资源时，显示进度条为忙，并注册观察者用于下载结束时隐藏进度条
		if (mCategoryManager.isLoading()) {
			mProDialog.show();
			mCategoryManager.addObserver(mObserver);
		}
	}
}
