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
	
	public static String getCurrentDate(){
		Date date = new Date();
		return String.valueOf(date.getTime());
	}
	

	public static long getTimeInterval(long time1, long time2){
		return (time1 - time2)/1000;
	}
	
	public static String getRemainingTime(long leftTime){
    	Integer lMinutes = Integer.valueOf((int)(leftTime/60));
    	Integer lSeconds = Integer.valueOf((int)(leftTime - lMinutes * 60));
    	String lMinutesStr = lMinutes>=10 ? String.valueOf(lMinutes):("0"+String.valueOf(lMinutes));
    	String lSecondsStr = lSeconds>=10 ? String.valueOf(lSeconds):("0"+String.valueOf(lSeconds));
    	return lMinutesStr+":"+lSecondsStr;
	}
	


}
