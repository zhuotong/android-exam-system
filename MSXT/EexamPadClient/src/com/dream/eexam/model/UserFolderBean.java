package com.dream.eexam.model;

public class UserFolderBean {
	private String index;
	private String name;
	
	public UserFolderBean(String index, String name) {
		super();
		this.index = index;
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
