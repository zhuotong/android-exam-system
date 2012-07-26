package com.dream.eexam.base;

import com.dream.eexam.model.QuestionProgress;
import com.dream.eexam.util.ActivityStackControlUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
	public Integer getCurrentQuestionIndex(){
		Integer cqIndex = sharedPreferences.getInt("cqIndex", 0);
		if(cqIndex!=null&&cqIndex>0){
			Log.i(LOG_TAG,"getCurrentQuestionIndex()...cqIndex="+String.valueOf(cqIndex));
			return cqIndex;
		}else{
			Log.i(LOG_TAG,"getCurrentQuestionIndex()...cqIndex not saved");
			return 1;
		}
	}
	
	/**
	 * save question index you current view 
	 * @param cqIndex
	 */
	public void saveCurrentQuestionIndex(Integer cqIndex){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("cqIndex", cqIndex);  
		editor.commit();
		
		Log.i(LOG_TAG,"saveCurrentQuestionIndex()...cqIndex="+String.valueOf(cqIndex));
	}


}
