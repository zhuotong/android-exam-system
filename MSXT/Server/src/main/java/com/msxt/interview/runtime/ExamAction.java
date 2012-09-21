package com.msxt.interview.runtime;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.msxt.model.ExaminationCatalogQuestion;
import com.msxt.model.ExaminationCatalogQuestionAnswer;
import com.msxt.model.Interview;
import com.msxt.model.InterviewExamination;
import com.msxt.model.ExaminationCatalog;
import com.msxt.model.Question;
import com.msxt.model.QuestionChoiceItem;

@Named
@RequestScoped
public class ExamAction {
	@PersistenceContext
	private EntityManager em;
	
	public String getExam( HttpServletRequest request ) {
		String conversation = request.getParameter( "conversation" );
		String examId = request.getParameter( "examId" );
		
		InterviewExamination ie = em.find( InterviewExamination.class, examId );
		
		String csId = ie.getInterview().getConversationId();
		if( csId==null || csId.isEmpty() || !ie.getInterview().getConversationId().equals(conversation) ) 
			return "<error><code>201</code><desc>Invalid Conversation</desc></error>";
					
		StringBuffer sb = new StringBuffer();
		sb.append( "<examination>" ); 
		sb.append( "<examinationid>" ).append( ie.getId() ).append( "</examinationid>" );
		sb.append( "<name>" ).append( ie.getExam().getName() ).append( "</name>" );
		sb.append( "<time>" ).append( ie.getExam().getTime() ).append( "</time>" );
		sb.append( "<confuse>" ).append( ie.getExamConfuse()==1 ? true : false ).append( "</confuse>" );
		sb.append( "<catalogs>" );
		for( ExaminationCatalog ec : ie.getExam().getCatalogs() ) {
			sb.append( "<catalog>");  
			sb.append( "<index>" ).append( ec.getIndex() ).append("</index>");
			sb.append( "<name>" ).append( ec.getName() ).append("</name>");
			sb.append( "<catalogdesc><![CDATA[" ).append( ec.getDescription() ).append("]]></catalogdesc>");
			sb.append( "<questions>" ); 
			for( ExaminationCatalogQuestion ecq : ec.getQuestions() ) {
				sb.append( "<question>" );
				sb.append( "<index>" ).append( ecq.getIndex() ).append( "</index>" );
				sb.append( "<name>" ).append( ecq.getQuestion().getName() ).append("</name>");
				sb.append( "<questionid>" ).append( ecq.getId() ).append( "</questionid>" );
				sb.append( "<type>" ).append( ecq.getQuestion().getQuestionType().getName() ).append( "</type>" );
				sb.append( "<score>" ).append( ecq.getScore() ).append( "</score>" );
				sb.append( "<content><![CDATA[" ).append( ecq.getQuestion().getContent() ).append( "]]></content>");	
				if( ecq.getQuestion().getQuestionType().getId().equals("1") || ecq.getQuestion().getQuestionType().getId().equals("2") )
					sb.append( "<choices>" );
					for( QuestionChoiceItem qci : ecq.getQuestion().getChoiceItems() ) {
						sb.append( "<choice>" );    
						sb.append( "<index>" ).append( qci.getIndex() ).append( "</index>" );
						sb.append( "<label>" ).append( qci.getLabelName() ).append( "</label>" );
						sb.append( "<content>" ).append( qci.getContent() ).append( "</content>" );
						sb.append( "</choice>" );
					}
					sb.append( "</choices>" );
				sb.append( "</question>" );	
			}
			sb.append( "</questions>" );
			sb.append( "</catalog>" );
		}
		sb.append( "</catalogs>" );
		sb.append("</examination>" );
		
		ie.setStartTime( new Date() );
		em.persist( ie );
		return sb.toString();
	}
	
	public String submitAnswer( HttpServletRequest request ) {
		try {
			request.setCharacterEncoding( "utf-8" );
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		StringBuffer answerXML = new StringBuffer("");
		BufferedReader br = null;
		try {
			br = new BufferedReader( new InputStreamReader( request.getInputStream() ) );
			String line;
			while( (line = br.readLine())!=null ) {
				answerXML.append( line );
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if( br!=null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		String examinationId = null;
        String status = "";
        String desc = "";
        String score = "";
        
        try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        ByteArrayInputStream is = new ByteArrayInputStream( answerXML.toString().getBytes() );
	        Document doc = db.parse( is );
	        is.close();
        
	        Element root = doc.getDocumentElement();
	        examinationId = getExaminationId( root );
	        desc = validate( root );
	        if( desc==null ) {
	        	InterviewExamination ie = em.find( InterviewExamination.class, examinationId );
	        	
	        	Element answersE = (Element)root.getElementsByTagName( "answers" ).item(0);
	     		saveAnswer( ie, answersE );
	     		status = "success";
	     		score = ie.getExamScore().toString();
	        } else {
	        	if( examinationId==null ) 
	        		examinationId = "empty";
	        	status = "fail";
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	status = "fail";
        	desc = "system exception";
		}
        StringBuffer sb = new StringBuffer();
        sb.append( "<submitresult>" );
        sb.append( "<examinationid>"+examinationId+"</examinationid>" );
    	sb.append( "<status>"+status+"</status>" );
    	if( status.equals("fail") )
    		sb.append( "<desc>"+desc+"</desc>" );
    	else
    		sb.append( "<score>"+ score +"</score>" );
        sb.append( "</submitresult>" );
        
        return sb.toString();
	}
	
	private String getExaminationId( Element examanswerE ) {
		String result;
		NodeList nl = examanswerE.getElementsByTagName( "examinationid" );
        if( nl.getLength()==0 )
        	result = null;  
        else {
        	result = nl.item(0).getTextContent();
        	result = result.trim().isEmpty() ? null : result.trim();
        }
        return result;
	}
	
	private String validate( Element examanswerE ) {
		String examinationId = getExaminationId( examanswerE );
       
        if( examinationId == null )
        	return "examinationid is empty";  
        
    	InterviewExamination ie = em.find( InterviewExamination.class, examinationId );
    	if( ie==null )
        	return "can't find examination";  
    	
		if( !Interview.STATUS.GOING.name().equals(ie.getInterview().getStatus() ) ) 
			return "This interview is not going";
		
		if( ie.getStatus() == InterviewExamination.STATUS.SUBMITTED )
			return "examination is submitted, can't resubmit";
		
		if( ie.getStatus() == InterviewExamination.STATUS.STARTED_OVERTIME )
			return "examination is overtime, if you really need to submit please request administrator reset this examination";
			
		NodeList nl = examanswerE.getElementsByTagName( "conversation" );
        if( nl.getLength()==0 )
        	return "can't set conversation value";  
        
    	String conversationId = nl.item(0).getTextContent();
    	if( !ie.getInterview().getConversationId().equals( conversationId ) ) 
    		return "conversation id value is invalid";
    	
    	return null;
	}
	
	private void saveAnswer( InterviewExamination ie, Element answersE ) {
		float tatalScore = 0;
		NodeList nl = answersE.getChildNodes();
		for( int i=0; i<nl.getLength(); i++ ) {
			Element answerE = (Element)nl.item(i);
			String questionid = answerE.getElementsByTagName("questionid").item(0).getTextContent();
			String content = answerE.getElementsByTagName("content").item(0).getTextContent();
			
			ExaminationCatalogQuestion  ecq = em.find( ExaminationCatalogQuestion.class, questionid );
			ExaminationCatalogQuestionAnswer ecqa = new ExaminationCatalogQuestionAnswer();
			ecqa.setInterviewExamination( ie );
			ecqa.setExamQuestion( ecq );
			ecqa.setAnswer( content.isEmpty() ? null : content );
			
			Question q = ecq.getQuestion();
			String questionTypeId = q.getQuestionType().getId();
			if( questionTypeId.equals("1") || questionTypeId.equals("2") ) {
				if( q.getRightAnswer().equals( content ) ) {
					ecqa.setResult( 1 );
					ecqa.setScore( ecq.getScore() );
				} else {
					ecqa.setResult( 0 );
					ecqa.setScore( 0 );
				}
				
				tatalScore += ecqa.getScore();
			}
			
			em.persist( ecqa );
		}
		
		ie.setExamScore( tatalScore );
		ie.setEndTime( new Date() );
		em.persist( ie );
	}
}
