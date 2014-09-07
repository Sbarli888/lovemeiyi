package com.huawei.ptn.util.bitmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.huawei.ptn.common.Constants;
import com.huawei.ptn.util.BitmapUtils;
import com.huawei.ptn.util.MyFileCacheManager;
import com.huawei.ptn.util.NetworkManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyBitmapPool {
	
	public static final int LOADING = 16;
	public static final int NOTLOADING = 32;
	private static final int THREAD_COUNT = 6;
	
	private static final int NORMAL_BITMAP_WHAT = 8;
	private static final int ROUNDED_BITMAP_WHAT = 16;
	
	private int mCount;
	private Context mContext;
	private LinkedBlockingQueue<Runnable> mFileQueue = new LinkedBlockingQueue<Runnable>(100);
	private ThreadPoolExecutor mFileExecutor = new ThreadPoolExecutor(3, 3, 10L, TimeUnit.SECONDS, mFileQueue);
	
	private OnLoadOverListener mLoadOverListener;
	private OnRecycleListener mRecycleListener;
	
	private LinkedBlockingQueue<Runnable> mNetQueue = new LinkedBlockingQueue<Runnable>(200);
	private ThreadPoolExecutor mNetExecutorService = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, 20L, TimeUnit.SECONDS, mNetQueue, new ThreadPoolExecutor.DiscardOldestPolicy());
	
	private LinkedList<String> mQueue;
	private String mUrl;
	private HashMap<String, Bitmap> mUrlBitmap;
	private HashMap<String, Integer> mUrlStatus;
	
	private int mCurMsg;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case NORMAL_BITMAP_WHAT: //Normal
				updateBitmap(msg, false);
				break;
			case ROUNDED_BITMAP_WHAT: //Rounded
				updateBitmap(msg, true);
				break;
			default:
				break;
			}
		}
	};
	
	private MyBitmapPool(Context context, int count) {
		mContext = context;
		mUrlBitmap = new HashMap<String, Bitmap>();
		mQueue = new LinkedList<String>();
		mUrlStatus = new HashMap<String, Integer>();
		mCount = count;
	}
	
	public static MyBitmapPool instance(Context context, int count) {
		return new MyBitmapPool(context, count);
	}
	
	private void updateBitmap(Message msg, boolean isRound) {
		Bitmap bitmap = (Bitmap) msg.obj;
		if (isRound) {
			bitmap = BitmapUtils.getRoundedCornerBitmap(bitmap, 10);
		}
		mUrl = msg.getData().getString("map");
		if (bitmap != null) {
			addBitmap(mUrl, bitmap);
			if (mLoadOverListener != null) {
				mLoadOverListener.onLoadOver(mUrl, bitmap);
			}
		}
		mUrlStatus.put(mUrl, Integer.valueOf(NOTLOADING));
	}
	
	public void addBitmap(String url, Bitmap bitmap) {
		synchronized(this) {
			if (mQueue.size() > mCount) {
				String str = mQueue.poll();
				recycle(str);
				mUrlBitmap.remove(str);
			}
			mUrlBitmap.put(url, bitmap);
			mQueue.add(url);
		}
		return;
	}
	
	public void recycle(String url) {
		synchronized(this) {
			Bitmap bitmap = mUrlBitmap.get(url);
			if (bitmap != null) {
				if (mRecycleListener != null) {
					mRecycleListener.onRecycle(url);
				}
				bitmap.recycle();
			}
		}
		return;
	}
	
	public void recycleAll() {
		Iterator<String> it = mUrlBitmap.keySet().iterator();
		while(it.hasNext()) {
			recycle(it.next());
		}
		mUrlBitmap.clear();
		mUrlStatus.clear();
		mQueue.clear();
		return;
	}
	
	private void reqBitmap(final String url, final int what) {
		mFileExecutor.execute(new Runnable() {

			public void run() {
				Bitmap bm = fromFile(url); //先从本地缓存加载
				if (bm != null) {
					Bundle bundle = new Bundle();
					bundle.putString("map", url);
					Message msg = mHandler.obtainMessage(what, bm);
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				} else {
					mNetExecutorService.execute(new Runnable() {

						public void run() {
							Bitmap bm2 = fromNet(url);
							Bundle bundle = new Bundle();
							bundle.putString("map", url);
							Message msg = mHandler.obtainMessage(what, bm2);
							msg.setData(bundle);
							MyFileCacheManager.instance(mContext).writeBitmapToSD(mContext, Constants.PICTURE_DIR, url, bm2, Bitmap.CompressFormat.JPEG);
							mHandler.sendMessage(msg);
						}
						
					});
				}
			}
			
		});
		return;
	}
	
	public void reqBitmap(String url, boolean isRounded) {
		mCurMsg = 0;
		if (isRounded) {
			mCurMsg = ROUNDED_BITMAP_WHAT;
		} else {
			mCurMsg = NORMAL_BITMAP_WHAT;
		}
		
		if (mUrlStatus.get(url) == null || LOADING != mUrlStatus.get(url)) {
			mUrlStatus.put(url, Integer.valueOf(LOADING));
			reqBitmap(url, mCurMsg);
		}
		return;
	}
	
	private Bitmap fromFile(String fileName) {
		return MyFileCacheManager.instance(mContext).getBitmapFromSD(mContext, Constants.PICTURE_DIR, fileName);
	}
	
	private Bitmap fromNet(String url) {
		return NetworkManager.instance(mContext).getBitmap(url, 1);
	}
	
	public static interface OnLoadOverListener {
		public void onLoadOver(String str, Bitmap bitmap);
	}
	
	public static interface OnRecycleListener {
		public void onRecycle(String str);
	}
	
	public Bitmap getBitmap(String url) {
		synchronized(this) {
			Bitmap bm = mUrlBitmap.get(url);
			if (bm != null) {
				toTail(url);
			}
			return bm;
		}
	}

	private void toTail(String url) {
		mQueue.remove(url);
		mQueue.add(url);
	}
	
	public void setLoadOverListener(OnLoadOverListener listener) {
		mLoadOverListener = listener;
	}
	
	public void setRecycleListener(OnRecycleListener listener) {
		mRecycleListener = listener;
	}
	
	public void setCount(int count) {
		mCount = count;
	}
	
	public void removeLoadOverListener() {
		mLoadOverListener = null;
	}

}


























