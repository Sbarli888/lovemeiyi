package com.huawei.ptn.adapter;

import com.huawei.ptn.util.BasicInfoManager;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BasicInfoAdapter extends BaseAdapter {

	private static final String TAG = BasicInfoAdapter.class.getSimpleName();

	private BasicInfoManager mBasicInfoManager;

	private Context mContext;

	private MyDataSetObserver mObserver;

	private class MyDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			// super.onChanged();
			notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
			// super.onInvalidated();
			notifyDataSetInvalidated();
		}

	}

	public int getCount() {
		return mBasicInfoManager.size();
	}

	public Object getItem(int position) {
		return mBasicInfoManager.get(position);
	}

	public long getItemId(int position) {
		return mBasicInfoManager.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		return null;
	}

}
