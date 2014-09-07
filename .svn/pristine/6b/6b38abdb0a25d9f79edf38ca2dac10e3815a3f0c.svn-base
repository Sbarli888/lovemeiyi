package com.huawei.ptn;

import com.huawei.ptn.util.ScreenTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class NewerGuideActivity extends Activity implements
		View.OnClickListener {
	
	private final int GUIDE_PICTURES_SIZE = 5;
	private final int FLIP_MIN_DISTANCE = 100;
	private int mCurrentPos;

	private int mDownX;
	private int mUpX;

	private ViewFlipper mViewFlipper;
	private View page1;
	private View page2;
	private View page3;
	private View page4;
	private View pageLast;

	private LinearLayout mRadioLy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newer_guide);
		initChildViews();

		((ImageView) pageLast.findViewById(R.id.guide_to_main))
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						navigateToMain();
					}
				});

	}

	private void navigateToMain() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void initChildViews() {
		initViewFlipper();
		initImageView(page1);
		initImageView(page2);
		initImageView(page3);
		initImageView(page4);
		initImageView(pageLast);
		initRadioSpot();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.guide_next) {
			showNextView();
		} else if ((v.getId() == R.id.guide_pre)
				&& (!mViewFlipper.getCurrentView().equals(page1))) {
			showPrevView();
		}
		return;
	}

	private void showPrevView() {
		if (mViewFlipper.getCurrentView().equals(page1)) {
			navigateToMain();
			return;
		}

		show2dPrevious();

		int old = mCurrentPos;
		mCurrentPos = mCurrentPos - 1;
		if (mCurrentPos < 0) {
			mCurrentPos += GUIDE_PICTURES_SIZE;
		}

		if (mRadioLy != null) {
			mRadioLy.getChildAt(old).setBackgroundResource(
					R.drawable.inactive_page_spot);
			mRadioLy.getChildAt(mCurrentPos).setBackgroundResource(
					R.drawable.active_page_spot);
		}
	}

	private void showNextView() {
		if (mViewFlipper.getCurrentView().equals(pageLast)) {
			navigateToMain();
			return;
		}
		
		show2dNext();

		int old = mCurrentPos;
		mCurrentPos = (mCurrentPos + 1) % GUIDE_PICTURES_SIZE;

		if (mRadioLy != null) {
			mRadioLy.getChildAt(old).setBackgroundResource(
					R.drawable.inactive_page_spot);
			mRadioLy.getChildAt(mCurrentPos).setBackgroundResource(
					R.drawable.active_page_spot);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
			showPrevView();
		}
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mDownX = (int) event.getX();
			return true;
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			mUpX = (int) event.getX();
			if (((mUpX - mDownX) > FLIP_MIN_DISTANCE)
					&& !mViewFlipper.getCurrentView().equals(page1)) {
				showPrevView();
			}
			if ((mUpX - mDownX) < -FLIP_MIN_DISTANCE) {
				showNextView();
			}
		}
		return true;
	}

	private void initImageView(View view) {
		ImageView imagePrev = (ImageView) view.findViewById(R.id.guide_pre);
		ImageView imageNext = (ImageView) view.findViewById(R.id.guide_next);
		imagePrev.setOnClickListener(this);
		imageNext.setOnClickListener(this);
	}

	private void initRadioSpot() {
		mRadioLy = (LinearLayout) findViewById(R.id.home_radio_ly);

		int spotSize = ScreenTools.getInstance(this).dip2px(10);
		LinearLayout.LayoutParams radioLayoutParams = new LinearLayout.LayoutParams(
				spotSize, spotSize);
		radioLayoutParams.setMargins(spotSize, 0, 0, 0);

		for (int i = 0; i < GUIDE_PICTURES_SIZE; i++) {
			ImageView radioIv = new ImageView(this);
			radioIv.setLayoutParams(radioLayoutParams);
			radioIv.setBackgroundResource(R.drawable.inactive_page_spot);
			mRadioLy.addView(radioIv);
		}

		// 默认处于第一个图片
		if (mRadioLy != null && mRadioLy.getChildCount() > 0) {
			mRadioLy.getChildAt(0).setBackgroundResource(
					R.drawable.active_page_spot);
			mCurrentPos = 0;
		}
	}

	private void initViewFlipper() {
		LayoutInflater inflater = LayoutInflater.from(this);
		page1 = inflater.inflate(R.layout.newer_guide_page_1, null);
		page2 = inflater.inflate(R.layout.newer_guide_page_2, null);
		page3 = inflater.inflate(R.layout.newer_guide_page_3, null);
		page4 = inflater.inflate(R.layout.newer_guide_page_4, null);
		pageLast = inflater.inflate(R.layout.newer_guide_last, null);

		mViewFlipper = (ViewFlipper) findViewById(R.id.newer_flipper);
		mViewFlipper.addView(page1);
		mViewFlipper.addView(page2);
		mViewFlipper.addView(page3);
		mViewFlipper.addView(page4);
		mViewFlipper.addView(pageLast);
	}

	private void show2dNext() {
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_out));
		mViewFlipper.showNext();
	}
	
	private void show2dPrevious() {
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));
		mViewFlipper.showPrevious();
	}
	
}
