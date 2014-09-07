package com.huawei.ptn.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class MyFileCacheManager {

	private static MyFileCacheManager mManager;
	private Context mContext;
	private long mTimeDuration;

	private MyFileCacheManager(Context context) {
		mContext = context;
	}

	private boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static MyFileCacheManager instance(Context context) {
		if (mManager == null) {
			mManager = new MyFileCacheManager(context);
		}
		return mManager;
	}

	public void delFileByTime(final String path, int hour) {
		if (!hasSDCard()) {
			return;
		}
		mTimeDuration = 24 * 60 * 60 * 1000 * hour;
		new Thread(path) {

			@Override
			public void run() {
				File[] fileArray = new File(path).listFiles(new TimeFilter());
				if (fileArray != null) {
					int min = Math.min(50, fileArray.length);
					for (int i = 0; i < min; i++) {
						fileArray[i].delete();
					}
				}
			}

		}.start();
	}

	public Bitmap getBitmapFromSD(Context context, String filePath,
			String fileName) {

		if (!hasSDCard() || fileName == null) {
			return null;
		}

		String md5 = CryptoManager.instance(context).toMd5(fileName);
		File file = new File(filePath, md5);
		if (!file.exists()) {
			return null;
		}

		return BitmapFactory.decodeFile(filePath + "/" + md5);
	}

	public void writeBitmapToSD(Context context, String filePath,
			String fileName, Bitmap bitmap, Bitmap.CompressFormat cf) {
		
		if (!hasSDCard() || bitmap == null) {
			return;
		}

		String fullPathname = filePath
				+ CryptoManager.instance(context).toMd5(fileName);
		File file = new File(fullPathname);
		if (file.exists()) {
			
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(fullPathname);
				bitmap.compress(cf, 100, fos);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private class TimeFilter implements FileFilter {

		public TimeFilter() {

		}

		public boolean accept(File file) {
			long time = file.lastModified();
			if ((System.currentTimeMillis() - time) > mTimeDuration) {
				return true;
			}
			return false;
		}

	}

}
