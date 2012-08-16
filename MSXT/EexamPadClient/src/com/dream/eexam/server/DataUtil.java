package com.dream.eexam.server;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.msxt.client.model.Examination;
import com.msxt.client.model.Examination.Question;
import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.model.Examination.Catalog;
import com.msxt.client.model.transfer.Message2ModelTransfer;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.server.ServerProxy.STATUS;
import com.msxt.client.server.WebServerProxy;

public class DataUtil {

	public static void main(String args[]){
		WebServerProxy proxy = new WebServerProxy("192.168.1.101",8080);
		Result result = proxy.login("test", "test");
		if(STATUS.SUCCESS.equals(result.getStatus())){
			System.out.println(result.getSuccessMessage());
		}else if(STATUS.ERROR.equals(result.getStatus())){
			System.out.println(result.getErrorMessage());
		}
		
	}
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	public Examination getExam(Result result) {
		DocumentBuilder db;
		ByteArrayInputStream is;
		Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			is = new ByteArrayInputStream(result.getSuccessMessage().getBytes());
			doc = db.parse(is);
			is.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element root = doc.getDocumentElement();
		Examination exam = Message2ModelTransfer.Factory.getInstance().parseExamination(root);
		return exam;
	}
	
	public Examination getExam(FileInputStream is) {
		DocumentBuilder db;
//		ByteArrayInputStream is;
		Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			is = new ByteArrayInputStream(xmMsg.getBytes());
			doc = db.parse(is);
			is.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element root = doc.getDocumentElement();
		Examination exam = Message2ModelTransfer.Factory.getInstance().parseExamination(root);
		return exam;
	}
	
	public int getExamQuestionSum(Examination exam) {
		int sum = 0;
		List<Catalog> catalogs = exam.getCatalogs();
		for (Catalog catalog : catalogs) {
			for (Question question : catalog.getQuestions()) {
				if(question!=null){
					sum++;
				}
			}
		}
		return sum;
	}
	
	public Question getQuestionByCidQid(Examination exam,int cid, int qid){
		List<Catalog> catalogs = exam.getCatalogs();
		for(Catalog catalog:catalogs){
			if(catalog.getIndex() == cid){
				List<Question> questions = catalog.getQuestions();
				for(Question question:questions){
					if(question.getIndex() == qid){
						return question;
					}
				}
			}
		}
		return null;
	}
	
	public Question getNewQuestionByCidQid(Examination exam,int cid, int qid,int mvDirect){
		
		List<Catalog> catalogs = exam.getCatalogs();
		
		int newcid;
		int newqid;
		
	    if(qid == 1 && mvDirect == -1){
			newcid = cid - 1;
			if(newcid>0){
				for(Catalog catalog:catalogs){
					if(catalog.getIndex() == newcid){
						List<Question> questions = catalog.getQuestions();
						return questions.get(questions.size()-1);
					}
				}
			}else{
				return null;//this is first question of exam
			}
		}
	    
	    if(mvDirect == 1){
	    	newcid = cid;
	    	newqid = qid + 1;
	    	Question newQuestion = null;	    	
	    	for(Catalog catalog:catalogs){
				if(catalog.getIndex() == newcid){
					List<Question> questions = catalog.getQuestions();
					int queSum = questions.size();
					if(newqid > queSum){//this question is last one of current catalog will move to next catalog
						newcid++;//go to next catalog
						newqid = 1;
						continue;
					}
					
					for(Question question:questions){
						if(question.getIndex() == newqid){
							return question;
						}
					}
					
					if(newqid==1){//this question is first one of current catalog will move to next catalog
						newcid--;
						continue;
					}
				}
			}
	    }
		
		
		
		return null;
	}
	

	
	
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	public LoginSuccessResult getLoginSuccessResult(Result result) {
		DocumentBuilder db;
		ByteArrayInputStream is;
		Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			is = new ByteArrayInputStream(result.getSuccessMessage().getBytes());
			doc = db.parse(is);
			is.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element root = doc.getDocumentElement();
		LoginSuccessResult successResult = Message2ModelTransfer.Factory.getInstance().parseResult(root);
		return successResult;
	}	

}
