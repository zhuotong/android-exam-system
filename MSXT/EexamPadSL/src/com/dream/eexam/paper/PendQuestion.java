package com.dream.eexam.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.R;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import com.dream.exam.bean.Catalog;
import com.dream.exam.bean.Exam;
import com.dream.exam.bean.ExamXMLParse;
import com.dream.exam.bean.Question;

public class PendQuestion extends BaseActivity {
	
	public final static String LOG_TAG = "PendQuestions";

	GridView gridList;
    String qType = null;
    String qid = null;
	
	//data statement
	PendQueListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	Exam exam;
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
//		// get question
//		cId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_CATALOG_ID,sharedPreferences);
//		if (cId == 0) cId = 1;
//		qId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_QUESTON_ID,sharedPreferences);
//		if (qId == 0) qId = 1;
//    	question = ExamParse.getQuestion(exam, cId, qId);
	}
	
	List<Question> pendQuestions = new ArrayList<Question>();
	public void loadAnswer(){
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
    	//load pending questions
		for(Catalog catalog: exam.getCatalogs()){
			List<Question> questions = catalog.getQuestions();
			for(Question question: questions){
				Cursor cursor2 = dbUtil.fetchAnswer(catalog.getIndex(),question.getIndex());
				if(cursor2.moveToNext()){
					continue;
				}
				cursor2.close();
				pendQuestions.add(question);
			}
		}
		dbUtil.close();		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_questions);
        mContext = getApplicationContext();
        //set List
        gridList = (GridView)findViewById(R.id.gridview);
        adapter = new PendQueListAdapter(pendQuestions);
        gridList.setAdapter(adapter);
        gridList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
			}      	
        });
    }
    
    class PendQueListAdapter extends BaseAdapter{
    	List<Question> questions = new ArrayList<Question>();
    	private LayoutInflater mInflater;
    	public PendQueListAdapter(List<Question> questions){
    		this.questions = questions;
    		mInflater = LayoutInflater.from(mContext);  
    	}
		@Override
		public int getCount() {
			return questions.size();
		}
		@Override
		public Object getItem(int position) {
			return questions.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(LOG_TAG,"getView()..."+" position="+position);
			ViewHolder holder;  
            if (convertView == null){  
                holder = new ViewHolder();  
                convertView = mInflater.inflate(R.layout.pending_questions_item, null);  
                holder.questionBtn = (Button) convertView.findViewById(R.id.questionBtn);  
                convertView.setTag(holder);  
            }else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            Question question = questions.get(position);
            qid = question.getQuestionid();
            holder.questionBtn.setText(question.getIndex());  
            holder.questionBtn.setOnClickListener(new Button.OnClickListener() {
    			public void onClick(View v) {
    				
    			}
    		});
            return convertView; 
		}
    }
    
    static class ViewHolder{
    	Button questionBtn;
    }
	
}
