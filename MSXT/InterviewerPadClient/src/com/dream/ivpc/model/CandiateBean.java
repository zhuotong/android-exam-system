package com.dream.ivpc.model;

public class CandiateBean extends BaseBean {
	private String time;
	private String position;
	private String name;
	
	public CandiateBean() {
		super();
	}
	
	public CandiateBean(String time, String position, String name) {
		super();
		this.time = time;
		this.position = position;
		this.name = name;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	
}
