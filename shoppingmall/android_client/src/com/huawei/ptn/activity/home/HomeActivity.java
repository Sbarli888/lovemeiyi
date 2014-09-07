package com.huawei.ptn.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;
import com.huawei.ptn.activity.HomeActivityGroup;
import com.huawei.ptn.activity.category.BasicInfoActivity;
import com.huawei.ptn.activity.category.ImageList;
import com.huawei.ptn.adapter.BestGalleryAdapter;
import com.huawei.ptn.adapter.HotGalleryAdapter;
import com.huawei.ptn.adapter.NewGalleryAdapter;
import com.huawei.ptn.common.Constants;
import com.huawei.ptn.common.HomeLayout;
import com.huawei.ptn.model.BannerItem;
import com.huawei.ptn.model.NewsItem;
import com.huawei.ptn.util.BannerManager;
import com.huawei.ptn.util.BestGoodsManager;
import com.huawei.ptn.util.HotGoodsManager;
import com.huawei.ptn.util.NewGoodsManager;
import com.huawei.ptn.util.ScreenTools;
import com.huawei.ptn.util.ViewUtils;
import com.huawei.ptn.view.ExpansionView;
import com.huawei.ptn.view.MyViewFlipper;

public class HomeActivity extends Activity {

	private static final String TAG = HomeActivity.class.getSimpleName();

	private int HOME_VOICE_RECOGNITION_REQUEST_CODE = 1235; // 语音识别请求码，取值任意

	private final Context mContext = this;

	private BannerManager mBannerManager;

	private ProgressDialog mProDialog;

	private MyViewFlipper viewFlipper;

	private ScrollView scrollView;
	private RelativeLayout bannerLayout;
	private LinearLayout radioLayout;

	private EditText m_Edit_search_input;
	private Button m_btn_search_submit;
	private TextView m_shake_search_btn;
	private TextView m_voice_search_btn;

	private LinearLayout indexContent;

	// 折叠模块的哈希表，用FunctionId索引
	private HashMap<String, ExpansionView> expansionMap = new HashMap();

	private ExpansionView m_bestgoods_expansionview;
	private ExpansionView m_newgoods_expansionview;
	private ExpansionView m_hotgoods_expansionview;
	private ExpansionView m_news_expansionview;

	private Gallery gallery_best;
	private Gallery gallery_new;
	private Gallery gallery_hot;

	private BestGoodsManager bestgoodsmanager;
	private NewGoodsManager newgoodsmanager;
	private HotGoodsManager hotgoodsmanager;

	private ListView news_ListView;
	private HashMap<Integer, NewsItem> mNewsData_act = new HashMap<Integer, NewsItem>();

	private MyDataSetObserver mObserver = new MyDataSetObserver();
	private Best_DataSetObserver observer_best;
	private New_DataSetObserver observer_new;
	private Hot_DataSetObserver observer_hot;

	private class MyDataSetObserver extends DataSetObserver {
		private boolean loaded = false;

		@Override
		public void onChanged() {
			if (!mBannerManager.isLoading()) {
				if (loaded == false) {
					loaded = true;
					int size = mBannerManager.size();
					viewFlipper.setSize(size);
					int spotSize = ScreenTools.getInstance(mContext).dip2px(10);
					for (int i = 0; i < size; i++) {
						BannerItem item = mBannerManager.get(i);
						// 初始化ViewFlipper
						ImageView flipperIv = new ImageView(mContext);
						Bitmap bitmap = item.getBitmap();
						if (bitmap != null) {
							flipperIv.setImageBitmap(item.getBitmap());
						} else {
							flipperIv.setImageResource(R.drawable.img_loading);
						}

						// 设置图片点击事件
						flipperIv.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								// TODO:根据四种分类分别进行跳转
							}

						});
						viewFlipper.addView(flipperIv);

						// 初始化Radio spot
						ImageView radioIv = new ImageView(mContext);
						LinearLayout.LayoutParams radioLayoutParams = new LinearLayout.LayoutParams(
								spotSize, spotSize);
						radioLayoutParams.setMargins(spotSize, 0, 0, 0);
						radioIv.setLayoutParams(radioLayoutParams);
						radioIv.setBackgroundResource(R.drawable.inactive_page_spot);
						radioLayout.addView(radioIv);

					}

					// 默认处于第一个图片
					if (radioLayout != null && radioLayout.getChildCount() > 0) {
						radioLayout.getChildAt(0).setBackgroundResource(
								R.drawable.active_page_spot);
					}

					// 设置监听器，当ViewFlipper中图片变化时，相应地改变radio的显示
					viewFlipper
							.setOnChangeListener(new MyViewFlipper.OnChangeListener() {

								public void onChange(int old, int current) {
									radioLayout
											.getChildAt(old)
											.setBackgroundResource(
													R.drawable.inactive_page_spot);
									radioLayout
											.getChildAt(current)
											.setBackgroundResource(
													R.drawable.active_page_spot);
								}

							});

					// 开始定时
					viewFlipper.startFlipperTimer();

					// 数据加载完成后，取消进度条的显示
					//mProDialog.dismiss();

					// 继续加载GOODS_BEST模块
					bestgoodsmanager.load(Constants.URL_GOODS_BEST);
				}
			}
			// 图片解码完毕后再刷新BANNER，未解码完时让其他模块继续加载
			if (!mBannerManager.isDecoding()) {
				Log.d(TAG, "View Flipper :: Decode");
				viewFlipper.removeAllViews();
				int size = mBannerManager.size();
				for (int i = 0; i < size; i++) {
					BannerItem item = mBannerManager.get(i);

					// 初始化ViewFlipper
					ImageView flipperIv = new ImageView(mContext);
					Bitmap bitmap = item.getBitmap();
					if (bitmap != null) {
						flipperIv.setImageBitmap(item.getBitmap());
					} else {
						flipperIv.setImageResource(R.drawable.img_loading);
					}
					viewFlipper.addView(flipperIv);
				}
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

		setContentView(R.layout.shop_home);

		// 初始化各布局及组件
		init();

		// 搜索功能的实现
		search();

		// BANNER广告
		Ad_Banner();

		// 精品推荐
		BestGoods();

		// 新品上市
		NewGoods();

		// 热卖商品
		HotGoods();

		// 站内新闻
		doNews();

	}

	private View addImageView(int id) {
		ImageView iv = new ImageView(this);
		iv.setImageResource(id);
		return iv;
	}

	private void Ad_Banner() {
		mBannerManager = new BannerManager(this);
		mBannerManager.clear();
		mBannerManager.load();

		mProDialog = new ProgressDialog(this.getParent());
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.setMessage(getResources().getString(R.string.loading));
		mProDialog.setIndeterminate(true);

		if (mBannerManager.isLoading()) {
			mProDialog.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.indeterminate_drawable));
			mProDialog.show();
			mBannerManager.addObserver(mObserver);
		}
	}

	private void init() {
		// 搜索
		m_Edit_search_input = (EditText) findViewById(R.id.search_key_input);
		m_btn_search_submit = (Button) findViewById(R.id.home_search_button);
		m_shake_search_btn = (TextView) findViewById(R.id.shake_search_btn_home);
		m_voice_search_btn = (TextView) findViewById(R.id.voice_search_btn_home);
		// BANNER广告
		scrollView = (ScrollView) findViewById(R.id.shop_home_scroller);

		viewFlipper = (MyViewFlipper) findViewById(R.id.home_view_flipper);
		bannerLayout = (RelativeLayout) findViewById(R.id.home_banner_rl);
		RelativeLayout.LayoutParams bannerLayoutParams = (LayoutParams) bannerLayout
				.getLayoutParams();
		bannerLayoutParams.height = ScreenTools.getInstance(mContext)
				.getSreenHeight() / 7;

		bannerLayout.setLayoutParams(bannerLayoutParams);

		viewFlipper.addView(addImageView(R.drawable.default_pic));

		radioLayout = (LinearLayout) findViewById(R.id.home_radio_ly);

		// 精品推荐/热卖商品/新品上市/文章列表 共享该布局，采用addView方法动态添加项
		indexContent = ((LinearLayout) findViewById(R.id.home_index_content));

	}

	private void search() {

		// 搜索输入框触碰DOWN时转向search_activity
		m_Edit_search_input.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					Intent i_search = new Intent(HomeActivity.this,
							SearchActivity.class);
					i_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					// 将Activity转换成view
					Window window = HomeActivityGroup
							.getInstance()
							.getLocalActivityManager()
							.startActivity(
									SearchActivity.class.getSimpleName(),
									i_search);
					View view = window.getDecorView();
					HomeActivityGroup.getInstance().setContentView(view);

				}
				return true;
			}
		});

		// 按键按下时搜索值为edit:hint的内容(连衣裙)
		m_btn_search_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent i_search = new Intent(HomeActivity.this, ImageList.class);
				i_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle data = new Bundle();

				data.putInt("request_code", Constants.HOME_SEARCH_REQUEST);
				data.putString("search_input", m_Edit_search_input.getHint()
						.toString());
				data.putString(Constants.IMAGE_LIST_TITLE, "\""
						+ m_Edit_search_input.getHint().toString() + "\""
						+ "的搜索结果：");
				i_search.putExtras(data);

				// 将Activity转换成view
				Window window = HomeActivityGroup
						.getInstance()
						.getLocalActivityManager()
						.startActivity(ImageList.class.getSimpleName(),
								i_search);
				View view = window.getDecorView();
				HomeActivityGroup.getInstance().setContentView(view);

				// startActivity(i_search);
			}
		});

		m_shake_search_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i_search = new Intent(HomeActivity.this,
						SearchActivity.class);
				i_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				// 将Activity转换成view
				Window window = HomeActivityGroup
						.getInstance()
						.getLocalActivityManager()
						.startActivity(SearchActivity.class.getSimpleName(),
								i_search);
				View view = window.getDecorView();
				HomeActivityGroup.getInstance().setContentView(view);
			}
		});

		PackageManager pm = getPackageManager();
		List activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {

			m_voice_search_btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startVoiceRecognitionActivity();
				}
			});

		} else {

			m_voice_search_btn.setEnabled(false);
			m_voice_search_btn.setText("Recognizer not present");

		}

	}

	private void startVoiceRecognitionActivity() {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "LoveMeiyi.com");

		// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.getParent().startActivityForResult(intent,
				HOME_VOICE_RECOGNITION_REQUEST_CODE);
	}

	public void handleActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == HOME_VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList voice_list = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			// mList.setAdapter(new ArrayAdapter(this,
			// android.R.layout.simple_list_item_1, voice_list));

			final String[] arrayOfString = new String[voice_list.size()];

			for (int i = 0; i < voice_list.size(); i++) {
				arrayOfString[i] = ((String) voice_list.get(i));
			}

			Builder alert = new AlertDialog.Builder(
					HomeActivity.this.getParent()).setTitle(
					getResources().getString(
							R.string.voice_search_please_choose)).setItems(
					arrayOfString, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							String search_key_words = arrayOfString[which];
							Intent i_search = new Intent(HomeActivity.this,
									ImageList.class);
							i_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							Bundle data = new Bundle();
							data.putInt("request_code",
									Constants.HOME_SEARCH_REQUEST);
							// Log.d(TAG, "m_Edit_search_input.getText() = " +
							// m_Edit_search_input.getText().toString());
							data.putString("search_input", search_key_words);
							data.putString(Constants.IMAGE_LIST_TITLE, "\""
									+ search_key_words + "\"" + "的搜索结果：");

							i_search.putExtras(data);

							Window window = HomeActivityGroup
									.getInstance()
									.getLocalActivityManager()
									.startActivity(
											ImageList.class.getSimpleName(),
											i_search);
							View view = window.getDecorView();
							HomeActivityGroup.getInstance()
									.setContentView(view);
						}

					});

			alert.show();

			/*
			 * this.dialogBuilder
			 * .setTitle(getResources().getString(R.string.shake_type))
			 * .setItems(arrayOfString, null) .show();
			 */
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		ViewUtils.showAlertDialog(this.getParent(),
				mContext.getResources().getString(R.string.logout_tips),
				mContext.getResources().getString(R.string.logout_content),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						MyApplication.getInstance().exit();
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		viewFlipper.startFlipperTimer();
	}

	@Override
	protected void onStop() {
		super.onStop();
		viewFlipper.stopFlipperTimer();
	}

	private void BestGoods() {

		m_bestgoods_expansionview = getExpansionView(Constants.HomeLayout_BestGoods.FUNCTIONID);
		m_bestgoods_expansionview.setChildView(View.inflate(HomeActivity.this,
				R.layout.app_gallery_best, null));

		m_bestgoods_expansionview.setTitleSuffix(true);

		HomeLayout Best_homelayout = new HomeLayout(
				Constants.HomeLayout_BestGoods.FUNCTIONID,
				Constants.HomeLayout_BestGoods.TITLE,
				Constants.HomeLayout_BestGoods.TYPE,
				Constants.HomeLayout_BestGoods.ISFOLD,
				Constants.HomeLayout_BestGoods.URL);

		m_bestgoods_expansionview.baseHome(Best_homelayout,
				new HomeActivity.OnExpandableListener() {

					public void onShow(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

					}

					public void onLoadData(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {
						showBestGallery(paramChildView, paramFunctionId, 0);

					}

					public void onHide(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

					}

				});
		HomeActivity.this.addView(this.m_bestgoods_expansionview.getRootView());
	}

	private class Best_DataSetObserver extends DataSetObserver {
		private View childview;
		private int m_position_init;
		private boolean loaded = false;

		public Best_DataSetObserver(final View childview,
				final int position_init, int type) {
			this.childview = childview;
			this.m_position_init = position_init;
		}

		public void onChanged() {
			Log.d(TAG, "onChanged()");

			if (!bestgoodsmanager.isLoading()) {
				TextView goods_name = (TextView) childview
						.findViewById(R.id.home_best_goods_item_name);
				goods_name.setText(bestgoodsmanager.get(
						m_position_init % bestgoodsmanager.size()).getName());

				TextView goods_price = (TextView) childview
						.findViewById(R.id.goods_Price);
				goods_price.setText("￥ "
						+ Double.toString(bestgoodsmanager.get(
								m_position_init % bestgoodsmanager.size())
								.getmShopPrice()));

				// 继续加载"新品上市"
				if (loaded == false) {
					newgoodsmanager.load(Constants.URL_GOODS_NEW);
					loaded = true;
				}
			}
		}
	};

	private void showBestGallery(View paramChildView, String paramFunctionId,
			int type) {

		final View childview = paramChildView;

		bestgoodsmanager = new BestGoodsManager(paramChildView.getContext());
		bestgoodsmanager.clear();

		// 由Ad_banner模块加载完后开启best_goods模块的加载线程
		// bestgoodsmanager.load();

		gallery_best = (Gallery) paramChildView
				.findViewById(R.id.home_best_gallery);
		final BestGalleryAdapter gallery_adapter = new BestGalleryAdapter(
				paramChildView.getContext(), bestgoodsmanager);
		gallery_best.setAdapter(gallery_adapter);

		// 开启新的线程加载5张图片，并通知adapter更新数据。

		final int m_position_init = Integer.MAX_VALUE / 2; // gallery初始position
		gallery_best.setCallbackDuringFling(false);
		gallery_best.setSelection(m_position_init); // 设置初始化显示中间的图片，形成左右都可循环的效果

		// 观察加载线程，商品名、加载完后立即显示, 图片待加载完后再显示
		observer_best = new Best_DataSetObserver(paramChildView,
				m_position_init, type);
		bestgoodsmanager.addObserver(observer_best);

		gallery_best.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				Log.d(TAG, "onItemSelected");

				if (!bestgoodsmanager.isLoading()) {
					TextView goods_name = (TextView) childview
							.findViewById(R.id.home_best_goods_item_name);
					goods_name.setText(bestgoodsmanager.get(
							arg2 % bestgoodsmanager.size()).getName());

					TextView goods_price = (TextView) childview
							.findViewById(R.id.goods_Price);
					goods_price.setText("￥ "
							+ Double.toString(bestgoodsmanager.get(
									arg2 % bestgoodsmanager.size())
									.getmShopPrice()));

					gallery_adapter.setSelectItem(arg2);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		gallery_best.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				long goods_id = bestgoodsmanager.get(
						arg2 % bestgoodsmanager.size()).getId();
				String goods_name = bestgoodsmanager.get(
						arg2 % bestgoodsmanager.size()).getName();
				startBasicInfoActivity(goods_id, goods_name);
			}
		});

		m_bestgoods_expansionview.expand();
	}

	private void NewGoods() {

		m_newgoods_expansionview = getExpansionView(Constants.HomeLayout_NewGoods.FUNCTIONID);
		m_newgoods_expansionview.setChildView(View.inflate(HomeActivity.this,
				R.layout.app_gallery_new, null));

		m_newgoods_expansionview.setTitleSuffix(true);

		HomeLayout New_homelayout = new HomeLayout(
				Constants.HomeLayout_NewGoods.FUNCTIONID,
				Constants.HomeLayout_NewGoods.TITLE,
				Constants.HomeLayout_NewGoods.TYPE,
				Constants.HomeLayout_NewGoods.ISFOLD,
				Constants.HomeLayout_NewGoods.URL);

		m_newgoods_expansionview.baseHome(New_homelayout,
				new HomeActivity.OnExpandableListener() {

					public void onShow(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

					}

					public void onLoadData(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {
						showNewGallery(paramChildView, paramFunctionId, false);

					}

					public void onHide(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

					}

				});
		HomeActivity.this.addView(this.m_newgoods_expansionview.getRootView());
	}

	private class New_DataSetObserver extends DataSetObserver {
		private View childview;
		private int m_position_init;
		private boolean loaded = false;

		public New_DataSetObserver(final View childview, final int position_init) {
			this.childview = childview;
			this.m_position_init = position_init;
		}

		public void onChanged() {
			Log.d(TAG, "New_DataSetObserver :: onChanged()");

			if (!newgoodsmanager.isLoading()) {
				TextView goods_name = (TextView) childview
						.findViewById(R.id.home_new_goods_item_name);
				goods_name.setText(newgoodsmanager.get(
						m_position_init % newgoodsmanager.size()).getName());

				TextView goods_price = (TextView) childview
						.findViewById(R.id.goods_Price);
				goods_price.setText("￥ "
						+ Double.toString(newgoodsmanager.get(
								m_position_init % newgoodsmanager.size())
								.getmShopPrice()));

				// 继续加载"新品上市"
				if (loaded == false) {
					hotgoodsmanager.load(Constants.URL_GOODS_HOT);
					loaded = true;
				}

			}
		}
	};

	private void showNewGallery(View paramChildView, String paramFunctionId,
			boolean paramBoolean) {

		final View childview = paramChildView;

		newgoodsmanager = new NewGoodsManager(paramChildView.getContext());
		newgoodsmanager.clear();
		// 由best_goods模块加载完后开启new_goods模块的加载线程

		gallery_new = (Gallery) paramChildView
				.findViewById(R.id.home_new_gallery);
		final NewGalleryAdapter gallery_adapter = new NewGalleryAdapter(
				paramChildView.getContext(), newgoodsmanager);
		gallery_new.setAdapter(gallery_adapter);

		// 开启新的线程加载5张图片，并通知adapter更新数据。

		final int m_position_init = Integer.MAX_VALUE / 2; // gallery初始position
		gallery_new.setCallbackDuringFling(false);
		gallery_new.setSelection(m_position_init); // 设置初始化显示中间的图片，形成左右都可循环的效果

		// 观察加载线程，商品名、加载完后立即显示, 图片待加载完后再显示
		observer_new = new New_DataSetObserver(paramChildView, m_position_init);
		newgoodsmanager.addObserver(observer_new);

		gallery_new.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				Log.d(TAG, "onItemSelected");

				if (!newgoodsmanager.isLoading()) {
					TextView goods_name = (TextView) childview
							.findViewById(R.id.home_new_goods_item_name);
					goods_name.setText(newgoodsmanager.get(
							arg2 % newgoodsmanager.size()).getName());

					TextView goods_price = (TextView) childview
							.findViewById(R.id.goods_Price);
					goods_price.setText("￥ "
							+ Double.toString(newgoodsmanager.get(
									arg2 % newgoodsmanager.size())
									.getmShopPrice()));

					gallery_adapter.setSelectItem(arg2);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		gallery_new.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				long goods_id = newgoodsmanager.get(
						arg2 % newgoodsmanager.size()).getId();
				String goods_name = newgoodsmanager.get(
						arg2 % newgoodsmanager.size()).getName();
				startBasicInfoActivity(goods_id, goods_name);
			}
		});

		m_newgoods_expansionview.expand();
	}

	private void HotGoods() {

		m_hotgoods_expansionview = getExpansionView(Constants.HomeLayout_HotGoods.FUNCTIONID);
		m_hotgoods_expansionview.setChildView(View.inflate(HomeActivity.this,
				R.layout.app_gallery_best, null));

		m_hotgoods_expansionview.setTitleSuffix(true);

		HomeLayout Hot_homelayout = new HomeLayout(
				Constants.HomeLayout_HotGoods.FUNCTIONID,
				Constants.HomeLayout_HotGoods.TITLE,
				Constants.HomeLayout_HotGoods.TYPE,
				Constants.HomeLayout_HotGoods.ISFOLD,
				Constants.HomeLayout_HotGoods.URL);

		m_hotgoods_expansionview.baseHome(Hot_homelayout,
				new HomeActivity.OnExpandableListener() {

					public void onShow(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

					}

					public void onLoadData(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {
						showHotGallery(paramChildView, paramFunctionId, false);

					}

					public void onHide(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

					}

				});
		HomeActivity.this.addView(this.m_hotgoods_expansionview.getRootView());
	}

	private class Hot_DataSetObserver extends DataSetObserver {
		private View childview;
		private int m_position_init;

		public Hot_DataSetObserver(final View childview, final int position_init) {
			this.childview = childview;
			this.m_position_init = position_init;
		}

		public void onChanged() {
			Log.d(TAG, "onChanged()");

			if (!hotgoodsmanager.isLoading()) {
				TextView goods_name = (TextView) childview
						.findViewById(R.id.home_best_goods_item_name);
				goods_name.setText(hotgoodsmanager.get(
						m_position_init % hotgoodsmanager.size()).getName());

				TextView goods_price = (TextView) childview
						.findViewById(R.id.goods_Price);
				goods_price.setText("￥ "
						+ Double.toString(hotgoodsmanager.get(
								m_position_init % hotgoodsmanager.size())
								.getmShopPrice()));

				//取消进度条
				mProDialog.dismiss();
			}
		}
	};

	private void showHotGallery(View paramChildView, String paramFunctionId,
			boolean paramBoolean) {

		final View childview = paramChildView;

		hotgoodsmanager = new HotGoodsManager(paramChildView.getContext());

		// 由best_goods模块加载完后开启new_goods模块的加载线程

		gallery_hot = (Gallery) paramChildView
				.findViewById(R.id.home_best_gallery);
		final HotGalleryAdapter gallery_adapter = new HotGalleryAdapter(
				paramChildView.getContext(), hotgoodsmanager);
		gallery_hot.setAdapter(gallery_adapter);

		// 开启新的线程加载5张图片，并通知adapter更新数据。

		final int m_position_init = Integer.MAX_VALUE / 2; // gallery初始position
		gallery_hot.setCallbackDuringFling(false);
		gallery_hot.setSelection(m_position_init); // 设置初始化显示中间的图片，形成左右都可循环的效果

		// 观察加载线程，商品名、加载完后立即显示, 图片待加载完后再显示
		observer_hot = new Hot_DataSetObserver(paramChildView, m_position_init);
		hotgoodsmanager.addObserver(observer_hot);

		gallery_hot.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				Log.d(TAG, "onItemSelected");

				if (!hotgoodsmanager.isLoading()) {
					TextView goods_name = (TextView) childview
							.findViewById(R.id.home_best_goods_item_name);
					goods_name.setText(hotgoodsmanager.get(
							arg2 % hotgoodsmanager.size()).getName());

					TextView goods_price = (TextView) childview
							.findViewById(R.id.goods_Price);
					goods_price.setText("￥ "
							+ Double.toString(hotgoodsmanager.get(
									arg2 % hotgoodsmanager.size())
									.getmShopPrice()));

					gallery_adapter.setSelectItem(arg2);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		gallery_hot.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				long goods_id = hotgoodsmanager.get(
						arg2 % hotgoodsmanager.size()).getId();
				String goods_name = hotgoodsmanager.get(
						arg2 % hotgoodsmanager.size()).getName();
				startBasicInfoActivity(goods_id, goods_name);
			}
		});

		m_hotgoods_expansionview.expand();
	}

	private void doNews() {

		// 初始化ExpansionView对象
		m_news_expansionview = getExpansionView(Constants.HomeLayout_News.FUNCTIONID);

		// 填充下拉布局
		m_news_expansionview.setChildView(View.inflate(HomeActivity.this,
				R.layout.app_news, null));

		// 初始化为展开状态
		m_news_expansionview.setTitleSuffix(true);

		HomeLayout news_homelayout = new HomeLayout(
				Constants.HomeLayout_News.FUNCTIONID,
				Constants.HomeLayout_News.TITLE,
				Constants.HomeLayout_News.TYPE,
				Constants.HomeLayout_News.ISFOLD, Constants.HomeLayout_News.URL);

		m_news_expansionview.baseHome(news_homelayout,
				new HomeActivity.OnExpandableListener() {

					public void onHide(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

					}

					public void onLoadData(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

						showNewsList(paramChildView, paramFunctionId, false);

					}

					public void onShow(View paramHomeItemView,
							View paramChildView, String paramFunctionId) {

						// showNewsList(paramChildView, paramFunctionId, true);

					}

				});

		HomeActivity.this.addView(this.m_news_expansionview.getRootView());
	}

	public void showNewsList(View paramChildView, String paramFunctionId,
			boolean paramBoolean) {
		final View childview = paramChildView;
		// 新闻标题
		TextView localTextView = (TextView) paramChildView
				.findViewById(R.id.reportText);

		// 新闻标题列表的listview
		news_ListView = (ListView) paramChildView
				.findViewById(R.id.report_list);

		// 根据paramFunctionId找到ExpansionView
		m_news_expansionview = expansionMap.get(paramFunctionId);

		final ArrayList<Map<String, Object>> mNewsData_listview = new ArrayList<Map<String, Object>>();

		final Handler newshandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case 1:

						SimpleAdapter adapter = new SimpleAdapter(
								childview.getContext(), mNewsData_listview,
								R.layout.home_news_item,
								new String[] { "title" },
								new int[] { R.id.news_title });

						news_ListView.setAdapter(adapter);

						// 展开
						// localExpansionView.setTitleSuffix(false);
						m_news_expansionview.collapse();

						news_ListView
								.setOnItemClickListener(new OnItemClickListener() {

									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										switch (arg1.getId()) {
										case R.id.report_item:

											startNewsDetailActivity(arg2);
											Log.d(TAG, "list_position = "
													+ arg2);

											break;
										}
									}
								});
						break;
					}
				}
			}
		};

		new Thread() {
			public void run() {
				int[] news_id = { 1, 21, 12, 6 };
				String[] news_title = { "新闻1", "新闻2", "新闻3", "新闻4" };

				for (int i = 0; i < news_title.length; i++) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("title", news_title[i]);
					mNewsData_listview.add(item);

					NewsItem news_item = new NewsItem(news_id[i],
							news_title[i], null);

					mNewsData_act.put(i, news_item);
				}

				Message msg = newshandler.obtainMessage(1);
				newshandler.sendMessage(msg);
			}
		}.start();

		localTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.d(TAG, "news more Clicked");
				startNewsListctivity();
			}
		});

	}

	private void startNewsDetailActivity(int position) {

		Intent i_newsdetail = new Intent(HomeActivity.this,
				NewsDetailActivity.class);
		i_newsdetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// 获取news_id
		int news_id = mNewsData_act.get(position).getNewsId();
		Bundle data = new Bundle();
		data.putInt("news_id", news_id);

		i_newsdetail.putExtras(data);

		// 将Activity转换成view
		Window window = HomeActivityGroup
				.getInstance()
				.getLocalActivityManager()
				.startActivity(NewsDetailActivity.class.getSimpleName(),
						i_newsdetail);
		View view = window.getDecorView();
		HomeActivityGroup.getInstance().setContentView(view);
	}

	private void startNewsListctivity() {

		Intent i_newslist = new Intent(HomeActivity.this,
				NewsListActivity.class);
		i_newslist.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// 将Activity转换成view
		Window window = HomeActivityGroup
				.getInstance()
				.getLocalActivityManager()
				.startActivity(NewsListActivity.class.getSimpleName(),
						i_newslist);
		View view = window.getDecorView();
		HomeActivityGroup.getInstance().setContentView(view);

	}

	private void startBasicInfoActivity(long goods_id, String goods_name) {
		Log.d(TAG, "startBasicInfoActivity goods_id=" + goods_id
				+ "  goods_name=" + goods_name);

		Intent i_BasicInfo = new Intent(HomeActivity.this,
				BasicInfoActivity.class);
		i_BasicInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Bundle data = new Bundle();
		data.putInt("request_code", Constants.HOME_BEST_REQUEST);
		data.putLong(Constants.GOODS_ID, goods_id);
		data.putString(Constants.IMAGE_LIST_TITLE, goods_name);

		i_BasicInfo.putExtras(data);

		// 将Activity转换成view
		Window window = HomeActivityGroup
				.getInstance()
				.getLocalActivityManager()
				.startActivity(BasicInfoActivity.class.getSimpleName(),
						i_BasicInfo);
		View view = window.getDecorView();
		HomeActivityGroup.getInstance().setContentView(view);
	}

	/*
	 * @Function Name: getExpansionView
	 * 
	 * @Description : 通过模块功能ID去索引ExpansionView，如果没有则创建
	 * 
	 * @param : String paramFunctionId, 功能模块ID，例如: "精彩推荐"、"站内新闻"等
	 * 
	 * @return : ExpansionView
	 * 
	 * * @Create Date : 2012-04-23
	 */
	private ExpansionView getExpansionView(String paramFunctionId) {

		ExpansionView localExpansionView = (ExpansionView) expansionMap
				.get(paramFunctionId);

		if (localExpansionView == null) {
			localExpansionView = new ExpansionView(this);

			if (expansionMap.containsKey(paramFunctionId)) {
				expansionMap.remove(paramFunctionId);
			}

			expansionMap.put(paramFunctionId, localExpansionView);
		}

		return localExpansionView;
	}

	/*
	 * @Function Name: addview
	 * 
	 * @Description : 通过LinearLayout::addView方法动态添加线性布局项
	 * 
	 * @param : View paramExpansionView 折叠式布局
	 * 
	 * @return : void
	 * 
	 * @Create Date : 2012-04-23
	 */
	private void addView(View paramExpansionView) {
		// LinearLayout布局添加组件到容器之前，必须设置它的LinearLayout.LayoutParams (指定组件的宽度和高度)
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
				-1, -2);
		localLayoutParams.setMargins(0, ScreenTools.getInstance(mContext)
				.dip2px(4), 0, 0);

		// 如果已经存在则删除后新增
		if (this.indexContent.indexOfChild(paramExpansionView) != -1)
			this.indexContent.removeView(paramExpansionView);

		this.indexContent.addView(paramExpansionView, localLayoutParams);
	}

	/*
	 * @Class Name : OnExpandableListener
	 * 
	 * @Description : 抽象类，由具体的功能模块实现方法完成数据加载、隐藏、显示等功能
	 * 
	 * @Create Date : 2012-04-23
	 */
	public static interface OnExpandableListener {
		// 初始化时加载数据
		public abstract void onLoadData(View paramHomeItemView,
				View paramChildView, String paramFunctionId);

		// 隐藏时需要完成的操作
		public abstract void onHide(View paramHomeItemView,
				View paramChildView, String paramFunctionId);

		// 展开时需要完成的操作
		public abstract void onShow(View paramHomeItemView,
				View paramChildView, String paramFunctionId);
	}
}
