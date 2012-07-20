package com.msxt.interviewer;

import java.util.List;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.model.Interviewer;
import com.msxt.model.Interviewer_;

@Named
@RequestScoped
public class InterviewerCreator {
	@PersistenceContext
	private EntityManager em;
	
    @Inject
    private Messages messages;
    
	private final Interviewer newInterview = new Interviewer();

	public Interviewer getNewInterview() {
		return newInterview;
	}
	
	public String create() {
        if (verifyIdCodeIsAvailable()) {	
            em.persist( newInterview );
            messages.info( new DefaultBundleKey("registration_registered") ).params( newInterview.getName() );
            return "search";
        } else {
        	return null;
        }
    }
	
	private boolean verifyIdCodeIsAvailable() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<Interviewer> cq = cb.createQuery(Interviewer.class);
    	Root<Interviewer> fr = cq.from(Interviewer.class);
    	cq.select( fr ).where( cb.equal( fr.get(Interviewer_.idCode), newInterview.getIdCode() ) );
    	
    	TypedQuery<Interviewer> q = em.createQuery( cq );
    	List<Interviewer> existing = q.getResultList();
    	
        if ( existing != null && existing.size()>0 ) {
            messages.error( new BundleKey("messages", "account_usernameTaken") )
                    .params( newInterview.getName() );
            return false;
        }

        return true;
    }
}
