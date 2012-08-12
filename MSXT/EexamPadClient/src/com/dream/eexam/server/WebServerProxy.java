package com.dream.eexam.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class WebServerProxy implements ServerProxy{
	private String conversationId;
	private String server;
	private int port;
	
	private static final String LOGIN_URI = "/msxt/runinterview/loginAction/login";
	private static final String GET_EXAM_URI = "/msxt/runinterview/examAction/getExam";
	private static final String SUBMIT_ANSWER_URI = "/msxt/runinterview/examAction/submitAnswer";
	
	public WebServerProxy(String server, int port){
		this.server = server;
		this.port = port;
	}
	
	@Override
	public Result login(String loginName, String loginPassword) {
		Result result = new Result();
		
		HttpURLConnection conn = null;
    	BufferedReader br = null;
        try {
			URL loginURL = new URL("http://" + server + ":" + port + LOGIN_URI);
			conn = (HttpURLConnection)loginURL.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod( "POST" );
			conn.connect();
			
			OutputStream os = conn.getOutputStream();
			os.write( ("loginName="+loginName+"&loginPassword=" + loginPassword).getBytes("utf-8") );
			os.close();
			
			br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );  
            String response = "";  
            String readLine = null;  
            while( (readLine =br.readLine() ) != null ) 
                response = response + readLine;  
            
            result.setStatus( STATUS.SUCCESS );
            result.setSuccessMessage( response );
            
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus( STATUS.ERROR );
			result.setErrorMessage( "不能连接到服务器" );
		} finally {
			if( br!=null )
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if( conn!=null )
	            conn.disconnect();
		}
        return result;
	}

	@Override
	public Result getExam(String examId) {
		Result result = new Result();
		
		HttpURLConnection conn = null;
    	BufferedReader br = null;
        try {
			URL loginURL = new URL("http://" + server + ":" + port + GET_EXAM_URI);
			conn = (HttpURLConnection)loginURL.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod( "POST" );
			conn.connect();
			
			OutputStream os = conn.getOutputStream();
			os.write( ("conversation="+conversationId+"&examId=" + examId).getBytes("utf-8") );
			os.close();
			
			br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );  
            String response = "";  
            String readLine = null;  
            while( (readLine =br.readLine() ) != null ) 
                response = response + readLine;  
            
            result.setStatus( STATUS.SUCCESS );
            result.setSuccessMessage( response );
            
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus( STATUS.ERROR );
			result.setErrorMessage( "不能连接到服务器" );
		} finally {
			if( br!=null )
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if( conn!=null )
	            conn.disconnect();
		}
        return result;
	}

	@Override
	public Result submitAnswer(String examinationid, Map<String, String> answers) {
		StringBuffer sb = new StringBuffer( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" );
		sb.append( "<examanswer>" );
		sb.append( "<conversation>" ).append( conversationId ).append( "</conversation>" );
		sb.append( "<examinationid>" ).append( examinationid ).append( "</examinationid>" );
		sb.append( "<answers>" );
		for( Map.Entry<String, String> en : answers.entrySet() ) {
			sb.append( "<answer>" );
			sb.append( "<questionid>" ).append( en.getKey() ).append( "</questionid>" );
			sb.append( "<content><![CDATA[" ).append( en.getValue() ).append( "]]></content>" ); 
			sb.append( "</answer>" );
		}
		sb.append( "</answers>" ); 
		sb.append( "</examanswer>" ); 
		
		Result result = new Result();
		
		HttpURLConnection conn = null;
    	BufferedReader br = null;
        try {
			URL loginURL = new URL("http://" + server + ":" + port + SUBMIT_ANSWER_URI);
			conn = (HttpURLConnection)loginURL.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("POST");
			conn.connect();
			
			OutputStream os = conn.getOutputStream();
			os.write( sb.toString().getBytes("utf-8") );
			os.close();
			
			br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );  
            String response = "";  
            String readLine = null;  
            while( (readLine =br.readLine() ) != null ) 
                response = response + readLine;  
            
            result.setStatus( STATUS.SUCCESS );
            result.setSuccessMessage( response );
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus( STATUS.ERROR );
			result.setErrorMessage( "不能连接到服务器" );
		} finally {
			if( br!=null )
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if( conn!=null )
	            conn.disconnect();
		}
        return result;
	}

	@Override
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
}
