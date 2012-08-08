package com.msxt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="examination_catalog_question_answer")
public class ExaminationCatalogQuestionAnswer {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne
	@JoinColumn(name="interview_exam_id")
	private InterviewExamination interviewExamination;
	
	@ManyToOne
	@JoinColumn(name="exam_question_id")
	private ExaminationCatalogQuestion examQuestion;
	
	@Column(name="answer")
	private String answer;
	
	@Column(name="result")
	private float result;
	
	@Column(name="score")
	private float score;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public InterviewExamination getInterviewExamination() {
		return interviewExamination;
	}
	
	public void setInterviewExamination(InterviewExamination interviewExamination) {
		this.interviewExamination = interviewExamination;
	}
	
	public ExaminationCatalogQuestion getExamQuestion() {
		return examQuestion;
	}
	
	public void setExamQuestion(ExaminationCatalogQuestion examQuestion) {
		this.examQuestion = examQuestion;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public float getResult() {
		return result;
	}
	
	public void setResult(float result) {
		this.result = result;
	}
	
	public float getScore() {
		return score;
	}
	
	public void setScore(float score) {
		this.score = score;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}
}
