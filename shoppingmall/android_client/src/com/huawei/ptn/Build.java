package com.huawei.ptn;

public class Build {

	public static String CLIENT_MODEL;
	public static String CLIENT_VERSION;
	
	static {
		CLIENT_MODEL = android.os.Build.MODEL + android.os.Build.VERSION.RELEASE;
		CLIENT_VERSION = "1.0.0.0";
	}
	
}















