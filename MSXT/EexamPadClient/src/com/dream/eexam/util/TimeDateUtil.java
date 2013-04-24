package com.dream.eexam.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateUtil {
	public final static String TIME_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

	public static String getCurrentTimeStr(){
		DateFormat formatter = new SimpleDateFormat(TIME_FORMAT_1);
        String time = formatter.format(new Date());
        return time;
	}
	
	public static String transferTime2Str(long time){
		DateFormat df = new SimpleDateFormat(TIME_FORMAT_1);
        return df.format(time);
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
