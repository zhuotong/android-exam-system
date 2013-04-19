package com.dream.eexam.model;

public class ExamProgress {

	private String seq;
	private String cId;
	private String qId;
	private String qIdStr;
	private String answer;

	public ExamProgress(String seq, String cId, String qId, String qIdStr,
			String answer) {
		super();
		this.seq = seq;
		this.cId = cId;
		this.qId = qId;
		this.qIdStr = qIdStr;
		this.answer = answer;
	}
	
	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getqId() {
		return qId;
	}

	public void setqId(String qId) {
		this.qId = qId;
	}

	public String getqIdStr() {
		return qIdStr;
	}

	public void setqIdStr(String qIdStr) {
		this.qIdStr = qIdStr;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
