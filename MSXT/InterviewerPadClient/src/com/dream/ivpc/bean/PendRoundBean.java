package com.dream.ivpc.bean;

public class PendRoundBean extends XMLBean {
	private boolean success;
	private String rId;
	private String rTime;
	private String rName;
	private String rCandidate;
	private String rAppPosition;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getrId() {
		return rId;
	}
	public void setrId(String rId) {
		this.rId = rId;
	}
	public String getrTime() {
		return rTime;
	}
	public void setrTime(String rTime) {
		this.rTime = rTime;
	}
	public String getrName() {
		return rName;
	}
	public void setrName(String rName) {
		this.rName = rName;
	}
	public String getrCandidate() {
		return rCandidate;
	}
	public void setrCandidate(String rCandidate) {
		this.rCandidate = rCandidate;
	}
	public String getrAppPosition() {
		return rAppPosition;
	}
	public void setrAppPosition(String rAppPosition) {
		this.rAppPosition = rAppPosition;
	}
	
}
