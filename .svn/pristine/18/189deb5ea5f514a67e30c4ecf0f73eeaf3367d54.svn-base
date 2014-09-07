package com.huawei.ptn.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class ScreenTools {
	
	private static ScreenTools mScreenTools;
	
	private Context mContext;
	
	private Display defaultDisplay;
	
	private float mDensity = 0; 
	
	private ScreenTools(Context context) {
		mContext = context;
	}
	
	public static ScreenTools getInstance(Context context) {
		if (mScreenTools == null) {
			mScreenTools = new ScreenTools(context);
		}
		return mScreenTools;
	}
	
	private float getDensity() {
		if (mDensity == 0) {
			mDensity = mContext.getResources().getDisplayMetrics().density;
		}
		return mDensity;
	}
	
	public int dip2px(int dip) {
		return (int) (0.5F + getDensity()*dip);
	}
	
	public int px2dip(int px) {
		return (int)(0.5F + px / getDensity());
	}
	
	// get screen width value int pixels
	public int getScreenWidth() {
		return mContext.getResources().getDisplayMetrics().widthPixels;
	}
	
	// get screen height value int pixels
	public int getSreenHeight() {
		return mContext.getResources().getDisplayMetrics().heightPixels;
	}

	public Display getDefaultDisplay() {
		if (defaultDisplay == null) {
			defaultDisplay = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		}
		return defaultDisplay;
	}
	
	public int getHeight() {
		return getDefaultDisplay().getHeight();
	}
	
	public int getWidth() {
		return getDefaultDisplay().getWidth();
	}
	
	public int percentHeight(float percent) {
		return (int)(percent * getHeight());
	}
	
	public int percentWidth(float percent) {
		return (int)(percent * getWidth());
	}
	
}








































