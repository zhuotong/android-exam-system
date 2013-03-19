package com.dream.eexam.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.dream.eexam.util.ActivityManage;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

public class BaseActivity extends Activity {

	public final static String LOG_TAG = "BaseActivity";
	protected SharedPreferences sharedPreferences;
	
	public void printStoredDataInDB(Context mContext){
		Log.i(LOG_TAG,"----------------Start print data in SQLLite-----------------");
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
		dbUtil.printStoredDataInDB();
    	dbUtil.close();
    	Log.i(LOG_TAG,"----------------End print data in SQLLite-----------------");
    	
	}
	
	public void printSharedPreferencesData(SharedPreferences sharedPreferences){
		Log.i(LOG_TAG,"----------------Start Print data in sharedPreferences-----------------");
		SPUtil.printAllSPData(sharedPreferences);
		Log.i(LOG_TAG,"----------------End Print data in sharedPreferences-----------------");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		
		//add Activity to ActivityStackControlUtil to manage
		ActivityManage.add(this);
		
		//get SharedPreferences Object
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		
        printSharedPreferencesData(sharedPreferences);
        printStoredDataInDB(getApplicationContext());
		
		//hide title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	protected void onDestroy() {
		Log.i(LOG_TAG,"onDestroy()...");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(LOG_TAG,"onPause()...");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i(LOG_TAG,"onRestart()...");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(LOG_TAG,"onResume()...");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.i(LOG_TAG,"onStart()...");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(LOG_TAG,"onStop()...");
		super.onStop();
	}
	
	@Override
	public void finish() {
		Log.i(LOG_TAG,"finish()...");
		super.finish();
	}
	
	public void ShowDialog(String title,String msg) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
		}).show();
	}
	
	public String getWifiIP() {
		// get wifi service
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = (ipAddress& 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF)+ "." + (ipAddress >> 24 & 0xFF);
		return ip;
	}
	
	/**
	 * get question index you last view 
	 * @return
	 */
	public Integer getccIndex(){
		Integer ccIndex = sharedPreferences.getInt(SPUtil.SP_KEY_CCINDEX, 0);
		if(ccIndex!=null&&ccIndex>0){
			Log.i(LOG_TAG,"getccIndex()...ccIndex="+String.valueOf(ccIndex));
			return ccIndex;
		}else{
			Log.i(LOG_TAG,"getccIndex()...ccIndex not saved");
			return 1;
		}
	}
	
	public Integer getcqIndex(){
		Integer cqIndex = sharedPreferences.getInt(SPUtil.SP_KEY_CQINDEX, 0);
		if(cqIndex!=null&&cqIndex>0){
			Log.i(LOG_TAG,"getcqIndex()...cqIndex="+String.valueOf(cqIndex));
			return cqIndex;
		}else{
			Log.i(LOG_TAG,"getcqIndex()...cqIndex not saved");
			return 1;
		}		
	}

	
	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		Log.i(LOG_TAG,"inputStream2String...");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
	
	public void saveFile(String path, String fileName,String content) {
//		Log.i(LOG_TAG,"saveFile...");
//		Log.i(LOG_TAG,"path:"+path);
//		Log.i(LOG_TAG,"fileName:"+fileName);
//		Log.i(LOG_TAG,"content:"+content);
		try {
	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
	            File dir = new File(path);  
	            if (!dir.exists()) {  
	                dir.mkdirs();  
	            } 
	            FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);  
	            fos.write(content.getBytes());  
	            fos.close();  
	        }  
			
		} catch (Exception e) {
			Log.e(LOG_TAG, "an error occured while writing file...", e);
		}
		Log.i(LOG_TAG,"saveFile end.");
	}
	
	public void deleteFile(String path, String fileName) {
		Log.i(LOG_TAG,"deleteFile...");
		try {
	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
	            File file = new File(path+File.separator+fileName);  
	            if (file.exists()) {  
	                file.delete();  
	            } 
	        }  
		} catch (Exception e) {
			Log.e(LOG_TAG, "an error occured while writing file...", e);
		}
		Log.i(LOG_TAG,"deleteFile end.");
	}
	
	//
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	    	if((System.currentTimeMillis()-exitTime) > 2000){
	    		Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.msg_exit_hint), Toast.LENGTH_SHORT).show();                                
	    		exitTime = System.currentTimeMillis();
	    	}else{
			    finish();
			    ActivityManage.finishProgram();
			    System.exit(0);
		    }
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void goHome(Context context){
		Intent intent = new Intent();
		intent.setClass( context, LoginActivity.class);
		finish();
		startActivity(intent);
	}
	
	//clear data in SP
	public void clearSP(){
    	SharedPreferences.Editor editor = sharedPreferences.edit(); 
    	editor.clear();
    	editor.commit();
	}
	
	//clear data in DB
	public void clearDB(Context context){
    	DatabaseUtil dbUtil = new DatabaseUtil(context);
    	dbUtil.open();
    	dbUtil.deleteAllAnswers();
    	dbUtil.close();
	}
	
	//delete file fold and file under it
	public void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					this.deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}

}
