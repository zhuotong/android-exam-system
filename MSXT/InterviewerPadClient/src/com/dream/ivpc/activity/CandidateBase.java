package com.dream.ivpc.activity;

import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.activity.report.CandidateInfoExamDetail;
import com.dream.ivpc.activity.report.CandidateExamRpt;
import com.dream.ivpc.activity.resume.ResumePicture;

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
		//set candidateInfoTV
//		Bundle bundle = this.getIntent().getExtras();
//		name  = bundle.getString("name");
//		position  = bundle.getString("position");
//		if(name!=null&&position!=null){
//			candidateInfoTV.setText(position +":" + name);
//		}
		
		goBack.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
	        	Intent intent = new Intent();
				intent.setClass( mContext, CandidateList.class);
				startActivity(intent);  
			}
			
		});
		
    }

/*	protected void setFooter(Button button){
		//set candidateInfoTV
		button.setBackgroundColor(R.drawable.bg_footer_button_select);
    }*/
	
	public void go2Resume(View view){
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("position", position);
		intent.setClass( mContext, ResumePicture.class);
		startActivity(intent); 
	}
	
	public void go2Report(View view){
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("position", position);
		intent.setClass( mContext, CandidateExamRpt.class);
		startActivity(intent); 
	}
	
	public void go2Detail(View view){
		Intent intent = new Intent();
		intent.putExtra("name", name);
		intent.putExtra("position", position);
		intent.setClass( mContext, CandidateInfoExamDetail.class);
		startActivity(intent); 
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
