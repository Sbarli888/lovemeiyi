package com.huawei.ptn.adapter;

import com.huawei.ptn.R;
import com.huawei.ptn.model.CategoryItem;
import com.huawei.ptn.util.CategoryManager;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

	private CategoryManager mCategoryManager;

	private Context mContext;

	private MyDataSetObserver mObserver;

	private class MyDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
			notifyDataSetInvalidated();
		}

	}

	public CategoryAdapter(Context context, CategoryManager categoryManager) {
		mCategoryManager = categoryManager;
		mContext = context;
		mObserver = new MyDataSetObserver();
		mCategoryManager.addObserver(mObserver);
	}

	public int getCount() {
		return mCategoryManager.size();
	}

	public Object getItem(int index) {
		return mCategoryManager.get(index);
	}

	public long getItemId(int index) {
		CategoryItem item = mCategoryManager.get(index);
		return item.getId();
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// 创建新的view
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.tab_category_setting_list_item,
					null);
			
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.setting_list_item_text);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CategoryItem cItem = mCategoryManager.get(index);
		holder.tv.setText(cItem.getName());

		return convertView;
	}
	
	private static class ViewHolder {
		TextView tv;
	}

}












