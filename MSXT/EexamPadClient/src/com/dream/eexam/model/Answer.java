package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Answer extends BaseBean{
	private Integer questionId;
	private Integer answerId;
	private List<String> choiceIds = new ArrayList<String>();
	private String choiceIdsString;

	public Answer(Integer questionId, Integer answerId, String choiceIdsString) {
		super();
		this.questionId = questionId;
		this.answerId = answerId;
		this.choiceIdsString = choiceIdsString;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

	public List<String> getChoiceIds() {
		return choiceIds;
	}

	public void setChoiceIds(List<String> choiceIds) {
		this.choiceIds = choiceIds;
	}

	public String getChoiceIdsString() {
		return choiceIdsString;
	}

	public void setChoiceIdsString(String choiceIdsString) {
		this.choiceIdsString = choiceIdsString;
	}


	
	
	
	
}
