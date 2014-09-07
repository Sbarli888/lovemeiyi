package com.huawei.ptn.activity.more;
import com.huawei.ptn.R;
import com.huawei.ptn.activity.MoreActivityGroup;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SettingActivity extends Activity {
	
	private static final String TAG = SettingActivity.class.getSimpleName();
	
	private RelativeLayout shake_type_layout;
	private TextView shake_type_selected;
	
	SharedPreferences share_prefs;
	SharedPreferences.Editor sp_editor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_activity);
		  
		init();
		shake_settting();
	}
 
	private void init(){
		shake_type_layout = (RelativeLayout)findViewById(R.id.shake_setting_item);
		shake_type_selected = (TextView)findViewById(R.id.shake_type_selected);
		
		//更换版本后设置依然生效
		share_prefs = getSharedPreferences("shake", MODE_PRIVATE);
		sp_editor = share_prefs.edit();
		boolean m_shake_vibrate_config = share_prefs.getBoolean("shake_vibrate_config", true);
		boolean m_shake_sound_config = share_prefs.getBoolean("shake_sound_config", true);
		if((m_shake_vibrate_config == true)
			&&(m_shake_sound_config == true)){
			
			shake_type_selected.setText(getResources().getString(R.string.shake_type_selected1));
			
		}else if((m_shake_vibrate_config == true)
				&&(m_shake_sound_config == false)){
			
			shake_type_selected.setText(getResources().getString(R.string.shake_type_selected2));
			
		}else if((m_shake_vibrate_config == false)
				&&(m_shake_sound_config == true)){
			
			shake_type_selected.setText(getResources().getString(R.string.shake_type_selected2));
			
		}else
		{
			shake_type_selected.setText(getResources().getString(R.string.shake_type_selected4));
		}
		
	}
 
	private void shake_settting(){
		shake_type_layout.setOnClickListener(new OnClickListener(){	
			public void onClick(View v) {
				Builder alert = new AlertDialog.Builder(SettingActivity.this.getParent())
				.setTitle(getResources().getString(R.string.shake_type))
				
				.setSingleChoiceItems(new  String[] {getResources().getString(R.string.shake_type_selected1),
						getResources().getString(R.string.shake_type_selected2),
						getResources().getString(R.string.shake_type_selected3),
						getResources().getString(R.string.shake_type_selected4)},  0,   
				  new  DialogInterface.OnClickListener() {  
					
				     public void onClick(DialogInterface dialog,  int  item_id) {
					    Log.d(TAG, "item_id = " + item_id);
				        switch(item_id){
				        	case 0:
						    	sp_editor.putBoolean("shake_vibrate_config", true);
						    	sp_editor.putBoolean("shake_sound_config", true);
				        		shake_type_selected.setText(getResources().getString(R.string.shake_type_selected1));
				        		break;
				        	case 1:
						    	sp_editor.putBoolean("shake_vibrate_config", true);
						    	sp_editor.putBoolean("shake_sound_config", false);
				        		shake_type_selected.setText(getResources().getString(R.string.shake_type_selected2));
				        		break;
				        	case 2:
						    	sp_editor.putBoolean("shake_vibrate_config", false);
						    	sp_editor.putBoolean("shake_sound_config", true);
				        		shake_type_selected.setText(getResources().getString(R.string.shake_type_selected3));
				        		break;
				        	case 3:
						    	sp_editor.putBoolean("shake_vibrate_config", false);
						    	sp_editor.putBoolean("shake_sound_config", false);
				        		shake_type_selected.setText(getResources().getString(R.string.shake_type_selected4));
				        		break;
				        	default:
				        		break;
				        }
				        sp_editor.commit();

				        dialog.dismiss();  
				     }  
				  }  
				)
				.setNegativeButton("取消" ,  null );
				
				alert.show();

		    }
		});
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MoreActivity.class);
		Window window = MoreActivityGroup.getInstance()
				.getLocalActivityManager()
				.startActivity(MoreActivity.class.getSimpleName(), intent);
		View view = window.getDecorView();
		MoreActivityGroup.getInstance().setContentView(view);
	}
}