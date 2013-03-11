package com.dream.eexam.util;

import java.util.Iterator;
import java.util.Map;

import android.content.SharedPreferences;
import android.util.Log;

public class SPUtil {
	public final static String LOG_TAG = "SPUtil";
	
	//login infor key
	public final static String SP_KEY_HOST = "host";
	public final static String SP_KEY_ID = "id";
	public final static String SP_KEY_PWD = "password";
	
	public final static String SP_KEY_USER_HOME = "userhome";
	
	//
	public final static String SP_KEY_LOGIN_FILE_PATH = "loginResultFilePath";
	public final static String SP_KEY_LOGIN_FILE = "loginResultFile";
	
	//exam infor key
	public final static String SP_KEY_EXAM_PATH = "examPath";
	public final static String SP_KEY_EXAM_FILE = "examFile";
	public final static String SP_KEY_EXAM_STATUS = "exam_status";
	public final static String SP_KEY_EXAM_START_TIME = "starttime";
	
	//examStatus value
	public final static String SP_VALUE_EXAM_STATUS_START = "start";
	public final static String SP_VALUE_EXAM_STATUS_END = "end";
	
//	public final static String SP_KEY_EXAM_START_FLAG = "exam_start_flag";
	
	//answer infor key
	public final static String SP_KEY_CCINDEX = "ccIndex";
	public final static String SP_KEY_CQINDEX = "cqIndex";
	

	//save String to SP
	public static void save2SP(String key,String value,SharedPreferences sp){
		SharedPreferences.Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	//save boolean to SP
	public static void save2SP(String key,boolean value,SharedPreferences sp){
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	//save float to SP
	public static void save2SP(String key,float value,SharedPreferences sp){
		SharedPreferences.Editor edit = sp.edit();
		edit.putFloat(key, value);
		edit.commit();
	}
	
	//save int to SP
	public static void save2SP(String key,int value,SharedPreferences sp){
		SharedPreferences.Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	
	//save long to SP
	public static void save2SP(String key,long value,SharedPreferences sp){
		SharedPreferences.Editor edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}
	
	//get by string
	public static String getFromSP(String key,SharedPreferences sp){
		return sp.getString(key, null);
	}
	
	//clear data in SP
	public void clearSP(SharedPreferences sp){
    	SharedPreferences.Editor editor = sp.edit(); 
    	editor.clear();
    	editor.commit();
	}
	
	public static void printAllSPData(SharedPreferences sp){
		Log.i(LOG_TAG,"----------------data in sharedPreferences-----------------");
		
		//print all stored data in sharedPreferences
		Map<String, ?> dataInSP = sp.getAll();
		Iterator it = dataInSP.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			Log.i(LOG_TAG,"key=" + key + " value=" + value);
		}
	}
	
}
