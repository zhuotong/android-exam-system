package com.dream.exam.bean;

import java.util.List;

public class ExamParse extends Exam{

	public Question getFirstQuestion(){
		List<Catalog> catalogs = this.getCatalogs();
		Catalog catalog= catalogs.get(0);
		List<Question> questions = catalog.getQuestions();
		Question question = questions.get(0);
		return question;
	}
	
}
