package com.dream.eexam.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateUtil {
	
	public static String getCurrentTime(){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(new Date());
        return time;
	}
}
