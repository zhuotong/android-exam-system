package com.msxt.examination;

import com.msxt.common.FoggySearchCriteria;

public class ExamSearchCriteria extends FoggySearchCriteria {
	private static final long serialVersionUID = -2331251274479033469L;
	
	private String positionId;
	
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
}
