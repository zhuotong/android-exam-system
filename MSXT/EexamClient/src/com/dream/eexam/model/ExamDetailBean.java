package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

public class ExamDetailBean {
	private String examinationid;
	private String name;
	private Integer time;
	private String confuse;
	private List<CatalogBean> catalogs = new ArrayList<CatalogBean>();
	
	public String getExaminationid() {
		return examinationid;
	}
	public void setExaminationid(String examinationid) {
		this.examinationid = examinationid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getConfuse() {
		return confuse;
	}
	public void setConfuse(String confuse) {
		this.confuse = confuse;
	}
	public List<CatalogBean> getCatalogs() {
		return catalogs;
	}
	public void setCatalogs(List<CatalogBean> catalogs) {
		this.catalogs = catalogs;
	}
	
}
