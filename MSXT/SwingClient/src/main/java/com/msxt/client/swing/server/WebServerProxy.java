package com.msxt.client.swing.server;

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
		return null;
	}

	@Override
	public Result submitAnswer(String examinationid, Map<String, String> answers) {
		return null;
	}

	@Override
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
}
