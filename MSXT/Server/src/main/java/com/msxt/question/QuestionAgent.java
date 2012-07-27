package com.msxt.question;

import java.util.List;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.international.status.Messages;

import com.msxt.booking.i18n.DefaultBundleKey;
import com.msxt.common.HtmlUtil;
import com.msxt.model.Position;
import com.msxt.model.PositionQuestion;
import com.msxt.model.PositionQuestion_;
import com.msxt.model.Question;
import com.msxt.model.QuestionChoiceItem;

@Named
@RequestScoped
public class QuestionAgent {
	@PersistenceContext
    private EntityManager em;
	
	@Inject
	private Messages messages;
	
	private Question selectedQuestion = new Question();
	
	private List<QuestionChoiceItem> qciList;
	
	private String choiceLabels;
	private String choiceContents;
	private String[] positionIds;
	private String[] positionNames;
	
	public void selectQuestion(final String id) {
		selectedQuestion = em.find(Question.class, id);
		String typeId = selectedQuestion.getQuestionType().getId();
		if( typeId.equals("1") || typeId.equals("2") ) {
			qciList = selectedQuestion.getChoiceItems();
			qciList.get(0);
		}
		selectPositions();
    }
	
	public void selectQuestion4View(final String id) {
		selectQuestion( id );
		String htmlQC = HtmlUtil.transferCommon2HTML( selectedQuestion.getContent() );
		selectedQuestion.setContent( htmlQC );
		em.detach( selectedQuestion );
		if( qciList!=null && !qciList.isEmpty() ) {
			for( QuestionChoiceItem qci : qciList ) {
				qci.setContent( HtmlUtil.transferCommon2HTML( qci.getContent() ) );
				em.detach( qci );
			}
		}
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
		
		String typeId = q.getQuestionType().getId();
		if( typeId.equals("1") || typeId.equals("2") ) {
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
		return "search?faces-redirect=true";
	}
	
	public synchronized Question getSelectedQuestion() {
		return selectedQuestion;
	}	
	
	public List<QuestionChoiceItem> getChoiceItems(){
		return qciList;
	}
	
	private void selectPositions(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Position> cq = cb.createQuery( Position.class );
		Root<PositionQuestion> root = cq.from( PositionQuestion.class );
		cq.select( root.get(PositionQuestion_.position) ).where( cb.equal( root.get(PositionQuestion_.question), selectedQuestion) );
		
		List<Position> r = em.createQuery( cq ).getResultList();
		positionIds = new String[r.size()];
		positionNames = new String[r.size()];
		for( int i=0; i<r.size(); i++ ) {
			positionIds[i] = r.get(i).getId();
			positionNames[i] = r.get(i).getName();
		}
	}
	
	public String delete(String id){
		Question q = em.find( Question.class, id );
		Object result = em.createQuery("select COUNT(eq) from ExaminationQuestion eq where eq.question=:q").setParameter("q", q).getSingleResult();
		if( ((Number)result).intValue()>0 ) {
			messages.error( new DefaultBundleKey("msxt_question_delete_failure") ).params( q.getName() );
			return "search";
		} else { 
			em.remove( q );
			return "search?faces-redirect=true";
		}
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

	public String[] getPositionNames() {
		return positionNames;
	}
}
