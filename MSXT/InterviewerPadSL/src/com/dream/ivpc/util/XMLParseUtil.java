package com.dream.ivpc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import android.util.Xml;

import com.dream.ivpc.model.CandiateBean;
import com.dream.ivpc.model.ExamBean;
import com.dream.ivpc.model.ExamRptBean;
import com.dream.ivpc.model.ExamRptPageBean;
import com.dream.ivpc.model.LoginResult;
import com.dream.ivpc.model.ResumeBean;
import com.dream.ivpc.model.ResumePageBean;

public class XMLParseUtil {
	public final static String LOG_TAG = "XMLParseUtil";

	public static LoginResult parseLoginResult(InputStream is) {
		LoginResult bean = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("result")) {
						bean = new LoginResult();
					} else if (parser.getName().equals("success") && bean!=null ) {
						bean.setSuccess(true);
					} else if (parser.getName().equals("user_id") && bean!=null) {
						bean.setUserId(parser.nextText());
					} else if (parser.getName().equals("name") && bean!=null) {
						bean.setUserName(parser.nextText());
					} else if (parser.getName().equals("token") && bean!=null) {
						bean.setToken(parser.nextText());
					} else if (parser.getName().equals("failure") && bean!=null) {
						bean.setSuccess(false);
					} else if (parser.getName().equals("error_code") && bean!=null) {
						bean.setError_code(parser.nextText());
					} else if (parser.getName().equals("error_desc") && bean!=null) {
						bean.setError_desc(parser.nextText());
					} 
					break;
				case XmlPullParser.END_TAG:
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

		return bean;
	}
	
	public static List<CandiateBean> parseCandidates(InputStream is) {
		List<CandiateBean> beanList = new ArrayList<CandiateBean>();
		CandiateBean bean = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("candidate")) {
						bean = new CandiateBean();
					} else if (parser.getName().equals("time") && bean!=null ) {
						bean.setTime(parser.nextText());
					} else if (parser.getName().equals("position") && bean!=null) {
						bean.setPosition(parser.nextText());
					} else if (parser.getName().equals("name") && bean!=null ) {
						bean.setName(parser.nextText());
					} else if (parser.getName().equals("phase") && bean!=null ) {
						bean.setPhase(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("candidate")) {
						beanList.add(bean);
						bean = null;
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

		return beanList;
	}
	
	public static ResumeBean parseResume(InputStream is) {
		ResumeBean resumeBean = new ResumeBean();
		List<ResumePageBean> beanList = null;
		ResumePageBean bean = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("pages")) {
						beanList = new ArrayList<ResumePageBean>();
					} else if (parser.getName().equals("size")) {
						resumeBean.setSize(Integer.valueOf(parser.nextText()));
					} else if (parser.getName().equals("page")) {
						bean = new ResumePageBean();
					} else if (parser.getName().equals("index")) {
						bean.setIndex(Integer.valueOf(parser.nextText()));
					} else if (parser.getName().equals("content")) {
						bean.setContent(new StringBuffer(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("page")) {
						beanList.add(bean);
					}else if(parser.getName().equals("pages")){
						resumeBean.setRpbList(beanList);
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

		return resumeBean;
	}

	public static List<ExamBean> parseExams(InputStream is) {
		List<ExamBean> beanList = new ArrayList<ExamBean>();
		ExamBean bean = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("exam")) {
						bean = new ExamBean();
					} else if (parser.getName().equals("examid") && bean!=null ) {
						bean.setExamId(parser.nextText());
					} else if (parser.getName().equals("examname") && bean!=null) {
						bean.setExamName(parser.nextText());
					} 
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("exam")) {
						beanList.add(bean);
						bean = null;
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

		return beanList;
	}
	
	public static ExamRptBean parseExamRpt(InputStream is) {
		ExamRptBean rptBean = new ExamRptBean();
		List<ExamRptPageBean> pageList = null;
		ExamRptPageBean pagebean = null;
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("pages")) {
						pageList = new ArrayList<ExamRptPageBean>();
					} else if (parser.getName().equals("size")) {
						rptBean.setPageSize(Integer.valueOf(parser.nextText()));
					} else if (parser.getName().equals("page")) {
						pagebean = new ExamRptPageBean();
					} else if (parser.getName().equals("index")) {
						pagebean.setIndex(Integer.valueOf(parser.nextText()));
					} else if (parser.getName().equals("content")) {
						pagebean.setContent(new StringBuilder(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("page")) {
						pageList.add(pagebean);
					}else if(parser.getName().equals("pages")){
						rptBean.setPageList(pageList);
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
		return rptBean;
	}
}
