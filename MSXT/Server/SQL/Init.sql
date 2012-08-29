insert into examination (ID, version, position_id, name, time) values ('ff80818138cfe61b0138cfeba14c0000', 0, 'ff80818138563d620138564121e40000', 'Software Engineer Iterview A', 45);

insert into examination_catalog (ID, version, exam_id, name, description, idx) values ('ff80818138cfe61b0138cfeba1520001', 0, 'ff80818138cfe61b0138cfeba14c0000', 'Single Select1', 'Test for JavaSE base knowlege', 0);
insert into examination_catalog (ID, version, exam_id, name, description, idx) values ('ff80818138cfe61b0138cfeba153000c', 0, 'ff80818138cfe61b0138cfeba14c0000', 'Single Select2', 'Test for JavaSE exception knowlege', 1);

insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1520002', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c251540138c2535cc80000', 0, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1520003', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c25b74dd0000', 1, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1520004', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c25d02c20006', 2, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1520005', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c25e54c0000c', 3, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1520006', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c25fbd740012', 4, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1530007', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c2612e550018', 5, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1530008', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c2663d81001e', 6, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1530009', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c26741e30024', 7, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba153000a', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c26884aa002a', 8, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba153000b', 0, 'ff80818138cfe61b0138cfeba1520001', 'ff80818138c2585f0138c26a31190030', 9, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba153000d', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c286270138c289b9440000', 0, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba153000e', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c2a11a0138c2a65eb00018', 1, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba153000f', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c286270138c28b96a30006', 2, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1530010', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c286270138c28d504f000c', 3, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1530011', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c286270138c28e813e0012', 4, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1530012', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c286270138c28fbf960018', 5, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1540013', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c2a11a0138c2a2b0670000', 6, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1540014', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c2a11a0138c2a3a7320006', 7, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1540015', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c2a11a0138c2a48215000c', 8, 5);
insert into examination_catalog_question (ID, version, catalog_id, question_id, idx, score) values ('ff80818138cfe61b0138cfeba1540016', 0, 'ff80818138cfe61b0138cfeba153000c', 'ff80818138c2a11a0138c2a591530012', 9, 5);

insert into interview (ID, version, start, apply_position_id, create_on, create_by, status, interviewer_id, login_name, login_password, conversation_id) values ('ff80818138d048940138d04c55980002', 76, '2012-08-28 15:00:00', 'ff80818138563d620138564121e40000', '2012-07-29 09:14:19', null, 'DOING', 'ff80818138ce66210138ce7216340001', 'test', 'test', 'c3844780-d490-4e34-899e-c5de0408d013');

insert into interview_examination (ID, version, interview_id, exam_id, exam_confuse, exam_score, start_time, end_time) values ('ff808181396c6cb801396c6dc2290000', 1, 'ff80818138d048940138d04c55980002', 'ff80818138cfe61b0138cfeba14c0000', 0, null, '2012-08-28 16:53:37', null);

insert into interviewer (ID, version, id_code, name, phone, age) values ('ff80818138ce66210138ce7216340001', 0, '320822198205200392', 'Felix Wu', '13962184712', 30);

insert into position (ID, version, name, next_position) values ('ff80818138563d620138564121e40000', 0, 'Software Engineer', 'P002');
insert into position (ID, version, name, next_position) values ('P002', 1, 'Senior Software Engineer', 'P003');
insert into position (ID, version, name, next_position) values ('P003', 1, 'Architect', 'P004');
insert into position (ID, version, name, next_position) values ('P004', 1, 'Senior Architect', 'P005');
insert into position (ID, version, name, next_position) values ('P005', 1, 'Chief Architect', null);

insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c25fbd740013', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c25fbd740012');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c26741e30025', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c26741e30024');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c26884aa002b', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c26884aa002a');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c26a311a0031', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c26a31190030');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c26b6bcb0037', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c26b6bcb0036');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c26e65ef0043', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c26e65ef0042');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c26fbc590049', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c26fbc590048');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c2712f3a004f', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c2712f39004e');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c27270240055', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c27270240054');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c273ea7e005b', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c273ea7e005a');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c275d58c0061', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c275d58b0060');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c2779b210067', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c2779b210066');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c278c71e006d', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c278c71e006c');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2585f0138c27a13900073', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c27a13900072');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c286270138c289b94a0001', 0, 'ff80818138563d620138564121e40000', 'ff80818138c286270138c289b9440000');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c286270138c28b96a30007', 0, 'ff80818138563d620138564121e40000', 'ff80818138c286270138c28b96a30006');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c286270138c28d504f000d', 0, 'ff80818138563d620138564121e40000', 'ff80818138c286270138c28d504f000c');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c286270138c28e813e0013', 0, 'ff80818138563d620138564121e40000', 'ff80818138c286270138c28e813e0012');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c286270138c28fbf960019', 0, 'ff80818138563d620138564121e40000', 'ff80818138c286270138c28fbf960018');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2a11a0138c2a2b06c0001', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2a11a0138c2a2b0670000');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2a11a0138c2a3a7320007', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2a11a0138c2a3a7320006');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2a11a0138c2a48215000d', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2a11a0138c2a48215000c');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2a11a0138c2a591530013', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2a11a0138c2a591530012');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c2a11a0138c2a65eb10019', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2a11a0138c2a65eb00018');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c662080138c66293610000', 0, 'ff80818138563d620138564121e40000', 'ff80818138c251540138c2535cc80000');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c667540138c6686a020000', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c25d02c20006');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c7504b0138c7524cd90000', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c25e54c0000c');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c7504b0138c752d50e0005', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c2612e550018');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c7504b0138c7536038000a', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c2663d81001e');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c7630f0138c76513980000', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c26ce59f003c');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138c7a6d90138c7a903ee0001', 0, 'ff80818138563d620138564121e40000', 'ff80818138c7a6d90138c7a903cf0000');
insert into position_question (ID, version, position_id, question_id) values ('ff80818138ca9dd70138caa434b30010', 0, 'ff80818138563d620138564121e40000', 'ff80818138c2585f0138c25b74dd0000');

insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c251540138c2535cc80000', 0, '1', 'JavaSE Base 1', 'public class Test {
    public static void main(String[] args){
        String s = null;
        System.out.println(String.valueOf(s));
    }
}

Please select the output result', 'D');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c25b74dd0000', 0, '1', 'JavaSE Base 2', 'public class Test {
    public static void main(String[] args){
       String s1 = new String("Test");
       String s2 = new String("Test");
       System.out.println( s1.equals(s2));
       System.out.println( s1==s2);
       System.out.println( s1.intern()==s2.intern());
    }
}

Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c25d02c20006', 0, '1', 'JavaSE Base 3', 'Please select the correct option for StringBuilder and StringBuffer', 'D');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c25e54c0000c', 0, '1', 'JavaSE Base 4', 'class Father {
	public final void flipper() { System.out.println("Father"); }
}

public class Son extends Father {
	public void flipper() {
		System.out.println("Son");
		super.flipper();
}

public static void main(String [] args) {
		new Son().flipper();
	}
}

Please select the output result', 'D');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c25fbd740012', 0, '1', 'JavaSE Base 5', 'class Top {
	public Top(String s) { System.out.print("B"); }
}

public class Bottom extends Top {
	public Bottom(String s) { System.out.print("D"); }
	public static void main(String [] args) {
	new Bottom("C");
	System.out.println(" ");
	} 
}

Please select the output result', 'D');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c2612e550018', 0, '1', 'JavaSE Base 6', 'class Father {
    private final void flipper() {
        System.out.println("Father");
    }
}

public class Son extends Father {
    public final void flipper() {
        System.out.println("Son");
    }

    public static void main(String[] args) {
        new Son().flipper();
    }
}

Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c2663d81001e', 0, '1', 'JavaSE Base 7', 'class Father {
	public Father() {
		System.out.println("This is Father!");
	}
	public Father(String s) {
		this();
		System.out.println("I am " + s);
	}
}

class Son extends Father {
	public Son(String s) {
		super(s);
		System.out.println("This is Son");
	}
	public Son() {
		this("I am Tom");
	}
    public static void main(String args[]) {
		Son s = new Son("Jack");
	}
}

Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c26741e30024', 0, '1', 'JavaSE Base 8', 'class Uber {
    static int y = 2;
    Uber(int x) {
        this();
        y = y * 2;
    }
    Uber() {
        y++;
    }
}

class Minor extends Uber {
    Minor(int i) {
        y = y + 3;
    }

    public static void main(String[] args) {
        new Minor(y);
        System.out.println(y);
    }
}
Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c26884aa002a', 0, '1', 'JavaSE Base 9', 'class A {
  public static int addOne(final int x) {
    return x+1;
  }
}

class B{
    public static void main(String[] args) {
        final int x = 1;
        System.out.println(A.addOne(x));
    }
}

Please select the output result', 'B');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c26a31190030', 0, '1', 'JavaSE Base 10', 'Please select the output result
class Base{
    private int i = 0;
}

class Sub extends Base{
    public static void main(String[] args) {
        System.out.println(i++);
    }
}', 'D');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c26b6bcb0036', 0, '1', 'JavaSE Base 11', 'Please select the output result
class A{
    public String toString(){
        return "4";
    }
}

class B extends A{
    public String toString(){
        return super.toString() +"3";
    }
    public   static   void   main(String[]   args)   {
        A a = new B();
        B b = new B();
        A a1 = new A();
        System.out.print(a.toString() + " ");
        System.out.print(b.toString()+ " ");
        System.out.print(a1.toString());
    }
}
', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c26ce59f003c', 0, '1', 'JavaSE Base 12', 'class A {
    int i = 1;
    public int f() {return i;}
    static String g() {return "A";}
}
class B extends A {
    int i = 2;
    public int f() {return -i;}
    static String g() {return "B";}
    public static void main(String[] args) {
         A a = new B();
         System.out.println(a.i);
         System.out.println(a.f());
         System.out.println(a.g());
    }
}

Please select the output result', 'B');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c26e65ef0042', 0, '1', 'JavaSE Base 13', 'public class Animal {
	public String noise() {
		return "peep";
	}

    	public static void main(String[] args) {
		Animal animal = new Dog();
		Cat cat = (Cat) animal;
		System.out.println(cat.noise());
	}
}

class Cat extends Animal {
	public String noise() {
		return "miao";
	}
}

class Dog extends Animal {
	public String noise() {
		return "wang";
	}
}

Please select the output result', 'D');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c26fbc590048', 0, '1', 'JavaSE Base 14', 'public class Animal {
	public String noise() {
		return "peep";
	}
    	public static void main(String[] args) {
		Animal animal = new Cat();
		System.out.println(animal.noise());
	}
}

class Cat extends Animal {
	public String noise() {
		return "miao";
	}
}

Please select the output result', 'B');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c2712f39004e', 0, '1', 'JavaSE Base 15', 'public class Animal {
	public String noise() {
		return "peep";
	}
    	public static void main(String[] args) {
		Animal animal = new Husky();
		System.out.println(animal.noise());
	}
}

class Dog extends Animal {
	public String noise() {
		return "wang";
	}
}

class Husky extends Dog {
	public String noise() {
		return "oh";
	}
}

Please select the output result', 'B');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c27270240054', 0, '1', 'JavaSE Base 16', 'public class Animal {
	public static String noise() {
		return "peep";
	}
    	public static void main(String[] args) {
		Animal animal = new Cat();
		System.out.println(animal.noise());
	}
}
class Cat extends Animal {
	public static String noise() {
		return "miao";
	}
}

Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c273ea7e005a', 0, '1', 'JavaSE Base 17', 'public class Animal {
	private String noise() {
		return "peep";
	}
    	public static void main(String[] args) {
		Animal animal = new Cat();
		System.out.println(animal.noise());
	}
}
class Cat extends Animal {
	public String noise() {
		return "miao";
	}
}

Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c275d58b0060', 0, '2', 'JavaSE Base 18', 'Please select the correct options', 'ACD');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c2779b210066', 0, '1', 'JavaSE Base 19', 'abstract class SuperClass {
    abstract void process();
    SuperClass(){
       process();
    }
}

class SubClass extends SuperClass{
    private int value = 1;
    void process(){
        System.out.print(value);
    }
    public static void main(String[] args) {
        SubClass subClass = new SubClass();
        subClass.process();
    }
}

Please select the output result', 'C');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c278c71e006c', 0, '1', 'JavaSE Base 20', 'public class SuperClass {
    private int value = 1;
    public void method(SuperClass p) {
        this.value += p.value;
    }
    public void addValue(int v) {
        value += v;
    }
    public int getValue() {
        return value;
    }
}


class SubClass extends SuperClass {
    public void method(SubClass p) {
        p.addValue(2);
    }

    public static void main(String[] args) {
        SuperClass superClass = new SubClass();
        SubClass subClass = new SubClass();
        superClass.method(subClass);
        System.out.print(superClass.getValue() + ",");
        subClass.method(subClass);
        System.out.print(subClass.getValue());
    }
}

Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2585f0138c27a13900072', 0, '1', 'JavaSE Base 21', 'public class Useful {
    String s = "A";
    String getS(){
        return s;
    }
}

class MoreUseful extends Useful {
    String s = "B";
    String getS() {
        return s;
    }

    public static void main(String[] args) {
        Useful useful = new MoreUseful();
        System.out.print(useful.s + ",");
        System.out.print(useful.getS());
    }
}

Please select the output result', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c286270138c289b9440000', 0, '1', 'JavaSE Base Exception 1', 'public class ExTesting {
    public void f(){
        try {
            throw new Exception();
        } catch (Exception e) {
            throw new SimpleException();
        }
    }
    public static void main(String[] args) {
        ExTesting exTesting = new ExTesting();
        try {
            exTesting.f();
        } catch (SimpleException e) {
            System.out.println("System encounters Simple Exception");
        } catch (Exception e) {
            System.out.println("System encounters Exception");
        }
    }
}

class SimpleException extends Exception {}

What is the result of attempting to compile and run this code ?', 'C');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c286270138c28b96a30006', 0, '1', 'JavaSE Base Exception 2', 'public class ExTesting {
    public void f(){
        try {
            throw new Exception();
        } catch (Exception e) {
            throw new SimpleException();
        }
    }
    public static void main(String[] args) {
        ExTesting exTesting = new ExTesting();
        try {
            exTesting.f();
        } catch (SimpleException e) {
            System.out.println("System encounters Simple Exception");
        } catch (Exception e) {
            System.out.println("System encounters Exception");
        }
    }
}

class SimpleException extends RuntimeException {}

What is the result of attempting to compile and run this code ?', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c286270138c28d504f000c', 0, '1', 'JavaSE Base Exception 3', 'public class ExTesting {
    public void f() throws ChildException{
            throw new ChildException();
    }
    public static void main(String[] args) {
        ExTesting exTesting = new ExTesting();
        try {
            exTesting.f();
        } catch (GrandFatherException e) {
            System.out.println("Error A");
        } catch (FatherException e) {
            System.out.println("Error B");
        }
    }
}

class GrandFatherException extends Exception {}

class FatherException extends GrandFatherException {}

class ChildException extends FatherException {}

What is the result of attempting to compile and run this code ?', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c286270138c28e813e0012', 0, '1', 'JavaSE Base Exception 4', 'public class ExTesting {
    public static void main(String[] args) {
        System.out.println("return value of test() : " + test());
    }

    public static int test() {
        int i = 1;

        try {
            i = 1 / 0;
            return 1;
        } catch (Exception e) {
            return 2;
        } finally {
            System.out.println("finally block");
        }
    }
}

What is the result of attempting to compile and run this code ?', 'B');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c286270138c28fbf960018', 0, '1', 'JavaSE Base Exception 5', 'public class ExTesting {
    public static void main(String[] args) {
        System.out.println("return value of getValue(): " + getValue());
    }
    public static int getValue() {
        int i = 1;
        try {
            return i;
        } finally {
            i++;
        }
    }
}
What is the result of attempting to compile and run this code ?', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2a11a0138c2a2b0670000', 0, '1', 'JavaSE Base Exception 6', ' public class ExTesting {
    public static void main(String[] args) {
        System.out.println("return value of getValue(): " + getValue());
    }
    public static int getValue() {
        int i = 1;
        try {
            i = 4;
        } finally {
            i++;
            return i;
        }
    }
}
What is the result of attempting to compile and run this code ?', 'B');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2a11a0138c2a3a7320006', 0, '1', 'JavaSE Base Exception 7', ' public class ExTesting {
    public static void main(String[] args) {
        System.out.println(test());
    }

    public static String test() {
        try {
            return test1();
        } finally {
            System.out.println("finally block");
        }
    }

    public static String test1() {
        System.out.println("return statement");
        return "after return";
    }
}

What is the result of attempting to compile and run this code ?', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2a11a0138c2a48215000c', 0, '1', 'JavaSE Base Exception 8', 'public class ExTesting {
    public static void main(String[] args) {
        System.out.println("return value of getValue(): " + getNumberValue());
    }
    public static Number getNumberValue() {
        Number i = new Number(1);
        try {
            i.setValue(4);
            return i;
        } finally {
            i.setValue(5);
        }
    }
}
class Number {
    private int value;

    Number(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
What is the result of attempting to compile and run this code ?', 'B');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2a11a0138c2a591530012', 0, '1', 'JavaSE Base Exception 9', 'public class ExTesting {
    public void f() {
        try {
            throw new Exception();
        } catch (Exception e) {
            throw new SimpleException();
        }
    }
    public static void main(String[] args) {
        ExTesting exTesting = new ExTesting();
        try {
            exTesting.f();
        } catch (Exception e) {
            System.out.println("System encounters Exception");
        }
    }
}

class SimpleException extends Error {}

What is the result of attempting to compile and run this code ?', 'A');
insert into question (ID, version, question_type_id, name, content, right_answer) values ('ff80818138c2a11a0138c2a65eb00018', 0, '1', 'JavaSE Base Exception 10', 'public class ExTesting {
    public static void main(String[] args) {
        Father c = new Child();
        try {
            c.foo();
        } catch (IOException e) {
            System.out.println("Error A");
        } catch (Exception e) {
            System.out.println("Error B");
        }
    }
}

class Father {
    public void foo() throws IOException {
        throw new IOException();
    }
}

class Child extends Father {
    public void foo() throws Exception {
        throw new Exception();
    }
}

What is the result of attempting to compile and run this code ?', 'A');


insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c251540138c2535ccf0002', 0, 'ff80818138c251540138c2535cc80000', 'A', '""', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c251540138c2535ccf0003', 0, 'ff80818138c251540138c2535cc80000', 'B', 'Exception in thread "main" java.lang.NullPointerExceptio', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c251540138c2535ccf0004', 0, 'ff80818138c251540138c2535cc80000', 'C', 'Compilation error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c251540138c2535ccf0005', 0, 'ff80818138c251540138c2535cc80000', 'D', '"null"', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c25fbd740014', 0, 'ff80818138c2585f0138c25fbd740012', 'A', 'BD', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c25fbd740015', 0, 'ff80818138c2585f0138c25fbd740012', 'B', 'DB', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c25fbd750016', 0, 'ff80818138c2585f0138c25fbd740012', 'C', 'BDC', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c25fbd750017', 0, 'ff80818138c2585f0138c25fbd740012', 'D', 'Compilation error', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26741e30026', 0, 'ff80818138c2585f0138c26741e30024', 'A', '6', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26741e30027', 0, 'ff80818138c2585f0138c26741e30024', 'B', '7', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26741e30028', 0, 'ff80818138c2585f0138c26741e30024', 'C', '8', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26741e40029', 0, 'ff80818138c2585f0138c26741e30024', 'D', '9', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26884aa002c', 0, 'ff80818138c2585f0138c26884aa002a', 'A', '1', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26884aa002d', 0, 'ff80818138c2585f0138c26884aa002a', 'B', '2', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26884aa002e', 0, 'ff80818138c2585f0138c26884aa002a', 'C', '3', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26884aa002f', 0, 'ff80818138c2585f0138c26884aa002a', 'D', 'Compilation error', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26a311a0032', 0, 'ff80818138c2585f0138c26a31190030', 'A', '0', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26a311a0033', 0, 'ff80818138c2585f0138c26a31190030', 'B', '1', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26a311a0034', 0, 'ff80818138c2585f0138c26a31190030', 'C', '2', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26a311a0035', 0, 'ff80818138c2585f0138c26a31190030', 'D', 'Compilation error', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26b6bcb0038', 0, 'ff80818138c2585f0138c26b6bcb0036', 'A', '43 43 4 ', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26b6bcc0039', 0, 'ff80818138c2585f0138c26b6bcb0036', 'B', '4 43 4', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26b6bcc003a', 0, 'ff80818138c2585f0138c26b6bcb0036', 'C', '43 43 43', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26b6bcc003b', 0, 'ff80818138c2585f0138c26b6bcb0036', 'D', '4 43 43', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26e65ef0044', 0, 'ff80818138c2585f0138c26e65ef0042', 'A', 'peep', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26e65f00045', 0, 'ff80818138c2585f0138c26e65ef0042', 'B', 'miao', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26e65f00046', 0, 'ff80818138c2585f0138c26e65ef0042', 'C', 'wang', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26e65f00047', 0, 'ff80818138c2585f0138c26e65ef0042', 'D', 'Exception in thread "main"...', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26fbc59004a', 0, 'ff80818138c2585f0138c26fbc590048', 'A', 'peep', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26fbc59004b', 0, 'ff80818138c2585f0138c26fbc590048', 'B', 'miao', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26fbc5a004c', 0, 'ff80818138c2585f0138c26fbc590048', 'C', 'Exception in thread "main"...', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c26fbc5a004d', 0, 'ff80818138c2585f0138c26fbc590048', 'D', 'compilation error', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2712f3a0050', 0, 'ff80818138c2585f0138c2712f39004e', 'A', 'peep', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2712f3a0051', 0, 'ff80818138c2585f0138c2712f39004e', 'B', 'oh', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2712f3a0052', 0, 'ff80818138c2585f0138c2712f39004e', 'C', 'wang', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2712f3a0053', 0, 'ff80818138c2585f0138c2712f39004e', 'D', 'Exception in thread "main"...', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27270240056', 0, 'ff80818138c2585f0138c27270240054', 'A', 'peep', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27270240057', 0, 'ff80818138c2585f0138c27270240054', 'B', 'miao', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27270240058', 0, 'ff80818138c2585f0138c27270240054', 'C', 'compilation error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27270260059', 0, 'ff80818138c2585f0138c27270240054', 'D', 'Exception in thread "main"...', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c273ea7e005c', 0, 'ff80818138c2585f0138c273ea7e005a', 'A', 'peep', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c273ea7e005d', 0, 'ff80818138c2585f0138c273ea7e005a', 'B', 'miao', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c273ea7e005e', 0, 'ff80818138c2585f0138c273ea7e005a', 'C', 'compilation error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c273ea7f005f', 0, 'ff80818138c2585f0138c273ea7e005a', 'D', 'Exception in thread "main"...', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c275d58c0062', 0, 'ff80818138c2585f0138c275d58b0060', 'A', 'cannot create instance of abstract class', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c275d58c0063', 0, 'ff80818138c2585f0138c275d58b0060', 'B', 'abstract class must has abstract method', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c275d58c0064', 0, 'ff80818138c2585f0138c275d58b0060', 'C', 'abstract class can has non abstract method', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c275d58c0065', 0, 'ff80818138c2585f0138c275d58b0060', 'D', 'abstract class can be inherited', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2779b220068', 0, 'ff80818138c2585f0138c2779b210066', 'A', '0', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2779b220069', 0, 'ff80818138c2585f0138c2779b210066', 'B', '0', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2779b22006a', 0, 'ff80818138c2585f0138c2779b210066', 'C', '01', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c2779b22006b', 0, 'ff80818138c2585f0138c2779b210066', 'D', '10', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c278c71e006e', 0, 'ff80818138c2585f0138c278c71e006c', 'A', '2,3', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c278c71e006f', 0, 'ff80818138c2585f0138c278c71e006c', 'B', '3,2', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c278c71e0070', 0, 'ff80818138c2585f0138c278c71e006c', 'C', '3,3', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c278c71e0071', 0, 'ff80818138c2585f0138c278c71e006c', 'D', '2,2', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27a13900074', 0, 'ff80818138c2585f0138c27a13900072', 'A', 'A,B', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27a13900075', 0, 'ff80818138c2585f0138c27a13900072', 'B', 'B,A', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27a13900076', 0, 'ff80818138c2585f0138c27a13900072', 'C', 'A,A', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2585f0138c27a13900077', 0, 'ff80818138c2585f0138c27a13900072', 'D', 'B,B', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c289b94a0002', 0, 'ff80818138c286270138c289b9440000', 'A', 'It will print "System encounters Simple Exception"', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c289b94b0003', 0, 'ff80818138c286270138c289b9440000', 'B', 'It will print "System encounters Exception"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c289b94b0004', 0, 'ff80818138c286270138c289b9440000', 'C', 'Compilation error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c289b94b0005', 0, 'ff80818138c286270138c289b9440000', 'D', 'There is no output', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28b96a30008', 0, 'ff80818138c286270138c28b96a30006', 'A', 'It will print "System encounters Simple Exception"', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28b96a30009', 0, 'ff80818138c286270138c28b96a30006', 'B', 'It will print "System encounters Exception"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28b96a3000a', 0, 'ff80818138c286270138c28b96a30006', 'C', 'Compilation error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28b96a3000b', 0, 'ff80818138c286270138c28b96a30006', 'D', 'There is no output', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28d504f000e', 0, 'ff80818138c286270138c28d504f000c', 'A', 'Compiler error', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28d5050000f', 0, 'ff80818138c286270138c28d504f000c', 'B', 'It will print "Error A"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28d50500010', 0, 'ff80818138c286270138c28d504f000c', 'C', 'It will print "Error B"', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28d50500011', 0, 'ff80818138c286270138c28d504f000c', 'D', 'The exception will go uncaught by both catch blocks', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28e813e0014', 0, 'ff80818138c286270138c28e813e0012', 'A', 'It will print     "return value of test() : 1      finally block"', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28e813e0015', 0, 'ff80818138c286270138c28e813e0012', 'B', 'It will print     "finally block      return value of test() : 2 "', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28e813e0016', 0, 'ff80818138c286270138c28e813e0012', 'C', 'It will print     "finally block      return value of test() : 1 "', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28e813e0017', 0, 'ff80818138c286270138c28e813e0012', 'D', 'It will print     "return value of test() : 2      finally block "', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28fbf97001a', 0, 'ff80818138c286270138c28fbf960018', 'A', 'It will print "return value of getValue(): 1" ', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28fbf97001b', 0, 'ff80818138c286270138c28fbf960018', 'B', 'It will print "return value of getValue(): 2"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28fbf97001c', 0, 'ff80818138c286270138c28fbf960018', 'C', 'Compiler error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c286270138c28fbf97001d', 0, 'ff80818138c286270138c28fbf960018', 'D', 'It will print "return value of getValue(): 0"', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a2b06c0002', 0, 'ff80818138c2a11a0138c2a2b0670000', 'A', 'It will print "return value of getValue(): 4" ', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a2b06d0003', 0, 'ff80818138c2a11a0138c2a2b0670000', 'B', 'It will print "return value of getValue(): 5"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a2b06d0004', 0, 'ff80818138c2a11a0138c2a2b0670000', 'C', 'Compiler error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a2b06d0005', 0, 'ff80818138c2a11a0138c2a2b0670000', 'D', 'It will print "return value of getValue(): 1"', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a3a7320008', 0, 'ff80818138c2a11a0138c2a3a7320006', 'A', 'It will print     "return statement     finally block     after return"', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a3a7320009', 0, 'ff80818138c2a11a0138c2a3a7320006', 'B', 'It will print     "return statement     after return     finally block"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a3a732000a', 0, 'ff80818138c2a11a0138c2a3a7320006', 'C', 'Compiler error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a3a732000b', 0, 'ff80818138c2a11a0138c2a3a7320006', 'D', 'It will print     "finally block     return statement     after return"', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a48216000e', 0, 'ff80818138c2a11a0138c2a48215000c', 'A', 'It will print "return value of getValue(): 4" ', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a48216000f', 0, 'ff80818138c2a11a0138c2a48215000c', 'B', 'It will print "return value of getValue(): 5"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a482160010', 0, 'ff80818138c2a11a0138c2a48215000c', 'C', 'Compiler error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a482160011', 0, 'ff80818138c2a11a0138c2a48215000c', 'D', 'It will print "return value of getValue(): 1"', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a591530014', 0, 'ff80818138c2a11a0138c2a591530012', 'A', 'It will throw exception', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a591530015', 0, 'ff80818138c2a11a0138c2a591530012', 'B', 'It will print "System encounters Exception"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a591540016', 0, 'ff80818138c2a11a0138c2a591530012', 'C', 'Compilation error', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a591540017', 0, 'ff80818138c2a11a0138c2a591530012', 'D', 'There is no output', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a65eb1001a', 0, 'ff80818138c2a11a0138c2a65eb00018', 'A', 'Compiler error', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a65eb1001b', 0, 'ff80818138c2a11a0138c2a65eb00018', 'B', 'It will print "Error A"', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a65eb1001c', 0, 'ff80818138c2a11a0138c2a65eb00018', 'C', 'It will print "Error B"', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c2a11a0138c2a65eb1001d', 0, 'ff80818138c2a11a0138c2a65eb00018', 'D', 'The exception will go uncaught by both catch blocks', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c667540138c6686a2a0001', 0, 'ff80818138c2585f0138c25d02c20006', 'A', 'StringBuilder is Thread Safe   
StringBuffer is Thread Safe', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c667540138c6686a2a0002', 0, 'ff80818138c2585f0138c25d02c20006', 'B', 'StringBuilder is Non Thread Safe   
StringBuffer is Non Thread Safe', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c667540138c6686a2a0003', 0, 'ff80818138c2585f0138c25d02c20006', 'C', 'StringBuilder is Thread Safe   
StringBuffer is Non Thread Safe', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c667540138c6686a2a0004', 0, 'ff80818138c2585f0138c25d02c20006', 'D', 'StringBuilder is Non Thread Safe   
StringBuffer is Thread Safe', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7524ce40001', 0, 'ff80818138c2585f0138c25e54c0000c', 'A', 'Son 	 
Father', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7524ce40002', 0, 'ff80818138c2585f0138c25e54c0000c', 'B', 'Father 	 
Son 	 
Father', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7524ce40003', 0, 'ff80818138c2585f0138c25e54c0000c', 'C', 'Father', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7524ce40004', 0, 'ff80818138c2585f0138c25e54c0000c', 'D', 'Compilation error', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c752d5110006', 0, 'ff80818138c2585f0138c2612e550018', 'A', 'Son', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c752d5110007', 0, 'ff80818138c2585f0138c2612e550018', 'B', 'Father', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c752d5110008', 0, 'ff80818138c2585f0138c2612e550018', 'C', 'Father 	 
So', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c752d5110009', 0, 'ff80818138c2585f0138c2612e550018', 'D', 'Compilation error', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7536051000b', 0, 'ff80818138c2585f0138c2663d81001e', 'A', 'This is Father! 	
I am Jack 	
This is Son', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7536052000c', 0, 'ff80818138c2585f0138c2663d81001e', 'B', 'This is Father! 	
I am Jack 	
This is Son 	
I am Tom', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7536052000d', 0, 'ff80818138c2585f0138c2663d81001e', 'C', 'I am Tom 	
This is Father! 	
I am Jack', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7504b0138c7536052000e', 0, 'ff80818138c2585f0138c2663d81001e', 'D', 'Compile Error', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7630f0138c76513a90001', 0, 'ff80818138c2585f0138c26ce59f003c', 'A', '2 	 
-2 		
B ', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7630f0138c76513a90002', 0, 'ff80818138c2585f0138c26ce59f003c', 'B', '1 	 
-2     
A', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7630f0138c76513ae0003', 0, 'ff80818138c2585f0138c26ce59f003c', 'C', '2
-2
A', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138c7630f0138c76513ae0004', 0, 'ff80818138c2585f0138c26ce59f003c', 'D', '1
-2
B', 4);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138ca9dd70138caa434b90011', 0, 'ff80818138c2585f0138c25b74dd0000', 'A', 'true   
false   
true', 1);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138ca9dd70138caa434b90012', 0, 'ff80818138c2585f0138c25b74dd0000', 'B', 'false   
true   
true', 2);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138ca9dd70138caa434b90013', 0, 'ff80818138c2585f0138c25b74dd0000', 'C', 'true   
false   
false', 3);
insert into question_choice_item (ID, version, question_id, label_name, content, idx) values ('ff80818138ca9dd70138caa434b90014', 0, 'ff80818138c2585f0138c25b74dd0000', 'D', 'false   
true   
false', 4);

insert into question_type (ID, version, name, rendener, parent_id) values ('1', 0, 'Single Choice', null, null);
insert into question_type (ID, version, name, rendener, parent_id) values ('2', 0, 'Multiple Choice', null, null);
insert into question_type (ID, version, name, rendener, parent_id) values ('3', 0, 'Fill', null, null);
insert into question_type (ID, version, name, rendener, parent_id) values ('4', 0, 'Essay', null, null);

insert into user (ID, version, name, code, password, loginname, role_id) values ('ff808181384aa39601384aa43b5b0000', 0, '??', null, 'debug', 'debug', null);

