package com.huawei.ptn.activity.category;

import java.util.HashMap;
import java.util.Map;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;
import com.huawei.ptn.activity.CategoryActivityGroup;
import com.huawei.ptn.activity.HomeActivityGroup;
import com.huawei.ptn.activity.home.HomeActivity;
import com.huawei.ptn.adapter.ImageAdapter;
import com.huawei.ptn.common.Constants;
import com.huawei.ptn.model.ImageItem;
import com.huawei.ptn.util.ImageManager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageList extends ListActivity {

	private static final String TAG = ImageList.class.getSimpleName();

	private final Context mContext = this;

	private ImageManager mImageManager;

	private ProgressDialog mProDialog;

	private RelativeLayout mRelaLayoutRoot;

	private RelativeLayout mRelaLayout1;

	private RelativeLayout mRelaLayout2;

	private RelativeLayout mRelaLayout3;

	private TextView mTab1;

	private TextView mTab2;

	private TextView mTab3;

	private TextView mFirst;

	private int mCurrent = 2; // 默认上架时间

	private int mSortType = Constants.SORT_TYPE_PRICE_ASC;

	private long id;

	private String name;

	private int selectWidth;

	private int selectHeight;

	private int firstLeft;

	private int startLeft;

	private boolean isAdd = false;

	private int request_code;// 标识哪个Activity请求Imagelist

	private String m_search_input;

	private MyDataSetObserver mObserver = new MyDataSetObserver();

	private class MyDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			if (!mImageManager.isLoading()) {
				mProDialog.dismiss();
			}
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		setContentView(R.layout.image_list);

		mImageManager = new ImageManager(this);

		Intent intent = getIntent();

		// TODO: 后续对该部分代码整改，封装成函数，便于维护
		request_code = intent.getIntExtra("request_code", 0);
		Log.d(TAG, "request_code = " + request_code);
		switch (request_code) {

		case Constants.SEARCH_REQUEST:
		case Constants.HOME_SEARCH_REQUEST:

			name = intent.getStringExtra(Constants.IMAGE_LIST_TITLE);
			m_search_input = intent.getStringExtra("search_input");
			String url = Constants.SEARCH_INPUT;
			mImageManager.SetUrl(url, Constants.HTTP_POST);// 后续修改为POST方式;
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("search_input", m_search_input);
			mImageManager.SetPostParams(rawParams); // 传入POST方式的内容(键值对)
			break;

		case Constants.CATEGORY_REQUEST:
			name = intent.getStringExtra(Constants.IMAGE_LIST_TITLE);
			id = intent.getLongExtra(Constants.GOODS_ID, 0);
			url = Constants.CATEGOTY_GOOD + id;
			mImageManager.SetUrl(url, Constants.HTTP_GET);
			break;
		default:
			break;
		}

		initUi();
		initDialog();

		mImageManager.clear(); // 先将上次的清除
		mImageManager.load();

		setListAdapter(new ImageAdapter(this, mImageManager));

		// 当CategoryManager还在下载资源时，显示进度条为忙，并注册观察者用于下载结束时隐藏进度条
		if (mImageManager.isLoading()) {
			mProDialog.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.indeterminate_drawable));
			mProDialog.show();
			mImageManager.addObserver(mObserver);
		}
	}

	private void replace() {
		switch (mCurrent) {
		case R.id.slide_tab1:
			changeTop(mRelaLayout1);
			break;
		case R.id.slide_tab2:
			changeTop(mRelaLayout2);
			break;
		case R.id.slide_tab3:
			changeTop(mRelaLayout3);
			break;
		default:
			break;
		}
	}

	private void changeTop(RelativeLayout relativeLayout) {
		TextView oldTv = (TextView) relativeLayout.findViewWithTag("first");
		selectWidth = oldTv.getWidth();
		selectHeight = oldTv.getHeight();

		TextView titleTv = (TextView) findViewById(R.id.image_text_view);

		RelativeLayout.LayoutParams lr = new RelativeLayout.LayoutParams(
				selectWidth, selectHeight);
		lr.leftMargin = oldTv.getLeft() + relativeLayout.getLeft();
		lr.topMargin = oldTv.getTop() + relativeLayout.getTop()
				+ titleTv.getHeight();

		firstLeft = oldTv.getLeft() + relativeLayout.getLeft();

		TextView tv = new TextView(this);
		tv.setTag("move");
		tv.setBackgroundResource(R.drawable.slide_bar);
		tv.setTextSize(18);

		mRelaLayoutRoot.addView(tv, lr);
		relativeLayout.removeView(oldTv); // 移除上面一层（选中的那一层）
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ImageItem item = mImageManager.get(position);

		Intent intent = new Intent(this, BasicInfoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Constants.GOODS_ID, item.getmId());
		intent.putExtra(Constants.IMAGE_LIST_TITLE, name);

		switch (request_code) {
		case Constants.SEARCH_REQUEST:
		case Constants.HOME_SEARCH_REQUEST:
			intent.putExtra("request_code", Constants.SEARCH_REQUEST);
			Window window = HomeActivityGroup
					.getInstance()
					.getLocalActivityManager()
					.startActivity(BasicInfoActivity.class.getSimpleName(),
							intent);
			View view = window.getDecorView();
			HomeActivityGroup.getInstance().setContentView(view);
			break;

		case Constants.CATEGORY_REQUEST:
			intent.putExtra("request_code", Constants.CATEGORY_REQUEST);
			window = CategoryActivityGroup
					.getInstance()
					.getLocalActivityManager()
					.startActivity(BasicInfoActivity.class.getSimpleName(),
							intent);
			view = window.getDecorView();
			CategoryActivityGroup.getInstance().setContentView(view);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed()");

		Intent intent;
		Window window;
		View view;
		switch (request_code) {
		case Constants.CATEGORY_REQUEST:
			intent = new Intent(this, SecondCategoryActivity.class);
			window = CategoryActivityGroup
					.getInstance()
					.getLocalActivityManager()
					.startActivity(
							SecondCategoryActivity.class.getSimpleName(),
							intent);
			view = window.getDecorView();
			CategoryActivityGroup.getInstance().setContentView(view);
			break;
		case Constants.HOME_SEARCH_REQUEST:
		case Constants.SEARCH_REQUEST:
			intent = new Intent(this, HomeActivity.class);
			window = HomeActivityGroup.getInstance().getLocalActivityManager()
					.startActivity(HomeActivity.class.getSimpleName(), intent);
			view = window.getDecorView();
			HomeActivityGroup.getInstance().setContentView(view);
			break;
		default:
			break;
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (!isAdd) {
				Log.d(TAG, "isAdd");
				replace();
				isAdd = true;
			}

			TextView topSelect = (TextView) mRelaLayoutRoot
					.findViewWithTag("move");
			topSelect.setGravity(Gravity.CENTER);
			int tabLeft;
			int endLeft = 0;

			boolean run = false;

			switch (v.getId()) {
			case R.id.slide_tab1: // 商品价格（升序或者降序）
				if (mCurrent != R.id.slide_tab1) { // 首次点击按价格排序按钮，默认是升序排序
					Log.d(TAG, "R.id.slide_tab1");
					tabLeft = ((RelativeLayout) mTab1.getParent()).getLeft()
							+ mTab1.getLeft() + mTab1.getWidth() / 2;
					endLeft = tabLeft - selectWidth / 2;
					mCurrent = R.id.slide_tab1;
					topSelect.setText(mTab1.getText());
					run = true;
					reload(Constants.SORT_TYPE_PRICE_ASC);
				} else { // 否则，在升序和降序之间循环
					if (mSortType == Constants.SORT_TYPE_PRICE_ASC) {
						mSortType = Constants.SORT_TYPE_PRICE_DESC;
						reload(Constants.SORT_TYPE_PRICE_DESC);
					} else {
						mSortType = Constants.SORT_TYPE_PRICE_ASC;
						reload(Constants.SORT_TYPE_PRICE_ASC);
					}
				}
				break;
			case R.id.slide_tab2: // 上架时间
				if (mCurrent != R.id.slide_tab2) {
					Log.d(TAG, "R.id.slide_tab2");
					tabLeft = ((RelativeLayout) mTab2.getParent()).getLeft()
							+ mTab2.getLeft() + mTab2.getWidth() / 2;
					endLeft = tabLeft - selectWidth / 2;
					mCurrent = R.id.slide_tab2;
					topSelect.setText(mTab2.getText());
					run = true;
					reload(Constants.SORT_TYPE_UPLOAD_TIME);
				}
				break;
			case R.id.slide_tab3: // 更新时间
				if (mCurrent != R.id.slide_tab3) {
					Log.d(TAG, "R.id.slide_tab3");
					tabLeft = ((RelativeLayout) mTab3.getParent()).getLeft()
							+ mTab3.getLeft() + mTab3.getWidth() / 2;
					endLeft = tabLeft - selectWidth / 2;
					mCurrent = R.id.slide_tab3;
					topSelect.setText(mTab3.getText());
					run = true;
					reload(Constants.SORT_TYPE_UPDATE_TIME);
				}
				break;
			default:
				break;
			}

			if (run) {
				TranslateAnimation animation = new TranslateAnimation(
						startLeft, endLeft - firstLeft, 0, 0);
				startLeft = endLeft - firstLeft;
				animation.setDuration(100);
				animation.setFillAfter(true);
				topSelect.bringToFront();
				topSelect.startAnimation(animation);
			}
		}

	};

	private void reload(int sortType) {

		mImageManager.clear(); // 先将上次的清除
		mImageManager.load(sortType);

		mProDialog = new ProgressDialog(((ListActivity) mContext).getParent());
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.setMessage(getResources().getString(R.string.loading));
		mProDialog.setIndeterminate(true);

		setListAdapter(new ImageAdapter(mContext, mImageManager));

		// 当CategoryManager还在下载资源时，显示进度条为忙，并注册观察者用于下载结束时隐藏进度条
		if (mImageManager.isLoading()) {
			mProDialog.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.indeterminate_drawable));
			mProDialog.show();
			mImageManager.addObserver(mObserver);
		}
	}

	private void initDialog() {
		mProDialog = new ProgressDialog(this.getParent());
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.setMessage(getResources().getString(R.string.loading));
		mProDialog.setIndeterminate(true);
	}

	private void initUi() {
		TextView textView = (TextView) findViewById(R.id.image_text_view);
		textView.setText(name);

		mRelaLayoutRoot = (RelativeLayout) findViewById(R.id.image_list_root);

		mRelaLayout1 = (RelativeLayout) findViewById(R.id.slide_layout1);
		mRelaLayout2 = (RelativeLayout) findViewById(R.id.slide_layout2);
		mRelaLayout3 = (RelativeLayout) findViewById(R.id.slide_layout3);

		mTab1 = (TextView) findViewById(R.id.slide_tab1);
		mTab1.setOnClickListener(onClickListener);
		mTab2 = (TextView) findViewById(R.id.slide_tab2);
		mTab2.setOnClickListener(onClickListener);
		mTab3 = (TextView) findViewById(R.id.slide_tab3);
		mTab3.setOnClickListener(onClickListener);

		RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParam.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);

		mFirst = new TextView(this);
		mFirst.setTag("first");
		mFirst.setGravity(Gravity.CENTER);
		mFirst.setBackgroundResource(R.drawable.slide_bar);
		mFirst.setText(mTab2.getText());
		mFirst.setTextSize(18);

		switch (mCurrent) {
		case 1:
			mRelaLayout1.addView(mFirst, layoutParam);
			mCurrent = R.id.slide_tab1;
			break;
		case 2:
			mRelaLayout2.addView(mFirst, layoutParam);
			mCurrent = R.id.slide_tab2;
			break;
		case 3:
			mRelaLayout3.addView(mFirst, layoutParam);
			mCurrent = R.id.slide_tab3;
			break;
		default:
			break;
		}
	}

}
