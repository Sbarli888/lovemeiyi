package com.huawei.ptn.activity.home;

import com.huawei.ptn.R;
import com.huawei.ptn.activity.HomeActivityGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class NewsDetailActivity extends Activity {
 
 private static final String TAG = NewsDetailActivity.class.getSimpleName();
 
 private TextView news_title;
 private TextView news_date;
 private TextView news_content;
 private Handler m_NewsDetailHandler = new Handler();
 
 private static class m_NewsDetail_data{
  public static String news_title;
  public static String news_date;
  public static String news_content;
 }
 
 private class NewsDetailNetThread extends Thread{
  private int m_news_id;
  public NewsDetailNetThread(int news_id){
   m_news_id = news_id;
  }
  public void run(){
   
   //完成网络数据加载
   m_NewsDetail_data.news_title = "新闻标题";
   m_NewsDetail_data.news_date  = "2012-4-25";
   m_NewsDetail_data.news_content = "新闻内容1" +
     "新闻内容2" +
     "新闻内容3";
   
    
   m_NewsDetailHandler.post(new Runnable(){
    public void run(){
     showNewsDetail();
    }
   });
  }
 }
 
 private void showNewsDetail(){
  news_title.setText(m_NewsDetail_data.news_title);
  news_date.setText(m_NewsDetail_data.news_date);
  news_content.setText(m_NewsDetail_data.news_content);
 }
 protected void onCreate(Bundle savedInstanceState){
	  
	  super.onCreate(savedInstanceState);
	  requestWindowFeature(Window.FEATURE_NO_TITLE);
	  setContentView(R.layout.news_detail);
	  
	  InitView();
	  
	  doNewsDetail();
	  
	 }
	 
	 private void InitView(){
	  news_title = (TextView)findViewById(R.id.news_detail_title);
	  news_date = (TextView)findViewById(R.id.news_detail_time);
	  news_content = (TextView)findViewById(R.id.news_detail_content);
	 }
	 private void doNewsDetail(){
	  
	  Intent intent = getIntent();
	  Bundle data = intent.getExtras();
	  int news_id = (Integer) data.get("news_id");
	  
	  Log.d(TAG, "news_id = " + news_id);
	  
	  new NewsDetailNetThread(news_id).start();
	  
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