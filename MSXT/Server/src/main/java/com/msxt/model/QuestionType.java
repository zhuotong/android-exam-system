package com.msxt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author felix
 *
 */
@Entity
@Table(name="question_type")
public class QuestionType implements Serializable{
	private static final long serialVersionUID = 2969055642160100196L;

	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@Column(name="name")
	private String name;
	
	@Column(name="rendener")
	private String rendener;
	
	@ManyToOne
	@JoinColumn(name="parent_id")
	private QuestionType parent;
	
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
	public String getRendener() {
		return rendener;
	}
	public void setRendener(String rendener) {
		this.rendener = rendener;
	}
	public QuestionType getParent() {
		return parent;
	}
	public void setParent(QuestionType parent) {
		this.parent = parent;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
}
