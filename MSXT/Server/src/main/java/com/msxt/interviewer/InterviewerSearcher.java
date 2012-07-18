package com.msxt.interviewer;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.international.status.builder.TemplateMessage;
import org.jboss.solder.logging.Logger;

import com.msxt.common.FoggySearchCriteria;
import com.msxt.common.PageableSearcher;
import com.msxt.model.Interviewer;
import com.msxt.model.Interviewer_;

/**
 * @author felix
 *
 */
@Stateful
@SessionScoped
@Named
public class InterviewerSearcher extends PageableSearcher {
	
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    @Named("foggySearchCriteria")
    private FoggySearchCriteria searchCriteria;
    
    @Inject
    private Instance<TemplateMessage> messageBuilder;
    
    private List<Interviewer> interviewers = new ArrayList<Interviewer>();

	@Override
	public FoggySearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	@Override
	public void doSearch() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Interviewer> cquery = builder.createQuery(Interviewer.class);
        Root<Interviewer> root = cquery.from(Interviewer.class);

        cquery.select( root ).where( builder.or(
        								builder.like(builder.lower( root.get(Interviewer_.name) ), searchCriteria.getSearchPattern()),
        								builder.like(builder.lower( root.get(Interviewer_.idCode) ), searchCriteria.getSearchPattern()) ) );

        List<Interviewer> results = em.createQuery(cquery).setMaxResults( searchCriteria.getFetchSize() )
                                                           .setFirstResult( searchCriteria.getFetchOffset() )
                                                           .getResultList();

        nextPageAvailable = results.size() > searchCriteria.getPageSize();
        if (nextPageAvailable) {
            // NOTE create new ArrayList since subList creates unserializable list
        	interviewers = new ArrayList<Interviewer>( results.subList(0,  searchCriteria.getPageSize() ) );
        } else {
        	interviewers = results;
        }
        log.info( messageBuilder.get().text("Found {0} interviewer(s) matching search term [ {1} ] (limit {2})")
                                      .textParams( interviewers.size(), searchCriteria.getQuery(), searchCriteria.getPageSize() )
                                      .build()
                                      .getText() );
		
	}

	public List<Interviewer> getInterviewers() {
		return interviewers;
	}

}
