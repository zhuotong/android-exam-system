package com.msxt.client.model.transfer;

import java.util.List;
import java.util.Random;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.msxt.client.model.Examination;
import com.msxt.client.model.Examination.Choice;
import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.model.QUESTION_TYPE;
import com.msxt.client.model.SubmitSuccessResult;
import com.msxt.client.model.Examination.Question;

public class Message2ModelTransferImpl implements Message2ModelTransfer{
	
    public LoginSuccessResult parseResult( Element root ) {
    	LoginSuccessResult lsr = new LoginSuccessResult();
    	String interviewer = root.getElementsByTagName( "interviewer" ).item(0).getTextContent();
    	String jobtitle = root.getElementsByTagName( "jobtitle" ).item(0).getTextContent();
    	lsr.setInterviewer( interviewer );
    	lsr.setJobtitle( jobtitle );
    	
    	Element examinations = (Element)root.getElementsByTagName( "examinations" ).item(0);
    	NodeList examChildren = examinations.getChildNodes();
    	for( int i=0; i<examChildren.getLength(); i++ ) {
    		Element exam = (Element)examChildren.item(i);
    		String id = exam.getElementsByTagName( "id" ).item(0).getTextContent();
    		String name = exam.getElementsByTagName( "name" ).item(0).getTextContent();
    		String desc = exam.getElementsByTagName( "desc" ).item(0).getChildNodes().item(0).getTextContent();
    		
    		LoginSuccessResult.Examination le = new LoginSuccessResult.Examination();
    		le.setId( id );
    		le.setName( name );
    		le.setDesc( desc );
    		lsr.getExaminations().add( le );
    	}
    	return lsr;
    }
    
    public Examination parseExamination( Element examination ) {
    	Examination exam = new Examination();
    	String examinationid = examination.getElementsByTagName( "examinationid" ).item(0).getTextContent();
    	String name = examination.getElementsByTagName( "name" ).item(0).getTextContent();
    	String time = examination.getElementsByTagName( "time" ).item(0).getTextContent();
    	String confuse = examination.getElementsByTagName( "confuse" ).item(0).getTextContent();
    	
    	exam.setId( examinationid );
    	exam.setName( name );
    	exam.setTime(Integer.valueOf( time ) );
    	exam.setConfuse( Boolean.valueOf( confuse ) );
    	
    	Element catalogs = (Element)examination.getElementsByTagName( "catalogs" ).item(0);
    	NodeList cnl = catalogs.getChildNodes();
    	for( int i=0; i<cnl.getLength(); i++ ) {
    		Examination.Catalog ec = parseCatalog( (Element)cnl.item(i) ); 
    		if( exam.isConfuse() ) {
    			confuseCatalog( ec );
    		}
    		exam.getCatalogs().add( ec );
    	}
    	return exam;
    }
    
    private Examination.Catalog parseCatalog( Element catalog ) {
		String cindex = catalog.getElementsByTagName( "index" ).item(0).getTextContent();
		String cname = catalog.getElementsByTagName( "name" ).item(0).getTextContent();
		String cdesc = catalog.getElementsByTagName( "catalogdesc" ).item(0).getTextContent();
		
		Examination.Catalog ec = new Examination.Catalog();
		ec.setIndex( Integer.valueOf(cindex) );
		ec.setName( cname );
		ec.setDesc( cdesc );
		
		Element questions = (Element)catalog.getElementsByTagName( "questions" ).item(0);
		NodeList qnl = questions.getChildNodes();
		for( int j=0; j<qnl.getLength(); j++ ) {
			Examination.Question eq = parseQuestion( (Element)qnl.item(j) );
			ec.getQuestions().add(eq);
		}
		
		return ec;
    }
    
    private Examination.Question parseQuestion(Element question){
    	String index = question.getElementsByTagName("index").item(0).getTextContent();
		String name =  question.getElementsByTagName("name").item(0).getTextContent();
		String questionid = question.getElementsByTagName("questionid").item(0).getTextContent();
		String type = question.getElementsByTagName("type").item(0).getTextContent();			
		String score = question.getElementsByTagName("score").item(0).getTextContent();
		String content = question.getElementsByTagName("content").item(0).getTextContent();
		
		Examination.Question eq = new Examination.Question();
		eq.setIndex( Integer.valueOf( index ) );
		eq.setName( name );
		eq.setId( questionid );
		eq.setType( QUESTION_TYPE.values()[ Integer.valueOf(type) -1 ] );
		eq.setScore( Float.valueOf( score ) );
		eq.setContent( content );
		
		NodeList choicesE = question.getElementsByTagName( "choices" );
		if( choicesE.getLength()==1 ) {
			Element choices = (Element)choicesE.item( 0 );
			NodeList nl = choices.getChildNodes();
			for( int i=0; i<nl.getLength(); i++ ) {
				Element choice = (Element)nl.item(i);
				Examination.Choice ec = parseChoice(choice);
				eq.getChoices().add( ec );
			}
		}
		return eq;
    }
    
    private Examination.Choice parseChoice(Element choice){
    	String index = choice.getElementsByTagName( "index" ).item(0).getTextContent();
		String label = choice.getElementsByTagName( "label" ).item(0).getTextContent();
		String content = choice.getElementsByTagName( "content" ).item(0).getTextContent();
		
		Examination.Choice ec = new Examination.Choice();
		ec.setIndex( Integer.valueOf( index ) );
		ec.setLabel( label );
		ec.setActualLabel( label );
		ec.setContent( content );
		
		return ec;
    }
    
    private void confuseCatalog( Examination.Catalog ec ) {
    	List<Question> qs = ec.getQuestions();
    	for( Question q : qs ) {
			if( q.getType()==QUESTION_TYPE.SINGLE_CHOICE || q.getType()==QUESTION_TYPE.MULTIPLE_CHOICE ) {
				List<Choice> choices = q.getChoices();
				Random r = new Random();
				for( int i = choices.size()-1; i>0; i--) {
					int si = r.nextInt( i+1 );
					if( i != si ) {
						Choice oc = choices.get(i);
						Choice nc = choices.get(si);
						
						String tmp;
						tmp = oc.getActualLabel();
						oc.setActualLabel( nc.getActualLabel() ); 
						nc.setActualLabel( tmp );
						
						tmp = oc.getContent();
						oc.setContent( nc.getContent() );
						nc.setContent( tmp );
					}
				}
			}
		}
    	
    	Random r = new Random();
		for( int i = qs.size()-1; i>0; i--) {
			int si = r.nextInt( i+1 );
			if( i != si ) {
				Question oq = qs.get(i);
				Question nc = qs.get(si);
				qs.set( i, nc );
				qs.set( si, oq );
			}
		}
    }
    
    public SubmitSuccessResult parseSubmitResult( Element root ) {
    	SubmitSuccessResult result = new SubmitSuccessResult();
    	
    	String examinationid = root.getElementsByTagName( "examinationid" ).item(0).getTextContent();
    	String status = root.getElementsByTagName( "status" ).item(0).getTextContent();
    	String score = root.getElementsByTagName( "score" ).item(0).getTextContent();
    	
    	result.setExamId(examinationid);
    	result.setStatus(status);
    	result.setScore(Double.valueOf(score));
    	
    	return result;
    }
    
    public static void main(String[] args){
    	Examination.Catalog ec = new Examination.Catalog();
    	
    	Question q = new Question();
    	q.setIndex(1);
    	q.setName("test1");
    	q.setType( QUESTION_TYPE.SINGLE_CHOICE );
    	
    	Question q2 = new Question();
    	q2.setIndex(2);
    	q2.setName("test2");
    	q2.setType( QUESTION_TYPE.SINGLE_CHOICE );
    	
    	Question q3 = new Question();
    	q3.setIndex(3);
    	q3.setName("test3");
    	q3.setType( QUESTION_TYPE.SINGLE_CHOICE );
    	
    	Question q4 = new Question();
    	q4.setIndex(4);
    	q4.setName("test4");
    	q4.setType( QUESTION_TYPE.SINGLE_CHOICE );
    	
    	Choice c1 = new Choice();
    	c1.setIndex(1);
    	c1.setActualLabel( "A" );
    	c1.setLabel("A");
    	c1.setContent("AC");
    	
    	Choice c2 = new Choice();
    	c2.setIndex(2);
    	c2.setActualLabel( "B" );
    	c2.setLabel("B");
    	c2.setContent("BC");
    	
    	Choice c3 = new Choice();
    	c3.setIndex(3);
    	c3.setActualLabel( "C" );
    	c3.setLabel("C");
    	c3.setContent("CC");
    	
    	Choice c4 = new Choice();
    	c4.setIndex(4);
    	c4.setActualLabel( "D" );
    	c4.setLabel("D");
    	c4.setContent("DC");
    			
    	q.getChoices().add( c1 );
    	q.getChoices().add( c2 );
    	q.getChoices().add( c3 );
    	q.getChoices().add( c4 );
    	
    	c1 = new Choice();
    	c1.setIndex(1);
    	c1.setActualLabel( "A" );
    	c1.setLabel("A");
    	c1.setContent("AC");
    	
    	c2 = new Choice();
    	c2.setIndex(2);
    	c2.setActualLabel( "B" );
    	c2.setLabel("B");
    	c2.setContent("BC");
    	
    	c3 = new Choice();
    	c3.setIndex(3);
    	c3.setActualLabel( "C" );
    	c3.setLabel("C");
    	c3.setContent("CC");
    	
    	c4 = new Choice();
    	c4.setIndex(4);
    	c4.setActualLabel( "D" );
    	c4.setLabel("D");
    	c4.setContent("DC");
    	
    	q2.getChoices().add( c1 );
    	q2.getChoices().add( c2 );
    	q2.getChoices().add( c3 );
    	q2.getChoices().add( c4 );
    	
    	c1 = new Choice();
    	c1.setIndex(1);
    	c1.setActualLabel( "A" );
    	c1.setLabel("A");
    	c1.setContent("AC");
    	
    	c2 = new Choice();
    	c2.setIndex(2);
    	c2.setActualLabel( "B" );
    	c2.setLabel("B");
    	c2.setContent("BC");
    	
    	c3 = new Choice();
    	c3.setIndex(3);
    	c3.setActualLabel( "C" );
    	c3.setLabel("C");
    	c3.setContent("CC");
    	
    	c4 = new Choice();
    	c4.setIndex(4);
    	c4.setActualLabel( "D" );
    	c4.setLabel("D");
    	c4.setContent("DC");
    	
    	q3.getChoices().add( c1 );
    	q3.getChoices().add( c2 );
    	q3.getChoices().add( c3 );
    	q3.getChoices().add( c4 );
    	
    	c1 = new Choice();
    	c1.setIndex(1);
    	c1.setActualLabel( "A" );
    	c1.setLabel("A");
    	c1.setContent("AC");
    	
    	c2 = new Choice();
    	c2.setIndex(2);
    	c2.setActualLabel( "B" );
    	c2.setLabel("B");
    	c2.setContent("BC");
    	
    	c3 = new Choice();
    	c3.setIndex(3);
    	c3.setActualLabel( "C" );
    	c3.setLabel("C");
    	c3.setContent("CC");
    	
    	c4 = new Choice();
    	c4.setIndex(4);
    	c4.setActualLabel( "D" );
    	c4.setLabel("D");
    	c4.setContent("DC");
    	
    	q4.getChoices().add( c1 );
    	q4.getChoices().add( c2 );
    	q4.getChoices().add( c3 );
    	q4.getChoices().add( c4 );
    	
    	ec.getQuestions().add( q );
    	ec.getQuestions().add( q2 );
    	ec.getQuestions().add( q3 );
    	ec.getQuestions().add( q4 );
    	
    	System.out.println( ec.getQuestions().get(0) );
    	System.out.println( ec.getQuestions().get(1) );
    	System.out.println( ec.getQuestions().get(2) );
    	System.out.println( ec.getQuestions().get(3) );
    	new Message2ModelTransferImpl().confuseCatalog( ec );
    	System.out.println( "--------------------------------------------------------" );
    	System.out.println( ec.getQuestions().get(0) );
    	System.out.println( ec.getQuestions().get(1) );
    	System.out.println( ec.getQuestions().get(2) );
    	System.out.println( ec.getQuestions().get(3) );
    }	
}
