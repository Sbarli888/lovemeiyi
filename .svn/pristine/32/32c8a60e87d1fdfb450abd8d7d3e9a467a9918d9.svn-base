package com.huawei.ptn.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.huawei.ptn.common.Constants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class CacheImageUtil {

	private static final String TAG = CacheImageUtil.class.getSimpleName();

	public static Bitmap getCacheBitmap(String imageurl) {

		Bitmap bitmap = null;

		SdCardUtil.init();

		String fileName = getFileName(imageurl);

		String localImagePath = SdCardUtil.IMAGE_FILE_PATH + fileName;

		Log.d(TAG, "localImagePath = "  + localImagePath);
		File localImageFile = new File(localImagePath);
		
		//先从本地缓冲中查找图片，没有图片才中网络中下载并保存到本地
		if(localImageFile.exists()){
			Log.i(TAG, "local cache image");
			bitmap = BitmapFactory.decodeFile(localImagePath);
			
		}else{
			Log.i(TAG, "remote server image");
			try {
				localImageFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(localImageFile);

				bitmap = BitmapUtils.loadBitmap(Constants.IMAGE_BASE_URL + "/"
						+ imageurl, null);
				/*
				 * compress(Bitmap.CompressFormat format, int quality,
				 * OutputStream stream) Write a compressed version of the bitmap
				 * to the specified outputstream.
				 */
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				Log.e(TAG, "file error!");
				e.printStackTrace();
			}

		}
		return bitmap;
	}

	private static String getFileName(String imageUrl) {
		if (imageUrl != null && imageUrl.length() != 0) {
			String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
			return fileName;
		} else {
			Log.i(TAG, "fileName = null");
			return null;
		}
	}
}
