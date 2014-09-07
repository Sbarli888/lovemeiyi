package com.huawei.ptn.model;

public class CategoryItem {
	
	private long id;
	
	private String name;
	
	private String keywords;
	
	private String description;
	
	private long parentId;
	
	private int sortOrder;
	
	private String templateFile;
	
	private String measureUnit;
	
	private int showInNav;
	
	private String style;
	
	private int isShow;
	
	private int grade;
	
	private String filterAttr;

	public CategoryItem() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public int getShowInNav() {
		return showInNav;
	}

	public void setShowInNav(int showInNav) {
		this.showInNav = showInNav;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getFilterAttr() {
		return filterAttr;
	}

	public void setFilterAttr(String filterAttr) {
		this.filterAttr = filterAttr;
	}

	public CategoryItem(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	

}







