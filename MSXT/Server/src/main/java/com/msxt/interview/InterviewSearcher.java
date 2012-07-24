package com.msxt.interview;

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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.predicate.ComparisonPredicate;
import org.hibernate.ejb.criteria.predicate.ComparisonPredicate.ComparisonOperator;
import org.jboss.seam.international.status.builder.TemplateMessage;
import org.jboss.solder.logging.Logger;

import com.msxt.common.FoggySearchCriteria;
import com.msxt.common.PageableSearcher;
import com.msxt.model.Interview;
import com.msxt.model.Interview_;
import com.msxt.model.Interviewer_;
import com.msxt.model.Position_;

/**
 * @author felix
 *
 */
@Stateful
@SessionScoped
@Named
public class InterviewSearcher extends PageableSearcher {
	
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private InterviewSearchCriteria searchCriteria;
    
    @Inject
    private Instance<TemplateMessage> messageBuilder;
    
    private List<Interview> interviews = new ArrayList<Interview>();

	@Override
	public FoggySearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	@Override
	public void doSearch() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Interview> cq = cb.createQuery(Interview.class);
        Root<Interview> root = cq.from( Interview.class );
    	
        cq.select( root );
        
        List<Predicate> pl = new ArrayList<Predicate>();
        if( searchCriteria.getQuery()!=null && !searchCriteria.getQuery().isEmpty() )
        	pl.add( cb.like( root.get(Interview_.interviewer).get( Interviewer_.idCode) , searchCriteria.getSearchPattern() ) );
        
        if( searchCriteria.getStartBegin() !=null ) 
        	pl.add( new ComparisonPredicate( (CriteriaBuilderImpl)cb, ComparisonOperator.GREATER_THAN_OR_EQUAL, root.get(Interview_.start), searchCriteria.getStartBegin() ) );
        
        if( searchCriteria.getStartEnd() !=null )
        	pl.add( new ComparisonPredicate( (CriteriaBuilderImpl)cb, ComparisonOperator.LESS_THAN_OR_EQUAL, root.get(Interview_.start), searchCriteria.getStartEnd() ) );
        
        if( searchCriteria.getPositionId()!=null && !searchCriteria.getPositionId().isEmpty() ) 
        	pl.add( cb.equal( root.get(Interview_.applyPosition).get(Position_.id), searchCriteria.getPositionId() ) );
        
        if( pl.size()>0 ) {
	        Predicate rp = null;
	        for( Predicate p: pl ) {
	        	if( rp == null )
	        		rp = p;
	        	else
	        		rp = cb.and( rp, p );
	        }
	        
	        cq.where( rp );
        }
        List<Interview> results = em.createQuery(cq).setMaxResults( searchCriteria.getFetchSize() )
                                                           .setFirstResult( searchCriteria.getFetchOffset() )
                                                           .getResultList();

        nextPageAvailable = results.size() > searchCriteria.getPageSize();
        if (nextPageAvailable) {
            // NOTE create new ArrayList since subList creates unserializable list
        	interviews = new ArrayList<Interview>( results.subList(0,  searchCriteria.getPageSize() ) );
        } else {
        	interviews = results;
        }
        log.info( messageBuilder.get().text("Found {0} interview(s) matching search term [ {1} ] (limit {2})")
                                      .textParams( interviews.size(), searchCriteria.getQuery(), searchCriteria.getPageSize() )
                                      .build()
                                      .getText() );
		
	}

	public List<Interview> getInterviews() {
		return interviews;
	}

}
