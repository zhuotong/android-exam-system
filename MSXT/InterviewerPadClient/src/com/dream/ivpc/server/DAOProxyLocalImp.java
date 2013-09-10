package com.dream.ivpc.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import android.os.Environment;
import com.dream.ivpc.bean.CandidateBean;
import com.dream.ivpc.bean.LoginResultBean;
import com.dream.ivpc.bean.PendRoundBean;
import com.dream.ivpc.bean.XMLBean;
import com.dream.ivpc.model.ExamBean;
import com.dream.ivpc.util.FileUtil;

public class DAOProxyLocalImp implements DAOProxy {
	
	public final static String LOG_TAG = "DAOProxyLocalImp";

	private static DAOProxy getData = null;
	
	public static DAOProxy getInstance(){
		getData = new DAOProxyLocalImp();
		return getData;
	}
	
	@Override
	public LoginResultBean login(String adminId, String password) {
		String basePath = Environment.getExternalStorageDirectory()
				+ "/interviewer";
		String filePath = basePath + File.separator + "admin" 
				+ File.separator + adminId 
				+ File.separator + "login_result.xml";
		FileInputStream inputStream = FileUtil.getFileInputStream(filePath);
		return ParseResult.parseLoginResult(inputStream);
	}

	@Override
	public List<PendRoundBean> getRoundList(String adminId) {
		String basePath = Environment.getExternalStorageDirectory()
				+ "/interviewer";
		String filePath = basePath + File.separator + "admin" 
				+ File.separator + adminId 
				+ File.separator + "get_pending_interview_round.xml";
		FileInputStream inputStream = FileUtil.getFileInputStream(filePath);
		return ParseResult.getRoundList(inputStream);
	}

	@Override
	public CandidateBean getCandidateDetail(String adminId,String candidateId) {
		String basePath = Environment.getExternalStorageDirectory() + "/interviewer";
		String filePath = basePath + 
				File.separator + "admin" + 
				File.separator + adminId +
				File.separator + candidateId + 
				File.separator +  "get_interview_info.xml";
		FileInputStream inputStream = FileUtil.getFileInputStream(filePath);
		return ParseResult.getCandidateDetail(inputStream);
	}

	@Override
	public XMLBean getCandidateResume(InputStream is) {
		return null;
	}


	@Override
	public List<ExamBean> getCandiateExamRptList(String adminId,String candidateId) {
		String basePath = Environment.getExternalStorageDirectory() + "/interviewer";
		String filePath = basePath + 
				File.separator + "admin" + 
				File.separator + adminId +
				File.separator + candidateId + 
				File.separator +  "exams.xml";
		FileInputStream inputStream = FileUtil.getFileInputStream(filePath);
		return ParseResult.parseExams(inputStream);		
	}

	@Override
	public List<ExamBean> getCandiateExamRpt(String adminId,
			String candidateId, String examId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLBean getCandiateIntervew(InputStream is) {
		return null;
	}

}
