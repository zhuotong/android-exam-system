package com.dream.ivpc.chart;

public class Circle extends Chart {
	private float radius;
	private float cdx;
	private float cdy;
	
	public Circle(float radius, float cdx, float cdy) {
		super();
		this.radius = radius;
		this.cdx = cdx;
		this.cdy = cdy;
	}
	
	public Circle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Circle(String bgColor,float radius, float cdx, float cdy) {
		super(bgColor);
		this.radius = radius;
		this.cdx = cdx;
		this.cdy = cdy;
	}

	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public float getCdx() {
		return cdx;
	}
	public void setCdx(float cdx) {
		this.cdx = cdx;
	}
	public float getCdy() {
		return cdy;
	}
	public void setCdy(float cdy) {
		this.cdy = cdy;
	}
	
	
}
