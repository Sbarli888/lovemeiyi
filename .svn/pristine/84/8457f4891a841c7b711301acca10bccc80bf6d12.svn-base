package com.huawei.ptn.activity.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.app.Activity;
import android.app.Service;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.huawei.ptn.R;

//需要实现SensorEventListener接口
public class shakeActivity extends Activity implements
		SensorEventListener {
	Button clear;

	// 定义sensor管理器
	private SensorManager mSensorManager;

	// 震动
	private Vibrator vibrator;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.sensor_shake);

		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// 震动
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		clear = (Button) findViewById(R.id.shake_sensor);
		clear.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {
				clear.setText("你点击了按键!");
			}
		});
	}

	protected void onResume() {
		super.onResume();

		// 加速度传感器
		// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// 当传感器精度改变时回调该方法，Do nothing.
	}

	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();

		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			// 加速度达到一定值后认为已经摇动手机
			if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math
					.abs(values[2]) > 14)) {

				clear.setText("你已摇动了手机，下面的美衣你喜欢吗？");

				// 摇动手机后，震动提示
				vibrator.vibrate(500);
			}
		}
	}

}
