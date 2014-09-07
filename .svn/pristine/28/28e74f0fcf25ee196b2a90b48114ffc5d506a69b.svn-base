package com.huawei.ptn.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {
	
	private static final String TAG = ScrollLayout.class.getSimpleName();
	
	private static final int SNAP_VELOCITY = 600;
	
	private VelocityTracker mVelocityTracker; //用于判断用户手势
	
	private Scroller mScroller;
	
	private int mCurScreen;
	
	private int mDefaultScreen;
	
	private float mLastMotionX;
	
	private OnViewChangedListener mOnViewChangedListener;

	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ScrollLayout(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		mCurScreen = mDefaultScreen;
		mScroller = new Scroller(context);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft+childWidth, childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		
		scrollTo(mCurScreen * width, 0);
	}

	@Override
	public void computeScroll() {
		//super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		final int action = event.getAction();
		final float x = event.getX();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "onTouchEvent ACTION_DOWN");
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}
			if (!mScroller.isFinished()) { //屏幕按下时，应该停止滚动
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
			
		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "onTouchEvent ACTION_MOVE");
			int deltaX = (int)(mLastMotionX - x);
			if (IsCanMove(deltaX)) {
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}
				mLastMotionX = x;
				scrollBy(deltaX, 0);
			}
			break;
			
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "onTouchEvent ACTION_UP");
			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}
			
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				Log.i(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY &&
					mCurScreen < getChildCount() - 1) {
				Log.i(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();
			}
			
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			break;
			
		}
		
		return true;
	}
	
	private boolean IsCanMove(int deltaX) {
		if (getScrollX() <= 0 && deltaX < 0) {
			return false;
		}
		
		if (getScrollX() >= ((getChildCount() - 1) * getWidth()) && deltaX > 0) {
			return false;
		}
		
		return true;
	}
	
	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));
		if (getScrollX() != (whichScreen * getWidth())) {
			
			final int delta = whichScreen * getWidth() - getScrollX();
			mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta*2));
			
			mCurScreen = whichScreen;
			invalidate();	// Redraw the layout
			
			if (mOnViewChangedListener != null) {
				mOnViewChangedListener.OnViewChanged(mCurScreen);
			}
		}
		
	}
	
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth/2)/screenWidth;
		snapToScreen(destScreen);
	}

	public void setmOnViewChangedListener(
			OnViewChangedListener mOnViewChangedListener) {
		this.mOnViewChangedListener = mOnViewChangedListener;
	}
	

}























