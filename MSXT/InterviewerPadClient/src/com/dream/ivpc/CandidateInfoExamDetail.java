package com.dream.ivpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dream.ivpc.adapter.QuestionListAdapter;
import com.dream.ivpc.model.QuestionDetailBean;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CandidateInfoExamDetail extends CandidateInfoBase {
	public final static String LOG_TAG = "CandidateInfoExamDetail";

	ImageView imgGoBack = null;
	
	
	ImageView indexSortIcon = null;
	ImageView catalogSortIcon = null;
	ImageView qnameSortIcon = null;
	ImageView qresultSortIcon = null;
	
	int indexSortFlag = -1;
	int catalogSortFlag = -1;
	int qnameSortFlag = -1;
	int qresultSortFlag = -1;
	
	List<QuestionDetailBean> questionList = new ArrayList<QuestionDetailBean>();
	ListView listView;
	QuestionListAdapter adapter;	
	
//	Button examdetail;
	
	public List<QuestionDetailBean> getQuestionList(){
		List<QuestionDetailBean> questionList = new ArrayList<QuestionDetailBean>();
		QuestionDetailBean q1= new QuestionDetailBean(1,"Catalog 1","Question 1",true,"kjssssssssssssssssssssssssssssssssssssssssjssssssssssssssssssssssssssssssssssssssssjssssssssssssssssssssssssssssssssssssssssjssssssssssssssssssssssssssssssssssssssssjssssssssssssssssssssssssssssssssssssssssjssssssssssssssssssssssssssssssssssssssss...","A,B,C");
		QuestionDetailBean q2= new QuestionDetailBean(2,"Catalog 1","Question 2",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q3= new QuestionDetailBean(3,"Catalog 1","Question 3",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q4= new QuestionDetailBean(4,"Catalog 2","Question 4",false,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q5= new QuestionDetailBean(5,"Catalog 2","Question 5",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q6= new QuestionDetailBean(6,"Catalog 2","Question 6",false,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q7= new QuestionDetailBean(7,"Catalog 3","Question 7",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q8= new QuestionDetailBean(8,"Catalog 3","Question 8",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q9= new QuestionDetailBean(9,"Catalog 3","Question 9",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q10= new QuestionDetailBean(10,"Catalog 4","Question 10",false,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q11= new QuestionDetailBean(11,"Catalog 4","Question 11",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q12= new QuestionDetailBean(12,"Catalog 4","Question 12",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q13= new QuestionDetailBean(13,"Catalog 5","Question 13",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q14= new QuestionDetailBean(14,"Catalog 5","Question 14",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q15= new QuestionDetailBean(15,"Catalog 5","Question 15",false,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q16= new QuestionDetailBean(16,"Catalog 6","Question 16",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q17= new QuestionDetailBean(17,"Catalog 6","Question 17",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		QuestionDetailBean q18= new QuestionDetailBean(18,"Catalog 6","Question 18",true,"kjssssssssssssssssssssssssssssssssssssssss","A,B,C");
		
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
		questionList.add(q13);
		questionList.add(q14);
		questionList.add(q15);
		questionList.add(q16);
		questionList.add(q17);
		questionList.add(q18);
		
		return questionList;
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_info_examdetail);
 
        mContext = getApplicationContext();

        setHeader((TextView)findViewById(R.id.candidateInfo),(ImageView)findViewById(R.id.imgGoBack));
		setFooter((Button) findViewById(R.id.examdetail));
		
        imgGoBack = (ImageView) findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(goBackListener);
        
//        examdetail = (Button) findViewById(R.id.examdetail);
//        examdetail.setBackgroundResource(R.drawable.bg_footer_button_select);
        
        indexSortIcon = (ImageView) findViewById(R.id.indexSortIcon);
        catalogSortIcon = (ImageView) findViewById(R.id.catalogSortIcon);
        qnameSortIcon = (ImageView) findViewById(R.id.qnameSortIcon);
        qresultSortIcon = (ImageView) findViewById(R.id.qresultSortIcon);
        
        questionList = getQuestionList();
		Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
                return Integer.valueOf(arg0.getIndex()).compareTo(Integer.valueOf(arg1.getIndex()));  
            }  
        });
        
        //set List
        listView = (ListView)findViewById(R.id.question_list);
        adapter = new QuestionListAdapter(questionList,mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
            	Intent intent = new Intent();
    			intent.setClass( mContext, ChoiceList.class);
    			startActivity(intent);  
			}      	
        });
    }
    
	public void sortEvent(View view){
		switch(view.getId()){
			case(R.id.indexSortTable): sortByIndex(); break;
			case(R.id.catalogSortTable): sortByCatalog(); break;
			case(R.id.qnameSortTable):sortByQname();break;
			case(R.id.qresultSortTable):sortByQresult();
		}
	    adapter = new QuestionListAdapter(questionList,mContext);
	    listView.setAdapter(adapter);
	}

	public void sortByIndex(){
		switch(indexSortFlag){
			case -1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return Integer.valueOf(arg0.getIndex()).compareTo(Integer.valueOf(arg1.getIndex()));  
		            }  
		        });	
				indexSortFlag = 1;	
				indexSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return Integer.valueOf(arg1.getIndex()).compareTo(Integer.valueOf(arg0.getIndex()));  
		            }  
		        });
				indexSortFlag = -1;
				indexSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		catalogSortIcon.setImageDrawable(null);
		qnameSortIcon.setImageDrawable(null);
		qresultSortIcon.setImageDrawable(null);
	}
	
	public void sortByCatalog(){
		switch(catalogSortFlag){
			case -1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return arg0.getCatalog().compareTo(arg1.getCatalog());  
		            }  
		        });	
				catalogSortFlag = 1;	
				catalogSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return arg1.getCatalog().compareTo(arg0.getCatalog());  
		            }  
		        });
				catalogSortFlag = -1;
				catalogSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		indexSortIcon.setImageDrawable(null);
		qnameSortIcon.setImageDrawable(null);
		qresultSortIcon.setImageDrawable(null);
	}
	
	public void sortByQname(){
		switch(qnameSortFlag){
			case -1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return arg0.getQuestionName().compareTo(arg1.getQuestionName());  
		            }  
		        });	
				qnameSortFlag = 1;	
				qnameSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return arg1.getQuestionName().compareTo(arg0.getQuestionName());  
		            }  
		        });
				qnameSortFlag = -1;
				qnameSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		indexSortIcon.setImageDrawable(null);
		catalogSortIcon.setImageDrawable(null);
		qresultSortIcon.setImageDrawable(null);
	}
	
	public void sortByQresult(){
		switch(qresultSortFlag){
			case -1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return Boolean.valueOf(arg0.isResult()).compareTo(Boolean.valueOf(arg1.isResult()));  
		            }  
		        });	
				qresultSortFlag = 1;	
				qresultSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(questionList,new Comparator<QuestionDetailBean>(){  
		            public int compare(QuestionDetailBean arg0, QuestionDetailBean arg1) {  
		                return Boolean.valueOf(arg1.isResult()).compareTo(Boolean.valueOf(arg0.isResult()));  
		            }  
		        });
				qresultSortFlag = -1;
				qresultSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		indexSortIcon.setImageDrawable(null);
		catalogSortIcon.setImageDrawable(null);
		qnameSortIcon.setImageDrawable(null);
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
