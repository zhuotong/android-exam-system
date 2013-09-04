package com.dream.ivpc.bean;

import java.util.List;

public class CandidateBean {
	private String candidateId;
	private String candidateName;
	private String candidatePosition;
	private Round currRound;
	private List<Round> doneRounds;
	
	public String getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public String getCandidatePosition() {
		return candidatePosition;
	}
	public void setCandidatePosition(String candidatePosition) {
		this.candidatePosition = candidatePosition;
	}
	public Round getCurrRound() {
		return currRound;
	}
	public void setCurrRound(Round currRound) {
		this.currRound = currRound;
	}
	public List<Round> getDoneRounds() {
		return doneRounds;
	}
	public void setDoneRounds(List<Round> doneRounds) {
		this.doneRounds = doneRounds;
	}
	
}
