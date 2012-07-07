package com.msxt.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="position")
public class Position implements Serializable{
	private static final long serialVersionUID = 8350172956635419908L;

	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String   id;
	
	@Version
	private Integer version;
	
	@Column(name="name")
	private String name;  
	
	@ManyToOne
	@JoinColumn(name="next_position")
	private Position nextPosition; //高一级的职位
	
	@OneToMany(mappedBy="position")
	private List<Examination> exams;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Position getNextPosition() {
		return nextPosition;
	}
	public void setNextPosition(Position nextPosition) {
		this.nextPosition = nextPosition;
	}
	public List<Examination> getExams() {
		return exams;
	}
	public void setExams(List<Examination> exams) {
		this.exams = exams;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
}
