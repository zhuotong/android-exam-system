package com.msxt.question;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.international.status.Messages;

import com.msxt.model.Question;

@Stateful
@Model
public class QuestionCreator {
	 @PersistenceContext
	 private EntityManager em;

     @Inject
     private Messages messages;
     
     private final Question newQuestion = new Question();
     
     private String typeId;
     
     private List<String> choiceLabels = new ArrayList<String>();
     
     private List<String> choiceItems = new ArrayList<String>();
     
     @Produces
     @Named
     public Question getNewQuestion() {
         return newQuestion;
     }
     
     public String create() {
    	 return "search";
     }

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public List<String> getChoiceLabels() {
		return choiceLabels;
	}

	public void setChoiceLabels(List<String> choiceLabels) {
		this.choiceLabels = choiceLabels;
	}

	public List<String> getChoiceItems() {
		return choiceItems;
	}

	public void setChoiceItems(List<String> choiceItems) {
		this.choiceItems = choiceItems;
	}
}
