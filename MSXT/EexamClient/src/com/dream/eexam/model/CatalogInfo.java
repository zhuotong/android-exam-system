package com.dream.eexam.model;

public class CatalogInfo {
	private Integer index;
	private String desc;
	private Integer questionNumber;
	private Integer comquestionNumber;
	
	public CatalogInfo() {
		super();
	}
	public CatalogInfo(Integer index, String desc, Integer questionNumber,
			Integer comquestionNumber) {
		super();
		this.index = index;
		this.desc = desc;
		this.questionNumber = questionNumber;
		this.comquestionNumber = comquestionNumber;
	}
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}
	public Integer getComquestionNumber() {
		return comquestionNumber;
	}
	public void setComquestionNumber(Integer comquestionNumber) {
		this.comquestionNumber = comquestionNumber;
	}
	
	public Double getComPercentage() {
		return (1.0 * comquestionNumber)/questionNumber;
	}

	
}
