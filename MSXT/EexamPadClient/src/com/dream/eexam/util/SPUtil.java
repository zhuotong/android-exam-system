package com.dream.eexam.util;

import java.util.Iterator;
import java.util.Map;

import android.content.SharedPreferences;
import android.util.Log;

public class SPUtil {
	public final static String LOG_TAG = "SPUtil";
	
	public final static String CURRENT_USER_ID = "Current_User_Id";
	public final static String CURRENT_USER_PWD = "Current_User_Pwd";
	public final static String CURRENT_USER_HOME = "Current_User_Home";
	public final static String CURRENT_LOGIN_FILE_NAME = "Current_Login_File";
	
	public final static String CURRENT_EXAM_FILE_NAME = "Current_Exam_File";
	public final static String CURRENT_EXAM_STATUS = "Current_Exam_Status";
	public final static String CURRENT_EXAM_START_TIME = "Current_Exam_Start_Time";
	public final static String CURRENT_EXAM_SCORE = "Current_Exam_Score";
	public final static String CURRENT_EXAM_CATALOG = "ccIndex";
	public final static String CURRENT_EXAM_INDEX_IN_CATA = "cqIndex";
	public final static String CURRENT_EXAM_SUBMITTED_IDS = "Current_Exam_Submitted_Ids";
	public final static String CURRENT_EXAM_REMAINING_COUNT = "Current_Exam_Remaining_Count";
	
//	public final static String STATUS_LOGIN_NOT_START = "0";
//	public final static String STATUS_START_NOT_TIMEOUT_NOT_SUBMIT = "1";
//	public final static String STATUS_START_NOT_TIMEOUT_SUBMIT =  "2";
//	public final static String STATUS_START_TIMEOUT_NOT_SUBMIT =  "3";
//	public final static String STATUS_START_TIMEOUT_SUBMIT =  "4";
//	public final static String STATUS_START_SUBMIT_LOCAL =  "5";

	public final static int EXAM_STATUS_NOT_START = 1;
	public final static int EXAM_STATUS_START_GOING = 2;
	public final static int EXAM_STATUS_START_GOING_OBSOLETE =  3;
	public final static int EXAM_STATUS_START_PENDING_NEW =  4;
	public final static int EXAM_STATUS_END =  5;
	
	//login infor key
	public final static String SP_KEY_HOST = "host";
	public final static String SP_KEY_PORT = "port";
	

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
	
	//save String to SP
	public static void append2SP(String key,String value,SharedPreferences sp){
		SharedPreferences.Editor edit = sp.edit();
		String oldValue = SPUtil.getFromSP(key, sp); 
		edit.putString(key, (oldValue==null? "":(oldValue+",")) + value);
		edit.commit();
	}
	
	//get by string
	public static String getFromSP(String key,SharedPreferences sp){
		return sp.getString(key, null);
	}
	
	public static int getIntegerFromSP(String key,SharedPreferences sp){
		return sp.getInt(key, 0);
	}
	
	public static long getLongFromSP(String key,SharedPreferences sp){
		return sp.getLong(key, 0);
	}
	
	//clear data in SP
	public void clearSP(SharedPreferences sp){
    	SharedPreferences.Editor editor = sp.edit(); 
    	editor.clear();
    	editor.commit();
	}

	//clear data in SP
	public static void clearUserSP(SharedPreferences sp){
    	SharedPreferences.Editor editor = sp.edit(); 
    	editor.remove(SPUtil.CURRENT_USER_ID);
    	editor.remove(SPUtil.CURRENT_USER_PWD);
    	editor.remove(SPUtil.CURRENT_USER_HOME);
    	editor.remove(SPUtil.CURRENT_LOGIN_FILE_NAME);
    	editor.commit();
	}
	
	//clear data in SP
	public static void clearExamSP(SharedPreferences sp){
    	SharedPreferences.Editor editor = sp.edit(); 
    	editor.remove(SPUtil.CURRENT_EXAM_FILE_NAME);
    	editor.remove(SPUtil.CURRENT_EXAM_STATUS);
    	editor.remove(SPUtil.CURRENT_EXAM_START_TIME);
    	editor.remove(SPUtil.CURRENT_EXAM_SCORE);
    	editor.remove(SPUtil.CURRENT_EXAM_CATALOG);
    	editor.remove(SPUtil.CURRENT_EXAM_INDEX_IN_CATA);
    	editor.remove(SPUtil.CURRENT_EXAM_SUBMITTED_IDS);
    	editor.remove(SPUtil.CURRENT_EXAM_REMAINING_COUNT);
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
	
	public static String getAllSPData(SharedPreferences sp){
		Log.i(LOG_TAG,"----------------getAllSPData-----------------");
		StringBuffer sb = new StringBuffer();
		Map<String, ?> dataInSP = sp.getAll();
		Iterator it = dataInSP.entrySet().iterator();
		int i=1;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			sb.append(String.valueOf(i++)+". "+key.toString()+": "+value.toString()+"\n");
		}
		
		return sb.toString();
	}
	
}
