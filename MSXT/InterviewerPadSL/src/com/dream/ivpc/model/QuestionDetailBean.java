package com.dream.ivpc.model;

public class QuestionDetailBean extends QuestionBean{
	private String qcontent;
	private String qanswer;

	public QuestionDetailBean() {
		super();
	}

	public QuestionDetailBean(int index, String catalog, String questionName,
			boolean result,String qcontent, String qanswer) {
		super(index, catalog, questionName, result);
		this.qcontent = qcontent;
		this.qanswer = qanswer;
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
