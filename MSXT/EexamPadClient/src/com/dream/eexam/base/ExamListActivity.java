package com.dream.eexam.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import com.dream.eexam.model.ExamBaseBean;
import com.dream.eexam.model.LoginResultBean;
import com.dream.eexam.paper.MultiChoices;
import com.dream.eexam.paper.SingleChoices;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SystemConfig;
import com.dream.eexam.util.XMLParseUtil;
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
import android.widget.Spinner;
import android.widget.TextView;

public class ExamListActivity extends BaseActivity {
	private static final String LOG_TAG = "PapersActivity";

	//declare components
	private TextView nameTV = null;
	private TextView jobTitleTV = null;
	
	private TextView examDesc = null;
	private Spinner spinner;
	
	//buttons
	private Button startBtn;
	private Button clearBtn;
	
	//data
	LoginResultBean loginResultBean = new LoginResultBean();
	List<ExamBaseBean> examList = null;
	String conversation = null;
	String[] exams = null;
	ArrayAdapter<String> adapter;
	String examIdString = null;
	Context mContext;
	
	String downloadURL = null;
	String downloadExamFile = null;
	String downloadExamFilePath = null;
	
	String questionType = null;
	String questionTypeS = null;
	String questionTypeM = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		mContext = getApplicationContext();
		
		setContentView(R.layout.exam_list);
		
		questionTypeM = SystemConfig.getInstance().getPropertyValue("Question_Type_Multi_Select");
		questionTypeS = SystemConfig.getInstance().getPropertyValue("Question_Type_Single_Select");
		
		//get demoSessionStr and save to string array
		Bundle bundle = this.getIntent().getExtras();
		String loginResultFile  = bundle.getString("loginResultFile");
		String loginResultFilePath  = bundle.getString("loginResultFilePath");
		
		try {
	    	FileInputStream inputStream = new FileInputStream(new File(loginResultFilePath+ File.separator+loginResultFile));
	    	loginResultBean = XMLParseUtil.readLoginResult(inputStream);
	    	
		} catch (Exception e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		
		conversation = loginResultBean.getConversation();
		examList = loginResultBean.getExamList();
		if(examList!=null&&examList.size()>0){
			exams = new String[examList.size()];
			for(int i=0;i<examList.size();i++){
				exams[i] = examList.get(i).getName();
			}
		}
		
		nameTV = (TextView) this.findViewById(R.id.nameTV);
		nameTV.setText(loginResultBean.getInterviewer());
		
		jobTitleTV = (TextView) this.findViewById(R.id.jobTitleTV);
		jobTitleTV.setText(loginResultBean.getJobtitle());
		
		spinner = (Spinner) findViewById(R.id.Spinner01);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,exams);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner.setVisibility(View.VISIBLE);
		
		examDesc = (TextView) this.findViewById(R.id.examDesc);
		
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setText("Start");
		startBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Log.i(LOG_TAG,"onClick()...");
				
				downloadURL = SystemConfig.getInstance().getPropertyValue("Download_URL")+"conversation="+conversation+"&examId="+examIdString;;
				downloadExamFile = SystemConfig.getInstance().getPropertyValue("Download_Exam");
	        	downloadExamFilePath = Environment.getExternalStorageDirectory().getPath()+ File.separator + "eExam";
	        	
				Log.i(LOG_TAG,"downloadURL:"+downloadURL);
	        	new DownloadExamTask().execute(downloadURL);
			}			
		});
		
		clearBtn = (Button) findViewById(R.id.clearBtn);
		clearBtn.setText("Clear History");
		clearBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Log.i(LOG_TAG,"onClick()...");
				SharedPreferences.Editor editor = sharedPreferences.edit(); 
				editor.clear();
				editor.commit();
				
				DatabaseUtil dbUtil = new DatabaseUtil(mContext);
				dbUtil.open();
				dbUtil.deleteAllAnswers();
				dbUtil.close();
				
				ShowDialog("History is cleared!");
			}			
		});
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			Log.i(LOG_TAG,"onItemSelected()...");
			Log.i(LOG_TAG,"arg2="+String.valueOf(arg2));
			
			ExamBaseBean examBaseBean = loginResultBean.getExamList().get(arg2);
			examDesc.setText(examBaseBean.getDesc());
			examIdString = examBaseBean.getId();
			
			Log.i(LOG_TAG, "examIdString:"+examIdString);
			
			loadAnswerOfLasttime();
		}
		public void onNothingSelected(AdapterView<?> arg0) {
			Log.i(LOG_TAG,"onNothingSelected()...");
		}
	}
	
	private class DownloadExamTask extends AsyncTask<String, Void, String> {
		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			Log.i(LOG_TAG, "onPreExecute() called");
			progressDialog = ProgressDialog.show(ExamListActivity.this, null, "Download Exam data...", true, false); 
		}
		
	    @Override
	    protected String doInBackground(String... urls) {
//	    	InputStream inputStream = downloadUrl(urls[0]);//get inputStream from server
	    	InputStream inputStream = LoginActivity.class.getClassLoader().getResourceAsStream(downloadExamFile);
	    	//save stream to file
	    	try {
				saveFile(downloadExamFilePath, downloadExamFile, inputStream2String(inputStream));
			} catch (IOException e) {
				Log.i(LOG_TAG,"IOException:" + e.getMessage());
				return "failure";
			}
	        return "success";
	    }
	
	    @Override
	    protected void onPostExecute(String result) {
			progressDialog.dismiss();
			if("success".equals(result)){
				//get first question in paper
//				InputStream inputStream =  PapersActivity.class.getClassLoader().getResourceAsStream("sample_paper.xml");
				FileInputStream inputStream;
				try {
					inputStream = new FileInputStream(new File(downloadExamFilePath+ File.separator+downloadExamFile));
					questionType = XMLParseUtil.readQuestionType(inputStream,1,1);
		        	inputStream.close();
				} catch (FileNotFoundException e1) {
					Log.i(LOG_TAG,"FileNotFoundException:" + e1.getMessage());
				} catch (Exception e) {
					Log.i(LOG_TAG,"Exception:" + e.getMessage());
				}
	
				//move question
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(getccIndex()));
				intent.putExtra("cqIndex", String.valueOf(getcqIndex()));
				
				if(questionTypeM.equals(questionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( getBaseContext(), MultiChoices.class);
					startActivity(intent);
				}else if(questionTypeS.equals(questionType)){
					intent.putExtra("questionType", "Single Select");
					intent.setClass( getBaseContext(), SingleChoices.class);
					startActivity(intent);
				}else{
					ShowDialog("Invalid qeustion type:"+questionType);
				}
				
				//save exam start time
				saveStartTime();
				   					
			}
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