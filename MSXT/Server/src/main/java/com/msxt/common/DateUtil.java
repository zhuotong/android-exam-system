package com.msxt.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date getTodayStart() {
		Date today = Calendar.getInstance().getTime();
		today.setHours( 0 );
		today.setMinutes( 0 );
		today.setSeconds( 0 );
		return today;
	}
	
	public static Date getTodayEnd() {
		Date today = Calendar.getInstance().getTime();
		today.setHours( 23 );
		today.setMinutes( 59 );
		today.setSeconds( 59 );
		return today;
	}
	
    public static String getStringFromDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(date);
    }
}
