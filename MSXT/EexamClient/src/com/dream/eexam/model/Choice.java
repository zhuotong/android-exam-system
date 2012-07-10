package com.dream.eexam.model;

public class Choice {
	private Integer id;
	private String choiceDesc;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getChoiceDesc() {
		return choiceDesc;
	}
	public void setChoiceDesc(String choiceDesc) {
		this.choiceDesc = choiceDesc;
	}
	
	public Choice(Integer id, String choiceDesc) {
		super();
		this.id = id;
		this.choiceDesc = choiceDesc;
	}
	
	
	
}
