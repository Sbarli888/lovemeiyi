package com.huawei.ptn.activity.home;


import com.huawei.ptn.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
* Sample code that invokes the speech recognition intent API.
*/
public class Voice_test_activity extends Activity implements OnClickListener {

private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

private ListView mList;

private AlertDialog.Builder dialogBuilder;
/**
* Called with the activity is first created.
*/
@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// Inflate our UI from its XML layout description.
	setContentView(R.layout.voice_recognition);
	
	// Get display items for later interaction
	Button speakButton = (Button) findViewById(R.id.btn_speak);
	
	mList = (ListView) findViewById(R.id.listView1);
	
	this.dialogBuilder = new AlertDialog.Builder(this);

	// Check to see if a recognition activity is present
	PackageManager pm = getPackageManager();
	List activities = pm.queryIntentActivities(
	new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	
	if (activities.size() != 0) {
		speakButton.setOnClickListener(this);
	} else {
		speakButton.setEnabled(false);
		speakButton.setText("Recognizer not present");
	}
}

/**
* Handle the click on the start recognition button.
*/
	public void onClick(View v) {
	if (v.getId() == R.id.btn_speak) {
		startVoiceRecognitionActivity();
	}
	}

	/**
	* Fire an intent to start the speech recognition activity.
	*/
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
		RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "LoveMeiyi.com");
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
	/**
	* Handle the results from the recognition activity.
	*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it could have heard
			ArrayList voice_list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			//mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, voice_list));
			
			String[] arrayOfString = new String[voice_list.size()];
			
		    for (int i = 0; i< voice_list.size(); i++) {
		    	arrayOfString[i] = ((String)voice_list.get(i));
		    }
		    
		    this.dialogBuilder
			.setTitle(getResources().getString(R.string.shake_type))
			.setItems(arrayOfString, null)
		    .show();
		}
	
		super.onActivityResult(requestCode, resultCode, data);
	}
} 