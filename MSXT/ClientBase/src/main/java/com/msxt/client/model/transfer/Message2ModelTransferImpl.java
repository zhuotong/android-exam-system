package com.msxt.client.model.transfer;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.msxt.client.model.Examination;
import com.msxt.client.model.LoginSuccessResult;

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
		eq.setType( type );
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
		ec.setContent( content );
		
		return ec;
    }
}
