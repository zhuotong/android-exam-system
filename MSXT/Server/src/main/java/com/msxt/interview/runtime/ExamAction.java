package com.msxt.interview.runtime;


import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import com.msxt.model.ExaminationQuestion;
import com.msxt.model.InterviewExamination;
import com.msxt.model.ExaminationCatalog;
import com.msxt.model.QuestionChoiceItem;

@Named
@RequestScoped
public class ExamAction {
	@PersistenceContext
	private EntityManager em;
	
	public String getExam( HttpServletRequest request ) {
		String conversation = request.getParameter( "conversation" );
		String examId = request.getParameter( "examId" );
		
		InterviewExamination exam = em.find( InterviewExamination.class, examId );
		
		String csId = exam.getInterview().getConversationId();
		if( csId==null || csId.isEmpty() || !exam.getInterview().getConversationId().equals(conversation) ) 
			return "<error><code>201</code><desc>Invalid Conversation</desc></error>";
					
		StringBuffer sb = new StringBuffer();
		sb.append( "<examination>" ); 
		sb.append( "<examinationid>" ).append( exam.getId() ).append( "</examinationid>" );
		sb.append( "<name>" ).append( exam.getExam().getName() ).append( "</name>" );
		sb.append( "<time>" ).append( exam.getExam().getTime() ).append( "</time>" );
		sb.append( "<confuse>" ).append( exam.getExamConfuse()==1 ? true : false ).append( "</confuse>" );
		sb.append( "<catalogs>" );
		for( ExaminationCatalog ec : exam.getExam().getCatalogs() ) {
			sb.append( "<catalog>");  
			sb.append( "<index>" ).append( ec.getIndex() ).append("</index>");
			sb.append( "<catalogdesc><![CDATA[" ).append( ec.getDescription() ).append("]]></catalogdesc>");
			sb.append( "<questions>" ); 
			for( ExaminationQuestion eq : ec.getQuestions() ) {
				sb.append( "<question>" );
				sb.append( "<index>" ).append( eq.getIndex() ).append( "</index>" );
				sb.append( "<questionid>" ).append( eq.getId() ).append( "</questionid>" );
				sb.append( "<type>" ).append( eq.getQuestion().getQuestionType().getName() ).append( "</type>" );
				sb.append( "<score>" ).append( eq.getScore() ).append( "</score>" );
				sb.append( "<content><![CDATA[" ).append( eq.getQuestion().getContent() ).append( "]]></content>");	
				if( eq.getQuestion().getQuestionType().getId().equals("1") || eq.getQuestion().getQuestionType().getId().equals("2") )
					sb.append( "<choices>" );
					for( QuestionChoiceItem qci : eq.getQuestion().getChoiceItems() ) {
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
		
		return sb.toString();
	}
}
