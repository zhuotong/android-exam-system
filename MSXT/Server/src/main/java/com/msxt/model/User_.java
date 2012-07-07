package com.msxt.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(User.class)
public abstract class User_ {
    public static volatile SingularAttribute<User, String> id;
    
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, String> code;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> loginname;
}
