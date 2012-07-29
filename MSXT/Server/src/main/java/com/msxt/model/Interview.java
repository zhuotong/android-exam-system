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
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="interview")
public class Interview {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start")
	private Date start;
	
	@ManyToOne
	@JoinColumn(name="apply_position_id")
	private Position applyPosition;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_on")
	private Date createOn;
	
	@Column(name="create_by")
	private String createBy;
	
	@Column(name="status")
	private String status; //等待面试，正在面试，面试完成，缺席面试'
	
	@ManyToOne
	@JoinColumn(name="interviewer_id")
	private Interviewer interviewer;
	
	@Column(name="login_name")
	private String loginName;
	
	@Column(name="login_password")
	private String loginPassword;
	
	@OneToMany(mappedBy = "interview", cascade = CascadeType.REMOVE)
	private List<InterviewExamination> examinations;
	
	@OneToMany(mappedBy = "interview", cascade = CascadeType.REMOVE)
	private List<Evaluate> evaluates;
	
	@Column(name="conversation_id")
	private String conversationId;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	
	public Position getApplyPosition() {
		return applyPosition;
	}
	
	public void setApplyPosition(Position applyPosition) {
		this.applyPosition = applyPosition;
	}

	public Date getCreateOn() {
		return createOn;
	}
	
	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}
	public String getCreateBy() {
		return createBy;
	}
	
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public Interviewer getInterviewer() {
		return interviewer;
	}
	
	public void setInterviewer(Interviewer interviewer) {
		this.interviewer = interviewer;
	}
	
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getLoginPassword() {
		return loginPassword;
	}
	
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
	public List<InterviewExamination> getExaminations() {
		return examinations;
	}
	
	public void setExaminations(List<InterviewExamination> examinations) {
		this.examinations = examinations;
	}

	public List<Evaluate> getEvaluates() {
		return evaluates;
	}

	public void setEvaluates(List<Evaluate> evaluates) {
		this.evaluates = evaluates;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}
	
	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public static enum STATUS{
		WAITING, DOING, FINISH, ABSENT
	}
}
