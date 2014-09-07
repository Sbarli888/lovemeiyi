package com.huawei.ptn.config;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigManager {

	public static String LAST_LOAD_VERSION;
	
	public static String GUIDE_SHOWED ;
	
	private static final String mConfigName = "lovemeiyiPrefsFile";

	private static Context mContext = null;

	private static SharedPreferences sp;
	
	static {
		sp = null;
		LAST_LOAD_VERSION = "LastLoadVersion";
		GUIDE_SHOWED = "GuideShowed";
	}

	public static void init(Context context) {
		mContext = context;
		sp = context.getSharedPreferences(mConfigName, Context.MODE_PRIVATE);
	}

	public static Context getApplicationContext() {
		return mContext.getApplicationContext();
	}

	public static boolean getBoolean(String key) {
		if (sp != null) {
			return sp.getBoolean(key, false);
		}
		return false;
	}

	public static int getInt(String key) {
		if (sp != null) {
			return sp.getInt(key, -1);
		}
		return -1;
	}

	public static String getString(String key) {
		if (sp != null) {
			return sp.getString(key, null);
		}
		return null;
	}

	public static boolean putBoolean(String key, boolean value) {
		if (sp != null) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean(key, value);
			editor.commit();
			return true;
		}
		return false;
	}

	public static boolean putInt(String key, int value) {
		if (sp != null) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt(key, value);
			editor.commit();
			return true;
		}
		return false;
	}

	public static boolean putString(String key, String value) {
		if (sp != null) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(key, value);
			editor.commit();
			return true;
		}
		return false;
	}

	public static boolean removeKey(String key) {
		if (sp != null) {
			SharedPreferences.Editor editor = sp.edit();
			editor.remove(key);
			editor.commit();
			return true;
		}
		return false;
	}

}
