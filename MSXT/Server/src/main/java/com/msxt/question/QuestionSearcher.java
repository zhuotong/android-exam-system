package com.msxt.question;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.seam.international.status.builder.TemplateMessage;
import org.jboss.solder.logging.Logger;

import com.msxt.common.FoggySearchCriteria;
import com.msxt.common.PageableSearcher;
import com.msxt.model.Position;
import com.msxt.model.PositionQuestion;
import com.msxt.model.PositionQuestion_;
import com.msxt.model.Position_;
import com.msxt.model.Question;
import com.msxt.model.QuestionType_;
import com.msxt.model.Question_;

@Stateful
@Named
@SessionScoped
public class QuestionSearcher extends PageableSearcher {
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    QuestionSearchCriteria searchCriteria;
    
    @Inject
    private Instance<TemplateMessage> messageBuilder;
    
    private List<Question> questions = new ArrayList<Question>();
    
    @Produces
    @Named
    public List<Question> getQuestions() {
        return questions;
    }
    
	@Override
	public FoggySearchCriteria getSearchCriteria() {
		return searchCriteria;
	}
	
	public void selectPositionId( String positionId ) {
		searchCriteria.setPositionId( positionId );
	}
	
	public Position getSelectedPosition(){
		return em.find( Position.class, searchCriteria.getPositionId() );
	}
	
	@Override
	public void doSearch() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Question> cquery = builder.createQuery(Question.class);
		if( searchCriteria.getPositionId()!=null && searchCriteria.getPositionId().length()>0 ) {
	        Root<PositionQuestion> rp = cquery.from(PositionQuestion.class);
	        Path<Question> qp = rp.get(PositionQuestion_.question);
	        
	        Predicate p1 = builder.or(
					builder.like( builder.lower( qp.get(Question_.name) ), searchCriteria.getSearchPattern() ),
					builder.like( builder.lower( qp.get(Question_.content) ), searchCriteria.getSearchPattern() ) );
	        Predicate p2 = builder.equal( rp.get(PositionQuestion_.position).get(Position_.id), searchCriteria.getPositionId());
	        
	        if( searchCriteria.getQuestionTypeId()!=null && searchCriteria.getQuestionTypeId().length()>0 ) {
	        	Predicate p3 = builder.equal( qp.get(Question_.questionType).get(QuestionType_.id), searchCriteria.getQuestionTypeId() );
	        	cquery.select( qp ).where( p1, p2, p3);
	        } else {
	        	cquery.select( qp ).where( p1, p2);
	        }   
		} else {
			Root<Question> qp = cquery.from(Question.class);
	        Predicate p1 = builder.or(
					builder.like( builder.lower( qp.get(Question_.name) ), searchCriteria.getSearchPattern() ),
					builder.like( builder.lower( qp.get(Question_.content) ), searchCriteria.getSearchPattern() ) );	        
	        if( searchCriteria.getQuestionTypeId()!=null && searchCriteria.getQuestionTypeId().length()>0 ) {
	        	Predicate p3 = builder.equal( qp.get(Question_.questionType).get(QuestionType_.id), searchCriteria.getQuestionTypeId() );
	        	cquery.select( qp ).where( p1, p3);
	        } else {
	        	cquery.select( qp ).where( p1 );
	        }   
		}
		
		List<Question> results = em.createQuery(cquery).setMaxResults(searchCriteria.getFetchSize())
                .setFirstResult(searchCriteria.getFetchOffset()).getResultList();
		
        nextPageAvailable = results.size() > searchCriteria.getPageSize();
        if (nextPageAvailable) {
            // NOTE create new ArrayList since subList creates unserializable list
        	questions = new ArrayList<Question>( results.subList(0,  searchCriteria.getPageSize() ) );
        } else {
        	questions = results;
        }
        log.info(messageBuilder.get().text("Found {0} hotel(s) matching search term [ {1} ] (limit {2})")
                .textParams(questions.size(), searchCriteria.getQuery(), searchCriteria.getPageSize()).build().getText());
		
	}

}
