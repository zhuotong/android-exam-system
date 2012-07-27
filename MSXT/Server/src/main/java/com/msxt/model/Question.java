package com.msxt.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="question")
public class Question {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne
	@JoinColumn(name="question_type_id")
	private QuestionType questionType;
	
	@Column(name="name")
	private String name;
	
	@Column(name="content")
	private String content;
	
	@Column(name="right_answer")
	private String rightAnswer;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<QuestionChoiceItem> choiceItems;
	
	@Transient
	private boolean onUsed = false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRightAnswer() {
		return rightAnswer;
	}
	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
	public List<QuestionChoiceItem> getChoiceItems() {
		return choiceItems;
	}
	public void setChoiceItems(List<QuestionChoiceItem> choiceItems) {
		this.choiceItems = choiceItems;
	}
	public boolean isOnUsed() {
		return onUsed;
	}
	public void setOnUsed(boolean onUsed) {
		this.onUsed = onUsed;
	}
}
