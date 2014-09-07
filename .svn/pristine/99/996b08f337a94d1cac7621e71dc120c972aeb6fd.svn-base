package com.huawei.ptn.activity.category;

import com.huawei.ptn.MyApplication;
import com.huawei.ptn.R;
import com.huawei.ptn.common.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class GoodsDetailActivity extends Activity {
	private static final String TAG = GoodsDetailActivity.class.getSimpleName();

	private ProgressBar mProgress;
	private WebView mWebView;
	private int mProgressStatus = 0;

	private Handler mHandler = new Handler();

	private class MyWebChromeClient extends WebChromeClient {
		public void onProgressChanged(WebView view, final int newProgress) {

			mHandler.post(new Runnable() {
				public void run() {
					if (newProgress == 100) {
						mProgress.setVisibility(View.GONE);
					}
					mProgress.setProgress(newProgress);
				}
			});

			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goods_detail_info);

		Intent intent = getIntent();
		long goods_id = intent.getLongExtra("goods_id", 0);

		mProgress = (ProgressBar) findViewById(R.id.goods_detail_progressbar);

		mWebView = (WebView) findViewById(R.id.goods_detail_webview_content);
		mWebView.setWebChromeClient(new MyWebChromeClient());

		mWebView.getSettings().setBuiltInZoomControls(true); // 显示缩放工具
		mWebView.getSettings().setSupportZoom(true); // 可以缩放

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// 可以让不同的density的情况下，可以让页面进行适配
		if (metrics.densityDpi == 240) {
			mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		} else if (metrics.densityDpi == 160) {
			mWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (metrics.densityDpi == 120) {
			mWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		}

		Log.d(TAG, Constants.CATEGORY_DETAIL_INFO + Long.toString(goods_id));
		String url = Constants.CATEGORY_DETAIL_INFO + Long.toString(goods_id);
		mWebView.loadUrl(url);

	}

}
