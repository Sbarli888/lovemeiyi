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

	private int HOME_VOICE_RECOGNITION_REQUEST_CODE = 1235; // ����ʶ�������룬ȡֵ����

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

	// �۵�ģ��Ĺ�ϣ����FunctionId����
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
						// ��ʼ��ViewFlipper
						ImageView flipperIv = new ImageView(mContext);
						Bitmap bitmap = item.getBitmap();
						if (bitmap != null) {
							flipperIv.setImageBitmap(item.getBitmap());
						} else {
							flipperIv.setImageResource(R.drawable.img_loading);
						}

						// ����ͼƬ����¼�
						flipperIv.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								// TODO:�������ַ���ֱ������ת
							}

						});
						viewFlipper.addView(flipperIv);

						// ��ʼ��Radio spot
						ImageView radioIv = new ImageView(mContext);
						LinearLayout.LayoutParams radioLayoutParams = new LinearLayout.LayoutParams(
								spotSize, spotSize);
						radioLayoutParams.setMargins(spotSize, 0, 0, 0);
						radioIv.setLayoutParams(radioLayoutParams);
						radioIv.setBackgroundResource(R.drawable.inactive_page_spot);
						radioLayout.addView(radioIv);

					}

					// Ĭ�ϴ��ڵ�һ��ͼƬ
					if (radioLayout != null && radioLayout.getChildCount() > 0) {
						radioLayout.getChildAt(0).setBackgroundResource(
								R.drawable.active_page_spot);
					}

					// ���ü���������ViewFlipper��ͼƬ�仯ʱ����Ӧ�ظı�radio����ʾ
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

					// ��ʼ��ʱ
					viewFlipper.startFlipperTimer();

					// ���ݼ�����ɺ�ȡ������������ʾ
					//mProDialog.dismiss();

					// ��������GOODS_BESTģ��
					bestgoodsmanager.load(Constants.URL_GOODS_BEST);
				}
			}
			// ͼƬ������Ϻ���ˢ��BANNER��δ������ʱ������ģ���������
			if (!mBannerManager.isDecoding()) {
				Log.d(TAG, "View Flipper :: Decode");
				viewFlipper.removeAllViews();
				int size = mBannerManager.size();
				for (int i = 0; i < size; i++) {
					BannerItem item = mBannerManager.get(i);

					// ��ʼ��ViewFlipper
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

		// ��ʼ�������ּ����
		init();

		// �������ܵ�ʵ��
		search();

		// BANNER���
		Ad_Banner();

		// ��Ʒ�Ƽ�
		BestGoods();

		// ��Ʒ����
		NewGoods();

		// ������Ʒ
		HotGoods();

		// վ������
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
		// ����
		m_Edit_search_input = (EditText) findViewById(R.id.search_key_input);
		m_btn_search_submit = (Button) findViewById(R.id.home_search_button);
		m_shake_search_btn = (TextView) findViewById(R.id.shake_search_btn_home);
		m_voice_search_btn = (TextView) findViewById(R.id.voice_search_btn_home);
		// BANNER���
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

		// ��Ʒ�Ƽ�/������Ʒ/��Ʒ����/�����б� ����ò��֣�����addView������̬�����
		indexContent = ((LinearLayout) findViewById(R.id.home_index_content));

	}

	private void search() {

		// �����������DOWNʱת��search_activity
		m_Edit_search_input.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					Intent i_search = new Intent(HomeActivity.this,
							SearchActivity.class);
					i_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					// ��Activityת����view
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

		// ��������ʱ����ֵΪedit:hint������(����ȹ)
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
						+ "�����������");
				i_search.putExtras(data);

				// ��Activityת����view
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

				// ��Activityת����view
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
									+ search_key_words + "\"" + "�����������");

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
				goods_price.setText("�� "
						+ Double.toString(bestgoodsmanager.get(
								m_position_init % bestgoodsmanager.size())
								.getmShopPrice()));

				// ��������"��Ʒ����"
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

		// ��Ad_bannerģ����������best_goodsģ��ļ����߳�
		// bestgoodsmanager.load();

		gallery_best = (Gallery) paramChildView
				.findViewById(R.id.home_best_gallery);
		final BestGalleryAdapter gallery_adapter = new BestGalleryAdapter(
				paramChildView.getContext(), bestgoodsmanager);
		gallery_best.setAdapter(gallery_adapter);

		// �����µ��̼߳���5��ͼƬ����֪ͨadapter�������ݡ�

		final int m_position_init = Integer.MAX_VALUE / 2; // gallery��ʼposition
		gallery_best.setCallbackDuringFling(false);
		gallery_best.setSelection(m_position_init); // ���ó�ʼ����ʾ�м��ͼƬ���γ����Ҷ���ѭ����Ч��

		// �۲�����̣߳���Ʒ�����������������ʾ, ͼƬ�������������ʾ
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
					goods_price.setText("�� "
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
				goods_price.setText("�� "
						+ Double.toString(newgoodsmanager.get(
								m_position_init % newgoodsmanager.size())
								.getmShopPrice()));

				// ��������"��Ʒ����"
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
		// ��best_goodsģ����������new_goodsģ��ļ����߳�

		gallery_new = (Gallery) paramChildView
				.findViewById(R.id.home_new_gallery);
		final NewGalleryAdapter gallery_adapter = new NewGalleryAdapter(
				paramChildView.getContext(), newgoodsmanager);
		gallery_new.setAdapter(gallery_adapter);

		// �����µ��̼߳���5��ͼƬ����֪ͨadapter�������ݡ�

		final int m_position_init = Integer.MAX_VALUE / 2; // gallery��ʼposition
		gallery_new.setCallbackDuringFling(false);
		gallery_new.setSelection(m_position_init); // ���ó�ʼ����ʾ�м��ͼƬ���γ����Ҷ���ѭ����Ч��

		// �۲�����̣߳���Ʒ�����������������ʾ, ͼƬ�������������ʾ
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
					goods_price.setText("�� "
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
				goods_price.setText("�� "
						+ Double.toString(hotgoodsmanager.get(
								m_position_init % hotgoodsmanager.size())
								.getmShopPrice()));

				//ȡ��������
				mProDialog.dismiss();
			}
		}
	};

	private void showHotGallery(View paramChildView, String paramFunctionId,
			boolean paramBoolean) {

		final View childview = paramChildView;

		hotgoodsmanager = new HotGoodsManager(paramChildView.getContext());

		// ��best_goodsģ����������new_goodsģ��ļ����߳�

		gallery_hot = (Gallery) paramChildView
				.findViewById(R.id.home_best_gallery);
		final HotGalleryAdapter gallery_adapter = new HotGalleryAdapter(
				paramChildView.getContext(), hotgoodsmanager);
		gallery_hot.setAdapter(gallery_adapter);

		// �����µ��̼߳���5��ͼƬ����֪ͨadapter�������ݡ�

		final int m_position_init = Integer.MAX_VALUE / 2; // gallery��ʼposition
		gallery_hot.setCallbackDuringFling(false);
		gallery_hot.setSelection(m_position_init); // ���ó�ʼ����ʾ�м��ͼƬ���γ����Ҷ���ѭ����Ч��

		// �۲�����̣߳���Ʒ�����������������ʾ, ͼƬ�������������ʾ
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
					goods_price.setText("�� "
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

		// ��ʼ��ExpansionView����
		m_news_expansionview = getExpansionView(Constants.HomeLayout_News.FUNCTIONID);

		// �����������
		m_news_expansionview.setChildView(View.inflate(HomeActivity.this,
				R.layout.app_news, null));

		// ��ʼ��Ϊչ��״̬
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
		// ���ű���
		TextView localTextView = (TextView) paramChildView
				.findViewById(R.id.reportText);

		// ���ű����б��listview
		news_ListView = (ListView) paramChildView
				.findViewById(R.id.report_list);

		// ����paramFunctionId�ҵ�ExpansionView
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

						// չ��
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
				String[] news_title = { "����1", "����2", "����3", "����4" };

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

		// ��ȡnews_id
		int news_id = mNewsData_act.get(position).getNewsId();
		Bundle data = new Bundle();
		data.putInt("news_id", news_id);

		i_newsdetail.putExtras(data);

		// ��Activityת����view
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

		// ��Activityת����view
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

		// ��Activityת����view
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
	 * @Description : ͨ��ģ�鹦��IDȥ����ExpansionView�����û���򴴽�
	 * 
	 * @param : String paramFunctionId, ����ģ��ID������: "�����Ƽ�"��"վ������"��
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
	 * @Description : ͨ��LinearLayout::addView������̬������Բ�����
	 * 
	 * @param : View paramExpansionView �۵�ʽ����
	 * 
	 * @return : void
	 * 
	 * @Create Date : 2012-04-23
	 */
	private void addView(View paramExpansionView) {
		// LinearLayout����������������֮ǰ��������������LinearLayout.LayoutParams (ָ������Ŀ�Ⱥ͸߶�)
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
				-1, -2);
		localLayoutParams.setMargins(0, ScreenTools.getInstance(mContext)
				.dip2px(4), 0, 0);

		// ����Ѿ�������ɾ��������
		if (this.indexContent.indexOfChild(paramExpansionView) != -1)
			this.indexContent.removeView(paramExpansionView);

		this.indexContent.addView(paramExpansionView, localLayoutParams);
	}

	/*
	 * @Class Name : OnExpandableListener
	 * 
	 * @Description : �����࣬�ɾ���Ĺ���ģ��ʵ�ַ���������ݼ��ء����ء���ʾ�ȹ���
	 * 
	 * @Create Date : 2012-04-23
	 */
	public static interface OnExpandableListener {
		// ��ʼ��ʱ��������
		public abstract void onLoadData(View paramHomeItemView,
				View paramChildView, String paramFunctionId);

		// ����ʱ��Ҫ��ɵĲ���
		public abstract void onHide(View paramHomeItemView,
				View paramChildView, String paramFunctionId);

		// չ��ʱ��Ҫ��ɵĲ���
		public abstract void onShow(View paramHomeItemView,
				View paramChildView, String paramFunctionId);
	}
}
