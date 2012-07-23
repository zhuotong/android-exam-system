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
		String examId = request.getParameter( "examId" );
		
		InterviewExamination exam = em.find( InterviewExamination.class, examId );
		exam.getExam().getCatalogs();
		StringBuffer sb = new StringBuffer();
		sb.append( "<examination>" ); 
		sb.append( "<name>" ).append( exam.getExam().getName() ).append( "</name>" );
		sb.append( "<time>" ).append( exam.getExam().getTime() ).append( "</time>" );
		sb.append( "<catalogs>" );
		for( ExaminationCatalog ec : exam.getExam().getCatalogs() ) {
			sb.append( "<catalog>");  
			sb.append( "<index>" ).append( ec.getIndex() ).append("</index>");
			sb.append( "<desc><![CDATA[" ).append( ec.getDescription() ).append("]]></desc>");
			sb.append( "<questions>" ); 
			for( ExaminationQuestion eq : ec.getQuestions() ) {
				sb.append( "<question>" );
				sb.append( "<index>" ).append( eq.getIndex() ).append( "</index>" );
				sb.append( "<type>" ).append( eq.getQuestion().getQuestionType().getName() ).append( ":S</type>" );
				sb.append( "<score>" ).append( eq.getScore() ).append( "</score>" );
				sb.append( "<content><![CDATA[" ).append( eq.getQuestion().getContent() ).append( "]]></content>");	
				if( eq.getQuestion().getQuestionType().getName().equalsIgnoreCase("choice") )
					for( QuestionChoiceItem qci : eq.getQuestion().getChoiceItems() ) {
						sb.append( "<choice>" );    
						sb.append( "<index>" ).append( qci.getIndex() ).append( "</index>" );
						sb.append( "<label>" ).append( qci.getLabelName() ).append( "</label>" );
						sb.append( "<content>" ).append( qci.getContent() ).append( "</content>" );
						sb.append( "</choice>" );
					}
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
