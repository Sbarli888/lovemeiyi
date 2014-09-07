package com.huawei.ptn.activity.category;

import java.util.HashMap;
import java.util.Iterator;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;
import com.huawei.ptn.activity.CategoryActivityGroup;
import com.huawei.ptn.activity.HomeActivityGroup;
import com.huawei.ptn.activity.home.HomeActivity;
import com.huawei.ptn.activity.home.SearchActivity;
import com.huawei.ptn.common.Constants;
import com.huawei.ptn.model.BasicInfoItem;
import com.huawei.ptn.util.BasicInfoManager;
import com.huawei.ptn.util.ScreenTools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BasicInfoActivity extends Activity {

	private static final String TAG = BasicInfoActivity.class.getSimpleName();

	private Context mContext = this;

	private int request_code;

	private long id;

	private String name;

	private BasicInfoManager mBasicInfoManager;

	private LayoutInflater mLayoutInflater;

	private ProgressDialog mProDialog;

	private TextView mTitleTextView;

	private ImageView mImageView;

	private MyDataSetObserver mObserver = new MyDataSetObserver();

	private class MyDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			if (!mBasicInfoManager.isLoading()) {

				// 说明数据下载完成，在此处进行布局内容的填充
				LinearLayout rootLayout = (LinearLayout) ((Activity) mContext)
						.findViewById(R.id.goods_basic_info_sub_root);
				rootLayout.setVisibility(View.VISIBLE);

				if (mBasicInfoManager.size() == 0) {
					mProDialog.dismiss();
					return;
				}

				// 获取下载到的数据
				final BasicInfoItem bii = mBasicInfoManager.get(0);

				// 布局图像视图
				if (mImageView != null) {
					mImageView.setImageBitmap(bii.getBitmap());
				}

				// 布局商品基本内容
				TextView contentTextView = (TextView) ((Activity) mContext)
						.findViewById(R.id.goods_basic_info);
				String content = bii.getName()
						+ getResources().getString(R.string.crlf)
						+ getResources().getString(R.string.shop_price)
						+ bii.getmShopPrice()
						+ getResources().getString(R.string.price_unit)
						+ getResources().getString(R.string.crlf)
						+ getResources().getString(R.string.market_price)
						+ bii.getmMarketPrice()
						+ getResources().getString(R.string.price_unit)
						+ getResources().getString(R.string.crlf)
						+ getResources().getString(R.string.leftover)
						+ bii.getLeftover();

				contentTextView.setTextSize(getResources().getDimension(
						R.dimen.little_font_size));
				contentTextView.setTextColor(getResources().getColor(
						R.color.black));
				contentTextView.setText(content);

				// 布局"查看详情"视图
				TextView checkDetailTv = (TextView) ((Activity) mContext)
						.findViewById(R.id.check_details);
				checkDetailTv.setClickable(true);
				
				checkDetailTv.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent(BasicInfoActivity.this,
								GoodsDetailActivity.class);
						intent.putExtra("goods_id", bii.getId());
						startActivity(intent);
					}

				});

				// 布局"提示"视图
				LinearLayout tipsLayout = (LinearLayout) ((Activity) mContext)
						.findViewById(R.id.basic_tips_root);
				HashMap<String, String> tipsMap = (HashMap<String, String>) bii
						.getTips();
				Iterator<String> it = tipsMap.keySet().iterator();
				String tipsTitle;
				String tipsContent;
				TextView tipsTextView;
				ImageView dividerImageView;

				while (it.hasNext()) {
					tipsTitle = it.next();
					tipsContent = tipsMap.get(tipsTitle);

					// 标题
					tipsTextView = (TextView) mLayoutInflater.inflate(
							R.layout.text_view, null);
					tipsTextView.setTextSize(getResources().getDimension(
							R.dimen.middle_font_size));
					tipsTextView.setTextColor(getResources().getColor(
							R.color.chocolate));
					tipsTextView.setText(tipsTitle);
					tipsLayout.addView(tipsTextView);

					// 分隔符
					dividerImageView = (ImageView) mLayoutInflater.inflate(
							R.layout.divider, null);
					tipsLayout.addView(dividerImageView);

					// 内容
					tipsTextView = (TextView) mLayoutInflater.inflate(
							R.layout.text_view, null);
					tipsTextView.setTextSize(getResources().getDimension(
							R.dimen.little_font_size));
					tipsTextView.setTextColor(getResources().getColor(
							R.color.black));
					tipsTextView.setText(android.text.Html
							.fromHtml(tipsContent).toString().trim());
					tipsLayout.addView(tipsTextView);

					// 分隔符
					dividerImageView = (ImageView) mLayoutInflater.inflate(
							R.layout.divider, null);
					tipsLayout.addView(dividerImageView);
				}

				// "客服联系方式"视图
				final String phoneNumber = bii.getPhoneNumber();
				String qqNumber = bii.getQqNumber();
				TextView phoneTv = (TextView) ((Activity) mContext)
						.findViewById(R.id.phone_number);
				phoneTv.append(":" + phoneNumber);
				phoneTv.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent(
								"android.intent.action.DIAL", Uri.parse("tel:"
										+ phoneNumber));
						startActivityForResult(intent, 10);
					}

				});

				TextView qqTv = (TextView) ((Activity) mContext)
						.findViewById(R.id.qq_number);
				qqTv.append(":" + qqNumber);
				qqTv.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						Intent intent = new Intent();
						ComponentName cn = new ComponentName("com.tencent.qq",
								"com.tencent.qq.SplashActivity");
						intent.setComponent(cn);
						intent.setAction("android.intent.action.MAIN");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}

				});

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
		setContentView(R.layout.goods_basic_info);
		
		getDataFromIntent();
		initUi();
		initManager();
	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed()");

		Intent intent;
		Window window;
		View view;
		switch (request_code) {
		case Constants.CATEGORY_REQUEST:
			intent = new Intent(this, ImageList.class);
			window = CategoryActivityGroup.getInstance()
					.getLocalActivityManager()
					.startActivity(ImageList.class.getSimpleName(), intent);
			view = window.getDecorView();
			CategoryActivityGroup.getInstance().setContentView(view);
			break;
		case Constants.HOME_SEARCH_REQUEST:
		case Constants.SEARCH_REQUEST:
			intent = new Intent(this, ImageList.class);
			window = HomeActivityGroup.getInstance().getLocalActivityManager()
					.startActivity(ImageList.class.getSimpleName(), intent);
			view = window.getDecorView();
			HomeActivityGroup.getInstance().setContentView(view);
		case Constants.SHAKE_SEARCH_REQUEST:
			intent = new Intent(this, SearchActivity.class);
			window = HomeActivityGroup
					.getInstance()
					.getLocalActivityManager()
					.startActivity(SearchActivity.class.getSimpleName(), intent);
			view = window.getDecorView();
			HomeActivityGroup.getInstance().setContentView(view);
			break;
		case Constants.HOME_BEST_REQUEST:
			intent = new Intent(this, HomeActivity.class);
			window = HomeActivityGroup
					.getInstance()
					.getLocalActivityManager()
					.startActivity(HomeActivity.class.getSimpleName(), intent);
			view = window.getDecorView();
			HomeActivityGroup.getInstance().setContentView(view);
			break;
		default:
			break;
		}
	}
	
	private void getDataFromIntent() {
		Intent intent = getIntent();
		id = intent.getLongExtra(Constants.GOODS_ID, 0);
		name = intent.getStringExtra(Constants.IMAGE_LIST_TITLE);

		request_code = intent.getIntExtra("request_code", 0);
		if ((request_code == Constants.SEARCH_REQUEST)
				|| (request_code == Constants.SHAKE_SEARCH_REQUEST)
				|| (request_code == Constants.HOME_BEST_REQUEST)) {
			name = getResources().getString(R.string.goods_basic_info);
		}
		
		Log.d(TAG, "goods_id=" + id +"  goods_name=" + name);
		
	}
	
	private void initUi() {
		mLayoutInflater = getLayoutInflater();
		
		// 布局标题栏
		mTitleTextView = (TextView) findViewById(R.id.basic_title_text_view);
		// String series = getResources().getString(R.string.Series);
		// mTitleTextView.setText(name + series);
		mTitleTextView.setText(name);

		// 布局图像视图
		mImageView = (ImageView) findViewById(R.id.basic_image_view);
		// TODO:加载默认图片
		// imageView.setImageBitmap(bii.getBitmap());
		mImageView.setBackgroundResource(R.drawable.picture_frame);
		LayoutParams layoutPara = mImageView.getLayoutParams();

		layoutPara.width = ScreenTools.getInstance(mContext).getWidth();
		layoutPara.height = 5 * ScreenTools.getInstance(mContext).getHeight() / 16;
		mImageView.setLayoutParams(layoutPara);
		
		// 初始化进度对话框
		mProDialog = new ProgressDialog(this.getParent());
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.setMessage(getResources().getString(R.string.loading));
		mProDialog.setIndeterminate(true);
	}
	
	private void initManager() {
		// 初始化BasicInfoManager
		mBasicInfoManager = new BasicInfoManager(this);
		mBasicInfoManager.clear();
		mBasicInfoManager.load(id);

		// 当CategoryManager还在下载资源时，显示进度条为忙，并注册观察者用于下载结束时隐藏进度条
		if (mBasicInfoManager.isLoading()) {
			mProDialog.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.indeterminate_drawable));
			mProDialog.show();
			mBasicInfoManager.addObserver(mObserver);
		}
	}

}
