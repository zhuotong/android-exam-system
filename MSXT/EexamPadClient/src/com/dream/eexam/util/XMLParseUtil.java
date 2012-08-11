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
import com.dream.eexam.model.SubmitResultBean;

import android.util.Log;
import android.util.Xml;

public class XMLParseUtil {

	public final static String LOG_TAG = "XMLParseUtil";
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * (When submit login, server will send response xml data) at LoginActivity.java
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
     * (If login success, load login result data) at PapersActivity.java
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
	

	/*public static Question readQuestion(InputStream inputStream,Integer catalogIndex,Integer questionIndex) throws Exception {
		
		Log.i(LOG_TAG, "readQuestion()... ");
		
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		int eventCode = xmlpull.getEventType();
		int level = -1;
		
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
					if(level < 1 && "catalog".equals(xmlpull.getName())){//found catalog
						level = 1;
						break;
					}
					
					if(level == 1 && "index".equals(xmlpull.getName()) ){
						if(Integer.valueOf(xmlpull.nextText()) == catalogIndex){//found catalog and catalogIndex
							level = 2;
						}else{
							level = -1;
						}
						break;
					}
					
					if(level == 2 && "question".equals(xmlpull.getName()) ){//found question
						level = 3;
						break;
					}
					
					if(level == 3 && "index".equals(xmlpull.getName()) ){
						if(Integer.valueOf(xmlpull.nextText()) == questionIndex){//found question and questionIndex
							level = 4;
							question = new Question();
							question.setIndex(questionIndex);
						}else{
							level = 2;
						}
						break;
					}
					
					if(level == 4 ){
						if (("index".equals(xmlpull.getName()))) {
							question.setIndex(Integer.valueOf(xmlpull.nextText()));
						} else if ("questionid".equals(xmlpull.getName())) {
							question.setQuestionId(xmlpull.nextText());
						} else if ("type".equals(xmlpull.getName())) {
							question.setQuestionType(xmlpull.nextText());
						} else if ("score".equals(xmlpull.getName())) {
							question.setScore(Integer.valueOf(xmlpull.nextText()));
						} else if ("content".equals(xmlpull.getName())) {
							question.setContent(xmlpull.nextText());
						} else if ("choices".equals(xmlpull.getName())) {
							choicesList = new ArrayList<Choice>();
							level = 5;
						} 
						break;
					}
					
					if(level == 5 && "choice".equals(xmlpull.getName())){
						choice = new Choice();
						level = 6;
						break;
					}
					
					if(level == 6 && choice != null){
						if (("index".equals(xmlpull.getName()))) {
							choice.setChoiceIndex(Integer.valueOf(xmlpull.nextText()));
						}else if (("label".equals(xmlpull.getName()))) {
							choice.setChoiceLabel(xmlpull.nextText());
						}else if (("content".equals(xmlpull.getName()))) {
							choice.setChoiceContent(xmlpull.nextText());
						}
						break;
					}

					
				}
				case XmlPullParser.END_TAG: {
					if ("choice".equals(xmlpull.getName()) && choicesList != null) {
						choicesList.add(choice);
						level = 5;
					}else if ("choices".equals(xmlpull.getName()) && question != null) {
						question.setChoices(choicesList);
						isGetQuestion = true;
					}
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		Log.i(LOG_TAG, "readQuestion() end.");
		
		return question;
	}*/

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
		
		int level = -1;
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
					if (level <1 && "examination".equals(xmlpull.getName())) {//ExamDetailBean initial 
						detailBean = new ExamDetailBean();
						level = 1;
						break;
					} 
					
					if (level == 1 && detailBean !=null) {//ExamDetailBean initial 
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
							level = 2;
						} 
						break;
					} 
					
					if (level ==2 && "catalog".equals(xmlpull.getName())) {//ExamDetailBean initial 
						catalogBean = new CatalogBean();
						level = 3;
						break;
					} 
					
					if (level ==3 && catalogBean!=null) {//ExamDetailBean initial 
						if ("index".equals(xmlpull.getName())) {//CatalogBean set index 
							catalogBean.setIndex(Integer.valueOf(xmlpull.nextText()));
						} else if ("catalogdesc".equals(xmlpull.getName())) {//CatalogBean set catalogdesc 
							catalogBean.setDesc(xmlpull.nextText());
						} else if ("questions".equals(xmlpull.getName())) {//List questions initial 
							questions = new ArrayList<Question>();
							level = 4;
						}
						break;
					}
					
					if (level ==4 && "question".equals(xmlpull.getName())) {//ExamDetailBean initial 
						question = new Question();
						question.setCatalogIndex(catalogBean.getIndex());
						level = 5;
						break;
					} 
					
					if (level ==5 && question!=null) {//ExamDetailBean initial 
						if ("index".equals(xmlpull.getName())) {//Question set type
							question.setIndex(Integer.valueOf(xmlpull.nextText()));
						} else if ("questionid".equals(xmlpull.getName())) {//Question set name
							question.setQuestionId(xmlpull.nextText());
						} else if ("type".equals(xmlpull.getName())) {//Question set name
							question.setQuestionType(xmlpull.nextText());
						} else if ("score".equals(xmlpull.getName())) {//Question set name
							question.setScore(Integer.valueOf(xmlpull.nextText()));
						} else if ("content".equals(xmlpull.getName())) {//Question set name
							question.setContent(xmlpull.nextText());
						} else if ("choices".equals(xmlpull.getName())) {//List choices initial
							choices = new ArrayList<Choice>();
							level = 6;
						}
						break;
					}
					
					if (level ==6 && "choice".equals(xmlpull.getName())) {//ExamDetailBean initial 
						choice = new Choice();
						level = 7;
						break;
					}
					
					if (level ==7 && choice!=null) {//ExamDetailBean initial 
						if ("index".equals(xmlpull.getName())) {//Choice set index
							choice.setChoiceIndex(Integer.valueOf(xmlpull.nextText()));
						} else if ("label".equals(xmlpull.getName())) {//Choice set label
							choice.setChoiceLabel(xmlpull.nextText());
						} else if ("content".equals(xmlpull.getName())) {//Choice set content
							choice.setChoiceContent(xmlpull.nextText());
						}
						break;
					} 
				}
				case XmlPullParser.END_TAG: {
					
				    if ("choice".equals(xmlpull.getName()) && choices != null) {//Add choice
						choices.add(choice);
						level = 6;
						break;
					}
				    
				    if ("choices".equals(xmlpull.getName()) && question != null) {//Add choice
				    	question.setChoices(choices);
						level = 5;
						break;
					}
				    
				    if ("question".equals(xmlpull.getName()) && questions != null) {//Add choice
				    	questions.add(question);
						level = 4;
						break;
					}
				    
				    if ("questions".equals(xmlpull.getName()) && catalogBean != null) {//Set questions
				    	catalogBean.setQuestions(questions);
				    	level = 3;
				    	break;
					}
				    
				    if ("catalog".equals(xmlpull.getName()) && catalogs != null) {//Set questions
				    	catalogs.add(catalogBean);
				    	level = 2;
				    	break;
					}
				    
				    if ("catalogs".equals(xmlpull.getName()) && detailBean != null) {//Set questions
				    	detailBean.setCatalogs(catalogs);
				    	level = 1;
				    	break;
					}
				    
				}
			}
			eventCode = xmlpull.next();
		}
		
		return detailBean;
	}
	
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * (When submit exam, server will send response xml data about exam result) 
	 */
    public static SubmitResultBean readSubmitResult(InputStream inputStream) throws Exception{
		Log.i(LOG_TAG, "readLoginResult()...");
		XmlPullParser xmlpull = Xml.newPullParser();
		xmlpull.setInput(inputStream, "utf-8");
		
		SubmitResultBean bean = new SubmitResultBean();
		
		String examinationid = null;
		String status = null;
		String score = null;
		String desc = null;
		
		int eventCode = xmlpull.getEventType();
		
		while (eventCode != XmlPullParser.END_DOCUMENT) {
			switch (eventCode) {
				case XmlPullParser.START_DOCUMENT: {
					break;
				}
				case XmlPullParser.START_TAG: {
					if("submitresult".equals(xmlpull.getName()) ){
						bean = new SubmitResultBean();
					}else if("examinationid".equals(xmlpull.getName()) ){
				    	examinationid = xmlpull.nextText();
					}else if("status".equals(xmlpull.getName()) ){
						status = xmlpull.nextText();
					}else if("score".equals(xmlpull.getName()) ){
						score = xmlpull.nextText();
					}else if("desc".equals(xmlpull.getName()) ){
						desc = xmlpull.nextText();
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					break;
				}
			}
			eventCode = xmlpull.next();
		}
		
		if(examinationid!=null) bean.setExaminationid(examinationid);
		if(status!=null) bean.setStatus(status);
		if(score!=null) bean.setScore(score);
		if(desc!=null) bean.setDesc(desc);
		
		return bean;
	}
}
