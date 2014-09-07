package com.huawei.ptn.common;

import android.os.Environment;

public class Constants {
	
	public static final String URI_BASE_URL = "http://m.lovemeiyi.com";
	
	public static final String IMAGE_BASE_URL = "http://www.lovemeiyi.com";

	public static final String APK_BASE_URL = "http://www.lovemeiyi.com";

	//API接口列表
	
	//输入搜索
	public static final String SEARCH_INPUT = URI_BASE_URL + "/search/input";
	
	//摇一摇搜索
	public static final String SEARCH_SHAKE_PHONE = URI_BASE_URL + "/search/shake/";
	
	//首页BANNER广告(output: img_id, img_path, link_type, img_link)
	//link_type: 1: app/goods;  2: app/article  3: app/webview  4: UCweb
	public static final String AD_BANNER = URI_BASE_URL + "/ad/banner/";
	
	//精品推荐API
	public static final String URL_GOODS_BEST = URI_BASE_URL + "/goods/best";
	
	//新品上市
	public static final String URL_GOODS_NEW = URI_BASE_URL + "/goods/newest";
	
	//热卖商品
	public static final String URL_GOODS_HOT = URI_BASE_URL + "/goods/hot";
	
	public static final String CATEGORY_FIRST = URI_BASE_URL + "/category/top";
	
	public static final String CATEGORY_SECOND = URI_BASE_URL + "/category/second/";
	
	public static final String CATEGOTY_GOOD = URI_BASE_URL + "/category/goods/";
	
	public static final String CATEGORY_BASIC_INFO = URI_BASE_URL + "/goods/info/";
	
	public static final String CATEGORY_DETAIL_INFO = URI_BASE_URL + "/goods/desc/";
	


	//获取APK版本信息及下载路径(VersionCode, VersionName, ApkPath)
	public static final String APK_VERSION = URI_BASE_URL + "/apk/get/0";//爱美衣app_id = 0;
	

	
	public static final String TITLE = "title";
	
	public static final String FIRST_ID = "firstid";
	
	public static final String GOODS_ID = "goodsid";
	
	public static final String IMAGE_LIST_TITLE = "goodsname";
	
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	
	public static final String CONTENT_TYPE_VALUE = "text/json; charset=utf-8";
	
	//version info
	public static int localVersion = 0;
	
	public static int serverVersion = 0;
	
	public static String downloadDir = "app/download/";
	
	public static int SORT_TYPE_UPLOAD_TIME = 0x0;
	
	public static int SORT_TYPE_PRICE_ASC = 0x1;
	
	public static int SORT_TYPE_PRICE_DESC = 0x2;
	
	public static int SORT_TYPE_UPDATE_TIME = 0x3;
	
	
	//TAB的索引
	public static final int TAB_HOME_INDEX = 0x0;
	
	public static final int TAB_CATEGORY_IDNEX = 0x1;
	
	public static final int TAB_CAR_INDEX = 0x2;
	
	public static final int TAB_ACCOUNT_IDNEX = 0x3;
	
	public static final int TAB_MOER_INDEX = 0x4; 
	
	//Activity请求码
	public static final int SEARCH_REQUEST = 0x1; 
	public static final int HOME_SEARCH_REQUEST = 0x2; 
	public static final int SHAKE_SEARCH_REQUEST = 0x3; 
	public static final int CATEGORY_REQUEST = 0x4;
	public static final int HOME_BEST_REQUEST = 0x5;

	//HTTP请求类型
	public static final int HTTP_GET = 0x1; 
	public static final int HTTP_POST = 0x2;
	
	
	public static final class HomeLayout_BestGoods {
	     public static final String  FUNCTIONID  = "BestGoods";
	     public static final String  TITLE       = "精品推荐";
	     public static final boolean ISFOLD      = true;      //初始状态是否展开的标志
	     public static final int     TYPE        = 1;         //1: 滚动图片; 2: 文章列表
	     public static final String  URL         = "";
	}
	
	public static final class HomeLayout_NewGoods {
	     public static final String  FUNCTIONID  = "NewGoods";
	     public static final String  TITLE       = "新品上市";
	     public static final boolean ISFOLD      = true;      //初始状态是否展开的标志
	     public static final int     TYPE        = 1;         //1: 滚动图片; 2: 文章列表
	     public static final String  URL         = "";
	}
	
	public static final class HomeLayout_HotGoods {
	     public static final String  FUNCTIONID  = "HotGoods";
	     public static final String  TITLE       = "热卖商品";
	     public static final boolean ISFOLD      = true;      //初始状态是否展开的标志
	     public static final int     TYPE        = 1;         //1: 滚动图片; 2: 文章列表
	     public static final String  URL         = "";
	}
	
	    
	public static final class HomeLayout_News {
	     public static final String  FUNCTIONID  = "news";
	     public static final String  TITLE       = "商城新闻";
	     public static final boolean ISFOLD      = true;      //初始状态是否展开的标志
	     public static final int     TYPE        = 2;         //1: 滚动图片; 2: 文章列表
	     public static final String  URL         = "";
	} 
	
	//SD卡上文件存储路径
	public static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/lovemeiyi/";
	public static final String PICTURE_DIR;
	
	static {
		PICTURE_DIR = BASE_PATH + "pic";
	}

}






















