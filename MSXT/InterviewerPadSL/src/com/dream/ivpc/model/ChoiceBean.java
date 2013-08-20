package com.dream.ivpc.model;

public class ChoiceBean extends BaseBean{
	private String label;
	private String content;
	
	public ChoiceBean() {
		super();
	}

	public ChoiceBean(String label, String content) {
		super();
		this.label = label;
		this.content = content;
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
