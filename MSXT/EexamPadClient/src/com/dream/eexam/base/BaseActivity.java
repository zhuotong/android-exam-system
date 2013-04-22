package com.dream.eexam.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.dream.eexam.paper.MultiChoices;
import com.dream.eexam.paper.PendQuestions;
import com.dream.eexam.paper.SingleChoices;
import com.dream.eexam.util.ActivityManage;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import com.msxt.client.model.QUESTION_TYPE;

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
	Context mContext;
	
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
		
		mContext = getApplicationContext();
		
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
				.setPositiveButton(mContext.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
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
		Integer ccIndex = sharedPreferences.getInt(SPUtil.CURRENT_EXAM_CATALOG, 0);
		if(ccIndex!=null&&ccIndex>0){
			Log.i(LOG_TAG,"getccIndex()...ccIndex="+String.valueOf(ccIndex));
			return ccIndex;
		}else{
			Log.i(LOG_TAG,"getccIndex()...ccIndex not saved");
			return 1;
		}
	}
	
	public Integer getcqIndex(){
		Integer cqIndex = sharedPreferences.getInt(SPUtil.CURRENT_EXAM_INDEX_IN_CATA, 0);
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
	
    public void go2ExamStart(Context context){
    	Intent intent = new Intent();
		intent.setClass( context, ExamStart.class);
		startActivity(intent); 
    }
    
	public void go2ExamContinue(Context context){
    	Intent intent = new Intent();
		intent.setClass( context, ExamContinue.class);
		startActivity(intent); 	
	}
	
	public void go2ExamResult(Context context){
    	Intent intent = new Intent();
		intent.setClass( context, ResultActivity.class);
		startActivity(intent); 
	}
	
	public void go2MultiQuestion(Context context){
    	Intent intent = new Intent();
		intent.setClass( context, MultiChoices.class);
		startActivity(intent); 		
	}
	
	public void go2SingleQuestion(Context context){
    	Intent intent = new Intent();
		intent.setClass( context, SingleChoices.class);
		startActivity(intent); 		
	}
	
	public void saveQuestionMovePara(int cId,int qId,QUESTION_TYPE qt,SharedPreferences sp){
		SPUtil.save2SP(SPUtil.CURRENT_EXAM_CATALOG, cId, sp);
		SPUtil.save2SP(SPUtil.CURRENT_EXAM_INDEX_IN_CATA, qId, sp);
		int qType;
		if(QUESTION_TYPE.MULTIPLE_CHOICE.equals(qt)){
			qType=2;
		}else{
			qType=1;
		}
		SPUtil.save2SP(SPUtil.CURRENT_EXAM_QUESTION_TYPE, qType, sp);
	}
	
	public void go2QuestionByType(QUESTION_TYPE qt,Context context){
		if(QUESTION_TYPE.MULTIPLE_CHOICE.equals(qt)){
			go2MultiQuestion(context);
		}else if(QUESTION_TYPE.SINGLE_CHOICE.equals(qt)){
			go2SingleQuestion(context);
		}
	}
	
	public void go2QuestionByType(int qt,Context context){
		if(qt==2){
			go2MultiQuestion(context);
		}else if(qt==1){
			go2SingleQuestion(context);
		}
	}
	
	public void go2PendingQuestions(Context context){
    	Intent intent = new Intent();
		intent.setClass( context, PendQuestions.class);
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
	
}
