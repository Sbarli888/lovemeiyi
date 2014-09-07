package com.huawei.ptn.util.bitmap;

import android.content.Context;
import android.graphics.Bitmap;

public class MyBitmapPoolCtrl {
	
	private static final int DEFAULT_COUNT = 30;
	private Context mContext;
	private MyBitmapPool mPool;
	
	public MyBitmapPoolCtrl(Context context) {
		mContext = context;
		mPool = MyBitmapPool.instance(context, DEFAULT_COUNT);
	}
	
	public Bitmap getBitmap(String url) {
		return mPool.getBitmap(url);
	}
	
	public void recycleAll() {
		mPool.recycleAll();
	}
	
	public void removeLoadOverListener() {
		mPool.removeLoadOverListener();
	}
	
	public void reqBitmap(String url) {
		mPool.reqBitmap(url, false);
	}
	
	public void reqCornerBitmap(String url) {
		mPool.reqBitmap(url, true);
	}
	
	public void setLoadOverListener(MyBitmapPool.OnLoadOverListener listener) {
		mPool.setLoadOverListener(listener);
	}
	
	public void setPoolCount(int count) {
		mPool.setCount(count);
	}
	
	public void setRecycleListener(MyBitmapPool.OnRecycleListener listener) {
		mPool.setRecycleListener(listener);
	}

}
























