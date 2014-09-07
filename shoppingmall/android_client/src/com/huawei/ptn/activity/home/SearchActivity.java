package com.huawei.ptn.activity.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.huawei.ptn.R;
import com.huawei.ptn.activity.HomeActivityGroup;
import com.huawei.ptn.activity.category.BasicInfoActivity;
import com.huawei.ptn.activity.category.ImageList;
import com.huawei.ptn.activity.more.SettingActivity;
import com.huawei.ptn.common.Constants;
import com.huawei.ptn.util.BitmapUtils;
import com.huawei.ptn.util.CacheImageUtil;
import com.huawei.ptn.util.HttpUtils;

public class SearchActivity extends Activity implements SensorEventListener, OnClickListener{
	
	private static final String TAG = SearchActivity.class.getSimpleName();
	
	private int VOICE_RECOGNITION_REQUEST_CODE = 1234; //����ʶ�������룬ȡֵ����
	
	private EditText m_Edit_search_input;
	private Button   m_btn_search_submit;
	
	private TextView m_shake_search_btn;
	private TextView m_voice_search_btn;
	private TextView m_shake_phone_tips;
	

	private LinearLayout m_LinearLayout_shake;
	private ImageView m_imageView_goods;
	private TextView m_Text_goods_name;
	
	private long m_goods_id;
	private String m_goods_name;
	private String m_goods_imageUrl;
	private Bitmap m_goods_bitmap;
	
	private boolean m_ShakedFlag = false;
	
	SharedPreferences share_prefs;
	SharedPreferences.Editor sp_editor;
	boolean m_shake_vibrate_config;
	boolean m_shake_sound_config;
	
	private AlertDialog.Builder dialogBuilder;
	
	private AlertDialog listDialog;
	
	// ����sensor������
	private SensorManager mSensorManager;
	
	// ��
	private Vibrator vibrator;
	
	//����
	private MediaPlayer shake_sound;
	
	private ProgressDialog mProDialog;
	
	
	boolean mLoading;
	private String url = Constants.SEARCH_SHAKE_PHONE;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 1:
					DisplayShakeGoods();
					mProDialog.dismiss();
					mLoading = false;
					break;

				default:
					break;
				}
			}
		}
	};

	private List activities;
	
	private void DisplayShakeGoods(){
		
		if(m_shake_sound_config){
			// ҡ���ֻ��������Ч
			shake_sound.start();
		}

		m_imageView_goods.setImageBitmap(m_goods_bitmap);
		m_Text_goods_name.setText(m_goods_name);
		m_shake_phone_tips.setText(getResources().getString(R.string.shaked_phone_tips));
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.search_activity);
		
		init();
		
		//�������ܵ�ʵ��
		search();
		
		shake_phone();
		
		voice_search();
	}
	
	private void init(){
		//����
		m_Edit_search_input=(EditText)findViewById(R.id.search_key_input);
		m_btn_search_submit=(Button)findViewById(R.id.home_search_button);
		
		// ��ȡ�������������
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// ��
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		
		shake_sound = MediaPlayer.create(getBaseContext(), R.raw.shake_sound);
		
		//ҡһҡ��Ʒ���
		mLoading = false;
		mProDialog = new ProgressDialog(this.getParent());
		mProDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProDialog.setMessage(getResources().getString(R.string.shake_loading));
		mProDialog.setIndeterminate(true);
		
		//ҡһҡ��Ʒ��ʾ���֣��ڸò����ϼ���OnClick��ת����Ʒ����
		m_LinearLayout_shake = (LinearLayout)findViewById(R.id.shake_layout);
		m_Text_goods_name =(TextView)findViewById(R.id.shake_goods_name);
		m_imageView_goods = (ImageView)findViewById(R.id.shake_goods_image);
		
		m_voice_search_btn = (TextView)findViewById(R.id.voice_search_btn);
		m_shake_search_btn = (TextView)findViewById(R.id.shake_search_btn);
		
		m_shake_phone_tips = (TextView)findViewById(R.id.shake_goods_tips);
		
		
		share_prefs = getSharedPreferences("shake", MODE_PRIVATE);
		sp_editor = share_prefs.edit();
		
		this.dialogBuilder = new AlertDialog.Builder(this);
		
		
	}
	
	private void search(){

		//������ʱ����ֵΪedit:hint������(����ȹ)
		m_btn_search_submit.setOnClickListener(new OnClickListener(){
		   	public void onClick(View v) {   

			   	//m_Edit_search_input.setPressed(true);
			   	//m_Edit_search_input.setFocusable(true);
			   	//m_Edit_search_input.requestFocus();
			   	//TODO: ���ת��SearchActivity��ʱ��ֱ�ӽ�EditText�������㣿
		   		
			   	//����ʱ���ȹر������
			   	hideSoftInputMode(m_Edit_search_input);
		   		
			    Intent i_search = new Intent(SearchActivity.this, ImageList.class);
			    i_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    Bundle data = new Bundle();
			    
			    data.putInt("request_code", Constants.SEARCH_REQUEST);
				
				
				String EmptyInput = "";
				 
				if(m_Edit_search_input.getText().toString().equals(EmptyInput)){
					Log.d(TAG, "m_Edit_search_input.getHint() = " + m_Edit_search_input.getHint().toString());
					data.putString("search_input", m_Edit_search_input.getHint().toString());
					data.putString(Constants.IMAGE_LIST_TITLE, 
							"\"" + m_Edit_search_input.getHint().toString() + "\"" + "���������");
				}
				else{
					
					Log.d(TAG, "m_Edit_search_input.getText() = " + m_Edit_search_input.getText().toString());
					data.putString("search_input", m_Edit_search_input.getText().toString());
					data.putString(Constants.IMAGE_LIST_TITLE, 
							"\"" + m_Edit_search_input.getText().toString() + "\"" + "���������");
				}
				
				i_search.putExtras(data);
			
				Window window = HomeActivityGroup.getInstance()
						.getLocalActivityManager().startActivity(ImageList.class.getSimpleName(), i_search);
				View view = window.getDecorView();
				HomeActivityGroup.getInstance().setContentView(view);

		   	}
		});
		
		m_btn_search_submit.setOnEditorActionListener(new OnEditorActionListener(){
	
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				showSoftInputMode(m_Edit_search_input);
				return false;
			}
		});
		
		
	}
	

	
	private void shake_phone(){
		m_shake_search_btn.setOnClickListener(new OnClickListener(){
		   	public void onClick(View v) { 
				mLoading = true;
				m_ShakedFlag = true;
				showWaitDialog();
				new NetworkThread().start();
		   	}
		});
		
		m_LinearLayout_shake.setOnClickListener(new OnClickListener(){
		   	public void onClick(View v) { 
		   			if(m_ShakedFlag)
		   			{
						Intent intent = new Intent(SearchActivity.this, BasicInfoActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra(Constants.GOODS_ID, m_goods_id);
						intent.putExtra(Constants.IMAGE_LIST_TITLE, m_goods_name);
						intent.putExtra("request_code", Constants.SHAKE_SEARCH_REQUEST);
		
						Window window = HomeActivityGroup.getInstance()
								.getLocalActivityManager().startActivity(
										BasicInfoActivity.class.getSimpleName(), intent);
						View view = window.getDecorView();
						HomeActivityGroup.getInstance().setContentView(view);
		   			}
		   	}
		});
	}
	
	private void voice_search(){
		
		PackageManager pm = getPackageManager();
		List activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0) {
			
			m_voice_search_btn.setOnClickListener(this);
			
		} else {
			
			m_voice_search_btn.setEnabled(false);
			m_voice_search_btn.setText("Recognizer not present");
			
		}

	}
	
	public void onClick(View v) {
		if (v.getId() == R.id.voice_search_btn) {
			startVoiceRecognitionActivity();
		}
	}
	
	private void hideSoftInputMode(EditText editText)
	{
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).
		hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	private void showSoftInputMode(EditText editText)
	{
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).
		showSoftInputFromInputMethod(editText.getWindowToken(), RESULT_OK);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// �����������ȸı�ʱ�ص��÷�����Do nothing.
	}

	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();

		// values[0]:X�ᣬvalues[1]��Y�ᣬvalues[2]��Z��
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			// ���ٶȴﵽһ��ֵ����Ϊ�Ѿ�ҡ���ֻ�
			if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math
					.abs(values[2]) > 14)) {

				//��ֹ�ڼ��صĹ�����ظ�ҡ��
				if(mLoading==false)
				{
					m_ShakedFlag = true;
					mLoading = true;
					showWaitDialog();
					new NetworkThread().start();
					
					getShakeConfig();
					
					if(m_shake_vibrate_config){
						// ҡ���ֻ������ʾ
						vibrator.vibrate(500);
					}

				}
			}
		}
	}
	
	private void getShakeConfig(){
		
		//δ���ù������£�Ĭ�϶���TRUE
		m_shake_vibrate_config = share_prefs.getBoolean("shake_vibrate_config", true);
		Log.d(TAG, "m_shake_vibrate_config = " + m_shake_vibrate_config);
		m_shake_sound_config = share_prefs.getBoolean("shake_sound_config", true);
		Log.d(TAG, "m_shake_sound_config = " + m_shake_sound_config);
	}
	
	private class NetworkThread extends Thread {

		@Override
		public void run() {
			try {
				String content = HttpUtils.getRequest(url);
				System.out.println("content is : " + content);
				if (content != null) {
					Log.i(TAG, "content = " + content);
					JSONArray jsonArray = new JSONArray(content);
					parse(jsonArray);
				} else {
					Log.i(TAG, "contents from server is null");
				}
			} catch (JSONException e) {

				Log.e(TAG, e.toString());
			} catch (ClientProtocolException e) {

				Log.e(TAG, e.toString());
			} catch (IOException e) {

				Log.e(TAG, e.toString());
			}
		}

		private void parse(JSONArray array) {
			try {
				int count = array.length();
				if (count == 0) {
					Log.e(TAG, "count == 0");
				}
				for (int i = 0; i < count; i++) {
					JSONObject obj = array.getJSONObject(i);
					m_goods_id = obj.getInt("goods_id");
					m_goods_name = obj.getString("goods_name");
					m_goods_imageUrl = obj.getString("original_img");
	
					m_goods_bitmap = CacheImageUtil.getCacheBitmap(m_goods_imageUrl);
				}
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				Log.e(TAG, e.toString());
			}
		}

	}
	
	public void showWaitDialog() {
		mProDialog.show();
	}
	
	
	protected void onResume() {
		super.onResume();

		// ���ٶȴ�����
		// ����SENSOR_DELAY_UI��SENSOR_DELAY_FASTEST��SENSOR_DELAY_GAME�ȣ�
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	private void startVoiceRecognitionActivity() {
		
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "LoveMeiyi.com");

		//intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		this.getParent().startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	
	public void handleActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it could have heard
			ArrayList voice_list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			//mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, voice_list));
			
			final String[] arrayOfString = new String[voice_list.size()];
			
		    for (int i = 0; i< voice_list.size(); i++) {
		    	arrayOfString[i] = ((String)voice_list.get(i));
		    }
		    
		    
		    Builder alert = new AlertDialog.Builder(SearchActivity.this.getParent())
			.setTitle(getResources().getString(R.string.voice_search_please_choose))
			.setItems(arrayOfString, new DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String search_key_words = arrayOfString[which];
				    Intent i_search = new Intent(SearchActivity.this, ImageList.class);
				    i_search.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    Bundle data = new Bundle();
				    data.putInt("request_code", Constants.SEARCH_REQUEST);
					//Log.d(TAG, "m_Edit_search_input.getText() = " + m_Edit_search_input.getText().toString());
					data.putString("search_input", search_key_words);
					data.putString(Constants.IMAGE_LIST_TITLE, 
							"\"" + search_key_words + "\"" + "���������");
					
					i_search.putExtras(data);
					
					Window window = HomeActivityGroup.getInstance()
							.getLocalActivityManager().startActivity(ImageList.class.getSimpleName(), i_search);
					View view = window.getDecorView();
					HomeActivityGroup.getInstance().setContentView(view);
				}
				
			});
		    
		    alert.show();
		    

		    /*
		    this.dialogBuilder
			.setTitle(getResources().getString(R.string.shake_type))
			.setItems(arrayOfString, null)
		    .show();
		    */
		}
	
		super.onActivityResult(requestCode, resultCode, data);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it could have heard
			ArrayList voice_list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			//mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, voice_list));
			
			String[] arrayOfString = new String[voice_list.size()];
			
		    for (int i = 0; i< voice_list.size(); i++) {
		    	arrayOfString[i] = ((String)voice_list.get(i));
		    }
		    /*
		    Builder alert = new AlertDialog.Builder(SearchActivity.this.getParent())
			.setTitle(getResources().getString(R.string.shake_type))
			.setItems(arrayOfString, null);
		    
		    alert.show();
		    */
		    /*
		    this.dialogBuilder
			.setTitle(getResources().getString(R.string.shake_type))
			.setItems(arrayOfString, null)
		    .show();
		    */
		}
	
		super.onActivityResult(requestCode, resultCode, data);
    }
	/*
	         this.dialogBuilder.setTitle(R.string.voice_search_please_choose);
    this.dialogBuilder.setItems(arrayOfString, new DialogInterface.OnClickListener()
    {
     	private String[] val$items;

		public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            Log.d("voice", "content:" + paramInt + " " + this.val$items[paramInt] + " ");
            String str = this.val$items[paramInt].replace("��", "");
            SearchActivity.this.searchSubmit(str);
            SearchActivity.this.listDialog.dismiss();
          }
    });
    
	 */
	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		Log.d(TAG, "onBackPressed");
		Intent intent = new Intent(this, HomeActivity.class);
		Window window = HomeActivityGroup.getInstance()
		  .getLocalActivityManager()
		   .startActivity(HomeActivity.class.getSimpleName(), intent);	
		View view = window.getDecorView();
		HomeActivityGroup.getInstance().setContentView(view);
	}
}
