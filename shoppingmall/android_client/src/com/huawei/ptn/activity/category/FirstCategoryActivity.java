package com.huawei.ptn.activity.category;

import com.huawei.ptn.MainActivity;
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

public class FirstCategoryActivity extends ListActivity {

	private static final String TAG = FirstCategoryActivity.class.getSimpleName();

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
		setContentView(R.layout.tab_category_list);
		
		initDialog();
		initManager();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		CategoryItem gi = mCategoryManager.get(position);

		Intent intent = new Intent(this, SecondCategoryActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Constants.TITLE, gi.getName());
		intent.putExtra(Constants.FIRST_ID, gi.getId());

		// 将Activity转换成view
		Window window = CategoryActivityGroup.getInstance()
				.getLocalActivityManager().startActivity(
						SecondCategoryActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		CategoryActivityGroup.getInstance().setContentView(view);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mProDialog.dismiss();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MainActivity.getInstance().setCurrentTab(Constants.TAB_HOME_INDEX);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	private void initManager() {
		mCategoryManager = new CategoryManager(this);
		mCategoryManager.clear(); // 先将上次的清除
		mCategoryManager.load(Constants.CATEGORY_FIRST);
		
		setListAdapter(new CategoryAdapter(this, mCategoryManager));

		// 当CategoryManager还在下载资源时，显示进度条为忙，并注册观察者用于下载结束时隐藏进度条
		if (mCategoryManager.isLoading()) {
			mProDialog.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.indeterminate_drawable));
			mProDialog.show();
			mCategoryManager.addObserver(mObserver);
		}
	}

	private void initDialog() {
		mProDialog = new ProgressDialog(this.getParent());
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.setMessage(getResources().getString(R.string.loading));
		mProDialog.setIndeterminate(true);
	}
}
