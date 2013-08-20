package com.dream.ivpc.model;

import java.util.ArrayList;
import java.util.List;

public class ResumeBean extends BaseBean {
	private Integer size;
	private List<ResumePageBean> rpbList = new ArrayList<ResumePageBean>();
	
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public List<ResumePageBean> getRpbList() {
		return rpbList;
	}
	public void setRpbList(List<ResumePageBean> rpbList) {
		this.rpbList = rpbList;
	}
	
}