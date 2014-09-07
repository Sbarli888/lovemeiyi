package com.huawei.ptn.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

public class CryptoManager {
	
	private static CryptoManager instance;
	
	private Context mContext;
	
	private CryptoManager(Context context) {
		mContext = context;
	}
	
	public static CryptoManager instance(Context context) {
		if (instance == null) {
			instance = new CryptoManager(context);
		}
		return instance;
	}
	
	private String toHexString(byte[] source, String divider) {
		StringBuilder sb = new StringBuilder();
		int len = source.length;
		for (int i = 0; i < len; i++) {
			sb.append(Integer.toHexString(0xff & source[i])).append(divider);
		}
		return sb.toString();
	}
	
	public String toMd5(String source) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(source.getBytes());
			result = toHexString(md.digest(), "");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

}





















