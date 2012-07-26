package com.msxt.question;

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
import com.msxt.model.PositionQuestion_;
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
	private String[] positionIds;
	
	public void selectQuestion(final String id) {
		selectedQuestion = em.find(Question.class, id);
		String typeId = selectedQuestion.getQuestionType().getId();
		if( typeId.equals("1") || typeId.equals("2") ) {
			selectChoiceItems();
		}
		selectPositions();
    }

	public String save(){
		Question q = em.find( Question.class, selectedQuestion.getId() );
		em.createQuery("delete PositionQuestion pq where pq.question=:q").setParameter("q", q).executeUpdate();
		for( String pid : positionIds ) {
			Position p = em.find( Position.class, pid );
			PositionQuestion pq = new PositionQuestion();
			pq.setPosition( p );
			pq.setQuestion( q );
			em.persist( pq );
		}
		q.setContent( selectedQuestion.getContent() );
		q.setRightAnswer( selectedQuestion.getRightAnswer() );
		em.persist( q );
		
		if( q.getQuestionType().getName().equalsIgnoreCase("choice") ) {
			em.createQuery("delete QuestionChoiceItem item where item.question=:q").setParameter("q", q).executeUpdate();
			String[] cls = choiceLabels.split("\\|#\\|");
			String[] cis = choiceContents.split("\\|#\\|");
			for( int i=0; i<cls.length; i++ ) {
				if( cls[i]!=null && cls[i].length()>0 ) {
					QuestionChoiceItem ci = new QuestionChoiceItem();
					ci.setLabelName( cls[i].trim() );
					ci.setContent( cis[i] );
					ci.setIndex( i+1 );
					ci.setQuestion( q );
					em.persist( ci );
				}
			}
		}
		return "search";
	}
	
	public synchronized Question getSelectedQuestion() {
		return selectedQuestion;
	}	
	
	public List<QuestionChoiceItem> getChoiceItems(){
		return qciList;
	}
	
	private void selectChoiceItems(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QuestionChoiceItem> cq = cb.createQuery(QuestionChoiceItem.class);
		Root<QuestionChoiceItem> rp = cq.from( QuestionChoiceItem.class );
		cq.select( rp ).where( cb.equal( rp.get(QuestionChoiceItem_.question ), selectedQuestion) );
		
		qciList = em.createQuery( cq ).getResultList();
	}
	
	private void selectPositions(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Position> cq = cb.createQuery( Position.class );
		Root<PositionQuestion> root = cq.from( PositionQuestion.class );
		cq.select( root.get(PositionQuestion_.position) ).where( cb.equal( root.get(PositionQuestion_.question), selectedQuestion) );
		
		List<Position> r = em.createQuery( cq ).getResultList();
		positionIds = new String[r.size()];
		for( int i=0; i<r.size(); i++ )
			positionIds[i] = r.get(i).getId();
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

	public String[] getPositionIds() {
		return positionIds;
	}

	public void setPositionIds(String[] positionIds) {
		this.positionIds = positionIds;
	}
	
}
