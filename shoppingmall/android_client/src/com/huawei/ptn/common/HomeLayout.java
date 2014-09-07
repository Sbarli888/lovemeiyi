package com.huawei.ptn.common;

public class HomeLayout{
 
	private String  functionId;
	private String  title;
	private int     type;        //1: 滚动图片; 2: 文章列表
	private boolean isFold;      //折叠标志，初始状态是否折叠，如果是展开则加载数据
	private String  url;         //API地址
   
	public HomeLayout()
	{
	}

	public HomeLayout(String  functionId, String  title, int type, boolean isFold, String  url)
	{
		this.functionId = functionId;
		this.title      = title;
		this.type       = type;
		this.isFold     = isFold;
		this.url        = url;
	}


	  public String getFunctionId()
	  {
	    return this.functionId;
	  }


	  public String getTitle()
	  {
	    return this.title;
	  }

	  public int getType()
	  {
	    return this.type;
	  }
	  
	  public boolean getisFold()
	  {
	    return this.isFold;
	  }

	  public String getUrl()
	  {
	    return this.url;
	  }

	  public void setFunctionId(String paramString)
	  {
	    this.functionId = paramString;
	  }

	  public void setTitle(String paramString)
	  {
	    this.title = paramString;
	  }

	  public void setType(int paramString)
	  {
	    this.type = paramString;
	  }

	  public void setisFold(Boolean paramisFold)
	  {
	    this.isFold = paramisFold;
	  }

	  public void setUrl(String paramString)
	  {
	    this.url = paramString;
	  }
}
