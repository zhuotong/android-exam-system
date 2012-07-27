package com.msxt.common;

public class HtmlUtil {
	
	public static String transferCommon2HTML( String content ) {
		return content.replaceAll("&", "&amp;")
					  .replaceAll(" ",  "&nbsp;")
					  .replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
					  .replaceAll("<",  "&lt;")
					  .replaceAll(">",  "&gt;")
					  .replaceAll("\n", "<br/>")
					  .replaceAll("\"", "&quot;");
	}
	
}
