package com.dream.ivpc;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.model.QuestionBean;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CandidateInfoExamDetail extends CandidateInfoBase {
	public final static String LOG_TAG = "CandidateInfoExamDetail";

	ImageView imgGoBack = null;
	
	List<QuestionBean> questionList = new ArrayList<QuestionBean>();
	ListView listView;
	QuestionListAdapter adapter;	
	
	public List<QuestionBean> getQuestionList(){
		List<QuestionBean> questionList = new ArrayList<QuestionBean>();
		QuestionBean q1= new QuestionBean(1,"Catalog 1","Question 1",true);
		QuestionBean q2= new QuestionBean(2,"Catalog 1","Question 2",true);
		QuestionBean q3= new QuestionBean(3,"Catalog 1","Question 3",true);
		QuestionBean q4= new QuestionBean(4,"Catalog 2","Question 4",false);
		QuestionBean q5= new QuestionBean(5,"Catalog 2","Question 5",true);
		QuestionBean q6= new QuestionBean(6,"Catalog 2","Question 6",false);
		QuestionBean q7= new QuestionBean(7,"Catalog 3","Question 7",true);
		QuestionBean q8= new QuestionBean(8,"Catalog 3","Question 8",true);
		QuestionBean q9= new QuestionBean(9,"Catalog 3","Question 9",true);
		QuestionBean q10= new QuestionBean(10,"Catalog 4","Question 10",false);
		QuestionBean q11= new QuestionBean(11,"Catalog 4","Question 11",true);
		QuestionBean q12= new QuestionBean(12,"Catalog 4","Question 12",true);
		QuestionBean q13= new QuestionBean(13,"Catalog 5","Question 13",true);
		QuestionBean q14= new QuestionBean(14,"Catalog 5","Question 14",true);
		QuestionBean q15= new QuestionBean(15,"Catalog 5","Question 15",false);
		QuestionBean q16= new QuestionBean(16,"Catalog 6","Question 16",true);
		QuestionBean q17= new QuestionBean(17,"Catalog 6","Question 17",true);
		QuestionBean q18= new QuestionBean(18,"Catalog 6","Question 18",true);
		
		questionList.add(q1);
		questionList.add(q2);
		questionList.add(q3);
		questionList.add(q4);
		questionList.add(q5);
		questionList.add(q6);
		questionList.add(q7);
		questionList.add(q8);
		questionList.add(q9);
		questionList.add(q10);
		questionList.add(q11);
		questionList.add(q12);
//		questionList.add(q13);
//		questionList.add(q14);
//		questionList.add(q15);
//		questionList.add(q16);
//		questionList.add(q17);
//		questionList.add(q18);
		
		return questionList;
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_info_examdetail);
 
        mContext = getApplicationContext();
        
        imgGoBack = (ImageView) findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(goBackListener);
        
        questionList = getQuestionList();
        
        //set List
        listView = (ListView)findViewById(R.id.question_list);
        adapter = new QuestionListAdapter(questionList,mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
//            	Intent intent = new Intent();
//    			intent.setClass( mContext, CandidateInfoResume.class);
//    			startActivity(intent);  
			}      	
        });
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
