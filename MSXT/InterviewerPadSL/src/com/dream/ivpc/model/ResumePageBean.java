package com.dream.ivpc.model;

public class ResumePageBean {
	private Integer index;
	private StringBuffer content;
	
	public ResumePageBean() {
		super();
	}
	
	public ResumePageBean(Integer index, StringBuffer content) {
		super();
		this.index = index;
		this.content = content;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public StringBuffer getContent() {
		return content;
	}
	
	public void setContent(StringBuffer content) {
		this.content = content;
	}
	
}
