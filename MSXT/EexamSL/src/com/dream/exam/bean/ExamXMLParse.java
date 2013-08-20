package com.dream.exam.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import android.util.Xml;

public class ExamXMLParse {

	public final static String LOG_TAG = "ExamXMLParse";
	
	public static Exam parseExam(InputStream is) {
		Exam exam = null;
		String objCursor = null;
		
		//exam fields
		List<Catalog> catalogs = null;
		
		//catalog fields
		Catalog catalog = null;
		List<Question> questions = null;
		
		//question fields
		Question question = null;
		List<Choice> choices = null;
		
		//choice fields
		Choice choice = null;
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				
				String tagName = parser.getName();
				
				if(eventType == XmlPullParser.START_TAG){
					Log.i(LOG_TAG, "start tag:"+tagName);
					
					if (tagName.equals("examination")) {
						exam = new Exam();
						objCursor = "exam";//cursor to mark current object
						continue;
					}else if("exam".equalsIgnoreCase(objCursor) && exam!=null ){
						if (tagName.equals("examinationid")) {
							exam.setExamId(parser.nextText());
						}else if (tagName.equals("name") ) {
							exam.setName(parser.nextText());
						}else if (tagName.equals("time") ) {
							exam.setTime(parser.nextText());
						}else if (tagName.equals("confuse") ) {
							if("true".equalsIgnoreCase(parser.nextText())){
								exam.setConfuse(true);
							}else{
								exam.setConfuse(false);
							}
						}else if (tagName.equals("catalogs")) {
							catalogs = new ArrayList<Catalog>();
						}
					}
					 
					if (tagName.equals("catalog") ) {
						catalog = new Catalog();
						objCursor = "catalog";
					}else if("catalog".equalsIgnoreCase(objCursor) && catalog!=null){
						if (tagName.equals("index")) {
						    catalog.setIndex(Integer.valueOf(parser.nextText()));
						}else if (tagName.equals("name") ) {
							catalog.setName(parser.nextText());
						}else if (tagName.equals("catalogdesc") ) {
							catalog.setDesc(parser.nextText());
						}else if (tagName.equals("questions") ) {
							questions = new ArrayList<Question>();
						}
					}
					
					if (tagName.equals("question") ) {
						question = new Question();
						objCursor = "question";
					}else if("question".equalsIgnoreCase(objCursor) && question!=null){
						if (tagName.equals("index")) {
							question.setIndex(Integer.valueOf(parser.nextText()));
						}else if (tagName.equals("name") ) {
							question.setName(parser.nextText());
						}else if (tagName.equals("questionid") ) {
							question.setQuestionid(parser.nextText());
						}else if (tagName.equals("type") ) {
							question.setType(Integer.valueOf(parser.nextText()));
						}else if (tagName.equals("score") ) {
							question.setScore(Integer.valueOf(parser.nextText()));
						}else if (tagName.equals("content") ) {
							question.setContent(parser.nextText());
						}else if (tagName.equals("choices") ) {
							choices = new ArrayList<Choice>();
						}
					}
					
					if (tagName.equals("choice") ) {
						choice = new Choice();
						objCursor = "choice";
					}else if("choice".equalsIgnoreCase(objCursor) || choice!=null){
						if (tagName.equals("index")) {
							choice.setIndex(Integer.valueOf(parser.nextText()));
						}else if (tagName.equals("label") ) {
							choice.setLabel(parser.nextText());
						}else if (tagName.equals("content") ) {
							choice.setContent(parser.nextText());
						}
					}					
				}
				
				if(eventType == XmlPullParser.END_TAG){
					
					if (tagName.equals("choice") && choices!=null) {
						choices.add(choice);
						choice = null;
					}else if (tagName.equals("choices") && question!=null) {
						question.setChoices(choices);
						objCursor = "question";
					}else if (tagName.equals("question") && questions!=null) {
						questions.add(question);
						question = null;
					}else if (tagName.equals("questions") && catalog!=null) {
						catalog.setQuestions(questions);
						objCursor = "catalog";
					}else if (tagName.equals("catalog")) {
						catalogs.add(catalog);
						catalog = null;
					}else if (tagName.equals("catalogs")) {
						exam.setCatalogs(catalogs);
						objCursor = "exam";
					}else if (tagName.equals("examination")) {
						
					}
				}
			}
		} catch (XmlPullParserException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (NumberFormatException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		return exam;
	}
}
