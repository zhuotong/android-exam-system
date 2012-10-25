package com.dream.ivpc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class CandidateInfoBase extends BaseActivity {

	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	public void go2Resume(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, CandidateInfoResume.class);
		startActivity(intent); 
	}
	
	public void go2Report(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, CandidateInfoExamRpt.class);
		startActivity(intent); 
	}
	
	public void go2Detail(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, CandidateInfoExamDetail.class);
		startActivity(intent); 
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

}
