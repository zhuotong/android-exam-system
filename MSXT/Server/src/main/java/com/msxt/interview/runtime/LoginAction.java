package com.msxt.interview.runtime;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import com.msxt.common.DateUtil;
import com.msxt.model.ExaminationCatalog;
import com.msxt.model.ExaminationCatalogQuestion;
import com.msxt.model.Interview;
import com.msxt.model.InterviewExamination;
import com.msxt.model.Interview_;

@Named
@RequestScoped
public class LoginAction {
	@PersistenceContext
	private EntityManager em;
	
	public String login( HttpServletRequest request ) throws UnsupportedEncodingException {
		request.setCharacterEncoding( "utf-8" );
		String name = request.getParameter( "loginName" );
		String password = request.getParameter( "loginPassword" );
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<Interview> cq = cb.createQuery(Interview.class);
    	Root<Interview> rp = cq.from( Interview.class );
    	cq.select( rp ).where( cb.equal(rp.get(Interview_.loginName), name), cb.equal(rp.get(Interview_.loginPassword), password) );
    	
    	String des = "Invalid login name or login password";
		List<Interview> ivs = em.createQuery( cq ).getResultList();
		if( ivs !=null && ivs.size()>0 ) {
			Interview iv = ivs.get(0); 
			
			if( iv.getStatus().equals( Interview.STATUS.UNFINISH_WAITING.name() ) ) {
				if( iv.getStart().compareTo( DateUtil.getTodayStart() ) == -1 ) {
					iv.setStatus( Interview.STATUS.UNFINISH_ABSENT.name() );
					em.persist( iv );
					des = "Interview is over due";				
				} else if ( iv.getStart().compareTo( DateUtil.getTodayEnd() ) >-1 ) {//limit to today
					des = "Interview is not doing on today";
				} else {
					String conversation = UUID.randomUUID().toString();
					StringBuffer sb = new StringBuffer();
					sb.append( "<login>" );
					sb.append( "<status>success</status>" );
					sb.append( "<conversation>" ).append( conversation ).append( "</conversation>" );
					sb.append( "<interviewer>" ).append( iv.getInterviewer().getName() ).append( "</interviewer>" ); 
					sb.append( "<jobtitle>" ).append( iv.getApplyPosition().getName() ).append( "</jobtitle>" );
					sb.append( "<examinations>" );
					
					for( InterviewExamination ie : iv.getExaminations() ) {
						if( ie.getStatus() == InterviewExamination.STATUS.SUBMITTED )
							continue;
						
						sb.append( "<examination>" );
						sb.append( "<id>").append( ie.getId() ).append( "</id>" );
						sb.append( "<name>").append( ie.getExam().getName() ).append( "</name>" );
						
						int totalScore = 0;
						int totalQuestion = 0;
						for( ExaminationCatalog ec : ie.getExam().getCatalogs() ) {
							for( ExaminationCatalogQuestion eq : ec.getQuestions() ) {
								totalScore += eq.getScore();
								totalQuestion++;
							}
						}
						sb.append( "<desc><![CDATA[Catalog count:").append( ie.getExam().getCatalogs().size() )
						  .append( " Total question count : " ).append( totalQuestion )
						  .append( " Total score : ").append( totalScore )
						  .append( "]]></desc>" );
						sb.append( "</examination>" );
					}		
					sb.append( "</examinations>" );
					sb.append( "</login>" );
					
					iv.setStatus( Interview.STATUS.GOING.name() );
					iv.setConversationId( conversation );
					
					return sb.toString();
				}
			} else if( iv.getStatus().equals( Interview.STATUS.UNFINISH_ABSENT.name() ) ) {
				des = "Interview is over due";
			} else if( iv.getStatus().equals( Interview.STATUS.GOING.name() ) ) {
				des = "This interview already login. If you want relogin, please request administrator reset status!";
			} else {
				des = "Interview is finished";
			}
		} else {
			des = "Invalid login name or login password";
		}

		return "<login><status>failed</status><desc>"+des+"</desc></login>";
	}
}
