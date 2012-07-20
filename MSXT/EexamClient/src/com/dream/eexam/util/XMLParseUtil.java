package com.dream.eexam.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import com.dream.eexam.model.Paper;
import android.util.Xml;

public class XMLParseUtil {

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
	public static List<Paper> readPaperByPull(InputStream inputStream) throws Exception {
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
	
}
