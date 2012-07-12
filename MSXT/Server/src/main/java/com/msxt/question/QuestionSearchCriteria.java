package com.msxt.question;

import com.msxt.common.FoggySearchCriteria;

public class QuestionSearchCriteria extends FoggySearchCriteria {
	private static final long serialVersionUID = -2331251274479033469L;
	
	private String questionTypeId;
	private String positionId;
	
	public String getQuestionTypeId() {
		return questionTypeId;
	}
	public void setQuestionTypeId(String questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
}
