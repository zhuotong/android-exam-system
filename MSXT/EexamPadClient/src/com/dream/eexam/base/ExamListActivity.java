package com.dream.eexam.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;
import com.dream.eexam.paper.MultiChoices;
import com.dream.eexam.paper.SingleChoices;
import com.dream.eexam.server.DataUtil;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SystemConfig;
import com.msxt.client.model.Examination;
import com.msxt.client.model.Examination.Question;
import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.WebServerProxy;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.server.ServerProxy.STATUS;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ExamListActivity extends BaseActivity {
	private static final String LOG_TAG = "ExamListActivity";

	ImageView imgHome = null;
	
	//declare components
	private TextView nameTV = null;
	private TextView jobTitleTV = null;
	private TextView examDesc = null;
	private Spinner spinner;
	
	//buttons
	private Button startBtn;
//	private Button clearBtn;
	
	LoginSuccessResult succResult = new LoginSuccessResult();
	List<com.msxt.client.model.LoginSuccessResult.Examination> examinations;
	
	String conversation = null;
	String[] exams = null;
	ArrayAdapter<String> adapter;
	String examIdString = null;
	Context mContext;
	
	String downloadExamFile = null;
	String downloadExamFilePath = null;
	
	String fQuestionType = null;
	String[] questionTypes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		mContext = getApplicationContext();
		setContentView(R.layout.exam_list);
		
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goHome(mContext);
			}
		});
		
		questionTypes = getResources().getStringArray(R.array.question_types);
		
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
		examinations = succResult.getExaminations();
		if(examinations!=null&&examinations.size()>0){
			exams = new String[examinations.size()];
			for(int i=0;i<examinations.size();i++){
				exams[i] = examinations.get(i).getName();
			}
		}else{
			exams = new String[0];
		}
		
		nameTV = (TextView) this.findViewById(R.id.nameTV);
		nameTV.setText(succResult.getInterviewer());
		
		jobTitleTV = (TextView) this.findViewById(R.id.jobTitleTV);
		jobTitleTV.setText(succResult.getJobtitle());
		
		spinner = (Spinner) findViewById(R.id.Spinner01);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,exams);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner.setVisibility(View.VISIBLE);
		
		examDesc = (TextView) this.findViewById(R.id.examDesc);
		
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Log.i(LOG_TAG,"onClick()...");
				
				downloadExamFile = SystemConfig.getInstance().getPropertyValue("Download_Exam");
	        	downloadExamFilePath = Environment.getExternalStorageDirectory().getPath()+ File.separator + "eExam";
	        	
	        	new DownloadExamTask().execute(examIdString);

			}			
		});

	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			Log.i(LOG_TAG,"onItemSelected()...");
			Log.i(LOG_TAG,"arg2="+String.valueOf(arg2));
			
			List<com.msxt.client.model.LoginSuccessResult.Examination> exams = succResult.getExaminations();
			com.msxt.client.model.LoginSuccessResult.Examination exam = exams.get(arg2);
			
			examDesc.setText(exam.getDesc());
			examIdString = exam.getId();
			
			Log.i(LOG_TAG, "examIdString:"+examIdString);
			
			loadAnswerOfLasttime();
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			Log.i(LOG_TAG,"onNothingSelected()...");
		}
	}
	
	private class DownloadExamTask extends AsyncTask<String, Void, String> {
		ProgressDialog progressDialog;
		Result examResult;
		
		@Override
		protected void onPreExecute() {
			Log.i(LOG_TAG, "onPreExecute() called");
			String displayMessage = mContext.getResources().getString(R.string.msg_download_exam)+"...";
			progressDialog = ProgressDialog.show(ExamListActivity.this, null,displayMessage, true, false); 
		}
		
	    @Override
	    protected String doInBackground(String... urls) {
        	ServerProxy proxy =  WebServerProxy.Factroy.getCurrrentInstance();
        	examResult = proxy.getExam(urls[0]);
    		if(STATUS.SUCCESS.equals(examResult.getStatus())){
    			saveFile(downloadExamFilePath, downloadExamFile, examResult.getSuccessMessage());
    		}else if(STATUS.ERROR.equals(examResult.getStatus())){
    			ShowDialog(examResult.getErrorMessage());
    			this.cancel(true);
    		}
	        return "success";
	    }
	
	    @Override
	    protected void onPostExecute(String result) {
			progressDialog.dismiss();
			if(STATUS.SUCCESS.equals(examResult.getStatus())){
				
				Examination exam = DataUtil.getExam(examResult);
				Question fQuestion = DataUtil.getQuestionByCidQid(exam, 1, 1);
				if(fQuestion==null){
					ShowDialog("Can not get question!");
					this.cancel(true);
					return;
				}
				
				fQuestionType = fQuestion.getType();
	
				//move question
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(getccIndex()));
				intent.putExtra("cqIndex", String.valueOf(getcqIndex()));
				intent.putExtra("questionType",fQuestionType);
				
				if(questionTypes[0].equals(fQuestionType)){
					intent.setClass( getBaseContext(), MultiChoices.class);
					startActivity(intent);
				}else if(questionTypes[1].equals(fQuestionType)){
					intent.setClass( getBaseContext(), SingleChoices.class);
					startActivity(intent);
				}else{
					ShowDialog("Invalid qeustion type:"+fQuestionType);
				}
				
				//save exam status
				saveExamStatus();
				
				//save exam start time
				saveStartTime();
				   					
			}
	    }
	}

	public void saveExamStatus(){
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		String examStatus = sharedPreferences.getString("exam_status", null);
		//if its first time to do exam, save start exam time
		if(examStatus==null){
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("exam_status", "start");
			editor.commit();		
		}
	}
	
	public void saveStartTime(){
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		long startTime = sharedPreferences.getLong("starttime", 0);
		Log.i(LOG_TAG, "startTime="+String.valueOf(startTime));
		
		//if its first time to do exam, save start exam time
		if(startTime==0){
			SharedPreferences.Editor editor = sharedPreferences.edit();
			long currentTime = Calendar.getInstance().getTimeInMillis();
			Log.i(LOG_TAG, "startTime="+String.valueOf(startTime));
			editor.putLong("starttime", currentTime);
			
			editor.commit();		
		}
	}
	
	public void loadAnswerOfLasttime(){
		Log.i(LOG_TAG, "loadAnswerOfLasttime()...");
    	DatabaseUtil dbUtil = new DatabaseUtil(this);
    	dbUtil.open();
    	Cursor cursor = dbUtil.fetchAllAnswers();
    	while (cursor.moveToNext()) {
			Log.i(LOG_TAG, "cid:" + cursor.getInt(0) + " qid:"+ cursor.getInt(1)+" qid_str:"+ cursor.getString(2) + " answer:" + cursor.getString(3));
		}
    	Log.i(LOG_TAG, "loadAnswerOfLasttime()...");
    	dbUtil.close();
	}
}