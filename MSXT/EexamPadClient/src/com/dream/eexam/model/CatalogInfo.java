package com.dream.eexam.model;

public class CatalogInfo {
	private Integer index;
	private String desc;
	private Integer fQuestionIndex;
	private Integer totalQuesions;
	private Integer answeredQuestions;
	
	public CatalogInfo(Integer index, String desc, Integer fQuestionIndex,
			Integer totalQuesions, Integer answeredQuestions) {
		super();
		this.index = index;
		this.desc = desc;
		this.fQuestionIndex = fQuestionIndex;
		this.totalQuesions = totalQuesions;
		this.answeredQuestions = answeredQuestions;
	}


	public Double getComPercentage() {
		return (1.0 * answeredQuestions)/totalQuesions;
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


	public Integer getfQuestionIndex() {
		return fQuestionIndex;
	}


	public void setfQuestionIndex(Integer fQuestionIndex) {
		this.fQuestionIndex = fQuestionIndex;
	}


	public Integer getTotalQuesions() {
		return totalQuesions;
	}


	public void setTotalQuesions(Integer totalQuesions) {
		this.totalQuesions = totalQuesions;
	}


	public Integer getAnsweredQuestions() {
		return answeredQuestions;
	}


	public void setAnsweredQuestions(Integer answeredQuestions) {
		this.answeredQuestions = answeredQuestions;
	}

	
	
}
