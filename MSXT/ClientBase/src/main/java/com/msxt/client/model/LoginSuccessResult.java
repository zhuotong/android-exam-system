package com.msxt.client.model;

import java.util.ArrayList;
import java.util.List;

public class LoginSuccessResult {
	private String interviewer;
	private String jobtitle;
	private List<Examination> examinations;
	
	public String getInterviewer() {
		return interviewer;
	}

	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}


	public String getJobtitle() {
		return jobtitle;
	}


	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}


	public List<Examination> getExaminations() {
		if( examinations == null )
			examinations = new ArrayList<Examination>();
		return examinations;
	}


	public void setExaminations(List<Examination> examinations) {
		this.examinations = examinations;
	}


	public static class Examination{
		private String id;
		private String name;
		private String desc;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
		public String toString(){
			return name;
		}
	}
}
