package com.dream.eexam.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dream.eexam.adapter.ChoiceAdapter;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.R;
import com.dream.eexam.util.SPUtil;
import com.dream.exam.bean.Exam;
import com.dream.exam.bean.ExamParse;
import com.dream.exam.bean.ExamXMLParse;
import com.dream.exam.bean.Question;

public class ChoiceQuestion extends BaseActivity {
	public final static String LOG_TAG = "ChoiceQuestion";
	
	TextView questionTV;
	Exam exam;
	Question question;
	
	ListView listView;
	ChoiceAdapter adapter;
	
	public void loadData(){
		//get exam
    	String home = SPUtil.getFromSP(SPUtil.CURRENT_APP_HOME, sharedPreferences);
    	String userId = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
    	String examId = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_ID, sharedPreferences);
    	FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(home+ File.separator+userId+ File.separator+examId));
			exam = ExamXMLParse.parseExam(inputStream);
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG,e.getMessage());
		}
		
		//get question
//		int cId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_CATALOG_ID, sharedPreferences);
//    	int qId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_QUESTON_ID, sharedPreferences);
    	question = ExamParse.getQuestion(exam, 1, 1);
	}
	
	void loadComponents(){
		ImageView backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeQuestion(1,1);
			}
		});  
		ImageView nextArrow = (ImageView)findViewById(R.id.nextArrow);
		nextArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeQuestion(1,2);
			}
		}); 
	}
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_list);
		mContext = getApplicationContext();
		
		loadData();
		loadComponents();
		
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setMovementMethod(ScrollingMovementMethod.getInstance()); 
        questionTV.setText(question.getContent());
        
        adapter = new ChoiceAdapter(mContext,question.getChoices(),question.getType(),"");
        listView = (ListView)findViewById(R.id.choicesListView);
        listView.setAdapter(adapter);
	}
	
	public void changeQuestion(int cId,int qId){
		question = ExamParse.getQuestion(exam,cId, qId);
		questionTV.setText(question.getContent());
		adapter = new ChoiceAdapter(mContext,question.getChoices(),question.getType(),"");
		listView.setAdapter(adapter);
	}

}
