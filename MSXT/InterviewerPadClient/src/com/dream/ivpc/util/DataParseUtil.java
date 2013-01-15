package com.dream.ivpc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;
import com.dream.ivpc.model.ResumePicBean;

public class DataParseUtil {

	public static List<ResumePicBean> parseResume(InputStream is) {
		List<ResumePicBean> beanList = null;
		ResumePicBean bean = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("pages")) {
						beanList = new ArrayList<ResumePicBean>();
					} else if (parser.getName().equals("page")) {
						bean = new ResumePicBean();
					} else if (parser.getName().equals("index")) {
						bean.setIndex(Integer.valueOf(parser.nextText()));
					} else if (parser.getName().equals("content")) {
						bean.setContent(new StringBuffer(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("pages")) {
						beanList.add(bean);
					}
					break;
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beanList;
	}
}
