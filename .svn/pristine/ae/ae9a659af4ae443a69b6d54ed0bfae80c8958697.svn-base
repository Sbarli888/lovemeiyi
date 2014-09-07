package com.huawei.ptn.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class ServiceUtils {
	
	private static final String TAG = ServiceUtils.class.getSimpleName();
	
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		Log.d(TAG, "Service number is : " + serviceList.size());
		if (serviceList.size() <= 0) {
			return false;
		}
		
		for (ActivityManager.RunningServiceInfo rsi : serviceList) {
			Log.d(TAG, "++++++++++++++++++++++isRunning+++++++++++++++++" + rsi.service.getClassName());
			if (rsi.service.getClassName().equals(className)) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
