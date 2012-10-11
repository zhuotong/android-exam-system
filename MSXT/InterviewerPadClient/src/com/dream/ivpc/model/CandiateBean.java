package com.dream.ivpc.model;

public class CandiateBean extends BaseBean {
	private String name;
	private String position;
	private String time;

	public CandiateBean() {
		super();
	}

	public CandiateBean(String name, String position, String time) {
		super();
		this.name = name;
		this.position = position;
		this.time = time;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
