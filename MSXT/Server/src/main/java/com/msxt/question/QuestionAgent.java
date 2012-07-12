package com.msxt.question;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.msxt.model.Position;
import com.msxt.model.PositionQuestion;
import com.msxt.model.Question;
import com.msxt.model.QuestionChoiceItem;
import com.msxt.model.QuestionChoiceItem_;

@Named
@RequestScoped
public class QuestionAgent {
	@PersistenceContext
    private EntityManager em;
	
	private Question selectedQuestion = new Question();
	
	private List<QuestionChoiceItem> qciList;
	
	private String choiceLabels;
	private String choiceContents;
	private List<String> positionIds = new ArrayList<String>();
	
	public void selectQuestion(final String id) {
		selectedQuestion = em.find(Question.class, id);
		if( selectedQuestion.getQuestionType().getName().equalsIgnoreCase("choice") ) {
			selectChoiceItems();
		}
    }

	public String save(){
		Question q = em.find( Question.class, selectedQuestion.getId() );
		for( String pid : positionIds ) {
			Position p = em.find( Position.class, pid );
			PositionQuestion pq = new PositionQuestion();
			pq.setPosition( p );
			pq.setQuestion( q );
			em.persist( pq );
		}
		return "search";
	}
	
	public synchronized Question getSelectedQuestion() {
		return selectedQuestion;
	}	
	
	public List<QuestionChoiceItem> getChoiceItems(){
		return qciList;
	}
	
	private  void selectChoiceItems(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QuestionChoiceItem> cq = cb.createQuery(QuestionChoiceItem.class);
		Root<QuestionChoiceItem> rp = cq.from( QuestionChoiceItem.class );
		cq.select( rp ).where( cb.equal( rp.get(QuestionChoiceItem_.question ), selectedQuestion) );
		
		qciList = em.createQuery( cq ).getResultList();
	}

	public String getChoiceLabels() {
		return choiceLabels;
	}

	public void setChoiceLabels(String choiceLabels) {
		this.choiceLabels = choiceLabels;
	}

	public String getChoiceContents() {
		return choiceContents;
	}

	public void setChoiceContents(String choiceContents) {
		this.choiceContents = choiceContents;
	}

	public List<String> getPositionIds() {
		return positionIds;
	}

	public void setPositionIds(List<String> positionIds) {
		this.positionIds = positionIds;
	}
	
}
