package com.dream.ivpc.model;

public class LoginResult {
	private boolean success;
	
	private String userId;
	private String userName;
	private String token;
	
	private String error_code;
	private String error_desc;
	

	public LoginResult() {
		super();
	}


	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getError_code() {
		return error_code;
	}


	public void setError_code(String error_code) {
		this.error_code = error_code;
	}


	public String getError_desc() {
		return error_desc;
	}


	public void setError_desc(String error_desc) {
		this.error_desc = error_desc;
	}

	
}
