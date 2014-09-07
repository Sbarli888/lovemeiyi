package com.huawei.ptn.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;

public class BitmapUtils {
	
	private static final String TAG = BitmapUtils.class.getSimpleName();
	
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	private static final int DEFAULT_SAMPLE_SIZE = 1;
	
	public static Bitmap loadBitmap(String url, Display currentDisplay) {
		return loadBitmap(url, currentDisplay, DEFAULT_SAMPLE_SIZE);
	}
	
	/**
	 * 从指定的URL加载位图，这将会持续一段时间，因此不应该从UI线程中直接调用
	 * @param url
	 * @return
	 */
	public static Bitmap loadBitmap(String url, Display currentDisplay, int sampleSize) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		
		try {
			in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();
			
			final byte[] data = dataStream.toByteArray();
			
//			int width = currentDisplay.getWidth();
//			int height = currentDisplay.getHeight();
			
			//加载图片尺寸，而非实际图片
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
//			bmpFactoryOptions.inJustDecodeBounds = true;
//			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bmpFactoryOptions);
//			int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
//			int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
//			
//			Log.v("HEIGHTRATIO", "" + heightRatio);  
//            Log.v("WIDTHRATIO", "" + widthRatio);
//            
//            if (heightRatio > 1 && widthRatio > 1) {
//            	if (heightRatio > widthRatio) {
//            		bmpFactoryOptions.inSampleSize = heightRatio;
//            	} else {
//            		bmpFactoryOptions.inSampleSize = widthRatio;
//            	}
//            } else if (heightRatio > widthRatio) {
//            	bmpFactoryOptions.inSampleSize = 
//            }
			
            bmpFactoryOptions.inJustDecodeBounds = false;
			bmpFactoryOptions.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bmpFactoryOptions);
		}catch (IOException e) {
			Log.e(TAG, "Could not load Bitmap from:" + url);
		} finally {
			closeStream(in);
			closeStream(out);
		}
		
		return bitmap;
	}
	
	/**
	 * 从指定的URL加载位图，这将会持续一段时间，因此不应该从UI线程中直接调用
	 * @param url
	 * @return
	 */
	public static Bitmap loadBitmapNew(String url) {
		Bitmap bitmap = null;
		
		try {
			byte[] dataImage = httpRequestByteArray(url);
			bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
		}catch (IOException e) {
			Log.e(TAG, "Could not load Bitmap from:" + url);
		}
		
		return bitmap;
	}

	/**
	 * 关闭指定数据流
	 * @param out
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				Log.e(TAG, "Could not close stream", e);
			}
		}
	}

	/**
	 * 使用临时的字节数组缓存将InputStream中的数据拷贝到OutputStream中
	 * @param in
	 * @param out
	 * @throws IOException 
	 */
	private static void copy(InputStream in, BufferedOutputStream out) throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}
	
	private static byte[] httpRequestByteArray(String url) throws ClientProtocolException, IOException {
		byte[] result = null;
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
		
		if (httpStatusCode == HttpStatus.SC_OK) {
			result = EntityUtils.toByteArray(httpResponse.getEntity());
		}
		
		return result;
	}
	
	/**
	 * 将Drawable转换成Bitmap
	 * @param drawable
	 * @return
	 */
	private static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config;
		if (drawable.getOpacity() != PixelFormat.OPAQUE) {
			config = Bitmap.Config.ARGB_8888;
		} else {
			config = Bitmap.Config.RGB_565;
		}
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * 从字节数组按照指定的分辨率解码出位图
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmapFromByteArray(byte[] data, int width, int height) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		opts.inJustDecodeBounds = false;
		
		int ratio = 0;
		if (opts.outHeight < opts.outWidth) {
			ratio = opts.outHeight / height;
		} else {
			ratio = opts.outWidth / width;
		}
		
		if (ratio <= 0) {
			ratio = 1;
		} 
		
		opts.inSampleSize = ratio;
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}
	
	/**
	 * 从文件中按照指定的分辨率解码出位图
	 * @param pathName
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String pathName, int width, int height) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, opts);
		opts.inJustDecodeBounds = false;
		
		int ratio = 0;
		if (opts.outHeight < opts.outWidth) {
			ratio = opts.outHeight / height;
		} else {
			ratio = opts.outWidth / width;
		}
		
		if (ratio <= 0) {
			ratio = 1;
		}
		opts.inSampleSize = ratio;
		return BitmapFactory.decodeFile(pathName, opts);
	}
	
	/**
	 * 将Drawable转换成带圆角的Bitmap
	 * @param drawable
	 * @param radius
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Drawable drawable, float radius) {
		Bitmap bm1 = drawableToBitmap(drawable);
		Bitmap bm2 = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bm2);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bm1.getWidth(), bm1.getHeight());
		RectF rectf = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(rectf, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bm1, rect, rect, paint);
		
		return bm2;
	}
	
	public static Bitmap toRoundCorner(Bitmap bitmap) {
		return getRoundedCornerBitmap(bitmap, 6);
	}
	
	/**
	 * 将Bitmap转换成带圆角的Bitmap
	 * @param bitmap
	 * @param radius
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width <= 0 || height <= 0) {
			return null;
		}
		
		Bitmap bm = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bm);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectf = new RectF(rect);
		
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(rectf, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		return bm;
	}

}






















