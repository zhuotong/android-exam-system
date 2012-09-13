package com.msxt.client.swing.model;

import java.util.HashMap;
import java.util.Map;

import com.msxt.client.model.Examination;
import com.msxt.client.swing.component.QuestionButton;

public class ExamBuildContext {
	private Examination exam;
	private Map<Examination.Question, QuestionButton> buttonMap;
	
	public ExamBuildContext(Examination e){
		exam = e;
		buttonMap = new HashMap<Examination.Question, QuestionButton>();
	}

	public Examination getExam() {
		return exam;
	}

	public void setExam(Examination exam) {
		this.exam = exam;
	}
	
	public QuestionButton getButton(Examination.Question o){
		return buttonMap.get( o );
	}
	
	public void setButton(Examination.Question o, QuestionButton c){
		buttonMap.put( o, c );
	}
}
