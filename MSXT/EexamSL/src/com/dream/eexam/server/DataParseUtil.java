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
import com.msxt.client.model.SubmitSuccessResult;
import com.msxt.client.model.transfer.Message2ModelTransfer;
import com.msxt.client.server.ServerProxy.Result;

public class DataParseUtil {

	public static LoginSuccessResult getSuccessResult(Result result) {
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
		LoginSuccessResult logiSuccResult = Message2ModelTransfer.Factory.getInstance().parseResult(root);
		
		return logiSuccResult;
	}
	
	public static LoginSuccessResult getSuccessResult(FileInputStream is) {
		DocumentBuilder db;
//		ByteArrayInputStream is;
		Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			is = new ByteArrayInputStream(result.getSuccessMessage().getBytes());
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
		LoginSuccessResult logiSuccResult = Message2ModelTransfer.Factory.getInstance().parseResult(root);
		
		return logiSuccResult;
	}
	/**
	 * 
	 * @param result
	 * @return
	 */
	public static Examination getExam(Result result) {
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
	
	/**
	 * 
	 * @param is
	 * @return
	 */
	public static Examination getExam(FileInputStream is) {
		DocumentBuilder db;
		Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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

	public static SubmitSuccessResult getSubmitSuccessResult(Result result) {
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
		SubmitSuccessResult submitSuccResult = Message2ModelTransfer.Factory.getInstance().parseSubmitResult(root);
		return submitSuccResult;
	}
	
	public static SubmitSuccessResult getSubmitSuccessResult(FileInputStream is) {
		DocumentBuilder db;
		Document doc = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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
		SubmitSuccessResult submitSuccResult = Message2ModelTransfer.Factory.getInstance().parseSubmitResult(root);
		
		return submitSuccResult;
	}
	/**
	 * 
	 * @param exam
	 * @return
	 */
	public static int getExamQuestionSum(Examination exam) {
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
	
	/**
	 * 
	 * @param exam
	 * @param cid
	 * @param qid
	 * @return
	 */
	public static Question getQuestionByCidQid(Examination exam,int cid, int qid){
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
	
	public static Question getNewQuestionByCidQid(Examination exam,int cid, int qid,int mvDirect){
		List<Catalog> catalogs = exam.getCatalogs();
		
		//go to previous question
	    if(mvDirect == -1){
	    	if(qid==1){
		    	int newCid = cid-1;
				if(cid>0){
					for(Catalog catalog:catalogs){
						if(catalog.getIndex() == newCid){
							List<Question> questions = catalog.getQuestions();
							return questions.get(questions.size()-1);
						}
					}
				}	    		
	    	}else{
	    		int newQid = qid-1;
				for(Catalog catalog:catalogs){
					if(catalog.getIndex() == cid){
						List<Question> questions = catalog.getQuestions();
						for(Question question:questions){
							if(question.getIndex() == newQid){
								return question;
							}
						}
					}
				}
	    	}

		}
	    
	    //go to next question
	    if(mvDirect == 1){
	    	int newQid = qid+1;
	    	for(Catalog catalog:catalogs){
				if(catalog.getIndex() == cid){
					List<Question> questions = catalog.getQuestions();
					int queSum = questions.size();
					if(newQid > queSum){//this question is last one of current catalog will move to next catalog
						cid++;//go to next catalog
						newQid = 1;
						continue;
					}else{
						for(Question question:questions){
							if(question.getIndex() == newQid){
								return question;
							}
						}
					}
				}
	    	}
	    }
		return null;
	}
	
	public static int getCidByQid(Examination exam,String qid){
		List<Catalog> catalogs = exam.getCatalogs();
		for(Catalog catalog:catalogs){
			List<Question> questions = catalog.getQuestions();
			for(Question question:questions){
				if(question.getId().equals(qid)){
					return catalog.getIndex();
				}
			}
		}
		return -1;
	}
	
	public static int getQuestionExamIndex(Examination exam,String qid){
		int index = 0;
		List<Catalog> catalogs = exam.getCatalogs();
		for(Catalog catalog:catalogs){
			List<Question> questions = catalog.getQuestions();
			for(Question question:questions){
				index++;
				if(question.getId().equals(qid)){
					return index;
				}
			}
		}
		return -1;
	}
	
	public static Question getQuestionById(Examination exam,String qid){
		List<Catalog> catalogs = exam.getCatalogs();
		for(Catalog catalog:catalogs){
			List<Question> questions = catalog.getQuestions();
			for(Question question:questions){
				if(question.getId().equals(qid)){
					return question;
				}
			}
		}
		return null;
	}
	
	public static Question getQuestionByIndexInExam(Examination exam,int indexInExam){
		int index = 0;
		List<Catalog> catalogs = exam.getCatalogs();
		for(Catalog catalog:catalogs){
			List<Question> questions = catalog.getQuestions();
			for(Question question:questions){
				index++;
				if(indexInExam == index){
					return question;
				}
			}
		}
		return null;
	}
	
	public static int getFirstQuestionIndexOfCatalog(Examination exam,int catalogIndex){
		int index = 1;
		List<Catalog> catalogs = exam.getCatalogs();
		for(Catalog catalog:catalogs){
			if(catalog.getIndex() == catalogIndex){
				return index;
			}else{
				List<Question> questions = catalog.getQuestions();
				index+=questions.size();
			}
		}
		return -1;
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
