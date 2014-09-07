package com.huawei.ptn.util.bitmap;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MyBitmapFactory {
	
	private Context mContext;
	private static MyBitmapFactory mBitmapFactory;
	private ArrayList<Bitmap> mCanRecycleList;
	private ArrayList<Bitmap> mUnRecycleList;
	
	private MyBitmapFactory(Context context) {
		mContext = context;
		mCanRecycleList = new ArrayList<Bitmap>();
		mUnRecycleList = new ArrayList<Bitmap>();
	}
	
	static MyBitmapFactory instance(Context context) {
		if (mBitmapFactory == null) {
			mBitmapFactory = new MyBitmapFactory(context);
		}
		return mBitmapFactory;
	} 

	public void toList(Bitmap bitmap, boolean canRecycle) {
		if (canRecycle) {
			mCanRecycleList.add(bitmap);
		} else {
			mUnRecycleList.add(bitmap);
		}
		return;
	}
	
	public Bitmap decodeByteArray(byte[] data, int offset, int length, BitmapFactory.Options opts) {
		return BitmapFactory.decodeByteArray(data, offset, length, opts);
	}
	
	public Bitmap decodeFile(String pathName, BitmapFactory.Options opts) {
		return BitmapFactory.decodeFile(pathName, opts);
	}
	
	public Bitmap decodeResource(int id, BitmapFactory.Options opts) {
		return decodeResource(id, opts, true);
	}
	
	public Bitmap decodeResource(int id, BitmapFactory.Options opts, boolean canRecycle) {
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), id, opts);
		if (bitmap != null) {
			if (canRecycle) {
				mCanRecycleList.add(bitmap);
			} else {
				mUnRecycleList.add(bitmap);
			}
		}
		return bitmap;
	}
}

























