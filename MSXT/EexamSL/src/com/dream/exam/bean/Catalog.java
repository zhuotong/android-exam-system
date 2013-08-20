package com.dream.exam.bean;

import java.util.List;

public class Catalog extends BaseBean {
	private int index;
	private String name;
	private String desc;
	private List<Question> questions;
	
	public Catalog() {
		super();
	}
	public Catalog(int index, String name, String desc, List<Question> questions) {
		super();
		this.index = index;
		this.name = name;
		this.desc = desc;
		this.questions = questions;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	
}
