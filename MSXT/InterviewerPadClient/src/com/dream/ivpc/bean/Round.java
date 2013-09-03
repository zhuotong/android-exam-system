package com.dream.ivpc.bean;

public class Round {
	private String id;
	private String type;
	private String name;
	private String planTime;
	private String doneTime;
	private boolean compFlag;
	
	public Round() {
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlanTime() {
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	public String getDoneTime() {
		return doneTime;
	}
	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}
	public boolean isCompFlag() {
		return compFlag;
	}
	public void setCompFlag(boolean compFlag) {
		this.compFlag = compFlag;
	}
	
	
	
}
