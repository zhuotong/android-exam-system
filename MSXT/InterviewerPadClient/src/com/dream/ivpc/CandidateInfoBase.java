package com.dream.ivpc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateInfoBase extends BaseActivity {

	protected Context mContext;
	protected String name = "";
	protected String position =""; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	protected void setHeader(TextView candidateInfoTV,ImageView goBack){
		//set candidateInfoTV
		Bundle bundle = this.getIntent().getExtras();
		name  = bundle.getString("name");
		position  = bundle.getString("position");
		if(name!=null&&position!=null){
			candidateInfoTV.setText(position +":" + name);
		}
		
		goBack.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
	        	Intent intent = new Intent();
				intent.setClass( mContext, CandidateList.class);
				startActivity(intent);  
			}
			
		});
		
    }

	protected void setFooter(Button button){
		//set candidateInfoTV
		button.setBackgroundColor(R.drawable.bg_footer_button_select);
    }
	
	public void go2Resume(View view){
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("position", position);
		intent.setClass( mContext, CandidateResumeGroup.class);
		startActivity(intent); 
	}
	
	public void go2Report(View view){
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("position", position);
		intent.setClass( mContext, CandidateInfoExamRpt.class);
		startActivity(intent); 
	}
	
	public void go2Detail(View view){
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("position", position);
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
