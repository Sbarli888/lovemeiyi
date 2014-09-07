package com.huawei.ptn.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkManager {
	
	private static final String CHARSET = "utf-8";
	private static final int CONN_TIME_OUT = 20000;
	private static final int READ_TIME_OUT = 20000;
    static final String LINEND = "\r\n";
    static final String MULTIPART_FORM_DATA = "multipart/form-data";
    static final String PREFIX = "--";
    
    private static NetworkManager mNetworkManager;
    private Context mContext;
    
    private NetworkManager(Context context) {
    	mContext = context;
    }
    
    public static NetworkManager instance(Context context) {
    	if (mNetworkManager == null) {
    		mNetworkManager = new NetworkManager(context);
    	}
    	return mNetworkManager;
    }
    
    public static boolean isAvailable(Context context) {
    	NetworkInfo ni = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    	if ((ni == null) || (!ni.isAvailable())) {
    		return false;
    	}
    	return true;
    }
	
	public Bitmap getBitmap(String url, int sampleSize) {
		if (!isAvailable(mContext)) {
			return null;
		}
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPurgeable = true;
		options.inSampleSize = sampleSize;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		HttpURLConnection huc = null;
		InputStream is = null;
		try {
			huc = (HttpURLConnection) new URL(url).openConnection();
			huc.setReadTimeout(READ_TIME_OUT);
			huc.setConnectTimeout(CONN_TIME_OUT);
			huc.setDoInput(true);
			huc.setDoOutput(true);
			huc.setUseCaches(false);
			huc.setRequestMethod("GET");
			huc.setRequestProperty("Connection", "close");
			huc.setRequestProperty("Charset", "utf-8");
			huc.connect();
			is = huc.getInputStream();
			bitmap = BitmapFactory.decodeStream(is, null, options);
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (huc != null) {
				huc.disconnect();
			}
		}
		return bitmap;
	}

}

















