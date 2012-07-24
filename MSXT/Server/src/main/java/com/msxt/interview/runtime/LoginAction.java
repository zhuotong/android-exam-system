package com.msxt.interview.runtime;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import com.msxt.common.DateUtil;
import com.msxt.model.Interview;
import com.msxt.model.InterviewExamination;
import com.msxt.model.Interview_;

@Named
@RequestScoped
public class LoginAction {
	@PersistenceContext
	private EntityManager em;
	
	public String login( HttpServletRequest request ) {
		String name = request.getParameter( "loginName" );
		String password = request.getParameter( "loginPassword" );
		
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<Interview> cq = cb.createQuery(Interview.class);
    	Root<Interview> rp = cq.from( Interview.class );
    	cq.select( rp ).where( cb.equal(rp.get(Interview_.loginName), name), cb.equal(rp.get(Interview_.loginPassword), password) );
    	
		List<Interview> ivs = em.createQuery( cq ).getResultList();
		boolean existFinish = false;
		boolean existNotStart = false;
		boolean existOverdue = false; 
		if( ivs !=null && ivs.size()>0 ) {
			for( Interview iv : ivs ) {
				if( iv.getStatus().equals( Interview.STATUS.WAITING ) || iv.getStatus().equals( Interview.STATUS.DOING ) ) 
					if( iv.getStart().compareTo( DateUtil.getTodayStart() ) == -1 ) {
						existOverdue = true;
					} else if ( iv.getStart().compareTo( DateUtil.getTodayEnd() ) >-1 ) {
						existNotStart = true;
					} else {
						StringBuffer sb = new StringBuffer();
						sb.append( "<login>" );
						sb.append( "<status>sucess</status>" );		
						sb.append( "<examinations>" );
						
						for( InterviewExamination exam : iv.getExaminations() ) {
							sb.append( "<examination>" );
							sb.append( "<id>").append( exam.getId() ).append( "</id>" );
							sb.append( "<name>").append( exam.getExam().getName() ).append( "</name>" );
							sb.append( "</examination>" );
						}		
						sb.append( "</examinations>" );
						sb.append( "</login>" );
						return sb.toString();
					}
				else
					existFinish = true;
			}
		} 

		String des = "";
		if( existNotStart ) {
			des = "Interview is not doing on today";
		} else if (existOverdue) {
			des = "Interview is over due";
		} else if (existFinish) {
			des = "Interview is finished";
		}
	
		return "<login><status>failed</status><desc>"+des+"</desc></login>";
	}
}
