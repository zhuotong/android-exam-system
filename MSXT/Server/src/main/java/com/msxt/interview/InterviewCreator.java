package com.msxt.interview;

import java.util.Date;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.international.status.Messages;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.common.Constants;
import com.msxt.model.Examination;
import com.msxt.model.Interview;
import com.msxt.model.InterviewExamination;
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
        if (verifyIdCodeIsAvailable()) {	
        	selectedInterviewer = em.find( Interviewer.class, selectedInterviewer.getId() );
        	newInterview.setInterviewer( selectedInterviewer );
        	
        	Position applyPosition = em.find( Position.class, applyPositionId );
        	newInterview.setApplyPosition( applyPosition );
        	
        	newInterview.setCreateOn( new Date() );
        	newInterview.setStatus( Interview.STATUS.WAITING.name() );
        	
            em.persist( newInterview );
            
            String[] examIds = examinationIds.split( Constants.SIMPLE_SPLIT_KEY );
            for( String examId : examIds ) {
            	InterviewExamination ie = new InterviewExamination();
            	ie.setInterview( newInterview );
            	
            	Examination e = em.find( Examination.class, examId );
            	ie.setExam( e );
            	
            	em.persist( ie );
            }
            messages.info( new DefaultBundleKey("registration_registered") ).params( selectedInterviewer.getName() );
            return "/interviewer/search";
        } else {
        	return null;
        }
    }
	
	private boolean verifyIdCodeIsAvailable() {
        return true;
    }
}
