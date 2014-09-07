package com.huawei.ptn.model;

import java.util.Map;

import android.graphics.Bitmap;

public class BasicInfoItem {

	private long id;
	
	private String name;
	
	private Bitmap bitmap;
	
	private String ImgUrl;
	
	private double mShopPrice;
	
	private double mMarketPrice;
	
	private String leftover;
	
	private Map<String, String> tips;
	
	private String phoneNumber;
	
	private String qqNumber;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public double getmShopPrice() {
		return mShopPrice;
	}

	public void setmShopPrice(double mShopPrice) {
		this.mShopPrice = mShopPrice;
	}

	public double getmMarketPrice() {
		return mMarketPrice;
	}

	public void setmMarketPrice(double mMarketPrice) {
		this.mMarketPrice = mMarketPrice;
	}

	public String getLeftover() {
		return leftover;
	}

	public void setLeftover(String leftover) {
		this.leftover = leftover;
	}

	public Map<String, String> getTips() {
		return tips;
	}

	public void setTips(Map<String, String> tips) {
		this.tips = tips;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getQqNumber() {
		return qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}


	public String getImgUrl() {
		return ImgUrl;
	}

	public void setImgUrl(String ImgUrl) {
		this.ImgUrl = ImgUrl;
	}
	
	public BasicInfoItem(long id, String name, Bitmap bitmap,
			double mShopPrice, double mMarketPrice, String leftover,
			Map<String, String> tips, String phoneNumber, String qqNumber) {
		super();
		this.id = id;
		this.name = name;
		this.bitmap = bitmap;
		this.mShopPrice = mShopPrice;
		this.mMarketPrice = mMarketPrice;
		this.leftover = leftover;
		this.tips = tips;
		this.phoneNumber = phoneNumber;
		this.qqNumber = qqNumber;
	}
	
	public BasicInfoItem(long id, String name, Bitmap bitmap, String ImgUrl, double mShopPrice) {
		super();
		this.id = id;
		this.name = name;
		this.bitmap = bitmap;
		this.ImgUrl = ImgUrl;
		this.mShopPrice = mShopPrice;
	}
	
}



































