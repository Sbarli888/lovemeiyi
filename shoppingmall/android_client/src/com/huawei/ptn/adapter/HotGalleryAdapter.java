package com.huawei.ptn.adapter;

import com.huawei.ptn.R;
import com.huawei.ptn.model.BasicInfoItem;
import com.huawei.ptn.util.HotGoodsManager;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;



public class HotGalleryAdapter extends BaseAdapter {

	private static final String TAG = HotGalleryAdapter.class.getSimpleName();
	
	private HotGoodsManager mHotGoodsManager;
	
	private int selectItem;

	//定义Context
	private Context mContext;
	
	private DataSetObserver mObserver = new DataSetObserver(){

		@Override
		public void onChanged() {
			notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
			notifyDataSetInvalidated();
		}
	};
	
	//定义整型数组，即图片资源
	private Integer[] mImageIds = {
		R.drawable.img_loading,
		R.drawable.img_loading,
		R.drawable.img_loading,
		R.drawable.img_loading,
		R.drawable.img_loading,
	};


	
	//声明ImageAdapter
	public HotGalleryAdapter(Context c,  HotGoodsManager hotgoodsmanager){
		mContext = c;
		mHotGoodsManager = hotgoodsmanager;
		mHotGoodsManager.addObserver(mObserver);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}


	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mHotGoodsManager.isLoading()){
			return mImageIds[position];
		}
		return mHotGoodsManager.get(position).getBitmap();
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "convertView = " + convertView + " position = " + position);
		ImageView imageView = new ImageView(mContext);

		//还在加载时显示加载图片
		if(mHotGoodsManager.isLoading()){
			imageView.setImageResource(R.drawable.img_loading);
		}else{
			Log.d(TAG, "mHotGoodsManager.size() = " + mHotGoodsManager.size());
			BasicInfoItem item = mHotGoodsManager.get(position % mHotGoodsManager.size());//循环显示
			Bitmap bitmap = item.getBitmap();
			if(bitmap != null){
				imageView.setImageBitmap(bitmap); 
			}
			else{
				imageView.setImageResource(R.drawable.img_loading);
			}
		}
		
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


		if(selectItem==position){
			//Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.hyperspace_out);


			//ScaleAnimation animation = new ScaleAnimation(0, 2, 0, 2, 
			//  ScaleAnimation.RELATIVE_TO_SELF, 0, ScaleAnimation.RELATIVE_TO_SELF, 0);
			//animation.setDuration(2000); 
			//imageview.startAnimation(animation);
			imageView.setLayoutParams(new Gallery.LayoutParams(180,180));
		}
		else{

			imageView.setLayoutParams(new Gallery.LayoutParams(120,120));
		}

		return imageView;
	}

	public void setSelectItem(int selectItem) {

		if (this.selectItem != selectItem) {                
			this.selectItem = selectItem;
			notifyDataSetChanged();
		}
	}
	
	private static class ViewHolder {
		ImageView imageView;
	}
}


