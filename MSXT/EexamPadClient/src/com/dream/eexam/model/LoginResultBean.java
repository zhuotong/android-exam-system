package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

public class LoginResultBean extends BaseBean {
	private String status;
	private String message;
	private String conversation;
	private String interviewer;
	private String jobtitle;
	private List<ExamBaseBean> examList = new ArrayList<ExamBaseBean>();
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getConversation() {
		return conversation;
	}
	public void setConversation(String conversation) {
		this.conversation = conversation;
	}
	public String getInterviewer() {
		return interviewer;
	}
	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}
	public String getJobtitle() {
		return jobtitle;
	}
	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}
	public List<ExamBaseBean> getExamList() {
		return examList;
	}
	public void setExamList(List<ExamBaseBean> examList) {
		this.examList = examList;
	}
	
	
}
