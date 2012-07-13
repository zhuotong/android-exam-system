package com.dream.eexam.paper;

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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.QuestionsAll;
import com.dream.eexam.base.QuestionsWaiting;
import com.dream.eexam.base.R;
import com.dream.eexam.model.Choice;
import com.dream.eexam.model.Question;

public class SingleChoices extends BaseActivity {

	//set question sub header
	private TextView currentTV = null;
	private TextView allTV = null;
	private TextView waitTV = null;
	
	private TextView questionTV = null;
	
	//LinearLayout listLayout
	ListView listView;
	
	//LinearLayout listFooter
	private Button preBtn;
	
	private Question question;
	List<Choice> choices = new ArrayList<Choice>();
	
	Context mContext;
	MyListAdapter adapter;
	List<Integer> listItemID = new ArrayList<Integer>();

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paper_single_choices);
        mContext = getApplicationContext();
        
        //hard code data
        List<Choice> choices = new ArrayList<Choice>();
    	choices.add(new Choice("A", "22"));
    	choices.add(new Choice("B", "78"));
    	choices.add(new Choice("C", "1"));
    	choices.add(new Choice("D", "100"));
        question = new Question(1,0, "What is the result? ",choices);
        
        //set question text
    	currentTV = (TextView)findViewById(R.id.header_tv_current);
    	currentTV.setBackgroundColor(Color.parseColor("#4428FF"));
    	currentTV.setText("Current(2)");
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
    	
        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setText(question.getQuestionDesc());
        questionTV.setTextColor(Color.BLACK);
        
        //set List
        listView = (ListView)findViewById(R.id.lvChoices);
        adapter = new MyListAdapter(choices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		RadioButton cb = (RadioButton)view.findViewById(R.id.list_select);
        		cb.setChecked(!cb.isChecked());
			}      	
        });
        
        preBtn = (Button)findViewById(R.id.preBtn);
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//go to question 1
				Intent intent = new Intent();
				intent.setClass( mContext, MultiChoices.class);
				startActivity(intent);
			}
		});
    
    }
    
    class MyListAdapter extends BaseAdapter{
    	List<Boolean> mChecked = new ArrayList<Boolean>();
    	List<Choice> choices = new ArrayList<Choice>();
    	
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<Choice> choices){
//    		choices = new ArrayList<Choice>();
    		
    		this.choices = choices;
    		
    		for(int i=0;i<choices.size();i++){
    			mChecked.add(false);
    		}
    	}

		@Override
		public int getCount() {
			return choices.size();
		}

		@Override
		public Object getItem(int position) {
			return choices.get(position);
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
				view = mInflater.inflate(R.layout.paper_single_choices_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
				holder.radioButton = (RadioButton)view.findViewById(R.id.radioButton);
				holder.index = (TextView)view.findViewById(R.id.index);
				holder.choiceDesc = (TextView)view.findViewById(R.id.choiceDesc);
				
				final int p = position;
				map.put(position, view);
				
				holder.radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						RadioButton cb = (RadioButton)buttonView;
						mChecked.set(p, cb.isChecked());
					}
				});
				
				view.setTag(holder);
			}else{
				Log.i(LOG_TAG,"position2 = "+position);
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			
			Choice choice = choices.get(position);
			holder.radioButton.setChecked(mChecked.get(position));
			holder.index.setText(String.valueOf(choice.getChoiceIndex()));
			holder.choiceDesc.setText(choice.getChoiceDesc());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	RadioButton radioButton;
    	TextView index;
    	TextView choiceDesc;
    }
}
