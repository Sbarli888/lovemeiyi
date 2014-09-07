package com.huawei.ptn.util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huawei.ptn.common.Constants;
import com.huawei.ptn.model.BannerItem;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

public class BannerManager {

	private static final String TAG = BannerManager.class.getSimpleName();

	private Handler mHandler = new Handler();

	private ArrayList<BannerItem> mBanners = new ArrayList<BannerItem>();

	private ArrayList<WeakReference<DataSetObserver>> mObservers = new ArrayList<WeakReference<DataSetObserver>>();

	private boolean mLoading;

	private boolean mDecoding = true;

	private Context mContext;

	public BannerManager(Context context) {
		mContext = context;
	}

	public boolean isLoading() {
		return mLoading;
	}

	public boolean isDecoding() {
		return mDecoding;
	}
	
	public void SetDecoded(){
		mDecoding = false;
		Log.d(TAG, "BannerManager::SetDecoded()");
	}

	public void clear() {
		mBanners.clear();
		notifyObservers();
	}

	private void add(BannerItem item) {
		mBanners.add(item);
		notifyObservers();
	}
	
	private void Notify(){
		Log.d(TAG, "BannerManager::notify()");
		notifyObservers();
	}

	public int size() {
		return mBanners.size();
	}

	public BannerItem get(int index) {
		return mBanners.get(index);
	}

	public void addObserver(DataSetObserver observer) {
		WeakReference<DataSetObserver> obs = new WeakReference<DataSetObserver>(
				observer);
		mObservers.add(obs);
	}

	private void notifyObservers() {
		final ArrayList<WeakReference<DataSetObserver>> observers = mObservers;
		final int count = observers.size();
		for (int i = count - 1; i >= 0; i--) {
			WeakReference<DataSetObserver> weak = observers.get(i);
			DataSetObserver obs = weak.get();
			if (obs != null) {
				obs.onChanged();
			} else {
				observers.remove(i);
			}
		}
	}

	public void load() {
		mLoading = true;
		new NetWorkThread().start();
	}

	private void DecodeImg() {
		mDecoding = true;
		new BitmapDecodeThread().start();
	}

	private class NetWorkThread extends Thread {

		@Override
		public void run() {
			String url = Constants.URI_BASE_URL + "/ad/banner/";
			Log.d(TAG, url);

			try {
				String content = HttpUtils.getRequest(url);
				if (content != null && content.length() != 0) {
					Log.d(TAG, "content=" + content);
					JSONArray jsonArray = new JSONArray(content);
					parse(jsonArray);
				} else {
					post();
					Log.d(TAG, "contents from server is null");
				}
			} catch (ClientProtocolException e) {
				post();
				Log.e(TAG, "ClientProtocolException:" + e.toString());
			} catch (IOException e) {
				post();
				Log.e(TAG, "IOException:" + e.toString());
			} catch (JSONException e) {
				post();
				Log.e(TAG, "JSONException:" + e.toString());
			}
		}

		private void parse(JSONArray array) {
			try {
				int count = array.length();
				if (count == 0) {
					Log.d(TAG, "count == 0");
					post();
				}
				for (int i = 0; i < count; i++) {
					JSONObject obj = array.getJSONObject(i);
					int imageId = obj.getInt("img_id");
					String imagePath = obj.getString("img_path");
					int linkType = obj.getInt("link_type");
					String imageLink = obj.getString("img_link");
					final BannerItem item = new BannerItem(imageId, imagePath,
							linkType, imageLink);

					final boolean done = (i == count - 1);

					mHandler.post(new Runnable() {

						public void run() {
							mLoading = !done;
							add(item);
						}

					});
				}

				mHandler.post(new Runnable() {

					public void run() {
						DecodeImg();
					}
				});

			} catch (JSONException e) {
				post();
				e.printStackTrace();
			}
		}

		private void post() {
			mHandler.post(new Runnable() {

				public void run() {
					mLoading = true;
					notifyObservers();
				}

			});
		}

	}

	// 单独开启一个线程用于解码图片
	private class BitmapDecodeThread extends Thread {

		public BitmapDecodeThread() {
			super();
		}

		public void run() {
			for (int i = 0; i < mBanners.size(); i++) {
				if (mBanners.get(i).getBitmap() == null) {
					loadBitmap(i);
				}
			}
			post();
		}

		private void loadBitmap(int i) {
			String imageUrl = mBanners.get(i).getImagePath();
			Bitmap bitmap = CacheImageUtil.getCacheBitmap(imageUrl);
			mBanners.get(i).setBitmap(bitmap);
		}

		private void post() {
			mHandler.post(new Runnable() {
				public void run() {
					SetDecoded();
					notifyObservers();
				}
			});
		}
	}

}
