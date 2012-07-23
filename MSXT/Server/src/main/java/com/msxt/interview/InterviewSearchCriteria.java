package com.msxt.interview;

import java.util.Date;

import com.msxt.common.FoggySearchCriteria;

public class InterviewSearchCriteria extends FoggySearchCriteria {
	private static final long serialVersionUID = -2331251274479033469L;
	
	private String positionId;
	
	private Date startBegin;
	
	private Date startEnd;
	
	public String getPositionId() {
		return positionId;
	}
	
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	
	public Date getStartBegin() {
		return startBegin;
	}

	public void setStartBegin(Date startBegin) {
		this.startBegin = startBegin;
	}

	public Date getStartEnd() {
		return startEnd;
	}

	public void setStartEnd(Date startEnd) {
		this.startEnd = startEnd;
	}
	
	
}
