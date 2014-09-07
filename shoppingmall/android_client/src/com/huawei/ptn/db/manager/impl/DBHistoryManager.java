package com.huawei.ptn.db.manager.impl;

import android.content.Context;

import com.huawei.ptn.db.DBConstant;
import com.huawei.ptn.db.manager.IDBHistory;

public class DBHistoryManager implements IDBHistory {

	private String DATABASE_NAME = null;
	
	private Context mContext = null;

	public DBHistoryManager(Context context) {
		super();
		mContext = context;
		DATABASE_NAME = mContext.getCacheDir().getAbsolutePath() + "/" + DBConstant.DATABASE_NAME;
	}
}
