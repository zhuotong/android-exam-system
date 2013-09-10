package com.dream.ivpc.server;

import java.io.InputStream;
import java.util.List;

import com.dream.ivpc.bean.CandidateBean;
import com.dream.ivpc.bean.LoginResultBean;
import com.dream.ivpc.bean.PendRoundBean;
import com.dream.ivpc.bean.XMLBean;
import com.dream.ivpc.model.ExamBean;

public interface DAOProxy {

	public LoginResultBean login(String userName,String password);
	
	public List<PendRoundBean> getRoundList(String adminId);
	
	public CandidateBean getCandidateDetail(String adminId,String candidateId);
	
	public XMLBean getCandidateResume(InputStream is);
	
	public List<ExamBean> getCandiateExamRptList(String adminId,String candidateId);
	
	public List<ExamBean> getCandiateExamRpt(String adminId,String candidateId,String examId);
	
	public XMLBean getCandiateIntervew(InputStream is);
	
}
