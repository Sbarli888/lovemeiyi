package com.huawei.ptn.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.huawei.ptn.R;
import com.huawei.ptn.util.ScreenTools;

public class MyViewFlipper extends ViewFlipper {
	
	private static int TIMER_COUNTER = 0;
	private static long TIMER_PERIOD = 500L;
	private static int FLIP_MIN_DISTANCE = 80;

	private OnChangeListener mChangeListener;
	private Context mContext;
	private boolean mNeedAuto = false;
	private Handler mHandler;
	private MyTimerTask mTimerTask;
	private int mNow;
	private Timer mTimer;
	private boolean mTimerFirst;
	private int mSizeOfView = 0;

	private Rotate3d mLeftAnimation;
	private Rotate3d mRightAnimation;

	private int mDownX;
	private int mUpX;

	static {
		TIMER_COUNTER = 8;
	}

	public MyViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mNow = 0;
		mTimerFirst = true;
		int halfWidth = ScreenTools.getInstance(mContext).getWidth() / 2;
		mLeftAnimation = new Rotate3d(0.0F, -90.0F, halfWidth, 0.0F);
		mLeftAnimation.setFillAfter(true);
		mLeftAnimation.setDuration(500L);
		mRightAnimation = new Rotate3d(90.0F, 0.0F, halfWidth, 0.0F);
		mRightAnimation.setFillAfter(true);
		mRightAnimation.setDuration(500L);
	}

	public void flipperShowNext() {
		if (mSizeOfView == 0) {
			return;
		}

		int old = mNow;
		if (mNow >= mSizeOfView - 1) {
			shakeViewAnim();
			return;
		}
		mNow++;

		show3dNext();

		if (mChangeListener != null) {
			mChangeListener.onChange(old, mNow);
		}
	}

	public void filpperShowPrevious() {
		if (mSizeOfView == 0) {
			return;
		}

		int old = mNow;
		if (old <= 0) {
			shakeViewAnim();
			return;
		}
		mNow--;

		show3dPrevious();

		if (mChangeListener != null) {
			mChangeListener.onChange(old, mNow);
		}
	}

	public void setSize(int sizeOfView) {
		mSizeOfView = sizeOfView;
		removeAllViews();
		mNow = 0;
		mTimerFirst = true;
	}

	public void setOnChangeListener(OnChangeListener onChangeListener) {
		mChangeListener = onChangeListener;
	}

	public void resetTimer() {
		if (mTimerTask != null) {
			mTimerTask.mCount = 0;
		}
	}

	@Override
	protected void onAttachedToWindow() {
		if (mNeedAuto) {
			startFlipperTimer();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		stopFlipperTimer();
	}

	public void startFlipperTimer() {
		stopFlipperTimer();
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (mTimerFirst) {
					if (mNow != (mSizeOfView - 1)) {
						flipperShowNext();
					}
				}
			}

		};

		mTimer = new Timer();
		mTimerTask = new MyTimerTask();
		mTimer.scheduleAtFixedRate(mTimerTask, 0L, TIMER_PERIOD);
	}

	public void stopFlipperTimer() {
		if (mTimerTask != null) {
			mTimerTask.mCount = 0;
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	public static interface OnChangeListener {
		public void onChange(int old, int current);
	}

	class MyTimerTask extends TimerTask {

		public int mCount = 0;

		@Override
		public void run() {
			if (mCount >= MyViewFlipper.TIMER_COUNTER) {
				mHandler.sendMessage(new Message());
				mCount = 0;
			}
			mCount++;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mDownX = (int) event.getX();
			return true;
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			mUpX = (int) event.getX();

			if ((mUpX - mDownX) > FLIP_MIN_DISTANCE) {
				filpperShowPrevious();
				resetTimer();
			}
			if ((mUpX - mDownX) < -FLIP_MIN_DISTANCE) {
				flipperShowNext();
				resetTimer();
			}
		}
		return true;
	}

	private void shakeViewAnim() {
		View view = this.getCurrentView();
		Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.shake);
		view.setAnimation(anim);
		view.startAnimation(anim);
		return;
	}

	private void show3dPrevious() {
		mLeftAnimation.reverseTransformation(true);
		mRightAnimation.reverseTransformation(true);
		setInAnimation(mRightAnimation);
		setOutAnimation(mLeftAnimation);
		showPrevious();
	}

	private void show2dPrevious() {
		setInAnimation(mContext.getApplicationContext(), R.anim.push_right_in);
		setOutAnimation(mContext.getApplicationContext(), R.anim.push_right_out);
		showPrevious();
	}

	private void show3dNext() {
		mLeftAnimation.reverseTransformation(false);
		mRightAnimation.reverseTransformation(false);
		setInAnimation(mRightAnimation);
		setOutAnimation(mLeftAnimation);
		showNext();
	}

	private void show2dNext() {
		setInAnimation(mContext.getApplicationContext(), R.anim.push_left_in);
		setOutAnimation(mContext.getApplicationContext(), R.anim.push_left_out);
		showNext();
	}

}
