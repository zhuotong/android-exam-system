package com.dream.ivpc.model;

public class QuestionDetailBean extends QuestionBean{
	private String qcontent;
	private String qanswer;

	public QuestionDetailBean() {
		super();
	}

	public String getQcontent() {
		return qcontent;
	}

	public QuestionDetailBean(String qcontent, String qanswer) {
		super();
		this.qcontent = qcontent;
		this.qanswer = qanswer;
	}

	public void setQcontent(String qcontent) {
		this.qcontent = qcontent;
	}

	public String getQanswer() {
		return qanswer;
	}

	public void setQanswer(String qanswer) {
		this.qanswer = qanswer;
	}


	
}
