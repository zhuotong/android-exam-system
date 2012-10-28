package com.dream.ivpc.model;

public class QuestionBean extends BaseBean {
	private int index;
	private String catalog;
	private String questionName;
	private boolean result;

	public QuestionBean() {
		super();
	}

	public QuestionBean(int index, String catalog, String questionName,
			boolean result) {
		super();
		this.index = index;
		this.catalog = catalog;
		this.questionName = questionName;
		this.result = result;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	
	
}
