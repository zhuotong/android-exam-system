package com.msxt.question.type;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.international.status.Messages;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.model.QuestionType;
import com.msxt.model.QuestionType_;


@Stateful
@ConversationScoped
@Named
public class QuestionTypeAgent {
	@PersistenceContext
    private EntityManager em;
	
	@Inject
	QuestionTypeSearcher searcher;
	@Inject
	private Conversation conversation;
	    
	@Inject
	private Messages messages;
	
	private QuestionType selectedQuestionType;
	
    @Begin
    public void selectQuestionType(final String id) {
    	conversation.setTimeout(600000); //10 * 60 * 1000 (10 minutes)
    	selectedQuestionType = em.find(QuestionType.class, id);
    }
    
    public void deleteQuestionType(final String id) {
    	QuestionType p = em.find( QuestionType.class, id);
    	QuestionType childType = getChildTypes(p);
    	
    	if( childType==null ) {
    		em.remove( p );
    		searcher.doSearch();
    		messages.info( new DefaultBundleKey("msxt_question_type_delete_success") )
	        		.params( p.getName() );
    	} else {
    		messages.error( new DefaultBundleKey("msxt_question_type_delete_failure") )
                    .params( p.getName(), childType.getName() );
    	}
    	
    }
    
    @Produces
    @RequestScoped
    @Named
    public QuestionType getSelectedQuestionType() {
        return selectedQuestionType;
    }
    
    private QuestionType getChildTypes( QuestionType p ) {
   	 	CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<QuestionType> cquery = builder.createQuery(QuestionType.class);
        Root<QuestionType> rp = cquery.from(QuestionType.class);

        cquery.select( rp ).where( builder.equal( rp.get(QuestionType_.parent), p) );
        List<QuestionType> results = em.createQuery(cquery).getResultList();
	   	if( results!=null && results.size()>0 )
	   		return results.get(0);
	   	else
	   		return null;
   }
}
