package com.dream.exam.bean;

import java.util.List;

public class Question extends BaseBean{
	private int index;
	private String name;
	private String questionid;
	private int type;
	private int score;
	private String content;
	private List<Choice> choices;
	
	public Question() {
		super();
	}
	public Question(int index, String name, String questionid, int type,
			int score, String content, List<Choice> choices) {
		super();
		this.index = index;
		this.name = name;
		this.questionid = questionid;
		this.type = type;
		this.score = score;
		this.content = content;
		this.choices = choices;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuestionid() {
		return questionid;
	}
	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
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
	
}
