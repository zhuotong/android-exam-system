package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private Integer id;
	private Integer questionType;//0 is single, 1 is multi
	private String questionDesc;
	private List<Choice> choices = new ArrayList<Choice>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuestionType() {
		return questionType;
	}
	public void setQuestionType(Integer questionType) {
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
	
	public Question(Integer id, Integer questionType, String questionDesc) {
		super();
		this.id = id;
		this.questionType = questionType;
		this.questionDesc = questionDesc;
	}
	
	public Question(Integer id, Integer questionType, String questionDesc,
			List<Choice> choices) {
		super();
		this.id = id;
		this.questionType = questionType;
		this.questionDesc = questionDesc;
		this.choices = choices;
	}
	
	

	
	
	
}
