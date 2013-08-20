package com.dream.exam.bean;

import java.util.List;

public class Exam extends BaseBean {
	private String examId;
	private String name;
	private String time;
	private boolean confuse;
	private List<Catalog> catalogs;
	
	public Exam() {
		super();
	}
	public Exam(String examId, String name, String time, boolean confuse,
			List<Catalog> catalogs) {
		super();
		this.examId = examId;
		this.name = name;
		this.time = time;
		this.confuse = confuse;
		this.catalogs = catalogs;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isConfuse() {
		return confuse;
	}
	public void setConfuse(boolean confuse) {
		this.confuse = confuse;
	}
	public List<Catalog> getCatalogs() {
		return catalogs;
	}
	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}
	
	
	
	
}
