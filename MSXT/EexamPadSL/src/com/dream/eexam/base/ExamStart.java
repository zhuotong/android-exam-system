package com.dream.eexam.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dream.eexam.base.R;
import com.dream.eexam.server.DataParseUtil;
import com.dream.eexam.util.SPUtil;
import com.msxt.client.model.Examination;
import com.msxt.client.model.Examination.Question;
import com.msxt.client.model.LoginSuccessResult;
import com.msxt.client.server.ServerProxy.Result;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
	TextView nameTV = null;
	TextView jobTitleTV = null;
	TextView examDesc = null;
	Spinner spinner;
	Button startBtn;
	
	//declare data object
	LoginSuccessResult succResult = new LoginSuccessResult();
	List<com.msxt.client.model.LoginSuccessResult.Examination> examinations;
	String conversation = null;
	String selectedExamId = null;
	String selectedExamName = null;
	@SuppressLint("UseSparseArrays")
	Map<Integer,String> examIdsMap = new HashMap<Integer,String>();
	List<String> examNames = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	QUESTION_TYPE fQuestionType = null;
	
	private void loadExamList(){
		String loginResultFilePath  = SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences);
		String loginResultFile  = SPUtil.getFromSP(SPUtil.CURRENT_USER_LOGIN_FILE_NAME, sharedPreferences);
		
		Log.i(LOG_TAG,"loginResultFilePath:"+loginResultFilePath);
		Log.i(LOG_TAG,"loginResultFile:"+loginResultFile);
		
		examNames.add("Exam A");
		examNames.add("Exam B");
		examNames.add("Exam C");
	
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
    			Result examResult = null;
    			//parse exam
				Examination exam = DataParseUtil.getExam(examResult);
				int ccIndex = 1;
				int cqIndex = 1;
				if(getccIndex()>0 && getcqIndex()>0){
					ccIndex = getccIndex();
					cqIndex = getcqIndex();
				}
				
				Question fQuestion = DataParseUtil.getQuestionByCidQid(exam, ccIndex, cqIndex);

				fQuestionType = fQuestion.getType();
				Log.i(LOG_TAG, "----------Start a New Exam!-----------------");
				
				SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.EXAM_STATUS_START_GOING, sharedPreferences);
				SPUtil.save2SP(SPUtil.CURRENT_EXAM_NAME, selectedExamName, sharedPreferences);
				go2QuestionByType(fQuestionType,mContext);
				saveQuestionMovePara(ccIndex,cqIndex,fQuestionType,sharedPreferences);
				
				//save exam start time
				saveStartTime();
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
			selectedExamName = examNames.get(arg2);
			Log.i(LOG_TAG, "examIdString:"+selectedExamId);
			Log.i(LOG_TAG,"---------------------------End---------------------------------");
		}
		
		public void onNothingSelected(AdapterView<?> arg0) {
			Log.i(LOG_TAG,"onNothingSelected()...");
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