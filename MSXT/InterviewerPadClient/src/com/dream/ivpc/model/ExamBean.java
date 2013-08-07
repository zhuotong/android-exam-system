package com.dream.ivpc.model;

public class ExamBean {
	private String examId;
	private String examName;
	
	public ExamBean() {
		super();
	}

	public ExamBean(String examId, String examName) {
		super();
		this.examId = examId;
		this.examName = examName;
	}
	
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	
}
