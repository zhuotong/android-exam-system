package com.dream.eexam.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dream.eexam.model.QuestionProgress;
import com.dream.eexam.util.ActivityStackControlUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;

public class BaseActivity extends Activity {

	public final static String LOG_TAG = "BaseActivity";
	protected SharedPreferences sharedPreferences;
	
	@Override
	public void finish() {
		Log.i(LOG_TAG,"finish()...");
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		ActivityStackControlUtil.add(this);
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		
		//set Orientation to PORTRAIT
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
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
	
	public void ShowDialog(String msg) {
		new AlertDialog.Builder(this).setTitle("Note").setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
		}).show();
	}
	
	/**
	 * 
	 * @param sharedPreferences
	 * @return
	 */
	public QuestionProgress getQuestionProgress(SharedPreferences sharedPreferences){
		Integer currentQueIndex = sharedPreferences.getInt("currentQueIndex", 0);
		Integer quesCount = sharedPreferences.getInt("quesCount", 0);
		String completedQueIdsString = sharedPreferences.getString("completedQueIdsString", null);
		
		return new QuestionProgress(currentQueIndex,quesCount,completedQueIdsString);
	}

	/**
	 * 
	 * @param sharedPreferences
	 * @param qp
	 */
	public void saveQuestionProgress(SharedPreferences sharedPreferences,QuestionProgress qp){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		Integer currentQueIndex = qp.getCurrentQueIndex();
		if(currentQueIndex!=null){
			editor.putInt("currentQueIndex", currentQueIndex);  
		}
		Integer quesCount = qp.getQuesCount();
		if(currentQueIndex!=null){
			editor.putInt("quesCount", quesCount);  
		}
		String completedQueIdsString = qp.getCompletedQueIdsString();
		if(currentQueIndex!=null){
			editor.putString("completedQueIdsString", completedQueIdsString);  
		}
	
		editor.commit();
	}
	
	/**
	 * get question index you last view 
	 * @return
	 */
	public Integer getccIndex(){
		Integer ccIndex = sharedPreferences.getInt("ccIndex", 0);
		if(ccIndex!=null&&ccIndex>0){
			Log.i(LOG_TAG,"getccIndex()...ccIndex="+String.valueOf(ccIndex));
			return ccIndex;
		}else{
			Log.i(LOG_TAG,"getccIndex()...ccIndex not saved");
			return 1;
		}
	}
	
	public Integer getcqIndex(){
		Integer cqIndex = sharedPreferences.getInt("cqIndex", 0);
		if(cqIndex!=null&&cqIndex>0){
			Log.i(LOG_TAG,"getcqIndex()...cqIndex="+String.valueOf(cqIndex));
			return cqIndex;
		}else{
			Log.i(LOG_TAG,"getcqIndex()...cqIndex not saved");
			return 1;
		}		
	}
	
	/**
	 * save question index you current view 
	 * @param cqIndex
	 */
	public void saveccIndexcqIndex(Integer ccIndex,Integer cqIndex){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("ccIndex", ccIndex);  
		editor.putInt("cqIndex", cqIndex); 
		editor.commit();
		Log.i(LOG_TAG,"saveccIndexcqIndex()...");
		Log.i(LOG_TAG,"ccIndex="+String.valueOf(ccIndex));
		Log.i(LOG_TAG,"cqIndex="+String.valueOf(cqIndex));
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
	
	/**
	 * 
	 * @param urlString
	 * @return
	 */
	public InputStream downloadUrl(String urlString){
        HttpURLConnection conn;
        InputStream stream = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        
	        // Starts the query
	        conn.connect();
	        stream = conn.getInputStream();
		} catch (IOException e) {
			Log.i(LOG_TAG,"IOException:" + e.getMessage());
		}

        return stream;
    }
	
	/**
	 * 
	 * @param path
	 * @param fileName
	 * @param content
	 */
	public void saveFile(String path, String fileName,String content) {
		Log.i(LOG_TAG,"saveFile...");
		Log.i(LOG_TAG,"path:"+path);
		Log.i(LOG_TAG,"fileName:"+fileName);
		Log.i(LOG_TAG,"content:"+content);
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


}
