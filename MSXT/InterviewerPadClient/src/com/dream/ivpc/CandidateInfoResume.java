package com.dream.ivpc;

import com.dream.ivpc.model.CandiateDetailBean;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateInfoResume extends CandidateInfoBase {
	public final static String LOG_TAG = "CandidateInfoResume";
	
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
