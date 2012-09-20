package com.msxt.interview;

import java.util.Date;
import java.util.List;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.international.status.Messages;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.common.Constants;
import com.msxt.model.Examination;
import com.msxt.model.Interview;
import com.msxt.model.InterviewExamination;
import com.msxt.model.Interview_;
import com.msxt.model.Interviewer;
import com.msxt.model.Position;

@Named
@RequestScoped
public class InterviewCreator {
	@PersistenceContext
	private EntityManager em;
	
    @Inject
    private Messages messages;
    
	private final Interview newInterview = new Interview();
	
	private Interviewer selectedInterviewer = new Interviewer();
	
	private String applyPositionId;
	
	private String examinationIds;
	
	public Interview getNewInterview() {
		return newInterview;
	}
	
	public void selectInterviewer(String id){
		selectedInterviewer = em.find( Interviewer.class, id) ;
	}
	
	public Interviewer getSelectedInterviewer() {
		return selectedInterviewer;
	}
	
	public String getApplyPositionId() {
		return applyPositionId;
	}

	public void setApplyPositionId(String applyPositionId) {
		this.applyPositionId = applyPositionId;
	}

	
	public String getExaminationIds() {
		return examinationIds;
	}

	public void setExaminationIds(String examinationIds) {
		this.examinationIds = examinationIds;
	}

	public String create() {
        if (verifyLoginNameIsAvailable()) {	
        	selectedInterviewer = em.find( Interviewer.class, selectedInterviewer.getId() );
        	newInterview.setInterviewer( selectedInterviewer );
        	
        	Position applyPosition = em.find( Position.class, applyPositionId );
        	newInterview.setApplyPosition( applyPosition );
        	
        	newInterview.setCreateOn( new Date() );
        	newInterview.setStatus( Interview.STATUS.UNFINISH_WAITING.name() );
        	
            em.persist( newInterview );
            
            String[] examIds = examinationIds.split( Constants.SIMPLE_SPLIT_KEY );
            for( int i=0; i<examIds.length; i+=2 ) {
            	String examId = examIds[i];
            	
            	InterviewExamination ie = new InterviewExamination();
            	ie.setInterview( newInterview );
            	
            	Examination e = em.find( Examination.class, examId );
            	ie.setExam( e );
            	ie.setExamConfuse( Integer.valueOf( examIds[i+1] ) );
            	
            	em.persist( ie );
            }
            messages.info( new DefaultBundleKey("msxt_interview_create_success") ).params( selectedInterviewer.getName() );
            return "/interviewer/search?faces-redirect=true";
        } else {
        	return null;
        }
    }
	
	private boolean verifyLoginNameIsAvailable() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Interview> cq = cb.createQuery( Interview.class ); 
		
		Root<Interview> pathRoot = cq.from( Interview.class );
		cq.select( pathRoot ).where( cb.equal( pathRoot.get( Interview_.loginName), newInterview.getLoginName() ) );
		
		List<Interview> result = em.createQuery( cq ).getResultList();
		if( result.size()>0 ) {
			messages.error( new DefaultBundleKey("msxt_interview_login_name_exist") ).params( newInterview.getLoginName() );
			return false;
		}
		
		return true;
    }
	
	
}
