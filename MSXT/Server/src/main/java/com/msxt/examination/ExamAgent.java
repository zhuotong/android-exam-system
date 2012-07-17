package com.msxt.examination;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.msxt.model.Examination;

@Named
@RequestScoped
public class ExamAgent {
	@PersistenceContext
    private EntityManager em;
	
	private Examination selectedExam = new Examination();
	
	private String catalogsXML;
	
	public void selectExamination(String id){
		selectedExam = em.find( Examination.class, id );
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
