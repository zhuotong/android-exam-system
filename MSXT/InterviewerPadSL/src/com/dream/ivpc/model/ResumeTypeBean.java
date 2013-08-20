package com.dream.ivpc.model;

public class ResumeTypeBean {
	private String typeName;
	private boolean isAvailable;
	
	public ResumeTypeBean(String typeName, boolean isAvailable) {
		super();
		this.typeName = typeName;
		this.isAvailable = isAvailable;
	}
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
}
