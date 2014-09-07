package com.huawei.ptn.adapter;

import com.huawei.ptn.R;
import com.huawei.ptn.model.ImageItem;
import com.huawei.ptn.util.ImageManager;
import com.huawei.ptn.util.ScreenTools;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private static final String TAG = ImageAdapter.class.getSimpleName();

	private ImageManager mImageManager;

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

	public ImageAdapter(Context context, ImageManager imageManager) {
		mContext = context;
		mImageManager = imageManager;
		mObserver = new MyDataSetObserver();

		mImageManager.addObserver(mObserver);
	}

	public int getCount() {
		return mImageManager.size();
	}

	public Object getItem(int index) {
		return mImageManager.get(index);
	}

	public long getItemId(int index) {
		ImageItem item = mImageManager.get(index);
		return item.getmId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// 创建新的view
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.image_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.goods_image);
			holder.nameView = (TextView) convertView.findViewById(R.id.goods_name);
			holder.marketView = (TextView) convertView.findViewById(R.id.market_price);
			holder.shopView = (TextView) convertView.findViewById(R.id.shop_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageItem item = mImageManager.get(position);

		holder.imageView.setImageBitmap(item.getmBitmap());
		holder.imageView.setBackgroundResource(R.drawable.picture_frame);

		LayoutParams layoutPara = holder.imageView.getLayoutParams();

		layoutPara.width = 2 * ScreenTools.getInstance(mContext).getWidth() / 5;
		layoutPara.height = ScreenTools.getInstance(mContext).getHeight() / 5;

		holder.imageView.setLayoutParams(layoutPara);

		holder.nameView.setText("" + item.getmName());

		holder.marketView.setText("市场价格：" + item.getmMarketPrice());

		holder.shopView.setText("店铺价格：" + item.getmShopPrice());

		return convertView;
	}
	
	private static class ViewHolder {
		ImageView imageView;
		TextView nameView;
		TextView marketView;
		TextView shopView;
	} 

}
