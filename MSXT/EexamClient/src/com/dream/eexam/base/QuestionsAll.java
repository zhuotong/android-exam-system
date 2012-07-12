package com.dream.eexam.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.R;
import com.dream.eexam.model.Answer;
import com.dream.eexam.paper.MultiChoices;

public class QuestionsAll extends BaseActivity {

	//set question sub header
	private TextView currentTV = null;
	private TextView allTV = null;
	private TextView waitTV = null;
	
	//LinearLayout listLayout
	ListView listView;
	
	List<Answer> answers = new ArrayList<Answer>();
	Context mContext;
	MyListAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_all);
        mContext = getApplicationContext();
        
        //hard code data
        answers.add(new Answer(1, 1,"A,B"));
        answers.add(new Answer(2, 2,"A"));
        answers.add(new Answer(3, 3,"C,D"));
        answers.add(new Answer(4, 4,"A,C,D"));
        answers.add(new Answer(5, 5,"B"));
        answers.add(new Answer(6, 6,"D,E"));
        answers.add(new Answer(7, 7,"E"));
        answers.add(new Answer(8, 8,"A,B,E"));
        answers.add(new Answer(9, 9,"C"));
        answers.add(new Answer(10, 10,"D"));
        
        //set question text
    	currentTV = (TextView)findViewById(R.id.header_tv_current);
    	currentTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//go to question 1
				Intent intent = new Intent();
				intent.setClass( mContext, MultiChoices.class);
				startActivity(intent);
			}
		});
        //set question text
    	allTV = (TextView)findViewById(R.id.header_tv_all);
    	allTV.setBackgroundColor(Color.parseColor("#4428FF"));
    	allTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//go to question 1
				Intent intent = new Intent();
				intent.setClass( mContext, QuestionsAll.class);
				startActivity(intent);
			}
		});
    	
        //set question text
    	waitTV = (TextView)findViewById(R.id.header_tv_waiting);
    	waitTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//go to question 1
				Intent intent = new Intent();
				intent.setClass( mContext, QuestionsWaiting.class);
				startActivity(intent);
			}
		});  
        
        
        //set List
        listView = (ListView)findViewById(R.id.questionsAll);
        adapter = new MyListAdapter(answers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {

			}      	
        });
    }
    
    class MyListAdapter extends BaseAdapter{
    	List<Answer> answers = new ArrayList<Answer>();
    	
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<Answer> answers){
    		this.answers = answers;
    	}

		@Override
		public int getCount() {
			return answers.size();
		}

		@Override
		public Object getItem(int position) {
			return answers.get(position);
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
				view = mInflater.inflate(R.layout.questions_all_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
//				holder.index = (TextView)view.findViewById(R.id.list_index);
				holder.questionDesc = (TextView)view.findViewById(R.id.list_questionDesc);
				holder.answerDesc = (TextView)view.findViewById(R.id.list_answerDesc);
				
//				final int p = position;
				map.put(position, view);
				
				view.setTag(holder);
			}else{
				Log.i(LOG_TAG,"position2 = "+position);
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			
			Answer answer = answers.get(position);
//			holder.index.setText(String.valueOf(answer.getId()));
			holder.questionDesc.setText(answer.getQuestionId()+" Q:");
			holder.answerDesc.setText("A:("+answer.getChoiceIdsString()+")");
			
			return view;
		}
    	
    }

    static class ViewHolder{
//    	TextView index;
    	TextView questionDesc;
    	TextView answerDesc;
    }
 
}
