package com.dream.eexam.model;

@Deprecated
public class SubmitResultBean extends BaseBean {
	private String examinationid;
	private String status;
	private String score;
	private String desc;
	
	public String getExaminationid() {
		return examinationid;
	}
	public void setExaminationid(String examinationid) {
		this.examinationid = examinationid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}	
	
}
