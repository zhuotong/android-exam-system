package com.msxt.interview;

import java.util.List;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.stream.XMLStreamException;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.common.Constants;
import com.msxt.model.Examination;
import com.msxt.model.Interview;
import com.msxt.model.InterviewExamination;
import com.msxt.model.Interview_;
import com.msxt.model.Position;

@Named
@RequestScoped
public class InterviewAgent {
	@PersistenceContext
    private EntityManager em;
	
	@Inject
	private Messages messages;
	
	private Interview selectedInterview  = new Interview();
	
	private String applyPositionId;
	
	private String examinationIds;
	
	public void selectInterview(String id){
		selectedInterview = em.find( Interview.class, id );
		selectedInterview.getInterviewer().getName();
		applyPositionId = selectedInterview.getApplyPosition().getId();
		selectedInterview.getExaminations().get(0).getExam().getName();
	}

	public Interview getSelectedInterview() {
		return selectedInterview;
	}
	
	public String save() throws XMLStreamException {
		if (verifyLoginNameIsAvailable()) {	
		    Interview interview = em.find( Interview.class, selectedInterview.getId() );

		    Position applyPosition = em.find( Position.class, applyPositionId );
		    interview.setApplyPosition( applyPosition );
		    interview.setLoginName( selectedInterview.getLoginName() );
		    interview.setLoginPassword( selectedInterview.getLoginPassword() );
		    interview.setStart( selectedInterview.getStart() );
 
		    em.persist( interview );
 
		    for( InterviewExamination ie : interview.getExaminations() )
		    	em.remove( ie );
			 
            String[] examIds = examinationIds.split( Constants.SIMPLE_SPLIT_KEY );
            for( int i=0; i<examIds.length; i+=2 ) {
            	String examId = examIds[i];
            	
            	InterviewExamination ie = new InterviewExamination();
            	ie.setInterview( interview );
            	
            	Examination e = em.find( Examination.class, examId );
            	ie.setExam( e );
            	
            	ie.setExamConfuse( Integer.valueOf( examIds[i+1] ) );
            	
            	em.persist( ie );
            }
            return "search?faces-redirect=true";
        } else {
        	return null;
        }
	}
	
	public String delete(String id) {
		Interview iv = em.find( Interview.class, id );
		if( iv.getStatus().equals( Interview.STATUS.WAITING.name() ) ) {
			em.remove( iv );
			messages.error( new BundleKey("messages", "msxt_interview_interview_deleted") );
			return "search?faces-redirect=true";
		} else {
			messages.error( new BundleKey("messages", "msxt_interview_interview_started") );
			return null;
		}
	}
	
	private boolean verifyLoginNameIsAvailable() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Interview> cq = cb.createQuery( Interview.class ); 
		
		Root<Interview> pathRoot = cq.from( Interview.class );
		cq.select( pathRoot ).where( cb.equal( pathRoot.get( Interview_.loginName), selectedInterview.getLoginName() ),
									 cb.notEqual( pathRoot.get(Interview_.id), selectedInterview.getId() ) );
		
		List<Interview> result = em.createQuery( cq ).getResultList();
		if( result.size()>0 ) {
			messages.error( new DefaultBundleKey("msxt_interview_login_name_exist") ).params( selectedInterview.getLoginName() );
			return false;
		}
		
		return true;
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
}
