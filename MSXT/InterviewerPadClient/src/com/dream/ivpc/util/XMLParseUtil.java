package com.dream.ivpc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import android.util.Xml;
import com.dream.ivpc.model.ExamRptBean;
import com.dream.ivpc.model.ExamRptPageBean;
import com.dream.ivpc.model.ResumeBean;
import com.dream.ivpc.model.ResumePageBean;

public class XMLParseUtil {
	public final static String LOG_TAG = "DataParseUtil";

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
