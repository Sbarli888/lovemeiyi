package com.huawei.ptn.model;

public class NewsItem {
	private int news_id;
	private String news_title;
	private String news_content;
	
	public NewsItem(int news_id, String news_title, String news_content){
		this.news_id = news_id;
		this.news_title = news_title;
		this.news_content = news_content;
	}
	
	public int getNewsId(){
		return news_id;
	}

	public String getTitle(){
		return news_title;
	}
	
	public String getContent(){
		return news_content;
	}
}
