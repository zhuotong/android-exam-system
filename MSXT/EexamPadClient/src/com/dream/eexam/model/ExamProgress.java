package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

public class ExamProgress {
	private Integer catalogIndex;
	private Integer questionIndex;
	private StringBuffer answerString;
	private List<Answer> answers = new ArrayList<Answer>();
	
	public Integer getCatalogIndex() {
		return catalogIndex;
	}
	public void setCatalogIndex(Integer catalogIndex) {
		this.catalogIndex = catalogIndex;
	}
	public Integer getQuestionIndex() {
		return questionIndex;
	}
	public void setQuestionIndex(Integer questionIndex) {
		this.questionIndex = questionIndex;
	}
	
	public StringBuffer getAnswerString() {
		return answerString;
	}
	public void setAnswerString(StringBuffer answerString) {
		this.answerString = answerString;
	}
	
	public void setAnswerString(List<Answer> answers) {
		StringBuffer sb = new StringBuffer();
		for(Answer answer: answers){
			sb.append(String.valueOf(answer.getQuestionId()));
			sb.append(":");
			sb.append(answer.getChoiceIdsString());
		}
		this.answerString = sb;
	}
	
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
}
