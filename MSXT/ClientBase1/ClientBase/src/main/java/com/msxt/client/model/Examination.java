package com.msxt.client.model;

import java.util.ArrayList;
import java.util.List;

public class Examination {
	private String id;
	private String name;
	private int time;
	private boolean confuse;
	private List<Catalog> catalogs;
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
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public boolean isConfuse() {
		return confuse;
	}
	public void setConfuse(boolean confuse) {
		this.confuse = confuse;
	}
	public List<Catalog> getCatalogs() {
		if( catalogs==null )
			catalogs = new ArrayList<Catalog>();
		return catalogs;
	}
	public void setCatalogs(List<Catalog> catalogs) {
		this.catalogs = catalogs;
	}
	
	public static class Catalog{
		private int index;
		private String desc;
		private String name;
		private List<Question> questions;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Question> getQuestions() {
			if( questions==null )
				questions = new ArrayList<Question>();
			return questions;
		}
		public void setQuestions(List<Question> questions) {
			this.questions = questions;
		}
	}
	
	public static class Question{
		private int index;
		private String name;
		private String id;
		private String type;			
		private float score;
		private String content;
		
		List<Choice> choices;
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public float getScore() {
			return score;
		}

		public void setScore(float score) {
			this.score = score;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public List<Choice> getChoices() {
			if( choices==null )
				choices = new ArrayList<Choice>();
			return choices;
		}

		public void setChoices(List<Choice> choices) {
			this.choices = choices;
		}
	}
	
	public static class Choice{
		private int index;
		private String label;
		private String content;
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
}
