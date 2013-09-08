package com.dream.ivpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.dream.ivpc.bean.CandidateBean;
import com.dream.ivpc.bean.LoginResultBean;
import com.dream.ivpc.bean.PendRoundBean;
import com.dream.ivpc.bean.XMLBean;

public class GetDateImp implements GetData {
	
	public final static String LOG_TAG = "GetDateImp";

	private static final String SERVER = "192.168.1.100";
	private static final String PORT = "8080";
	private static final String LOGIN_URI = "msxt2/interviewRunAction/interviewerLogin";
	private static GetData getData = null;
	
	public static GetData getInstance(){
		getData = new GetDateImp();
		return getData;
	}
	
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
	public LoginResultBean login(String userName,String password) {
		Map<String,String> params = new HashMap<String,String>();
		params.put("loginName", userName);
		params.put("loginPassword", password);
		InputStream is = getStream(params);
		return ParseResult.parseLoginResult(is);
	}


	@Override
	public List<PendRoundBean> getRoundList(InputStream is) {
		return null;
	}


	@Override
	public CandidateBean getCandidateDetail(InputStream is) {
		return null;
	}


	@Override
	public XMLBean getCandidateResume(InputStream is) {
		return null;
	}


	@Override
	public XMLBean getCandiateExamRpt(InputStream is) {
		return null;
	}


	@Override
	public XMLBean getCandiateIntervew(InputStream is) {
		return null;
	}
	
	


}
