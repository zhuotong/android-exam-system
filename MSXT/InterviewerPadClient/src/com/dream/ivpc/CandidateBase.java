package com.dream.ivpc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateBase extends BaseActivity {

	protected Context mContext;
	protected String name = "";
	protected String position =""; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	protected void setHeader(TextView candidateInfoTV,ImageView goBack){
		goBack.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
	        	Intent intent = new Intent();
				intent.setClass( mContext, CandidateDetail2.class);
				startActivity(intent);  
			}
		});
    }
	
    protected View.OnClickListener goBackListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Intent intent = new Intent();
			intent.setClass( mContext, CandidateList.class);
			startActivity(intent);  
        }  
    };
	
}
