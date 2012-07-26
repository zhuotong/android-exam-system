package com.dream.eexam.model;

public class Choice {
	private Integer choiceIndex;
	private String choiceDesc;
	
	private String choiceLabel;
	private String choiceContent;
	
	public Integer getChoiceIndex() {
		return choiceIndex;
	}
	public void setChoiceIndex(Integer choiceIndex) {
		this.choiceIndex = choiceIndex;
	}
	
	public String getChoiceDesc() {
		return choiceDesc;
	}
	public void setChoiceDesc(String choiceDesc) {
		this.choiceDesc = choiceDesc;
	}
	
	public String getChoiceLabel() {
		return choiceLabel;
	}
	public void setChoiceLabel(String choiceLabel) {
		this.choiceLabel = choiceLabel;
	}
	public String getChoiceContent() {
		return choiceContent;
	}
	public void setChoiceContent(String choiceContent) {
		this.choiceContent = choiceContent;
	}
	public Choice() {
		super();
	}
	public Choice(Integer choiceIndex, String choiceDesc) {
		super();
		this.choiceIndex = choiceIndex;
		this.choiceDesc = choiceDesc;
	}	
	
}
