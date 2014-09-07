package com.huawei.ptn.util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huawei.ptn.common.Constants;
import com.huawei.ptn.model.ImageItem;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;


public class ImageManager {

	private static final String TAG = ImageManager.class.getSimpleName();

	private String mBaseUrl;

	private int mHttpRequestType;

	private Map<String, String> m_PostParams;

	private Handler mHandler = new Handler();

	private ArrayList<ImageItem> mImages = new ArrayList<ImageItem>();

	private ArrayList<WeakReference<DataSetObserver>> mObservers = new ArrayList<WeakReference<DataSetObserver>>();

	private boolean mLoading;

	private Context mContext;

	public ImageManager(Context context) {
		mContext = context;
	}

	public void SetUrl(String BaseUrl, int HttpType) {
		mBaseUrl = BaseUrl;
		mHttpRequestType = HttpType;
	}

	public void SetPostParams(Map<String, String> rawParams) {
		m_PostParams = rawParams;
	}

	public boolean isLoading() {
		return mLoading;
	}

	public void clear() {
		mImages.clear();
		notifyObservers();
	}

	private void add(ImageItem item) {
		mImages.add(item);
		notifyObservers();
	}
	
	private void Notify(){
		Log.d(TAG, "ImageManager::notify()");
		notifyObservers();
	}
	

	public int size() {
		return mImages.size();
	}

	
	public ImageItem get(int index) {
		return mImages.get(index);
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

	public void load(int sortType) {
		mLoading = true;
		new NetworkThread(sortType).start();
	}

	public void load() {
		mLoading = true;
		new NetworkThread(Constants.SORT_TYPE_UPLOAD_TIME).start();
	}

	private void DecodeImg(){
		new BitMapDecodeThread().start();
	}
	
	private class NetworkThread extends Thread {

		private int mSortType;

		public NetworkThread(int sortType) {
			super();

			mSortType = sortType;
		}

		@Override
		public void run() {

			String url = mBaseUrl + "/" + mSortType;

			Log.d(TAG, url);

			try {
				String content = null;
				switch (mHttpRequestType) {
				case Constants.HTTP_GET:
					content = HttpUtils.getRequest(url);
					break;

				case Constants.HTTP_POST:
					content = HttpUtils.postRequest(url, m_PostParams);
					break;
				}

				if (content != null) {
					Log.i(TAG, "content = " + content);
					JSONArray jsonArray = new JSONArray(content);
					parse(jsonArray);
				} else {
					post();
					Log.i(TAG, "contents from server is null");
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
					Log.e(TAG, "count == 0");
					post();
				}
				for (int i = 0; i < count; i++) {
					JSONObject obj = array.getJSONObject(i);
					long id = obj.getInt("goods_id");
					String name = obj.getString("goods_name");
					double marketPrice = obj.getDouble("market_price");
					double shopPrice = obj.getDouble("shop_price");
					String imageUrl = obj.getString("original_img");
					Log.i(TAG, "name=" + name + " marketPrice=" + marketPrice
							+ " shopPrice=" + shopPrice);
					Log.i(TAG, "imageUrl=" + Constants.IMAGE_BASE_URL + "/"
							+ imageUrl);
					
					final ImageItem item = new ImageItem(id, name, imageUrl,
							shopPrice, marketPrice);

					final boolean done = (i == count - 1);

					// 每解析完一项就通知UI线程
					mHandler.post(new Runnable() {

						public void run() {
							mLoading = !done;
							add(item);
						}

					});
				}
				
				//加载完毕之后开启图片解析线程
				mHandler.post(new Runnable() {
					public void run() {
						DecodeImg();
					}
				});
				
			} catch (JSONException e) {
				post();
				Log.e(TAG, e.toString());
			}
		}

		private void post() {
			mHandler.post(new Runnable() {

				public void run() {
					mLoading = false;
					notifyObservers();
				}

			});
		}
	}
	

	//单独开启一个线程用于解码图片
	private class BitMapDecodeThread extends Thread {
		
		public BitMapDecodeThread() {
			super();
		}
		
		public void run(){
			
				for(int i = 0; i < mImages.size(); i++){
					if(mImages.get(i).getmBitmap() == null){
						loadbitmap(i);
					}
				}
				post();
				
		}
		
		private void loadbitmap(int i){
			String imageUrl = mImages.get(i).getmImageUrl();
			
			Bitmap bitmap = CacheImageUtil.getCacheBitmap(imageUrl);

			mImages.get(i).setmBitmap(bitmap);
		}
		
		private void post(){
			mHandler.post(new Runnable() {
				public void run() {
					Notify();
				}
			});
		}
	}
}
