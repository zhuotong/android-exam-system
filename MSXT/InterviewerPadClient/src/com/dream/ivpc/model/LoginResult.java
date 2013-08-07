package com.dream.ivpc.model;

public class LoginResult {
	private boolean success;
	private String sessionId;

	public LoginResult() {
		super();
	}

	public LoginResult(boolean success, String sessionId) {
		super();
		this.success = success;
		this.sessionId = sessionId;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
