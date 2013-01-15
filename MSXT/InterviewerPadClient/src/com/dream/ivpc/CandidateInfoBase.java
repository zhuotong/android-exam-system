package com.dream.ivpc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CandidateInfoBase extends BaseActivity {

	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	protected void setHeader(TextView candidateInfoTV){
		//set candidateInfoTV
		Bundle bundle = this.getIntent().getExtras();
		String name  = bundle.getString("name");
		String position  = bundle.getString("position");
		candidateInfoTV.setText(position +":" + name);
    }
	
	public void go2Resume(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, CandidateInfoResumePdf.class);
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
	
    View.OnClickListener goBackListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Intent intent = new Intent();
			intent.setClass( mContext, CandidateList.class);
			startActivity(intent);  
        }  
    };
	
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
