package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private Integer index;
	private String questionType;//0 is single, 1 is multi
	private String questionDesc;
	private List<Choice> choices = new ArrayList<Choice>();
	
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
	public String getQuestionDesc() {
		return questionDesc;
	}
	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}
	
	public List<Choice> getChoices() {
		return choices;
	}
	
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
	}
	
	public Question() {
		super();
	}
	
	public Question(Integer index, String questionType, String questionDesc) {
		super();
		this.index = index;
		this.questionType = questionType;
		this.questionDesc = questionDesc;
	}
	
	public Question(Integer index, String questionType, String questionDesc,
			List<Choice> choices) {
		super();
		this.index = index;
		this.questionType = questionType;
		this.questionDesc = questionDesc;
		this.choices = choices;
	}
	
	

	
	
	
}
