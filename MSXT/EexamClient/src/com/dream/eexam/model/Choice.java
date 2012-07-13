package com.dream.eexam.model;

public class Choice {
	private String choiceIndex;
	private String choiceDesc;
	
	public String getChoiceIndex() {
		return choiceIndex;
	}
	public void setChoiceIndex(String choiceIndex) {
		this.choiceIndex = choiceIndex;
	}
	
	public String getChoiceDesc() {
		return choiceDesc;
	}
	public void setChoiceDesc(String choiceDesc) {
		this.choiceDesc = choiceDesc;
	}
	
	public Choice(String choiceIndex, String choiceDesc) {
		super();
		this.choiceIndex = choiceIndex;
		this.choiceDesc = choiceDesc;
	}	
	
}
