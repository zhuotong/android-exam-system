package com.msxt.examination;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.stream.XMLStreamException;

import org.jboss.seam.transaction.TransactionPropagation;
import org.jboss.seam.transaction.Transactional;

import com.msxt.model.Examination;
import com.msxt.model.ExaminationCatalog;
import com.msxt.model.Position;

@Stateful
@javax.enterprise.inject.Model
@javax.enterprise.context.RequestScoped
public class ExamCreator {
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private CatalogParser cp;
	
	private String positionId;

	private Position selectedPosition;
	
	private final Examination newExam = new Examination();
	
	private String catalogsXML;
	
	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
		selectedPosition = em.find( Position.class, positionId );
	}

	public Position getSelectedPosition() {
		return selectedPosition;
	}

	public Examination getNewExam() {
		return newExam;
	}

	public String getCatalogsXML() {
		return catalogsXML;
	}

	public void setCatalogsXML(String catalogsXML) {
		this.catalogsXML = catalogsXML;
	}
	
	@Transactional(TransactionPropagation.REQUIRED)
	public String create() throws XMLStreamException {
		Position p = em.find( Position.class, positionId );
		newExam.setPosition( p );
		List<ExaminationCatalog> ecs = cp.parse( catalogsXML );
		for( ExaminationCatalog ec : ecs ) {
			ec.setExam( newExam );
		}
		newExam.setCatalogs( ecs );
		em.persist( newExam );
		return "search";
	}
	
	
}
