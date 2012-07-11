package com.dream.eexam.base;

import com.dream.eexam.paper.MultiChoices;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SessionsDemoVote extends BaseActivity {
	private static final String LOG_TAG = "SessionsDemoVote";
	
	private TextView spinnerText = null;
	private TextView spinnerText2 = null;
	private TextView spinnerText3 = null;
	
	private String[] exams = {"SCJP1","SCJP2","SCJP2","SCJP3","SCJP4"};
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	
	private Button startBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		
		setContentView(R.layout.sessions_demo_vote);
		
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
		
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setText("Start");
		startBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				
				//go to question 1
				Intent intent = new Intent();
				intent.setClass( getBaseContext(), MultiChoices.class);
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