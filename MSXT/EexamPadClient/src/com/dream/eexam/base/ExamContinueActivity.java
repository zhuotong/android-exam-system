package com.dream.eexam.base;

import java.io.File;
import java.io.FileInputStream;

import com.dream.eexam.paper.MultiChoices;
import com.dream.eexam.paper.SingleChoices;
import com.dream.eexam.server.DataUtil;
import com.dream.eexam.server.FileUtil;
import com.dream.eexam.util.DatabaseUtil;
import com.msxt.client.model.Examination;
import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.model.Examination.Question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExamContinueActivity extends BaseActivity {
	private static final String LOG_TAG = "ExamListActivity";

	ImageView imgHome = null;
	Context mContext;
	
	//Get interviewer information
	LoginSuccessResult succResult = null;
	TextView nameTV = null;
	TextView jobTitleTV = null;
	
	//Exam description
	TextView continueExam = null;
	String examFilePath = null;
	String examFileName = null;
    Examination exam;
    
	//buttons
	private Button continueBtn;
	private Button cancelBtn;
	
	String[] questionTypes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		
		setContentView(R.layout.exam_continue);
		mContext = getApplicationContext();
		
		//get demoSessionStr and save to string array
		Bundle bundle = this.getIntent().getExtras();
		String loginResultFile  = bundle.getString("loginResultFile");
		String loginResultFilePath  = bundle.getString("loginResultFilePath");
		try {
	    	FileInputStream inputStream = new FileInputStream(new File(loginResultFilePath+ File.separator+loginResultFile));
	    	succResult = DataUtil.getSuccessResult(inputStream);
		} catch (Exception e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goHome(mContext);
			}
		});
		
		questionTypes = getResources().getStringArray(R.array.question_types);
		
		//get Exam data
		examFilePath = Environment.getExternalStorageDirectory().getPath()+ File.separator + getResources().getString(R.string.app_file_home);
		examFileName = getResources().getString(R.string.exam_file_name);
    	FileInputStream examStream = FileUtil.getExamStream(examFilePath,examFileName);
    	exam = DataUtil.getExam(examStream);
    	
		nameTV = (TextView) this.findViewById(R.id.nameTV);
		nameTV.setText(succResult.getInterviewer());
		
		jobTitleTV = (TextView) this.findViewById(R.id.jobTitleTV);
		jobTitleTV.setText(succResult.getJobtitle());
		
		continueExam = (TextView) this.findViewById(R.id.continueExam);
		continueExam.setText(exam.getName()+getExamInfo());
		
		continueBtn = (Button) findViewById(R.id.continueBtn);
		continueBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
	        	
				Question fQuestion = DataUtil.getQuestionByCidQid(exam, 1, 1);
				if(fQuestion==null){
					ShowDialog("Can not get question!");
					return;
				}
				
				String fQuestionType = fQuestion.getType();
	
				//move question
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(getccIndex()));
				intent.putExtra("cqIndex", String.valueOf(getcqIndex()));
				intent.putExtra("questionType",fQuestionType);
				
				if(questionTypes[0].equals(fQuestionType)){
					intent.setClass( ExamContinueActivity.this, MultiChoices.class);
					startActivity(intent);
				}else if(questionTypes[1].equals(fQuestionType)){
					intent.setClass( ExamContinueActivity.this, SingleChoices.class);
					startActivity(intent);
				}else{
					ShowDialog("Invalid qeustion type:"+fQuestionType);
				}
			}			
		});
		
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
	        	//go to login page
	        	Intent intent = new Intent();
				intent.setClass( ExamContinueActivity.this, LoginActivity.class);
				startActivity(intent);  
			}			
		});
	}
	
	public String getExamInfo(){
		String continueExamInfo ;
		
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
		
		int examAnsweredQuestionSum = dbUtil.fetchAllAnswersCount();
		int examQuestionSum = DataUtil.getExamQuestionSum(exam);
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
		continueExamInfo = "("+String.valueOf(per)+" Completed)";
		
		dbUtil.close();
		
		return continueExamInfo;
	}

}