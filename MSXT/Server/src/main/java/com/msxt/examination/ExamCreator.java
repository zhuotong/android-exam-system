package com.msxt.examination;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.msxt.model.Position;

@Named
@RequestScoped
public class ExamCreator {
	@PersistenceContext
	private EntityManager em;
	
	private String positionId;

	private Position selectedPosition;
	
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

	public void setSelectedPosition(Position selectedPosition) {
		this.selectedPosition = selectedPosition;
	}
	
	
	
}
