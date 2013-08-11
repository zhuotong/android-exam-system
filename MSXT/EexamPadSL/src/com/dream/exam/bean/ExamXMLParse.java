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
				switch (eventType) {
				case XmlPullParser.START_TAG:
					
					if (parser.getName().equals("examination")) {
						exam = new Exam();
						objCursor = "exam";//cursor to mark current object
					}
					if("exam".equalsIgnoreCase(objCursor) && exam!=null ){
						if (parser.getName().equals("examinationid")) {
							exam.setExamId(parser.nextText());
						}else if (parser.getName().equals("examName") ) {
							exam.setExamId(parser.nextText());
						}else if (parser.getName().equals("time") ) {
							exam.setExamId(parser.nextText());
						}else if (parser.getName().equals("confuse") ) {
							exam.setExamId(parser.nextText());
							if("true".equalsIgnoreCase(parser.nextText())){
								exam.setConfuse(true);
							}else{
								exam.setConfuse(false);
							}
						}else if (parser.getName().equals("catalogs") && exam!=null) {
							catalogs = new ArrayList<Catalog>();
						}
					}
					 
					if (parser.getName().equals("catalog") ) {
						catalog = new Catalog();
						objCursor = "catalog";
					}
					if("catalog".equalsIgnoreCase(objCursor) && catalog!=null){
						if (parser.getName().equals("index")) {
							catalog.setIndex(Integer.valueOf(parser.nextText()));
						}else if (parser.getName().equals("name") ) {
							catalog.setName(parser.nextText());
						}else if (parser.getName().equals("catalogdesc") ) {
							catalog.setDesc(parser.nextText());
						}else if (parser.getName().equals("questions") ) {
							questions = new ArrayList<Question>();
						}
					}
					
					if (parser.getName().equals("question") ) {
						question = new Question();
						objCursor = "question";
					}
					if("question".equalsIgnoreCase(objCursor) && question!=null){
						if (parser.getName().equals("index")) {
							question.setIndex(Integer.valueOf(parser.nextText()));
						}else if (parser.getName().equals("name") ) {
							question.setName(parser.nextText());
						}else if (parser.getName().equals("questionid") ) {
							question.setQuestionid(parser.nextText());
						}else if (parser.getName().equals("type") ) {
							question.setType(Integer.valueOf(parser.nextText()));
						}else if (parser.getName().equals("score") ) {
							question.setScore(Integer.valueOf(parser.nextText()));
						}else if (parser.getName().equals("content") ) {
							question.setContent(parser.nextText());
						}else if (parser.getName().equals("choices") ) {
							choices = new ArrayList<Choice>();
						}
					}
					
					if (parser.getName().equals("choice") ) {
						choice = new Choice();
						objCursor = "choice";
					}
					if("choice".equalsIgnoreCase(objCursor)){
						if (parser.getName().equals("index")) {
							choice.setIndex(Integer.valueOf(parser.nextText()));
						}else if (parser.getName().equals("label") ) {
							choice.setLabel(parser.nextText());
						}else if (parser.getName().equals("content") ) {
							choice.setContent(parser.nextText());
						}
					}
					
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("choice")) {
						choices.add(choice);
						choice = null;
					}else if (parser.getName().equals("choices")) {
						question.setChoices(choices);
						objCursor = "question";
					}else if (parser.getName().equals("question")) {
						questions.add(question);
						question = null;
					}else if (parser.getName().equals("questions")) {
						catalog.setQuestions(questions);
						objCursor = "catalog";
					}else if (parser.getName().equals("catalog")) {
						catalogs.add(catalog);
						catalog = null;
					}else if (parser.getName().equals("catalogs")) {
						exam.setCatalogs(catalogs);
						objCursor = "exam";
					}else if (parser.getName().equals("examination")) {
						break;
					}
					break;
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
