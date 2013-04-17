package com.dream.eexam.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dream.eexam.paper.MultiChoices;
import com.dream.eexam.paper.SingleChoices;
import com.dream.eexam.server.DataParseUtil;
import com.dream.eexam.util.FileUtil;
import com.dream.eexam.util.SPUtil;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.msxt.client.model.QUESTION_TYPE;

/**
 * ExamStart Page
 * @author Timothy
 *
 */

public class ExamStart extends BaseActivity {
	private static final String LOG_TAG = "ExamStart";
	
	Context mContext;
	
	//declare components
	ImageView imgHome = null;
	private TextView nameTV = null;
	private TextView jobTitleTV = null;
	private TextView examDesc = null;
	private Spinner spinner;
	private Button startBtn;
	
	LoginSuccessResult succResult = new LoginSuccessResult();
	List<com.msxt.client.model.LoginSuccessResult.Examination> examinations;
	String conversation = null;
	
	String selectedExamId = null;
	Map<Integer,String> examIdsMap = new HashMap<Integer,String>();
	List<String> examNames = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	QUESTION_TYPE fQuestionType = null;
	String[] questionTypes;
	
	private void loadExamList(){
		questionTypes = getResources().getStringArray(R.array.question_types);
		String loginResultFilePath  = SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences);
		String loginResultFile  = SPUtil.getFromSP(SPUtil.CURRENT_USER_LOGIN_FILE_NAME, sharedPreferences);
		Log.i(LOG_TAG,"loginResultFilePath:"+loginResultFilePath);
		Log.i(LOG_TAG,"loginResultFile:"+loginResultFile);
		
		//get login successfully information
		try {
	    	FileInputStream inputStream = new FileInputStream(new File(loginResultFilePath+ File.separator+loginResultFile));
	    	succResult = DataParseUtil.getSuccessResult(inputStream);
		} catch (Exception e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		
		Log.i(LOG_TAG,"Load Pending Exams...");
		//get exam name list
		examinations = succResult.getExaminations();
		if(examinations!=null&&examinations.size()>0){
			int rSum = 0;
			String examIdSubmitted = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_SUBMITTED_IDS, sharedPreferences);
			Log.i(LOG_TAG,"examIdSubmitted:" + examIdSubmitted);
			
			for(int i=0;i<examinations.size();i++){
				String examId = examinations.get(i).getId();
				String examName = examinations.get(i).getName();
				if(examIdSubmitted==null || examIdSubmitted.indexOf(examId)==-1){
					examIdsMap.put(rSum, examId);
					examNames.add(rSum,examName);
					Log.i(LOG_TAG,"rSum:"+rSum + " examId:"+examId + " examName:"+ examName );
					rSum++;
				}
			}
		}
		
		Log.i(LOG_TAG,"There are " + String.valueOf(examNames.size()) + " exams pending.");
		SPUtil.save2SP(SPUtil.CURRENT_USER_EXAM_REMAINING_COUNT, String.valueOf(examNames.size()), sharedPreferences);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		mContext = getApplicationContext();
		setContentView(R.layout.exam_start);
		
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goHome(mContext);
			}
		});
		
		//load Exam List Information
		loadExamList();
		
		//initial component
		nameTV = (TextView) this.findViewById(R.id.nameTV);
		nameTV.setText(succResult.getInterviewer());
		
		jobTitleTV = (TextView) this.findViewById(R.id.jobTitleTV);
		jobTitleTV.setText(succResult.getJobtitle());
		
		examDesc = (TextView) this.findViewById(R.id.examDesc);
		
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
	        	new DownloadExamTask().execute(selectedExamId);
			}			
		});
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		spinner = (Spinner) findViewById(R.id.Spinner01);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,examNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner.setVisibility(View.VISIBLE);
	}



	class SpinnerSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			Log.i(LOG_TAG,"--------------------Spinner.onItemSelected()...-----------------");
			Log.i(LOG_TAG,"arg2="+String.valueOf(arg2));
			selectedExamId = examIdsMap.get(arg2);
			Log.i(LOG_TAG, "examIdString:"+selectedExamId);
			Log.i(LOG_TAG,"---------------------------End---------------------------------");
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
			progressDialog = ProgressDialog.show(ExamStart.this, null,displayMessage, true, false); 
		}
		
	    @Override
	    protected String doInBackground(String... urls) {
	    	Log.i(LOG_TAG, "doInBackground() called");
        	ServerProxy proxy =  WebServerProxy.Factroy.getCurrrentInstance();
        	examResult = proxy.getExam(urls[0]);
	        return "success";
	    }
	
	    @Override
	    protected void onPostExecute(String result) {
	    	Log.i(LOG_TAG, "onPostExecute()...");
	    	
			progressDialog.dismiss();
			if(STATUS.SUCCESS.equals(examResult.getStatus())){
				
				Log.i(LOG_TAG, "download exam successfully!");
				
				//clear Exam status and Exam record first
    			SPUtil.clearExamSP(sharedPreferences);
    			clearDB(mContext);
				
    			String examFileName = FileUtil.EXAM_FILE_PREFIX + selectedExamId+ FileUtil.FILE_SUFFIX_XML;
    			
    			//save to SD card
    			FileUtil fu = new FileUtil();
        		fu.saveFile(SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences), examFileName, examResult.getSuccessMessage());
        		
    			//save to SharedPreferences
    			SPUtil.save2SP(SPUtil.CURRENT_EXAM_FILE_NAME, examFileName, sharedPreferences);
    			
    			//parse exam
				Examination exam = DataParseUtil.getExam(examResult);
				int ccIndex = 1;
				int cqIndex = 1;
				if(getccIndex()>0 && getcqIndex()>0){
					ccIndex = getccIndex();
					cqIndex = getcqIndex();
				}
				
				Question fQuestion = DataParseUtil.getQuestionByCidQid(exam, ccIndex, cqIndex);
				if(fQuestion==null){
					ShowDialog(mContext.getResources().getString(R.string.dialog_note),
							"Can not get question!");
					this.cancel(true);
					return;
				}
				
				fQuestionType = fQuestion.getType();
	
				//move question
				Intent intent = new Intent();
				intent.putExtra(SPUtil.CURRENT_EXAM_CATALOG, String.valueOf(ccIndex));
				intent.putExtra(SPUtil.CURRENT_EXAM_INDEX_IN_CATA, String.valueOf(cqIndex));
				if(QUESTION_TYPE.MULTIPLE_CHOICE.equals(fQuestionType)){
					intent.putExtra("questionType",questionTypes[0]);
					intent.setClass( getBaseContext(), MultiChoices.class);
					startActivity(intent);
				}else if(QUESTION_TYPE.SINGLE_CHOICE.equals(fQuestionType)){
					intent.putExtra("questionType",questionTypes[1]);
					intent.setClass( getBaseContext(), SingleChoices.class);
					startActivity(intent);
				}else{
					ShowDialog(mContext.getResources().getString(R.string.dialog_note),"Invalid qeustion type.");
				}
				
				Log.i(LOG_TAG, "Start a New Exam!");
				
				SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.EXAM_STATUS_START_GOING, sharedPreferences);
				
				//save exam start time
				saveStartTime();
				   					
			}else if(STATUS.ERROR.equals(examResult.getStatus())){
		    	ShowDialog(mContext.getResources().getString(R.string.dialog_note),examResult.getErrorMessage());
			}
	    }
	}
	
	public void saveStartTime(){
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		long startTime = sharedPreferences.getLong(SPUtil.CURRENT_EXAM_START_TIME, 0);
		//if its first time to do exam, save start exam time
		if(startTime==0){
			SharedPreferences.Editor editor = sharedPreferences.edit();
			long currentTime = Calendar.getInstance().getTimeInMillis();
			Log.i(LOG_TAG, "startTime="+String.valueOf(startTime));
			editor.putLong(SPUtil.CURRENT_EXAM_START_TIME, currentTime);
			editor.commit();		
		}
	}
	
}