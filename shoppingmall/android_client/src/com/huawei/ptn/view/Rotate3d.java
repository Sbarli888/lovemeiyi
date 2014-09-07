package com.huawei.ptn.view;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3d extends Animation {

	public static final int ROTATE_X = 0;
	public static final int ROTATE_Y = 1;
	public static final int ROTATE_Z = 2;
	public static final int ROTATE_XY = 3;
	public static final int ROTATE_XZ = 4;
	public static final int ROTATE_YZ = 5;
	public static final int ROTATE_XYZ = 6;

	private Camera mCamera;
	private float mCenterX;
	private float mCenterY;
	private float mFromDegree;
	private float mSaveFromDegree;
	private float mSaveToDegree;
	private float mToDegree;
	private int mType;

	public Rotate3d(float fromDegree, float toDegree, float centerX,
			float centerY) {
		mFromDegree = fromDegree;
		mToDegree = toDegree;
		mCenterX = centerX;
		mCenterY = centerY;
		mSaveFromDegree = fromDegree;
		mSaveToDegree = toDegree;
		mType = ROTATE_Y;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {

		float degree = mFromDegree
				+ (interpolatedTime * (mToDegree - mFromDegree));
		Matrix matrix = t.getMatrix();

		if (degree <= -76.0F) {
			mCamera.save();
			rotate(-90.0F);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else if (degree >= 76.0F) {
			mCamera.save();
			rotate(90.0F);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else {
			mCamera.save();
			mCamera.translate(0, 0, mCenterX);
			rotate(degree);
			mCamera.translate(0, 0, -mCenterX);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		}

		matrix.preTranslate(-mCenterX, -mCenterY);
		matrix.postTranslate(mCenterX, mCenterY);
	}

	private void rotate(float degree) {
		switch (mType) {
		case ROTATE_X:
			mCamera.rotateX(degree);
			break;
		case ROTATE_Y:
			mCamera.rotateY(degree);
			break;
		case ROTATE_Z:
			mCamera.rotateZ(degree);
			break;
		case ROTATE_XY:
			mCamera.rotateX(degree);
			mCamera.rotateY(degree);
			break;
		case ROTATE_XZ:
			mCamera.rotateX(degree);
			mCamera.rotateZ(degree);
			break;
		case ROTATE_YZ:
			mCamera.rotateY(degree);
			mCamera.rotateZ(degree);
			break;
		case ROTATE_XYZ:
			mCamera.rotateX(degree);
			mCamera.rotateY(degree);
			mCamera.rotateZ(degree);
			break;
		default:
			break;
		}
	}

	public void reverseTransformation(boolean isRight) {
		if (isRight) {
			mFromDegree = -mSaveFromDegree;
			mToDegree = -mSaveToDegree;
		} else {
			mFromDegree = mSaveFromDegree;
			mToDegree = mSaveToDegree;
		}
		return;
	}

	public void setType(int type) {
		mType = type;
	}

	public int getType() {
		return mType;
	}
}
