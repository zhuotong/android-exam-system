package com.dream.ivpc.server;

import java.io.InputStream;
import java.util.List;

import com.dream.ivpc.bean.CandidateBean;
import com.dream.ivpc.bean.LoginResultBean;
import com.dream.ivpc.bean.PendRoundBean;
import com.dream.ivpc.bean.XMLBean;

public interface GetData {

	public LoginResultBean login(InputStream is);
	
	public List<PendRoundBean> getRoundList(InputStream is);
	
	public CandidateBean getCandidateDetail(InputStream is);
	
	public XMLBean getCandidateResume(InputStream is);
	
	public XMLBean getCandiateExamRpt(InputStream is);
	
	public XMLBean getCandiateIntervew(InputStream is);
	
}
