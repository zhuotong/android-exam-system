package com.msxt.model;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

public class Interview_ {
	public static volatile SingularAttribute<Interview, String> id;
	public static volatile SingularAttribute<Interview, Position> applyPosition;
	public static volatile SingularAttribute<Interview, Date> start;
	public static volatile SingularAttribute<Interview, Interviewer> interviewer;
	
	public static volatile SingularAttribute<Interview, String> loginName;
	public static volatile SingularAttribute<Interview, String> loginPassword;
}
