package com.msxt.examination;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
@RequestScoped
public class ExamCreator {
	@PersistenceContext
	private EntityManager em;
}
