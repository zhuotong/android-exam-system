package com.msxt.question.type;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.component.UIInput;
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
import com.msxt.model.QuestionType;
import com.msxt.model.QuestionType_;

@Stateful
@Model
public class QuestionTypeCreator {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Messages messages;

    private UIInput questionTypeNameInput;

    private final QuestionType newQuestionType = new QuestionType();
    
    private String parentTypeId;
    
    public void create() {
        if (verifyQuestionTypeNameIsAvailable()) {
        	if( parentTypeId != null ) {
        		QuestionType parentType = em.find( QuestionType.class, parentTypeId);        	
        		newQuestionType.setParent(parentType);
        	}
        	
            em.persist( newQuestionType );

            messages.info(new DefaultBundleKey("msxt_question_type_create_success"))
                         .params(newQuestionType.getName());
        } 
    }


    @Produces
    @Named
    public QuestionType getNewQuestionType() {
        return newQuestionType;
    }

    public UIInput getQuestionTypeNameInput() {
		return questionTypeNameInput;
	}


	public void setQuestionTypeNameInput(UIInput questionTypeNameInput) {
		this.questionTypeNameInput = questionTypeNameInput;
	}


	private boolean verifyQuestionTypeNameIsAvailable() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<QuestionType> cq = cb.createQuery(QuestionType.class);
    	Root<QuestionType> fr = cq.from(QuestionType.class);
    	cq.select( fr ).where( cb.equal(fr.get(QuestionType_.name), newQuestionType.getName()) );
    	
    	TypedQuery<QuestionType> q = em.createQuery( cq );
    	List<QuestionType> existing = q.getResultList();
    	
        if ( existing != null && existing.size()>0 ) {
            messages.warn( new BundleKey("messages", "account_usernameTaken") )
                    .defaults( "The position name '{0}' is already taken. Please choose another position name.")
                    .params( newQuestionType.getName() )
                    .targets( questionTypeNameInput.getClientId() );
            return false;
        }

        return true;
    }


	public List<QuestionType> getExistingQuestionTypes() {
		@SuppressWarnings("unchecked")
		List<QuestionType> existing = em.createQuery("select t from QuestionType t").getResultList();
		return existing;
	}


	public String getParentTypeId() {
		return parentTypeId;
	}


	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
	}
}
