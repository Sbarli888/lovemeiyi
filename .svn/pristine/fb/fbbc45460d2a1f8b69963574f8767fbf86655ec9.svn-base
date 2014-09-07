package com.huawei.ptn.util;

import java.io.File;

import com.huawei.ptn.common.Constants;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/*
 * SD卡操作知识点：
 * 
 1.加入sdcard操作权限；
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">
 </uses-permission>

 2.确认sdcard的存在；
 3.不能直接在非sdcard的根目录创建文件，而是需要先创建目录，再创建文件；

 Android中SD Card操作用到的两个类：
 (1)Environment类：访问环境变量;
 (2)StatFs类：类似于Linux系统的df命令，查看文件系统信息。
 */
public class SdCardUtil {
	private static final String TAG = SdCardUtil.class.getSimpleName();

	public static final double MB = 1024 * 1024;

	public final static String IMAGE_FILE_PATH = Constants.BASE_PATH
			+ "/image/";

	public final static String APK_UPDATE_PATH = Constants.BASE_PATH
			+ "/update/";

	public static void init() {
		File appRootPath = new File(Constants.BASE_PATH);
		File imageFilePath = new File(IMAGE_FILE_PATH);
		File apkUpdatePath = new File(APK_UPDATE_PATH);

		// 如果目录不存在则新建目录，程序首次运行执行
		if (!appRootPath.exists()) {
			appRootPath.mkdirs();
		}
		if (!imageFilePath.exists()) {
			imageFilePath.mkdirs();
		}
		if (!apkUpdatePath.exists()) {
			apkUpdatePath.mkdirs();
		}
	}

	// 计算SD卡可用空间(i9100实测结果: blocksize = 16384(16K)，可用空间6.4G
	public static int freeSpaceOnSd() {

		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());

		Log.d(TAG, "BlockSize = " + stat.getBlockSize());
		Log.d(TAG, "AvailableBlocks = " + stat.getAvailableBlocks());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

}
