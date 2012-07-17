package com.msxt.examination;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.inject.Instance;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.seam.international.status.builder.TemplateMessage;
import org.jboss.solder.logging.Logger;

import com.msxt.common.FoggySearchCriteria;
import com.msxt.common.PageableSearcher;
import com.msxt.model.Examination;
import com.msxt.model.Examination_;
import com.msxt.model.Position_;

@Stateful
@Named
@SessionScoped
public class ExamSearcher extends PageableSearcher{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ExamSearchCriteria searchCriteria;
    
    @Inject
    private Instance<TemplateMessage> messageBuilder;
    
    private List<Examination> exams = new ArrayList<Examination>();
    
	@Override
	public FoggySearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public List<Examination> getExams() {
        return exams;
    }
	
	@Override
	public void doSearch() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Examination> cquery = builder.createQuery(Examination.class);
		Root<Examination> rp = cquery.from(Examination.class);
		
		if( searchCriteria.getPositionId()!=null && searchCriteria.getPositionId().length()>0 ) {
	        Predicate p1 = builder.like( builder.lower( rp.get(Examination_.name) ), searchCriteria.getSearchPattern() );
	        Predicate p2 = builder.equal( rp.get(Examination_.position).get(Position_.id), searchCriteria.getPositionId());
	        cquery.select( rp ).where( p1, p2);
		} else {
			Predicate p1 = builder.like( builder.lower( rp.get(Examination_.name) ), searchCriteria.getSearchPattern() );
	        cquery.select( rp ).where( p1);     
		}
		
		List<Examination> results = em.createQuery(cquery).setMaxResults(searchCriteria.getFetchSize())
                .setFirstResult(searchCriteria.getFetchOffset()).getResultList();
		
        nextPageAvailable = results.size() > searchCriteria.getPageSize();
        if (nextPageAvailable) {
            // NOTE create new ArrayList since subList creates unserializable list
        	exams = new ArrayList<Examination>( results.subList(0,  searchCriteria.getPageSize() ) );
        } else {
        	exams = results;
        }
        for( Examination e : exams ) {
    		Object result = em.createQuery("select COUNT(ie) from InterviewExamination ie where ie.exam=:exam").setParameter("exam", e).getSingleResult();
    		if( (Long)result>0 )
    			e.setOnUsed( true );
    		else
    			e.setOnUsed( false );
        }
        log.info(messageBuilder.get().text("Found {0} examination(s) matching search term [ {1} ] (limit {2})")
                .textParams(exams.size(), searchCriteria.getQuery(), searchCriteria.getPageSize()).build().getText());
		
	}
}
