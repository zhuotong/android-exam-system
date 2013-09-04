package com.dream.ivpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import android.util.Xml;

import com.dream.ivpc.bean.CandidateBean;
import com.dream.ivpc.bean.LoginResultBean;
import com.dream.ivpc.bean.PendRoundBean;
import com.dream.ivpc.bean.Round;
import com.dream.ivpc.bean.XMLBean;

public class GetDateImp implements GetData {
	
	public final static String LOG_TAG = "GetDateImp";

	private static final String SERVER = "192.168.1.105";
	private static final String PORT = "8080";
	private static final String LOGIN_URI = "/msxt2/RequestDispatchServlet/interviewRunAction/interviewerLogin";
	
	
	public InputStream getStream(Map<String,String> params){
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		URL loginURL;
		try {
			loginURL = new URL("http://" + SERVER + ":" + PORT + LOGIN_URI);
			conn = (HttpURLConnection)loginURL.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod( "POST" );
			conn.connect();
			OutputStream os = conn.getOutputStream();
			
			StringBuilder paramsSB = new StringBuilder();
			for (Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				System.out.println("key=" + key + " value=" + value);
				if(paramsSB.length() == 0){
					paramsSB.append(key+"="+value);
				}else{
					paramsSB.append("&"+key+"="+value);
				}
			}
//			os.write( ("loginName="+urls[0]+"&loginPassword=" + urls[1]).getBytes("utf-8") );
			os.write( paramsSB.toString().getBytes("utf-8") );
			os.close();
			inputStream = conn.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}


	@Override
	public LoginResultBean login(InputStream is) {
		LoginResultBean bean = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("result")) {
						bean = new LoginResultBean();
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


	@Override
	public List<PendRoundBean> getRoundList(InputStream is) {
		List<PendRoundBean> beanList = new ArrayList<PendRoundBean>();
		PendRoundBean bean = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("rounds")) {
						beanList = new ArrayList<PendRoundBean>();
					} else if (parser.getName().equals("round")) {
						bean = new PendRoundBean();
					} else if (parser.getName().equals("id") && bean!=null ) {
						bean.setrId(parser.nextText());
					} else if (parser.getName().equals("plan_time") && bean!=null) {
						bean.setrTime(parser.nextText());
					} else if (parser.getName().equals("name") && bean!=null) {
						bean.setrName(parser.nextText());
					} else if (parser.getName().equals("candidate") && bean!=null) {
						bean.setrCandidate(parser.nextText());
					} else if (parser.getName().equals("apply_position") && bean!=null) {
						bean.setrAppPosition(parser.nextText());
					} 
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("round")) {
						beanList.add(bean);
						bean = null;
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

		return beanList;
	}


	@Override
	public CandidateBean getCandidateDetail(InputStream is) {
		CandidateBean bean = null;
		Round currRound = null;
		List<Round> doneRounds = null;
		Round doneRound = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("success")) {
						bean = new CandidateBean();
					} else if (parser.getName().equals("apply_position") && bean!=null ) {
						bean.setCandidatePosition(parser.nextText());
					} else if (parser.getName().equals("curr_round") && bean!=null) {
						currRound = new Round();
						currRound.setCompFlag(false);
					} else if (parser.getName().equals("id") && bean!=null && doneRound ==null && currRound!=null) {
						currRound.setId(parser.nextText());
					} else if (parser.getName().equals("type") && bean!=null && doneRound ==null && currRound!=null) {
						currRound.setType(parser.nextText());
					} else if (parser.getName().equals("name") && bean!=null && doneRound ==null && currRound!=null) {
						currRound.setName(parser.nextText());
					} else if (parser.getName().equals("plan_time") && bean!=null && doneRound ==null && currRound!=null ) {
						currRound.setPlanTime(parser.nextText());
					} else if (parser.getName().equals("done_rounds") && bean!=null) {
						doneRounds = new ArrayList<Round>();
					} else if (parser.getName().equals("done_round") && bean!=null && doneRounds!=null ) {
						doneRound = new Round();
						doneRound.setCompFlag(true);
					} else if (parser.getName().equals("id") && bean!=null && doneRound!=null) {
						doneRound.setId(parser.nextText());
					} else if (parser.getName().equals("type") && bean!=null && doneRound!=null) {
						doneRound.setType(parser.nextText());
					} else if (parser.getName().equals("name") && bean!=null && doneRound!=null) {
						doneRound.setName(parser.nextText());
					} else if (parser.getName().equals("done_time") && bean!=null && doneRound!=null) {
						doneRound.setDoneTime(parser.nextText());
					} 
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("done_round")) {
						doneRounds.add(doneRound);
						doneRound = null;
						break;
					}else if (parser.getName().equals("done_rounds")) {
						bean.setDoneRounds(doneRounds);
						doneRounds = null;
						break;
					}else if (parser.getName().equals("success")) {
						break;
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

		return bean;
	}


	@Override
	public XMLBean getCandidateResume(InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public XMLBean getCandiateExamRpt(InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public XMLBean getCandiateIntervew(InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}
	
	


}
