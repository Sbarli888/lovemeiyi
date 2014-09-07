package com.huawei.ptn.view;

import com.huawei.ptn.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

public class CornerListView extends ListView {

	public CornerListView(Context context) {
		super(context);
	}

	public CornerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CornerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int)ev.getX();
			int y = (int)ev.getY();
			int itemIndex = pointToPosition(x, y);
			if (AdapterView.INVALID_POSITION == itemIndex) {
				break;
			} else {
				if (itemIndex == 0) { //第一个列表项
					if (itemIndex == (getAdapter().getCount() - 1)) { //有且只有一个列表项
						setSelector(R.drawable.app_list_corner_round);
					} else {
						setSelector(R.drawable.app_list_corner_round_top);
					}
				} else if (itemIndex == (getAdapter().getCount() - 1)) {
					setSelector(R.drawable.app_list_corner_round_bottom);
				} else {
					setSelector(R.drawable.app_list_corner_shape);
				}
			}
		case MotionEvent.ACTION_UP:
			break;
			
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	

}
























