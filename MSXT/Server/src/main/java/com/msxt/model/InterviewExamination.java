package com.msxt.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="interview_examination")
public class InterviewExamination {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne
	@JoinColumn(name="interview_id")
	private Interview interview;
	
	@ManyToOne
	@JoinColumn(name="exam_id")
	private Examination exam;
	
	@OneToMany(mappedBy = "interviewExamination", cascade = CascadeType.REMOVE)
	private List<ExaminationCatalogQuestionAnswer> examQuestionAnswers;
	
	@Column(name="exam_confuse")
	private Integer examConfuse;
	
	@Column(name="exam_score")
	private Float examScore;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time")
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time")
	private Date endTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Interview getInterview() {
		return interview;
	}
	public void setInterview(Interview interview) {
		this.interview = interview;
	}
	public Examination getExam() {
		return exam;
	}
	public void setExam(Examination exam) {
		this.exam = exam;
	}
	
	public Float getExamScore() {
		return examScore;
	}
	
	public void setExamScore(Float examScore) {
		this.examScore = examScore;
	}
	
	public List<ExaminationCatalogQuestionAnswer> getExamQuestionAnswers() {
		return examQuestionAnswers;
	}
	
	public void setExamQuestionAnswers(
			List<ExaminationCatalogQuestionAnswer> examQuestionAnswers) {
		this.examQuestionAnswers = examQuestionAnswers;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
	public Integer getExamConfuse() {
		return examConfuse;
	}
	public void setExamConfuse(Integer examConfuse) {
		this.examConfuse = examConfuse;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public static enum STATUS{
		UNSTART, STARTED_UNOVERTIME, STARTED_OVERTIME, SUBMITTED 
	}
	
	public STATUS getStatus(){
		if( getStartTime() == null ) 
			return STATUS.UNSTART;
		else { 
			if( getEndTime() == null ) {
				Long span = System.currentTimeMillis() - getStartTime().getTime();
				if( span < (getExam().getTime()+10)*60*1000 )
					return STATUS.STARTED_UNOVERTIME;
				else
					return STATUS.STARTED_OVERTIME;
			} else {
				return STATUS.SUBMITTED;
			}
		}	
	}
}
