package com.dream.eexam.model;

import com.msxt.client.model.Examination.Choice;

public class ChoiceExt extends Choice {
	private boolean isChecked = false;

	public ChoiceExt() {
		super();
	}
	
	public ChoiceExt(boolean isChecked) {
		super();
		this.isChecked = isChecked;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
}
