package com.huawei.ptn.activity.home;
import com.huawei.ptn.R;
import com.huawei.ptn.activity.HomeActivityGroup;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class NewsListActivity extends Activity {
 
 private static final String TAG = NewsListActivity.class.getSimpleName();
 
 private ListView news_listview;
 
 protected void onCreate(Bundle savedInstanceState){
  
  super.onCreate(savedInstanceState);
  requestWindowFeature(Window.FEATURE_NO_TITLE);
  setContentView(R.layout.news_list_activity);
  
  InitView();
  
  
 }
 private void InitView(){
  news_listview = (ListView)findViewById(R.id.news_list);
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