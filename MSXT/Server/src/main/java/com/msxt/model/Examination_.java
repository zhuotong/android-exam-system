package com.msxt.model;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

public class Examination_ {
	public static volatile SingularAttribute<Examination, String> id;
	public static volatile SingularAttribute<Examination, String> name;  
	public static volatile SingularAttribute<Examination, Position> position;  
	public static volatile SingularAttribute<Examination, List<ExaminationCatalog>> catalogs;
}
