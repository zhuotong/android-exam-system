package com.msxt.question;

import javax.enterprise.inject.Produces;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.international.status.Messages;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.model.Position;
import com.msxt.model.PositionQuestion;
import com.msxt.model.Question;
import com.msxt.model.QuestionChoiceItem;
import com.msxt.model.QuestionType;

@Named
@RequestScoped
public class QuestionCreator {
	@PersistenceContext
	private EntityManager em;
	 
	@Inject
    private Messages messages;
	
	private final Question newQuestion = new Question();
	 
	private String typeId;
	 
	private String choiceLabels;
	 
	private String choiceItems;
	
	private String[] positionIds;
	
	@Produces
	@Named
	public Question getNewQuestion() {
	    return newQuestion;
	}
	 
	public String create() {
		QuestionType type = em.find( QuestionType.class, typeId);
		newQuestion.setQuestionType( type );
		em.persist( newQuestion );
		
		for( String pid : positionIds ) {
			Position p = em.find( Position.class, pid );
			PositionQuestion pq = new PositionQuestion();
			pq.setPosition( p );
			pq.setQuestion( newQuestion );
			em.persist( pq );
		}
		
		if( typeId.equals("1") || typeId.equals("2") ) {
			String[] cls = choiceLabels.split("\\|#\\|");
			String[] cis = choiceItems.split("\\|#\\|");
			for( int i=0; i<cls.length; i++ ) {
				if( cls[i]!=null && cls[i].length()>0 ) {
					QuestionChoiceItem ci = new QuestionChoiceItem();
					ci.setLabelName( cls[i].trim() );
					ci.setContent( cis[i] );
					ci.setIndex( i+1 );
					ci.setQuestion( newQuestion );
					em.persist( ci );
				}
			}
		}
		
		messages.info( new DefaultBundleKey("msxt_question_create_success") ).params( newQuestion.getName() );
		return "search?faces-redirect=true";
	}

	public String getTypeId() {
		return typeId;
	}
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getChoiceLabels() {
		return choiceLabels;
	}
	
	public void setChoiceLabels(String choiceLabels) {
		this.choiceLabels = choiceLabels;
	}
	
	public String getChoiceItems() {
		return choiceItems;
	}
	
	public void setChoiceItems(String choiceItems) {
		this.choiceItems = choiceItems;
	}
	
	public String[] getPositionIds() {
		return positionIds;
	}

	public void setPositionIds(String[] positionIds) {
		this.positionIds = positionIds;
	}
}
