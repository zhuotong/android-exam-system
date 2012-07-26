package com.dream.eexam.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

import com.dream.eexam.model.CatalogBean;
import com.dream.eexam.model.Choice;
import com.dream.eexam.model.Paper;
import com.dream.eexam.model.PaperBean;
import com.dream.eexam.model.Question;

import android.util.Log;
import android.util.Xml;

public class XMLParseUtil {

	public final static String LOG_TAG = "XMLParseUtil";
	
	public static List<Paper> readPaperListXmlByPull(InputStream inputStream) throws Exception {
		List<Paper> personList = null;
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();

		Paper paper = null;
		while (eventCode != XmlPullParser.END_DOCUMENT) {
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					personList = new ArrayList<Paper>();
					break;
				}
				case XmlPullParser.START_TAG: {
					if ("paper".equals(xmlpull.getName())) {
						paper = new Paper();
						paper.setIndex(xmlpull.getAttributeValue(0));
					} else if (paper != null) {
						if (("id".equals(xmlpull.getName()))) {
							paper.setId(xmlpull.nextText());
						} else if ("desc".equals(xmlpull.getName())) {
							paper.setDesc(xmlpull.nextText());
						}
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					if ("paper".equals(xmlpull.getName()) && paper != null) {
						personList.add(paper);
						paper = null;
					}
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		return personList;
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static Question readQuestion(InputStream inputStream,Integer catalogIndex,Integer questionIndex) throws Exception {
		
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();

		boolean isFoundCatalog = false;
		boolean isFoundQuestion = false;
		boolean stopRead = false;
		Question question = null;
		List<Choice> choicesList = new ArrayList<Choice>();
		Choice choice = null;
		while (eventCode != XmlPullParser.END_DOCUMENT && !stopRead ) {
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					break;
				}
				case XmlPullParser.START_TAG: {
					if ("catalog".equals(xmlpull.getName())) {
						String cIndex = xmlpull.getAttributeValue(0);
						if(Integer.valueOf(cIndex) == catalogIndex){
							isFoundCatalog = true;
						}
						break;
					}
					if(isFoundCatalog && "question".equals(xmlpull.getName())){
						String qIndex = xmlpull.getAttributeValue(0);
						if(Integer.valueOf(qIndex) == questionIndex){
							isFoundQuestion = true;
							question = new Question();
							question.setIndex(Integer.valueOf(qIndex));
						}
						break;
					}
					if (isFoundQuestion && question != null) {
						if (("name".equals(xmlpull.getName()))) {
							question.setQuestionDesc(xmlpull.nextText());
						} else if ("type".equals(xmlpull.getName())) {
							question.setQuestionType(xmlpull.nextText());
						}
						
						if ("choice".equals(xmlpull.getName())) {
							choice = new Choice();
						}else if (choice != null) {
							if (("index".equals(xmlpull.getName()))) {
								choice.setChoiceIndex(Integer.valueOf(xmlpull.nextText()));
							}else if (("label".equals(xmlpull.getName()))) {
								choice.setChoiceLabel(xmlpull.nextText());
							}else if (("content".equals(xmlpull.getName()))) {
								choice.setChoiceContent(xmlpull.nextText());
							}
							break;
						}
						break;
					}
				}
				case XmlPullParser.END_TAG: {
					if ("choice".equals(xmlpull.getName()) && choice != null) {
						choicesList.add(choice);
					}else if ("question".equals(xmlpull.getName()) && question != null) {
						question.setChoices(choicesList);
						stopRead = true;
					}
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		
		return question;
	}

	public static PaperBean readPaperBean(InputStream inputStream) throws Exception{
		Log.i(LOG_TAG, "readPaperBean()...");
		
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();
		
		PaperBean paperBean = null;
		List<CatalogBean> catalogBeans = null;
		CatalogBean catalogBean = null;
		List<Question> questions = null;
		Question question = null;
		List<Choice> choices = null;
		Choice choice = null;
		
		while (eventCode != XmlPullParser.END_DOCUMENT  ) {
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					break;
				}
				case XmlPullParser.START_TAG: {
					if ("paper".equals(xmlpull.getName())) {
						paperBean = new PaperBean();
					} else if ("desc".equals(xmlpull.getName())&& paperBean != null) {
						paperBean.setDesc(xmlpull.nextText());
					} else if ("time".equals(xmlpull.getName())&& paperBean != null) {
						paperBean.setTime(Integer.valueOf(xmlpull.nextText()));
					} else if ("catalogs".equals(xmlpull.getName())) {
						catalogBeans = new ArrayList<CatalogBean>();
					} else if ("catalog".equals(xmlpull.getName())) {
						catalogBean = new CatalogBean();
						catalogBean.setId(Integer.valueOf(xmlpull.getAttributeValue(0)));
					} else if ("catalogdesc".equals(xmlpull.getName())) {
						catalogBean.setDesc(xmlpull.nextText());
					} else if ("questions".equals(xmlpull.getName())) {
						questions = new ArrayList<Question>();
					} else if ("question".equals(xmlpull.getName())) {
						question = new Question();
						question.setIndex(Integer.valueOf(xmlpull.getAttributeValue(0)));
						choices = new ArrayList<Choice>();
					} else if ("type".equals(xmlpull.getName())&& question != null) {
						question.setQuestionType(xmlpull.nextText());
					} else if ("name".equals(xmlpull.getName())&& question != null) {
						question.setQuestionDesc(xmlpull.nextText());
					} else if ("choice".equals(xmlpull.getName())) {
						choice = new Choice();
					} else if ("index".equals(xmlpull.getName())&& choice != null) {
						choice.setChoiceIndex(Integer.valueOf(xmlpull.nextText()));
					} else if ("label".equals(xmlpull.getName())&& choice != null) {
						choice.setChoiceLabel(xmlpull.nextText());
					} else if ("content".equals(xmlpull.getName())&& choice != null) {
						choice.setChoiceContent(xmlpull.nextText());
					}
//					Log.i(LOG_TAG,xmlpull.getName()+""+xmlpull.nextText());
					break;
					
				}
				case XmlPullParser.END_TAG: {
					if ("catalogs".equals(xmlpull.getName())) {
						paperBean.setCatalogBeans(catalogBeans);
					}else if ("catalog".equals(xmlpull.getName())) {
						catalogBean.setQuestions(questions);
						catalogBeans.add(catalogBean);
					}else if ("question".equals(xmlpull.getName()) && question !=null) {
						question.setChoices(choices);
						questions.add(question);
					} else if ("choice".equals(xmlpull.getName())&& choice!=null) {
						choices.add(choice);
					} 
//					Log.i(LOG_TAG,xmlpull.getName());
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		
		return paperBean;
	}
}
