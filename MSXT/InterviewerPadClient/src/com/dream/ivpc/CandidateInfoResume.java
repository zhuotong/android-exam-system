package com.dream.ivpc;

import com.dream.ivpc.model.CandiateDetailBean;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateInfoResume extends CandidateInfoBase {
	public final static String LOG_TAG = "LoginActivity";
	
	ImageView imgGoBack = null;
	
//	Context mContext;
	TextView name;
	TextView gender;
	TextView age;
	TextView education;
	TextView experience;

	public CandiateDetailBean getCandiateDetailBean(){
		CandiateDetailBean bean = new CandiateDetailBean();
		bean.setName("Timothy");
		bean.setGender("Male");
		bean.setAge("30");
		bean.setEducation("Bachor");
		bean.setExperience("............................................\n..");
		
		return bean;
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_info_resume);
        mContext = getApplicationContext();
        
        imgGoBack = (ImageView) findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(goBackListener);
        
        name = (TextView) this.findViewById(R.id.name);
        gender = (TextView) this.findViewById(R.id.gender);
        age = (TextView) this.findViewById(R.id.age);
        education = (TextView) this.findViewById(R.id.education);
        experience = (TextView) this.findViewById(R.id.experience);
        
        CandiateDetailBean bean = getCandiateDetailBean();
        name.setText(bean.getName());
        gender.setText(bean.getGender());
        age.setText(bean.getAge());
        education.setText(bean.getEducation());
        experience.setText(bean.getExperience());

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
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
