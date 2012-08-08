package com.msxt.interview.runtime;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/runinterview/*")
public class RunningServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private LoginAction loginAction;
	
	@Inject
	private ExamAction examAction;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest( req, resp );
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest( req, resp );
	}
	
	private void processRequest( HttpServletRequest request, HttpServletResponse response ) throws IOException{
		response.setContentType("text/xml;charset=utf-8");
		
		String url = request.getRequestURI();
		url = url.substring( url.indexOf( "runinterview/" ) + "runinterview/".length() );
		
		int slashIndex = url.indexOf('/');
		int endIndex = url.indexOf('?');
		
		String action = url.substring(0, slashIndex);
		String method = endIndex==-1 ? url.substring( slashIndex + 1 ) : url.substring( slashIndex + 1, endIndex );
		
		String result = ""; 
		if( "loginAction".equals(action) && "login".equals( method ) ) 			
			result = loginAction.login( request );
		if( "examAction".equals(action) ) {
			try {
				Method m = examAction.getClass().getMethod( method, HttpServletRequest.class );
				result = (String)m.invoke(examAction, request );
			} catch (Exception e) {
				e.printStackTrace();
				result = "System Exception";
			}
		}
		response.getOutputStream().print( result );
	}
}
