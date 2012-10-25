package com.dream.ivpc.chart;

public class Bar2D extends Chart{
	Coordinate startC;
	Coordinate endC;
	
	public Bar2D() {
		super();
	}

	public Bar2D(Coordinate startC, Coordinate endC) {
		super();
		this.startC = startC;
		this.endC = endC;
	}

	public Coordinate getStartC() {
		return startC;
	}
	public void setStartC(Coordinate startC) {
		this.startC = startC;
	}
	public Coordinate getEndC() {
		return endC;
	}
	public void setEndC(Coordinate endC) {
		this.endC = endC;
	}
	
	
}
