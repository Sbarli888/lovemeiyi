package com.huawei.ptn.util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huawei.ptn.common.Constants;
import com.huawei.ptn.model.BasicInfoItem;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Display;

public class BasicInfoManager {

	private static final String TAG = BasicInfoManager.class.getSimpleName();

	private Handler mHandler = new Handler();

	private ArrayList<BasicInfoItem> mItems = new ArrayList<BasicInfoItem>();

	private ArrayList<WeakReference<DataSetObserver>> mObservers = new ArrayList<WeakReference<DataSetObserver>>();

	private boolean mLoading;

	private Context mContext;

	public BasicInfoManager(Context context) {
		mContext = context;
	}

	public boolean isLoading() {
		return mLoading;
	}

	public void clear() {
		mItems.clear();
		notifyObservers();
	}

	private void add(BasicInfoItem item) {
		mItems.add(item);
		notifyObservers();
	}

	public int size() {
		return mItems.size();
	}

	public BasicInfoItem get(int index) {
		return mItems.get(index);
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

	public void load(long id) {
		mLoading = true;
		new NetworkThread(id).start();
	}

	private class NetworkThread extends Thread {

		private long id;

		public NetworkThread(long id) {
			super();
			this.id = id;
		}

		@Override
		public void run() {

			String url = Constants.CATEGORY_BASIC_INFO + id;

			Log.d(TAG, url);

			try {
				String content = HttpUtils.getRequest(url);
				if (content != null) {
					Log.i(TAG, content);
					JSONObject jsonArray = new JSONObject(content);
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

		private void parse(JSONObject obj) {

			try {
				if (obj == null || obj.length() == 0) {
					Log.e(TAG, "obj == null || obj.length() == 0");
					post();
				}

				// 商品ID
				long id = obj.getInt("goods_id");
				// TODO:商品图片，后续有待扩展成图片列表，左右滑动
				String imageUrl = obj.getString("img_list");
				// 店铺价格
				double shopPrice = obj.getDouble("shop_price");
				// 市场价格
				double marketPrice = obj.getDouble("market_price");
				// 商品名称
				String name = obj.getString("goods_name");
				// 库存
				String leftover = obj.getString("goods_number");
				// 提示信息
				Map<String, String> tipsMap = new HashMap<String, String>();
				JSONArray tipsArray = obj.getJSONArray("tips");
				for (int j = 0; j < tipsArray.length(); j++) {
					JSONObject tipsObj = tipsArray.getJSONObject(j);
					String tipsTitle = tipsObj.getString("title");
					String tipsContent = tipsObj.getString("content");
					tipsMap.put(tipsTitle, tipsContent);
				}
				// 服务号码
				String phoneNumber = obj.getString("service_phone");
				// QQ号码
				String qqNumber = obj.getString("qq");

				Bitmap bitmap = CacheImageUtil.getCacheBitmap(imageUrl);

				final BasicInfoItem item = new BasicInfoItem(id, name, bitmap,
						shopPrice, marketPrice, leftover, tipsMap, phoneNumber,
						qqNumber);

				// 解析完就通知UI线程
				mHandler.post(new Runnable() {

					public void run() {
						mLoading = false;
						add(item);
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

}
