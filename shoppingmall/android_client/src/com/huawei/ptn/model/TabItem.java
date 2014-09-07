package com.huawei.ptn.model;

import android.content.Intent;

public class TabItem {
	
	private int title;
	private int icon;
	private int background;
	private Intent intent;
	
	public TabItem(int title, int icon, int background, Intent intent) {
		super();
		this.title = title;
		this.icon = icon;
		this.background = background;
		this.intent = intent;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	

}






