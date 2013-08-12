package com.dream.exam.bean;

import java.util.List;

public class ExamParse{

	public static Question getFirstQuestion(Exam exam){
		List<Catalog> catalogs = exam.getCatalogs();
		Catalog catalog= catalogs.get(0);
		List<Question> questions = catalog.getQuestions();
		Question question = questions.get(0);
		return question;
	}
	
}
