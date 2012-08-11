package com.dream.eexam.paper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.TextView;
import com.dream.eexam.base.R;
import com.dream.eexam.model.Question;
import com.dream.eexam.util.TimeDateUtil;

public class PendQuestions extends BaseQuestion {
	
	public final static String LOG_TAG = "MultiChoices";

	GridView gridList;
	
    String qType = null;
    Integer cid = null;
    Integer qid = null;
	
	Button preBtn;
	TextView questionIndex;
	Button nextBtn;
	
	//data statement
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	public void loadComponents(){
		homeTV = (TextView)findViewById(R.id.homeTV);
		remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		submitTV = (TextView)findViewById(R.id.submitTV);
		
		questionIndex = (TextView)findViewById(R.id.questionIndex);
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		pendQueNumber = (TextView)findViewById(R.id.pendQueNumber);
		
    	preBtn = (Button)findViewById(R.id.preBtn);
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);   	
    	nextBtn = (Button)findViewById(R.id.nextBtn);
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

        //set catalog bar(Left) 
        String questionIndexDesc = "Question "+ String.valueOf(currentQuestionIndex) +"/"+ String.valueOf(totalQuestions);
        questionIndex.setText(questionIndexDesc);
		questionIndex.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(currentCatalogIndex));
				intent.putExtra("cqIndex", String.valueOf(currentQuestionIndex));
				if(questionTypeM.equals(questionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( getBaseContext(), MultiChoices.class);
				}else if(questionTypeS.equals(questionType)){
					intent.putExtra("questionType", "Single Select");
					intent.setClass( getBaseContext(), SingleChoices.class);
				}
				finish();
				startActivity(intent);
			}
		});
        
        //set catalog bar(Center) 
        //set catalog bar(Center) 
		catalogsTV.setText(String.valueOf(currentCatalogIndex)+". "+
				detailBean.getCatalogDescByCid(currentCatalogIndex) + 
				"(Q" + String.valueOf(currentQuestionIndex)+" - " + "Q" + String.valueOf(currentQuestionIndex+questionSize-1)+")");
		catalogsTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
		
		 //set catalog bar(Right) 
		pendQueNumber.setText("Pending("+Integer.valueOf(pendQuestions.size())+")");
		pendQueNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(currentCatalogIndex));
				intent.putExtra("cqIndex", String.valueOf(currentQuestionIndex));
				if(questionTypeM.equals(questionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( getBaseContext(), PendQuestions.class);
				}else if(questionTypeS.equals(questionType)){
					intent.putExtra("questionType", "Single Select");
					intent.setClass( getBaseContext(), PendQuestions.class);
				}
				finish();
				startActivity(intent);
			}
		});

	
	}
	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_questions);
        mContext = getApplicationContext();
        loadComponents();
        loadAnswer();
        setHeader();
        //set List
        gridList = (GridView)findViewById(R.id.gridview);
        adapter = new MyListAdapter(pendQuestions);
        gridList.setAdapter(adapter);
        gridList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {

			}      	
        });
        setFooter();
    }
    
    public void setFooter(){
    	//set preBtn
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = -1;
				gotoNewQuestion(mContext,direction);
			}
		});
        
		//set completedSeekBar
		int per = 100 * answeredQuestions/totalQuestions;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"%");
		//set nextBtn
        nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = 1;
				gotoNewQuestion(mContext,direction);
			}
		});
    }
    
    //go to next or previous question
    public void gotoNewQuestion(){
    	Log.i(LOG_TAG, "gotoNewQuestion()...");
		
		Question newQuestion = detailBean.getQuestionByQid(currentQuestionIndex+direction);
		String newQuestionType = newQuestion.getQuestionType();
		if(newQuestionType!=null){
			//move question
			Intent intent = new Intent();
			intent.putExtra("ccIndex", String.valueOf(newQuestion.getCatalogIndex()));
			intent.putExtra("cqIndex", String.valueOf(currentQuestionIndex+direction));
			if(questionTypeM.equals(questionType)){
				intent.putExtra("questionType", "Multi Select");
				intent.setClass( getBaseContext(), MultiChoices.class);
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
    	private LayoutInflater mInflater;
    	
    	public MyListAdapter(List<Question> questions){
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
            qType = question.getQuestionType();
            cid = question.getCatalogIndex();
            qid = question.getIndex();
            
            holder.questionBtn.setText(String.valueOf(question.getIndex()));  
            holder.questionBtn.setOnClickListener(new Button.OnClickListener() {
    			public void onClick(View v) {
    				Log.i(LOG_TAG,"onClick()...");
    				
    				Button sButton = (Button)v;
//    	            String[] cidAndQids = sButton.getText().toString().split(",");
    	            String qid = sButton.getText().toString();
    	            Question nQuestion = detailBean.getQuestionByQid(Integer.valueOf(qid));
    	            
    				//move question
    				Intent intent = new Intent();
    				intent.putExtra("ccIndex", String.valueOf(nQuestion.getCatalogIndex()));
    				intent.putExtra("cqIndex", String.valueOf(nQuestion.getIndex()));
    				
    				if(questionTypeM.equals(qType)){
    					intent.putExtra("questionType", "Multi Select");
    					intent.setClass( getBaseContext(), MultiChoices.class);
    					startActivity(intent);
    				}else if(questionTypeS.equals(qType)){
    					intent.putExtra("questionType", "Single Select");
    					intent.setClass( getBaseContext(), SingleChoices.class);
    					startActivity(intent);
    				}else{
    					ShowDialog("Invalid qeustion type:"+questionType);
    				}
    				
//    				gotoNewQuestion();
    			}
    		});
            return convertView; 
		}
    	
    }
    
    static class ViewHolder{
    	Button questionBtn;
    }
}
