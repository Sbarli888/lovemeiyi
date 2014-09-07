package com.huawei.ptn.view;

import com.huawei.ptn.R;
import com.huawei.ptn.activity.home.HomeActivity;
import com.huawei.ptn.common.HomeLayout;

import android.app.Activity;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpansionView {
	
	private final String TAG = ExpansionView.class.getSimpleName();
	private LinearLayout mChildLayout;
	private View mChildView;
	private Activity mContext;
	private LinearLayout mHeadLayout;
	private View mItemView;
	private ImageView mLeft;
	private ImageView mRight;
	private String mTitle;
	private TextView mTitleView;

	private boolean isLoading; // 加载时置为true，让标题title显示"加载中..."
	private boolean isShow; // 折叠与展开的标志位；

	public ExpansionView(Activity activity) {
		mContext = activity;
		initView();
	}

	private void addItem(View view, View.OnClickListener listener) {
		if ((mChildLayout != null) && (view != null)
				&& (mChildLayout.indexOfChild(view) == -1)) {
			mChildLayout.addView(view);
		}
		mItemView.findViewById(R.id.home_item_view)
				.setOnClickListener(listener);
	}

	private void initView() {
		mItemView = View.inflate(mContext, R.layout.app_home_item, null);
		mTitleView = ((TextView) mItemView.findViewById(R.id.home_item_title));
		mItemView.findViewById(R.id.home_item_left_icon).setVisibility(
				View.GONE);// GONE不可见并且不占用空间
		mLeft = ((ImageView) mItemView.findViewById(R.id.home_item_left_icon));
		mRight = ((ImageView) mItemView.findViewById(R.id.home_item_right_icon));
		mChildLayout = ((LinearLayout) mItemView
				.findViewById(R.id.home_item_layout));
		mHeadLayout = ((LinearLayout) mItemView
				.findViewById(R.id.home_item_center_view));
	}

	public void baseHome(HomeLayout paramHomeLayout,
			final HomeActivity.OnExpandableListener paramOnExpandableListener) {
		// 功能模块ID
		final String FunctionId = paramHomeLayout.getFunctionId();

		// 设置功能模块的名称：例如，“精品推荐”、“站内新闻”
		String Title = paramHomeLayout.getTitle();
		setTitle(Title);

		// 获取当前模块的视图 itemView，等价于mItemView = View.inflate(this.context,
		// R.layout.app_home_item, null);
		final View localView = getItemView();

		// 让ChildView不可见，当数据加载完毕之后再展开
		collapse();

		// 根据isFold标志位判断该模块是否需要加载并显示数据
		if (paramHomeLayout.getisFold()) {
			// 调用onLoadData方法加载数据
			paramOnExpandableListener.onLoadData(localView, mChildView,
					FunctionId);

			// 加载完毕后展开
			mRight.setImageResource(R.drawable.android_order_trace_info_less);
		}
		addItem(mChildView, new View.OnClickListener() {

			public void onClick(View v) {

				// isShow表示初始状态是否折叠，false折叠，true不折叠

				// 折叠时
				if (false == isShow) {

					// 在这里完成展开时需要完成的操作
					paramOnExpandableListener.onShow(localView,
							ExpansionView.this.mChildView, FunctionId);
					expand(); // 展开

				}
				// 展开时
				else {

					// 在这里完成隐藏时需要完成的操作
					paramOnExpandableListener.onHide(localView,
							ExpansionView.this.mChildView, FunctionId);
					collapse();// 折叠
				}
			}
		});
	}

	public void collapse() {
		isShow = false;
		mChildView.setVisibility(View.GONE);
		mRight.setImageResource(R.drawable.android_order_trace_info_more);
	}

	public void expand() {
		isShow = true;
		mChildView.setVisibility(View.VISIBLE);
		mRight.setImageResource(R.drawable.android_order_trace_info_less);
	}

	public View getChildView() {
		return mChildView;
	}

	public View getHeadView() {
		return mHeadLayout;
	}

	public int getHeadViewChildCount() {
		return mHeadLayout.getChildCount();
	}

	public View getItemView() {
		return mItemView;
	}

	public ImageView getLeft() {
		return mLeft;
	}

	public ImageView getRight() {
		return mRight;
	}

	public View getRootView() {
		return mItemView;
	}

	public String getTitle() {
		return mTitleView.getText().toString();
	}

	public void setChildView(View view) {
		mChildView = view;
	}

	public void setHeadView(View paramView) {
		mHeadLayout.removeAllViews();
		mHeadLayout.addView(paramView);
	}

	public void setLeft(ImageView paramImageView) {
		mLeft = paramImageView;
	}

	public void setRight(ImageView paramImageView) {
		mRight = paramImageView;
	}

	public void setTitle(String paramString) {
		mTitle = paramString;
		mTitleView.setText(paramString);
	}

	// 是否显示加载字符
	public void setTitleSuffix(boolean paramIsLoading) {
		this.isLoading = paramIsLoading;
		TextView localTextView = mTitleView;

		String str_Loading = mContext.getResources()
				.getString(R.string.loading);

		String str_empty = "";

		if (isLoading) {

			localTextView.setText(localTextView.getText().toString()
					+ str_Loading);
		} else {
			String str_title = getTitle();
			str_title.replace(str_Loading, str_empty);
			localTextView.setText(str_title);
		}
	}
}
