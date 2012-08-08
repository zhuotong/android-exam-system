package com.msxt.examination;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.stream.XMLStreamException;

import com.msxt.common.HtmlUtil;
import com.msxt.model.Examination;
import com.msxt.model.ExaminationCatalog;
import com.msxt.model.ExaminationCatalogQuestion;
import com.msxt.model.Question;
import com.msxt.model.QuestionChoiceItem;

@Named
@RequestScoped
public class ExamAgent {
	@PersistenceContext
    private EntityManager em;
	
	@EJB
	private CatalogParser cp;
	
	@Inject
	private ExamSearcher searcher;
	
	private Examination selectedExam = new Examination();
	
	private String catalogsXML;
	
	public void selectExamination(String id){
		/*
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Examination> cq = cb.createQuery( Examination.class );
		Root<Examination> root = cq.from( Examination.class );
		root.join( Examination_.catalogs );
		cq.select( root )
	 	  .where( cb.equal( root.get(Examination_.id), id) );
		
		selectedExam = em.createQuery( cq ).getResultList().get(0);
		*/
		selectedExam = em.find( Examination.class, id );
		//Load lazy data
		for( ExaminationCatalog ec : selectedExam.getCatalogs() )
			ec.getQuestions().get(0).getQuestion();
	}
	
	public void selectExamination4Copy(String id){
		selectExamination(id);
		em.detach( selectedExam );
		selectedExam.setName( "Copy of " + selectedExam.getName() );
	}
	
	public void selectExamination4View(String id){
		selectedExam = em.find( Examination.class, id );
		//Load lazy data
		for( ExaminationCatalog ec : selectedExam.getCatalogs() ) 
			for( ExaminationCatalogQuestion eq : ec.getQuestions() ) {
				Question q = eq.getQuestion();
				for( QuestionChoiceItem qci : q.getChoiceItems() ) {
					String html = HtmlUtil.transferCommon2HTML( qci.getContent() );
					html = html.replaceAll("<br/>", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;");
					qci.setContent( html );
					em.detach( qci );
				}
				String htmlContent = HtmlUtil.transferCommon2HTML( q.getContent() );
				q.setContent( htmlContent );
				em.detach( q );
			}
	}
	
	public String save() throws XMLStreamException {
		Examination se = em.find( Examination.class, selectedExam.getId() );
		for( ExaminationCatalog ec : se.getCatalogs() )
			em.remove( ec );
		
		se.getCatalogs().clear();
		se.setName( selectedExam.getName() );
		se.setTime( selectedExam.getTime() );
		
		List<ExaminationCatalog> ecs = cp.parse( catalogsXML );
		for( ExaminationCatalog ec : ecs ) {
			ec.setExam( se );
			se.getCatalogs().add( ec );
		}
		em.persist( se );
		
		searcher.doSearch();
		return "search";
	}
	
	public String copy() throws XMLStreamException {
		Examination src = em.find( Examination.class, selectedExam.getId() );
		selectedExam.setPosition( src.getPosition() );	
		List<ExaminationCatalog> ecs = cp.parse( catalogsXML );
		for( ExaminationCatalog ec : ecs ) {
			ec.setExam( selectedExam );
			selectedExam.getCatalogs().add( ec );
		}
		em.persist( selectedExam );
		
		searcher.doSearch();
		return "search";
	}
	
	public void delete( String id ) {
		Examination exam = em.find( Examination.class, id );
		em.remove( exam );
		searcher.doSearch();
	}
	public Examination getSelectedExam() {
		return selectedExam;
	}

	public String getCatalogsXML() {
		return catalogsXML;
	}

	public void setCatalogsXML(String catalogsXML) {
		this.catalogsXML = catalogsXML;
	}
}
