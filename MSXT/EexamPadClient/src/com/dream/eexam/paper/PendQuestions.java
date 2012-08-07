package com.dream.eexam.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.base.R;
import com.dream.eexam.model.Question;

public class PendQuestions extends BaseQuestion {
	
	public final static String LOG_TAG = "MultiChoices";

	//components statement
	TextView questionHintTV = null;
	TextView questionTV = null;

	ListView gridList;
	
	Button preBtn;
	TextView questionIndex;
	Button nextBtn;
	
	//data statement
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	public void loadHeader(){
		homeTV = (TextView)findViewById(R.id.homeTV);
		
		remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);
		
		submitTV = (TextView)findViewById(R.id.submitTV);
		
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		waitTV = (TextView)findViewById(R.id.header_tv_waiting);
	}
	
	public void setHeader(){
		//set exam header(Left)
		homeTV.setText("Home");
		
		//set exam header(Center)
		remainingTimeLabel.setText("Time Remaining: ");
		remainingTime.setText(String.valueOf(detailBean.getTime())+" mins");
		
		//set exam header(Right)
		submitTV.setText("Submit");
        submitTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "submitTV.onClick()...");
				
		    	int waitQuestions = totalQuestions - answeredQuestions;
				 
				if (waitQuestions> 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(PendQuestions.this);
					builder.setMessage(String.valueOf(waitQuestions) + " question(s) are not answered, still submit?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											submitAnswer();
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											dialog.cancel();
										}
									});
					builder.show();
				} else {
					submitAnswer();
				}
			}
		});

		//set exam sub header
		catalogsTV.setText(String.valueOf(currentCatalogIndex)+". "+
				detailBean.getCatalogDescByCid(currentCatalogIndex) + 
				"(Q" + String.valueOf(currentQuestionIndex)+" - " + "Q" + String.valueOf(questionSize)+")");
//				"("+String.valueOf(detailBean.getCatalogSizeByCid(currentCatalogIndex))+")");
		catalogsTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
    	
        
        //set question text
    	waitTV.setText("Wait "+ String.valueOf(questionSize - comQuestionSize));
    	waitTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});  
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paper_multi_choices);
        mContext = getApplicationContext();

        loadHeader();
        loadAnswer();
        setHeader();
		
        String questionHint = "Q "+String.valueOf(question.getIndex())+"(Score:"+String.valueOf(question.getScore())+")";
        Log.i(LOG_TAG, "questionHint:"+questionHint);
        
        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setText(questionHint+ "\n"+ question.getContent());
        questionTV.setTextColor(Color.BLACK);	
        
        //set List
        gridList = (ListView)findViewById(R.id.lvChoices);
        adapter = new MyListAdapter(pendQuestions);
        gridList.setAdapter(adapter);
        gridList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {

			}      	
        });
    
    }
    
    //go to next or previous question
    public void gotoNewQuestion(){
    	Log.i(LOG_TAG, "gotoNewQuestion()...");
		
		Question newQuestion = detailBean.getQuestionByCidQid(currentCatalogIndex, currentQuestionIndex+direction);
		String newQuestionType = newQuestion.getQuestionType();
		if(newQuestionType!=null){
			//move question
			Intent intent = new Intent();
			intent.putExtra("ccIndex", String.valueOf(currentCatalogIndex));
			intent.putExtra("cqIndex", String.valueOf(currentQuestionIndex+direction));
			if(questionTypeM.equals(questionType)){
				intent.putExtra("questionType", "Multi Select");
				intent.setClass( getBaseContext(), PendQuestions.class);
			}else if(questionTypeS.equals(questionType)){
				intent.putExtra("questionType", "Single Select");
				intent.setClass( getBaseContext(), SingleChoices.class);
			}
			finish();
			startActivity(intent);
		}else{
			ShowDialog("Please Change Catalog!");
		}
    }
    
    class MyListAdapter extends BaseAdapter{
    	List<Question> questions = new ArrayList<Question>();
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<Question> questions){
    		this.questions = questions;
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
			View view;
			ViewHolder holder = null;
			if (map.get(position) == null) {
				Log.i(LOG_TAG,"position1 = "+position);
				
				LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mInflater.inflate(R.layout.pending_questions_item, null);
				holder = new ViewHolder();
				//set 3 component 
				holder.questionBtn = (CheckBox)view.findViewById(R.id.questionBtn);
				final int p = position;
				map.put(position, view);
				holder.questionBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
				view.setTag(holder);
			}else{
				Log.i(LOG_TAG,"position2 = "+position);
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			Question question = questions.get(position);
			holder.questionBtn.setText(String.valueOf(question.getIndex()));
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	Button questionBtn;
    }
}
