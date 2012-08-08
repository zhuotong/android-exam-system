package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private String questionId;
	private Integer index;
	private String questionType;
	private Integer score;
	private String content;
	private List<Choice> choices = new ArrayList<Choice>();
	private Integer catalogIndex;
	
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<Choice> getChoices() {
		return choices;
	}
	
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
	
	public Integer getCatalogIndex() {
		return catalogIndex;
	}
	public void setCatalogIndex(Integer catalogIndex) {
		this.catalogIndex = catalogIndex;
	}
	public Question() {
		super();
	}
	
	
	
}
