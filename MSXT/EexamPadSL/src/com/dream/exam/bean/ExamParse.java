package com.dream.exam.bean;

import java.util.List;

public class ExamParse{

	public static Question getQuestion(Exam exam,int cId,int qId){
		List<Catalog> catalogs = exam.getCatalogs();
		for(Catalog catalog: catalogs){
			if(catalog.getIndex() == cId){
				List<Question> questions = catalog.getQuestions();
				for(Question question:questions)
				if(question.getIndex() == qId){
					return question;
				}
			}			
		}
		return null;
	}
	
	
}
