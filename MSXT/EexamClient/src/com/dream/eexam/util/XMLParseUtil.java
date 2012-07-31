package com.dream.eexam.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

import com.dream.eexam.model.CatalogBean;
import com.dream.eexam.model.Choice;
import com.dream.eexam.model.ExamBaseBean;
import com.dream.eexam.model.ExamDetailBean;
import com.dream.eexam.model.InterviewBean;
import com.dream.eexam.model.LoginResultBean;
import com.dream.eexam.model.PaperBean;
import com.dream.eexam.model.Question;

import android.util.Log;
import android.util.Xml;

public class XMLParseUtil {

	public final static String LOG_TAG = "XMLParseUtil";
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
    public static String readLoginResultStatus(InputStream inputStream) throws Exception{
		Log.i(LOG_TAG, "readLoginResult()...");
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();
		String status = null;
		while (eventCode != XmlPullParser.END_DOCUMENT && status==null ) {
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					break;
				}
				case XmlPullParser.START_TAG: {
				    if("status".equals(xmlpull.getName()) ){
				    	status = xmlpull.nextText();
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		return status;
	}
    
    /**
     * 
     * @param inputStream
     * @return
     * @throws Exception
     */
	public static LoginResultBean readLoginResult(InputStream inputStream) throws Exception {

		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();

		LoginResultBean bean = null;
		List<ExamBaseBean> examList = null;
		ExamBaseBean examBaseBean = null;
		
		while (eventCode != XmlPullParser.END_DOCUMENT) {
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					bean = new LoginResultBean();
					break;
				}
				case XmlPullParser.START_TAG: {
					if ("status".equals(xmlpull.getName())&& bean!=null) {
						bean.setStatus(xmlpull.nextText());					
					} else if("conversation".equals(xmlpull.getName())&& bean!=null){
						bean.setConversation(xmlpull.nextText());
					} else if("interviewer".equals(xmlpull.getName())&& bean!=null){
						bean.setInterviewer(xmlpull.nextText());
					} else if("jobtitle".equals(xmlpull.getName())&& bean!=null){
						bean.setJobtitle(xmlpull.nextText());
					} else if("examinations".equals(xmlpull.getName())&& bean!=null){
						examList = new ArrayList<ExamBaseBean>();
					} else if("examination".equals(xmlpull.getName())&& examList!=null){
						examBaseBean = new ExamBaseBean();
					} else if("id".equals(xmlpull.getName())&& examBaseBean!=null){
						examBaseBean.setId(xmlpull.nextText());
					} else if("name".equals(xmlpull.getName())&& examBaseBean!=null){
						examBaseBean.setName(xmlpull.nextText());
					} else if("desc".equals(xmlpull.getName())&& examBaseBean!=null){
						examBaseBean.setDesc(xmlpull.nextText());
					} 
					break;
				}
				case XmlPullParser.END_TAG: {
					if ("examination".equals(xmlpull.getName()) && examList != null) {
						examList.add(examBaseBean);
					}else if("examinations".equals(xmlpull.getName()) && examList != null){
						bean.setExamList(examList);
					}
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		return bean;
	}
	
	/**
	 * 
	 * @param inputStream
	 * @param catalogIndex
	 * @param questionIndex
	 * @return
	 * @throws Exception
	 */
	public static Question readQuestion(InputStream inputStream,Integer catalogIndex,Integer questionIndex) throws Exception {
		
		Log.i(LOG_TAG, "readQuestion()... ");
		
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();

		boolean isFoundCatalog = false;
		boolean isFoundCatalogIndex = false;
		boolean isFoundQuestion = false;
		boolean isFoundQuestionIndex = false;
		
		boolean isGetQuestion = false;
		Question question = null;
		List<Choice> choicesList = null;
		Choice choice = null;
		
		Integer lasLineInteger = 1;
		while (eventCode != XmlPullParser.END_DOCUMENT && !isGetQuestion ) {
			Log.i(LOG_TAG, "Line "+String.valueOf(lasLineInteger++));
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					break;
				}
				case XmlPullParser.START_TAG: {
					if("catalog".equals(xmlpull.getName())){
						isFoundCatalog = true;
						break;
					}else if(isFoundCatalog && !isFoundCatalogIndex){
						if("index".equals(xmlpull.getName()) && Integer.valueOf(xmlpull.nextText()) == catalogIndex){
							isFoundCatalogIndex = true;
						}else{
							isFoundCatalog = false;
						}
						break;
					}
					
					if(isFoundCatalogIndex && "question".equals(xmlpull.getName())){
						isFoundQuestion = true;
						break;
					}else if(isFoundQuestion && !isFoundQuestionIndex){
						if("index".equals(xmlpull.getName()) && Integer.valueOf(xmlpull.nextText()) == questionIndex){
							isFoundQuestionIndex = true;
							question = new Question();
						}else{
							isFoundQuestion = false;
						}
						break;
					}
					
					if (question != null) {
						//initial one Question
						if (("index".equals(xmlpull.getName()))) {
							question.setIndex(Integer.valueOf(xmlpull.nextText()));
						} else if ("questionid".equals(xmlpull.getName())) {
							question.setQuestionId(xmlpull.nextText());
						} else if ("type".equals(xmlpull.getName())) {
							question.setQuestionType(xmlpull.nextText());
						} else if ("score".equals(xmlpull.getName())) {
							question.setScore(Integer.valueOf(xmlpull.nextText()));
						} else if ("content".equals(xmlpull.getName())) {
							question.setContent(xmlpull.getName());
						} else if ("choices".equals(xmlpull.getName())) {
							choicesList = new ArrayList<Choice>();
						} else if (choicesList!=null && "choice".equals(xmlpull.getName())) {
							choice = new Choice();
						} else if (choice != null) {
							if (("index".equals(xmlpull.getName()))) {
								choice.setChoiceIndex(Integer.valueOf(xmlpull.nextText()));
							}else if (("label".equals(xmlpull.getName()))) {
								choice.setChoiceLabel(xmlpull.nextText());
							}else if (("content".equals(xmlpull.getName()))) {
								choice.setChoiceContent(xmlpull.nextText());
							}
						}
						break;
					}
				}
				case XmlPullParser.END_TAG: {
					if ("choice".equals(xmlpull.getName()) && choicesList != null) {
						choicesList.add(choice);
					}else if ("choices".equals(xmlpull.getName()) && question != null) {
						question.setChoices(choicesList);
					}else if ("question".equals(xmlpull.getName()) && question != null) {
						isGetQuestion = true;
					}
					break;
				}
			}
			eventCode = xmlpull.next();
			Log.i(LOG_TAG, xmlpull.getName());
		}
		Log.i(LOG_TAG, "readQuestion() end.");
		
		return question;
	}

	/**
	 * 
	 * @param inputStream
	 * @param catalogIndex
	 * @param questionIndex
	 * @return
	 * @throws Exception
	 */
	public static String readQuestionType(InputStream inputStream,Integer catalogIndex,Integer questionIndex) throws Exception{
		Log.i(LOG_TAG, "readQuestionType()...");
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();

		boolean isFoundCatalog = false;
		boolean isFoundCatalogIndex = false;
		boolean isFoundQuestion = false;
		boolean isFoundQuestionIndex = false;
		
		String questiontype = null;
	    Integer i = 1;
		while (eventCode != XmlPullParser.END_DOCUMENT && questiontype==null ) {
			Log.i(LOG_TAG, String.valueOf(i++)+":"+ String.valueOf(isFoundCatalog) +","+ String.valueOf(isFoundCatalogIndex) +","+ String.valueOf(isFoundQuestion) +","+ String.valueOf(isFoundQuestionIndex) );
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					break;
				}
				case XmlPullParser.START_TAG: {
					if("catalog".equals(xmlpull.getName())){
						isFoundCatalog = true;
						break;
					}else if(isFoundCatalog && !isFoundCatalogIndex){
						if("index".equals(xmlpull.getName()) && Integer.valueOf(xmlpull.nextText()) == catalogIndex){
							isFoundCatalogIndex = true;
						}else{
							isFoundCatalog = false;
						}
						break;
					}
					
					if(isFoundCatalogIndex && "question".equals(xmlpull.getName())){
						isFoundQuestion = true;
						break;
					}else if(isFoundQuestion && !isFoundQuestionIndex){
						if("index".equals(xmlpull.getName()) && Integer.valueOf(xmlpull.nextText()) == questionIndex){
							isFoundQuestionIndex = true;
						}else{
							isFoundQuestion = false;
						}
						break;
					}
					
					if(isFoundQuestionIndex && "type".equals(xmlpull.getName())){
						questiontype = xmlpull.nextText();
						break;
					}
					
				}
				case XmlPullParser.END_TAG: {
					break;
				}
			}
			eventCode = xmlpull.next();
			
		}
		return questiontype;
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static ExamDetailBean readExamination(InputStream inputStream) throws Exception{
		Log.i(LOG_TAG, "readExamination()...");
		
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();
		
		ExamDetailBean detailBean = null;
		List<CatalogBean> catalogs = null;
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
					if ("examination".equals(xmlpull.getName())) {//ExamDetailBean initial 
						detailBean = new ExamDetailBean();
					} else if( detailBean!=null){
						if ("examinationid".equals(xmlpull.getName())&& detailBean != null) {//ExamDetailBean set examinationid
							detailBean.setExaminationid(xmlpull.nextText()); 
						} else if ("name".equals(xmlpull.getName())&& detailBean != null) {//ExamDetailBean set name
							detailBean.setName(xmlpull.nextText());
						} else if ("time".equals(xmlpull.getName())&& detailBean != null) {//ExamDetailBean set time
							detailBean.setTime(Integer.valueOf(xmlpull.nextText()));
						} else if ("confuse".equals(xmlpull.getName())&& detailBean != null) {//ExamDetailBean set name
							detailBean.setConfuse(xmlpull.nextText());
						} else if ("catalogs".equals(xmlpull.getName())) {//List catalogs initial 
							catalogs = new ArrayList<CatalogBean>();
						} else if (catalogs!= null){
							if ("catalog".equals(xmlpull.getName())) {//CatalogBean initial 
								catalogBean = new CatalogBean();
							} else if(catalogBean!=null){
								if ("index".equals(xmlpull.getName())) {//CatalogBean set index 
									catalogBean.setIndex(Integer.valueOf(xmlpull.nextText()));
								} else if ("catalogdesc".equals(xmlpull.getName())) {//CatalogBean set catalogdesc 
									catalogBean.setDesc(xmlpull.nextText());
								} else if ("questions".equals(xmlpull.getName())) {//List questions initial 
									questions = new ArrayList<Question>();
								} else if(questions!=null){
								 	if ("question".equals(xmlpull.getName())) {//Question initial 
										question = new Question();
									} else if ("index".equals(xmlpull.getName())&& question != null) {//Question set type
										question.setIndex(Integer.valueOf(xmlpull.nextText()));
									} else if ("questionid".equals(xmlpull.getName())&& question != null) {//Question set name
										question.setQuestionId(xmlpull.nextText());
									} else if ("type".equals(xmlpull.getName())&& question != null) {//Question set name
										question.setQuestionType(xmlpull.nextText());
									} else if ("score".equals(xmlpull.getName())&& question != null) {//Question set name
										question.setScore(Integer.valueOf(xmlpull.nextText()));
									} else if ("content".equals(xmlpull.getName())&& question != null) {//Question set name
										question.setContent(xmlpull.nextText());
									} else if ("choices".equals(xmlpull.getName())) {//List choices initial
										choices = new ArrayList<Choice>();
									} else if(choices!=null){
										if ("choice".equals(xmlpull.getName())) {//Choice initial
											choice = new Choice();
										} else if ("index".equals(xmlpull.getName())&& choice != null) {//Choice set index
											choice.setChoiceIndex(Integer.valueOf(xmlpull.nextText()));
										} else if ("label".equals(xmlpull.getName())&& choice != null) {//Choice set label
											choice.setChoiceLabel(xmlpull.nextText());
										} else if ("content".equals(xmlpull.getName())&& choice != null) {//Choice set content
											choice.setChoiceContent(xmlpull.nextText());
										}
									}
								}
							}
						}
					}
					
					break;
					
				}
				case XmlPullParser.END_TAG: {
					if ("catalogs".equals(xmlpull.getName())) {//Set catalogs
						detailBean.setCatalogs(catalogs);
					} else if ("catalog".equals(xmlpull.getName())) {//Add catalog
						catalogs.add(catalogBean);
					} else if ("questions".equals(xmlpull.getName()) && question != null) {//Set questions
						catalogBean.setQuestions(questions);
					} else if ("question".equals(xmlpull.getName())&& question != null) {//Add question
						questions.add(question);
					} else if ("choices".equals(xmlpull.getName()) && choice != null) {//Set choices
						question.setChoices(choices);
					} else if ("choice".equals(xmlpull.getName()) && choice != null) {//Add choice
						choices.add(choice);
					}
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		
		return detailBean;
	}
}
