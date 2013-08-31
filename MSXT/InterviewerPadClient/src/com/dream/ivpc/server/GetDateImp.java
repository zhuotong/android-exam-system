package com.dream.ivpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class GetDateImp implements GetData {

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
//			os.write( ("loginName="+urls[0]+"&loginPassword=" + urls[1]).getBytes("utf-8") );
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
	public void login() {
		// TODO Auto-generated method stub
	}

	@Override
	public void getCandidateList() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCandidateDetail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCandidateResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCandiateExamRpt() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCandiateIntervew() {
		// TODO Auto-generated method stub

	}

}
