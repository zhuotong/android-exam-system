package com.dream.eexam.util;

import java.util.ArrayList;
import java.util.List;

public class XMLUtil {
	public static String getPropertyValue( String xml, String property ){
		int index = xml.indexOf( property+"=" );
	    String value = null;
		if( index>0 ) {
			index = xml.indexOf("\"", index)+1;
			value = xml.substring( index, xml.indexOf("\"", index) );
		}
		
		return value;
	}
	
	public static List<String> getChildren( String xml ) {
		List<String> children = new ArrayList<String>();
		
		int startIndex = xml.indexOf('<');
		int endIndex = xml.indexOf('>', startIndex);
		int ti = xml.indexOf(' ', startIndex);
		if( ti>=0 && ti<endIndex )
			endIndex = ti;
		String parentTagName = xml.substring( startIndex+1, endIndex );
		int exitIndex = xml.indexOf( "</" + parentTagName + ">", startIndex + 1 ); 
		while( true ) { 
			startIndex = xml.indexOf('<', startIndex+1);
			if( startIndex <0 || startIndex >= exitIndex )
				break;
			
			endIndex = xml.indexOf('>', startIndex);
			if( xml.charAt( endIndex-1) != '/') {
				ti = xml.indexOf(' ', startIndex);
				if( ti>=0 && ti<endIndex )
					endIndex = xml.indexOf(' ', startIndex);
				String tagName = xml.substring( startIndex+1, endIndex );
				String endTagName = "</" + tagName + ">";
				endIndex = xml.indexOf( endTagName, endIndex ) + endTagName.length();
			} else {
				endIndex++;
			}
			
			children.add( xml.substring( startIndex, endIndex) );
			startIndex = endIndex-1;
		}
		
		return children;
	}
	
	public static String getElementValue( String element ) {
		int startIndex = element.indexOf('>')+1;
		int endIndex = element.indexOf("</");
		return element.substring( startIndex, endIndex );
	}
}
