package com.dream.ivpc.model;

public class ResumePicBean {
	private Integer index;
	private StringBuffer content;
	
	public ResumePicBean() {
		super();
	}
	
	public ResumePicBean(Integer index, StringBuffer content) {
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
