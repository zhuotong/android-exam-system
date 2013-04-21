package com.msxt.client.server;

import java.util.Map;

public interface ServerProxy {

	public Result login(String loginName, String loginPassword);
	
	public Result getExam(String examId);
	
	public Result submitAnswer( String examinationid, Map<String, String> answers);
	
	public void setConversationId(String conversationId);
	
	public static enum STATUS{
		SUCCESS, ERROR
	}
	
	public static class Result{
		private STATUS status;
		private String errorMessage;
		private String successMessage;
		
		public boolean isSuccess(){
			return status == STATUS.SUCCESS;
		}
		
		public STATUS getStatus() {
			return status;
		}
		public void setStatus(STATUS status) {
			this.status = status;
		}
		
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		public String getSuccessMessage() {
			return successMessage;
		}
		public void setSuccessMessage(String successMessage) {
			this.successMessage = successMessage;
		}
	}
	
	public static class Factroy{
		
		private static ServerProxy instance = null;
		
		public static ServerProxy createInstance( String server, int port ){
			instance = new WebServerProxy( server, port ); 
			return instance;
		}
		
		public static ServerProxy getCurrrentInstance(){
			return instance;
		}
	}
}
