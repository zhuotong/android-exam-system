package com.msxt.test;

import java.util.HashMap;
import java.util.Map;

import com.msxt.client.swing.server.ServerProxy.Result;
import com.msxt.client.swing.server.WebServerProxy;

public class WebServerProxyTest {
	public static void main(String[] args){
		testSubmitAnswer();
	}
	
	private static void testLogin(){
		WebServerProxy p = new WebServerProxy("localhost", 8080);
		Result r = p.login( "test", "test" );
		System.out.println( r.getSuccessMessage() );
		System.out.println( r.getErrorMessage() );
	}
	
	private static void testGetExam(){
		WebServerProxy p = new WebServerProxy("localhost", 8080);
		p.setConversationId( "fb11d0e5-9796-4035-9289-6b15cf3a3494" );
		Result r = p.getExam( "ff8081813904bd7e013904be8f850000" );
		System.out.println( r.getSuccessMessage() );
		System.out.println( r.getErrorMessage() );
	}
	
	private static void testSubmitAnswer(){
		WebServerProxy p = new WebServerProxy("localhost", 8080);
		p.setConversationId( "fb11d0e5-9796-4035-9289-6b15cf3a3494" );
		Map<String, String> a = new HashMap<String, String>();
		a.put("ff80818138cfe61b0138cfeba1520002", "A");
		a.put("ff80818138cfe61b0138cfeba1520003", "B");
		a.put("ff80818138cfe61b0138cfeba1520004", "c");
		a.put("ff80818138cfe61b0138cfeba1520005", "D");
		a.put("ff80818138cfe61b0138cfeba1520006", "A");
		a.put("ff80818138cfe61b0138cfeba1530007", "B");
		a.put("ff80818138cfe61b0138cfeba1530008", "C");
		a.put("ff80818138cfe61b0138cfeba1530009", "D");
		a.put("ff80818138cfe61b0138cfeba153000a", "A");
		a.put("ff80818138cfe61b0138cfeba153000b", "B");
		
		Result r = p.submitAnswer("ff8081813904bd7e013904be8f850000", a);
		System.out.println( r.getSuccessMessage() );
		System.out.println( r.getErrorMessage() );
	}
}
