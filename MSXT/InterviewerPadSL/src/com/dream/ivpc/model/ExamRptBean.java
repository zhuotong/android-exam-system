package com.dream.ivpc.model;

import java.util.List;

public class ExamRptBean {
	private int pageSize;
	private List<ExamRptPageBean> pageList;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<ExamRptPageBean> getPageList() {
		return pageList;
	}

	public void setPageList(List<ExamRptPageBean> pageList) {
		this.pageList = pageList;
	}

}
