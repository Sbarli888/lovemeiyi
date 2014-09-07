package com.huawei.ptn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.huawei.ptn.config.ConfigManager;
import com.huawei.ptn.db.dao.DBHelperImpl;
import com.huawei.ptn.service.UpdateService;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.util.Log;

public class MyApplication extends Application {
	
	private static final String TAG = MyApplication.class.getSimpleName();

	private List<Activity> activityList = new LinkedList<Activity>();
	private ArrayList<Service> serviceList = new ArrayList<Service>();

	private static MyApplication instance;
	private MainActivity mainActivity;
	
	private DBHelperImpl mDbHelperImpl;

	// 单例模式
	public static MyApplication getInstance() {
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	public void addService(Service service) {
		serviceList.add(service);
	}

	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			if (activity != null) {
				activity.finish();
			}
		}
		for (Service service : serviceList) {
			if (service != null) {
				Log.d(TAG, "service stop");
				if (service instanceof UpdateService) {
					((UpdateService)service).cancelNotification();
				}
				service.stopSelf();
			}
		}
		System.exit(0);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ConfigManager.init(getApplicationContext());
		instance = this;
		mDbHelperImpl = new DBHelperImpl(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public MainActivity getMainActivity() {
		return this.mainActivity;
	}

}
