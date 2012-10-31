package com.dream.ivpc;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.model.ChoiceBean;
import com.dream.ivpc.model.QuestionDetailBean;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChoiceList extends BaseActivity {

	protected Context mContext;
	protected ListView listView;
	
	ImageView imgGoBack = null;
	TextView qContent;
	TextView qAnswer;
	
	ChoiceListAdapter adapter;
	List<ChoiceBean> choiceList;

	public QuestionDetailBean getQuestionDetail(){
		return new QuestionDetailBean("Which of following is not Java key work?-----------------------------","A,B");
	}
	
	public List<ChoiceBean> getChoiceList(){
		List<ChoiceBean> choiceList = new ArrayList<ChoiceBean>();
		ChoiceBean beanA = new ChoiceBean("A","private");
		ChoiceBean beanB = new ChoiceBean("B","protected");
		ChoiceBean beanC = new ChoiceBean("C","public");
		ChoiceBean beanD = new ChoiceBean("D","static");
		choiceList.add(beanA);
		choiceList.add(beanB);
		choiceList.add(beanC);
		choiceList.add(beanD);
		return choiceList;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.candidate_questiondetail);
		mContext = getApplicationContext();

        imgGoBack = (ImageView) findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(goBackListener);
        
		QuestionDetailBean bean = getQuestionDetail();
		qContent = (TextView)findViewById(R.id.qcontent);
		qContent.setText(bean.getQcontent());
		
		qAnswer = (TextView)findViewById(R.id.qanswer);
		qAnswer.setText(bean.getQanswer());
		
		choiceList = getChoiceList();
        //set List
        listView = (ListView)findViewById(R.id.choice_list);
        adapter = new ChoiceListAdapter(choiceList,mContext);
        listView.setAdapter(adapter);
	}
	
    View.OnClickListener goBackListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Intent intent = new Intent();
			intent.setClass( mContext, CandidateInfoExamDetail.class);
			startActivity(intent);  
        }  
    };

}
