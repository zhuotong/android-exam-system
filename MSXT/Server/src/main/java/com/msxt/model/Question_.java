package com.msxt.model;

import javax.persistence.metamodel.SingularAttribute;

public class Question_ {
	public static volatile SingularAttribute<Question, String> id;
    
    public static volatile SingularAttribute<Question, String> name;
    
    public static volatile SingularAttribute<Question, String> content;
    
    public static volatile SingularAttribute<Question, QuestionType> questionType;
}
