package com.dream.eexam.model;

public class UserFolderBean {
	private int index;
	private String name;
	
	public UserFolderBean(int index, String name) {
		super();
		this.index = index;
		this.name = name;
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
	
}
