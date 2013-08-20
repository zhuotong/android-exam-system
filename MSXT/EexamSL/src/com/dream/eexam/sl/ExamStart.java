package com.dream.eexam.sl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.dream.eexam.sl.R;
import com.dream.eexam.paper.ChoiceQuestion;
import com.dream.eexam.paper.MultiChoices;
import com.dream.eexam.paper.SingleChoices;
import com.dream.eexam.util.SPUtil;
import com.dream.exam.bean.Exam;
import com.dream.exam.bean.ExamParse;
import com.dream.exam.bean.ExamXMLParse;
import com.dream.exam.bean.Question;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	
//	TextView examDesc = null;
	Spinner spinner;
	Button startBtn;
	
	String appHomePath;
	String userId;
	String examId;
	List<String> examIds = new ArrayList<String>();
	
	ArrayAdapter<String> adapter;
	
	public void loadExamList(String path){
		File parent = new File(path);
		File subFiles[] = parent.listFiles();
		for(File file:subFiles){
			examIds.add(file.getName());
		}
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
		
		appHomePath = Environment.getExternalStorageDirectory().getPath()+File.separator + getResources().getString(R.string.app_file_home);
		userId = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
		
		//load Exam List Information
		loadExamList(appHomePath+File.separator+"exam");
		
		//initial component
//		examDesc = (TextView) this.findViewById(R.id.examDesc);
		
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				//save exam start time
	        	Intent intent = new Intent();
				intent.setClass( ExamStart.this, ChoiceQuestion.class);
				startActivity(intent); 	
				
//				saveStartTime();
				
				SPUtil.save2SP(SPUtil.CURRENT_APP_HOME, appHomePath, sharedPreferences);
//				SPUtil.save2SP(SPUtil.CURRENT_USER_ID, userId, sharedPreferences);
				SPUtil.save2SP(SPUtil.CURRENT_EXAM_ID, examId, sharedPreferences);
//				
//				SPUtil.save2SP(SPUtil.CURRENT_CATALOG_ID, 1, sharedPreferences);
//				SPUtil.save2SP(SPUtil.CURRENT_QUESTON_ID, 1, sharedPreferences);
			}			
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		spinner = (Spinner) findViewById(R.id.Spinner01);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,examIds);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner.setVisibility(View.VISIBLE);
	}

	class SpinnerSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//			selectedExamId = examIdsMap.get(arg2);
//			selectedExamName = examNames.get(arg2);
			examId = examIds.get(arg2);
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
	
	class GetExamTask extends AsyncTask<String, Void, String> {
    	Exam exam;
    	
    	@Override
    	protected void onPreExecute() {

    	}
    	
        @Override
		protected String doInBackground(String... urls) {
        	Log.i(LOG_TAG, "doInBackground()...");
        	String home = urls[0];
        	String userId = urls[1];
        	String examId = urls[2];
        	FileInputStream inputStream;
			try {
				inputStream = new FileInputStream(new File(home+ File.separator+userId+ File.separator+examId));
				exam = ExamXMLParse.parseExam(inputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

        @Override
        protected void onPostExecute(String result) {
        	if(exam!=null){
        		Question question = ExamParse.getQuestion(exam,1,1);
        		if(question.getType() == 1){
		        	Intent intent = new Intent();
					intent.setClass( ExamStart.this, MultiChoices.class);
					startActivity(intent); 	
        		}else{
		        	Intent intent = new Intent();
					intent.setClass( ExamStart.this, SingleChoices.class);
					startActivity(intent); 	
        		}
        	}
        }
    }
	
}