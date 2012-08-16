package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CatalogBean {
	private Integer id;
	private Integer index;
	private String desc;
	private List<Question> questions = new ArrayList<Question>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public CatalogBean() {
		super();
	}
	
	public CatalogBean(Integer id, String desc) {
		super();
		this.id = id;
		this.desc = desc;
	}
	
	
	
	
}
