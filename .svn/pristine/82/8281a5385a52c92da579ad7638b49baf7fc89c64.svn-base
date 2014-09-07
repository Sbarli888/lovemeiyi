package com.huawei.ptn.util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huawei.ptn.common.Constants;
import com.huawei.ptn.model.BasicInfoItem;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.Display;


public class HotGoodsManager extends Activity {
	
	private static final String TAG = HotGoodsManager.class.getSimpleName();
	
	private Context mContext;
	
	private ArrayList<BasicInfoItem> mBasicInfo = new ArrayList<BasicInfoItem>();

	private String mUrl;
	
	private boolean mLoading = true;
	
	private Handler mHandler = new Handler();
	
	private ArrayList<WeakReference<DataSetObserver>> mObservers = new ArrayList<WeakReference<DataSetObserver>>();
	
	public HotGoodsManager(Context context) {
		mContext = context;
	}

	public boolean isLoading() {
		return mLoading;
	}

	public void clear() {
		mBasicInfo.clear();
		notifyObservers();
	}

	private void add(BasicInfoItem item) {
		mBasicInfo.add(item);
		notifyObservers();
	}

	private void Notify(){
		Log.d(TAG, "HotGoodsManager::notify()");
		notifyObservers();
	}
	
	public int size() {
		return mBasicInfo.size();
	}

	public BasicInfoItem get(int index) {
		return mBasicInfo.get(index);
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


	public void load(String url) {
		mLoading = true;
		mUrl = url;
		new NetworkThread().start();
	}

	private void DecodeImg(){
		new BitMapDecodeThread().start();
	}
	
	private class NetworkThread extends Thread {
		
		public NetworkThread() {
			super();

		}

		@Override
		public void run() {


			Log.d(TAG, mUrl);

			try {
				String content = HttpUtils.getRequest(mUrl);

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
					double shopPrice = obj.getDouble("shop_price");
					String imageUrl = obj.getString("goods_img");
					Log.i(TAG, "goods_id=" + id + "name=" + name + " shopPrice=" + shopPrice);
					Log.i(TAG, "imageUrl=" + Constants.IMAGE_BASE_URL + "/"
							+ imageUrl);

					final BasicInfoItem item = new BasicInfoItem(id, name, null, imageUrl, shopPrice);

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
				
				//先显示gallery的前三张图片，看不见的图片在后台加载
				int start_img_id = (Integer.MAX_VALUE/2)%mBasicInfo.size() - 1;
				int end_img_id = (Integer.MAX_VALUE/2)%mBasicInfo.size() + 1;
				for(int i = start_img_id; i<= end_img_id; i++){
					loadbitmap(i);
					post();
				}

				for(int i = 0; i < mBasicInfo.size(); i++){
					if(mBasicInfo.get(i).getBitmap() == null){
						loadbitmap(i);
					}
				}
				post();
		}
		
		private void loadbitmap(int i){
			String imageUrl = mBasicInfo.get(i).getImgUrl();
			
			Bitmap bitmap = CacheImageUtil.getCacheBitmap(imageUrl);
			
			mBasicInfo.get(i).setBitmap(bitmap);
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
