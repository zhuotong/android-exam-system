package com.dream.ivpc.model;

public class DetailBean {
	private String name;
	private int imageId;
	
	public DetailBean() {
		super();
	}
	public DetailBean(String name, int imageId) {
		super();
		this.name = name;
		this.imageId = imageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	
	
}
