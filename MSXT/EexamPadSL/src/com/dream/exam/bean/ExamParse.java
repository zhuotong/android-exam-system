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
	
	public static Catalog getCatalog(Exam exam,int cId){
		for(Catalog catalog: exam.getCatalogs()){
			if(catalog.getIndex() == cId){
				return catalog;
			}			
		}
		return null;
	}
	
	public static int getMaxQuestion(Exam exam,int cId){
		List<Question> questions = null;
		for(Catalog catalog: exam.getCatalogs()){
			if(catalog.getIndex() == cId){
				questions = catalog.getQuestions();
			}			
		}
		if(questions.size()>1){
			return questions.size();
		}else{
			return 0;
		}
		
	}
	
	
}
