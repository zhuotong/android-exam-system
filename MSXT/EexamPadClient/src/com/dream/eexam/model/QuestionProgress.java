package com.dream.eexam.model;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class QuestionProgress {
	private Integer currentQueIndex;
	private Integer quesCount;

	//ids for all questions
	private List<Integer> allQueIdsList = new ArrayList<Integer>();
	private String allQueIdsString;
	
	//ids for complete questions
	private List<Integer> completedQueIdsList = new ArrayList<Integer>();
	private String completedQueIdsString;
	
	//ids for waiting questions
	private List<Integer> waitingQueIdsList = new ArrayList<Integer>();
	private String waitingQueIdsString;
	
	public Integer getCurrentQueIndex() {
		return currentQueIndex;
	}

	public void setCurrentQueIndex(Integer currentQueIndex) {
		this.currentQueIndex = currentQueIndex;
	}

	public Integer getQuesCount() {
		return quesCount;
	}

	public void setQuesCount(Integer quesCount) {
		this.quesCount = quesCount;
	}

	public List<Integer> getAllQueIdsList() {
		return allQueIdsList;
	}

	public void setAllQueIdsList(List<Integer> allQueIdsList) {
		this.allQueIdsList = allQueIdsList;
	}

	public String getAllQueIdsString() {
		return allQueIdsString;
	}

	public void setAllQueIdsString(String allQueIdsString) {
		this.allQueIdsString = allQueIdsString;
	}

	public List<Integer> getCompletedQueIdsList() {
		return completedQueIdsList;
	}

	public void setCompletedQueIdsList(List<Integer> completedQueIdsList) {
		this.completedQueIdsList = completedQueIdsList;
	}

	public String getCompletedQueIdsString() {
		return completedQueIdsString;
	}

	public void setCompletedQueIdsString(String completedQueIdsString) {
		this.completedQueIdsString = completedQueIdsString;
	}

	public List<Integer> getWaitingQueIdsList() {
		return waitingQueIdsList;
	}

	public void setWaitingQueIdsList(List<Integer> waitingQueIdsList) {
		this.waitingQueIdsList = waitingQueIdsList;
	}

	public String getWaitingQueIdsString() {
		return waitingQueIdsString;
	}

	public void setWaitingQueIdsString(String waitingQueIdsString) {
		this.waitingQueIdsString = waitingQueIdsString;
	}

	public QuestionProgress() {
		super();
	}
	
	public QuestionProgress(Integer currentQueIndex, Integer quesCount,String completedQueIdsString) {
		super();
		this.currentQueIndex = currentQueIndex;
		this.quesCount = quesCount;
		this.completedQueIdsString = completedQueIdsString;
	}
	
}
