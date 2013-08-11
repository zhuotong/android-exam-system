package com.dream.exam.bean;

public class Choice extends BaseBean {
	private int index;
	private String label;
	private String content;
	
	public Choice() {
		super();
	}
	public Choice(int index, String label, String content) {
		super();
		this.index = index;
		this.label = label;
		this.content = content;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
