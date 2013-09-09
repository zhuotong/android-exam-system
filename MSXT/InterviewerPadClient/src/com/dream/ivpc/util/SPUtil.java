package com.dream.ivpc.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import android.content.SharedPreferences;
import android.util.Log;

public class SPUtil {

public final static String LOG_TAG = "SPUtil";
	
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
		
		String oldValue = SPUtil.getFromSP(key, sp);
		String newValue;
		if(oldValue==null){
			newValue = value;
		}else{
			newValue = oldValue + "," + value;
		}
		SharedPreferences.Editor edit = sp.edit();
		edit.putString(key, newValue);
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
	
	public static Map<String,String> getAllSPDataMap(SharedPreferences sp){
		Log.i(LOG_TAG,"----------------getAllSPData-----------------");
		Map<String,String> dataMap = new HashMap<String,String>();
		Map<String, ?> dataInSP = sp.getAll();
		Iterator it = dataInSP.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			dataMap.put(key.toString(), value.toString());
		}
		return dataMap;
	}
}
