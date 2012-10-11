package com.dream.ivpc.model;

public class CandiateDetailBean extends CandiateBean {
	private String gender;
	private String age;
	private String education;
	private String experience;

	public CandiateDetailBean() {
		super();
	}

	public CandiateDetailBean(String gender, String age, String education,
			String experience) {
		super();
		this.gender = gender;
		this.age = age;
		this.education = education;
		this.experience = experience;
	}

	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	
}
