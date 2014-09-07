package com.huawei.ptn.util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huawei.ptn.model.CategoryItem;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.Log;

public class CategoryManager {

	private static final String TAG = CategoryManager.class.getSimpleName();

	/**
	 * 用于后台线程将结果反馈给UI线程
	 */
	private Handler mHandler = new Handler();

	private ArrayList<CategoryItem> mCategories = new ArrayList<CategoryItem>();

	private ArrayList<WeakReference<DataSetObserver>> mObservers = new ArrayList<WeakReference<DataSetObserver>>();

	private boolean mLoading;

	private Context mContext;

	private String url;

	public CategoryManager(Context context) {
		mContext = context;
	}

	public boolean isLoading() {
		return mLoading;
	}

	public void clear() {
		mCategories.clear();
		notifyObservers();
	}

	private void add(CategoryItem item) {
		mCategories.add(item);
		notifyObservers();
	}

	public int size() {
		return mCategories.size();
	}

	public CategoryItem get(int index) {
		return mCategories.get(index);
	}

	public void addObserver(DataSetObserver observer) {
		WeakReference<DataSetObserver> obs = new WeakReference<DataSetObserver>(
				observer);
		mObservers.add(obs);
	}

	/**
	 * 开始下载分类信息
	 */
	public void load(String url) {
		mLoading = true;
		this.url = url;
		new NetworkThread().start();
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

	private class NetworkThread extends Thread {

		@Override
		public void run() {
			try {
				String content = HttpUtils.getRequest(url);
				System.out.println("content is : " + content);
				if (content != null) {
					JSONArray jsonArray = new JSONArray(content);
					parse(jsonArray);
				} else {
					post();
					Log.i(TAG, "contents from server is null");
				}
			} catch (JSONException e) {
				post();
				Log.e(TAG, e.toString());
			} catch (ClientProtocolException e) {
				post();
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				post();
				Log.e(TAG, e.toString());
			}
		}

		private void parse(JSONArray array) {
			try {
				int count = array.length();
				for (int i = 0; i < count; i++) {
					JSONObject obj = array.getJSONObject(i);
					long id = obj.getLong("cat_id");
					String name = obj.getString("cat_name");
					final CategoryItem item = new CategoryItem(id, name);

					final boolean done = (i == count - 1);

					mHandler.post(new Runnable() {

						public void run() {
							mLoading = !done;
							add(item);
						}

					});
				}
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
