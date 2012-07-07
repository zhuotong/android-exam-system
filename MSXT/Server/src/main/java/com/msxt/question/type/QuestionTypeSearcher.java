package com.msxt.question.type;

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
import com.msxt.model.QuestionType;
import com.msxt.model.QuestionType_;

@Named
@Stateful
@SessionScoped
public class QuestionTypeSearcher extends PageableSearcher{
	@Inject
	private Logger log;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Instance<TemplateMessage> messageBuilder;
    
	@Inject
	@Named("foggySearchCriteria")
	FoggySearchCriteria searchCriteria;
	
	private List<QuestionType> questionTypes = new ArrayList<QuestionType>();
	
	@Override
	public FoggySearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	@Override
	public void doSearch() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<QuestionType> cquery = builder.createQuery(QuestionType.class);
        Root<QuestionType> rqt = cquery.from(QuestionType.class);

        cquery.select( rqt ).where( builder.like(builder.lower( rqt.get(QuestionType_.name) ), searchCriteria.getSearchPattern()) );

        List<QuestionType> results = em.createQuery(cquery).setMaxResults(searchCriteria.getFetchSize())
                .setFirstResult(searchCriteria.getFetchOffset()).getResultList();

        nextPageAvailable = results.size() > searchCriteria.getPageSize();
        if (nextPageAvailable) {
            // NOTE create new ArrayList since subList creates unserializable list
        	questionTypes = new ArrayList<QuestionType>( results.subList(0,  searchCriteria.getPageSize() ) );
        } else {
        	questionTypes = results;
        }
        log.info(messageBuilder.get().text("Found {0} hotel(s) matching search term [ {1} ] (limit {2})")
                .textParams(questionTypes.size(), searchCriteria.getQuery(), searchCriteria.getPageSize()).build().getText());
		
	}

	public List<QuestionType> getQuestionTypes() {
		return questionTypes;
	}
	
}
