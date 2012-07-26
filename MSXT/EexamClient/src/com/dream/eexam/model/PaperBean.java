package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

public class PaperBean extends BaseBean {
	
	private String desc;
	private Integer time;
	private List<CatalogBean> catalogBeans = new ArrayList<CatalogBean>();
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public List<CatalogBean> getCatalogBeans() {
		return catalogBeans;
	}
	public void setCatalogBeans(List<CatalogBean> catalogBeans) {
		this.catalogBeans = catalogBeans;
	}
	
	public PaperBean() {
		super();
	}
	
	public PaperBean(String desc, List<CatalogBean> catalogBeans) {
		super();
		this.desc = desc;
		this.catalogBeans = catalogBeans;
	}
	
}
