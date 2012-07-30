package com.dream.eexam.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.dream.eexam.model.ExamBaseBean;
import com.dream.eexam.model.InterviewBean;
import com.dream.eexam.model.Paper;
import com.dream.eexam.model.Question;
import com.dream.eexam.model.QuestionProgress;
import com.dream.eexam.paper.MultiChoices;
import com.dream.eexam.paper.SingleChoices;
import com.dream.eexam.util.XMLParseUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class PapersActivity extends BaseActivity {
	private static final String LOG_TAG = "SessionsDemoVote";
	
	private TextView spinnerText = null;
	private TextView spinnerText2 = null;
	private TextView spinnerText3 = null;
	
	
	
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	
	private Button startBtn;
	private SharedPreferences sharedPreferences;
	
	InterviewBean bean;
	String[] exams = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		
		setContentView(R.layout.papers);
		
		//get demoSessionStr and save to string array
		Bundle bundle = this.getIntent().getExtras();
		String path  = bundle.getString("path");
		String fileName  = bundle.getString("fileName");
		
		Log.i(LOG_TAG,"path:"+path);
		Log.i(LOG_TAG,"fileName:"+fileName);
		
		//load paper List
//	    InputStream inputStream =  PapersActivity.class.getClassLoader().getResourceAsStream("sample_login_success.xml");
//	    InputStream inputStream =  PapersActivity.class.getClassLoader().getResourceAsStream(path+ File.separator+fileName);

		try {
	    	File file = new File(path+ File.separator+fileName);
	    	FileInputStream inputStream = new FileInputStream(file);
	    	bean = XMLParseUtil.readLoginSuccess(inputStream);
		} catch (Exception e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		
		List<ExamBaseBean> examList = bean.getExamList();
		if(examList!=null&&examList.size()>0){
			exams = new String[examList.size()];
			for(int i=0;i<examList.size();i++){
				exams[i] = examList.get(i).getName();
			}
		}
		
		spinnerText = (TextView) this.findViewById(R.id.spinnerText);
		spinnerText.setText("You applied job title:");
		
		spinnerText2 = (TextView) this.findViewById(R.id.spinnerText2);
		spinnerText2.setText("Java Software Engineer");
		
		spinnerText3 = (TextView) this.findViewById(R.id.spinnerText3);
		spinnerText3.setText("Choose 1 from below paper list:");
		
		spinner = (Spinner) findViewById(R.id.Spinner01);
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,exams);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner.setVisibility(View.VISIBLE);
		
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE); 
		
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setText("Start");
		startBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				
				//todo download paper
				
				//get first question in paper
				InputStream inputStream =  PapersActivity.class.getClassLoader().getResourceAsStream("sample_paper.xml");
				String questionType = null;
		        try {
		        	questionType = XMLParseUtil.readQuestionType(inputStream,1,1);
		        	inputStream.close();
				} catch (Exception e) {
					Log.i(LOG_TAG,e.getMessage());
				}

				//move question
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(getccIndex()));
				intent.putExtra("cqIndex", String.valueOf(getcqIndex()));
				
				if("Choice:M".equals(questionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( getBaseContext(), MultiChoices.class);
				}else if("Choice:S".equals(questionType)){
					intent.putExtra("questionType", "Single Select");
					intent.setClass( getBaseContext(), SingleChoices.class);
				}
				startActivity(intent);
				
			}			
		});
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			selectedSession = demoSessions[arg2];
		}
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
}