package com.dream.eexam.model;

@Deprecated
public class Choice {
	private Integer choiceIndex;
	private String choiceLabel;
	private String choiceContent;
	
	public Choice() {
		super();
	}
	
	public Integer getChoiceIndex() {
		return choiceIndex;
	}
	public void setChoiceIndex(Integer choiceIndex) {
		this.choiceIndex = choiceIndex;
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


	
}
