package com.dream.ivpc.model;

public class ResumeBean extends BaseBean {
	private String label;
	private String value;
	
	public ResumeBean() {
		super();
	}
	
	public ResumeBean(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}