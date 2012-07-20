package com.dream.eexam.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.dream.eexam.model.QuestionProgress;

public class MultiChoices extends BaseActivity {

	//set question sub header
	private TextView catalogsTV = null;
	private TextView currentTV = null;
	private TextView allTV = null;
	private TextView waitTV = null;
	
	private TextView questionTV = null;
	
	//LinearLayout listLayout
	ListView listView;
	
	//LinearLayout listFooter
	private Button preBtn;
	private Button nextBtn;
	
	private Question question;
	
	Context mContext;
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	
	SharedPreferences sharedPreferences;
	QuestionProgress qp ;

	public void setSubHeader(){
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		qp = getQuestionProgress(sharedPreferences);

		//set question text
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
//		catalogsTV.setText(String.valueOf(qp.getCurrentQueIndex()));
		catalogsTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
				if(pressedItemIndex!=-1){
					catalogsTV.setText(groups.get(pressedItemIndex));
				}
			}
		});

		//set question text
    	currentTV = (TextView)findViewById(R.id.header_tv_current);
    	currentTV.setText("Q "+String.valueOf(qp.getCurrentQueIndex()));
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
    	allTV.setText("All " +String.valueOf(qp.getQuesCount()));
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
    	waitTV.setText("Wait "+ String.valueOf(qp.getWaitingQueIdsList().size()));
    	waitTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//go to question 1
				Intent intent = new Intent();
				intent.setClass( mContext, QuestionsWaiting.class);
				startActivity(intent);
			}
		});  
        
	
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paper_multi_choices);
        mContext = getApplicationContext();

        setSubHeader();
        
        //hard code data
        List<Choice> choices = new ArrayList<Choice>();
    	choices.add(new Choice("A", "goto"));
    	choices.add(new Choice("B", "malloc"));
    	choices.add(new Choice("C", "extends"));
    	choices.add(new Choice("D", "FALSE"));
        question = new Question(1,1, "Which of the following are Java keywords?",choices);
        
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
        		CheckBox cb = (CheckBox)view.findViewById(R.id.list_select);
        		cb.setChecked(!cb.isChecked());
			}      	
        });
        
        preBtn = (Button)findViewById(R.id.preBtn);
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				preBtn.setBackgroundResource(R.drawable.btn_sys);
//				listItemID.clear();
//				for(int i=0;i<adapter.mChecked.size();i++){
//					if(adapter.mChecked.get(i)){
//						Choice choice = adapter.choices.get(i);
//						listItemID.add(Integer.valueOf(choice.getId()));
//					}
//				}
//				if(listItemID.size()==0){
//					ShowDialog("At lease choose one item!");
//				}else{
//					if(preBtn.isEnabled()){
//
//					}
//				}
			}
		});
        
        nextBtn = (Button)findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listItemID.clear();
				for(int i=0;i<adapter.mChecked.size();i++){
					if(adapter.mChecked.get(i)){
						Choice choice = adapter.choices.get(i);
						listItemID.add(choice.getChoiceIndex());
					}
				}
				
				if(listItemID.size()==0){
					AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
					builder.setMessage("Answer this question late?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											//save data
											int currentQueIndex = qp.getCurrentQueIndex().intValue();
											qp.setCurrentQueIndex(currentQueIndex+1);
											saveQuestionProgress(sharedPreferences,qp);
											
											//go to question 1
											Intent intent = new Intent();
											intent.setClass( mContext, SingleChoices.class);
											startActivity(intent);
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											dialog.cancel();
										}
									});
					builder.show();
				}else{
					//save data
					qp.setCurrentQueIndex(qp.getCurrentQueIndex().intValue()+1);
					saveQuestionProgress(sharedPreferences,qp);
					
					//go to question 1
					Intent intent = new Intent();
					intent.setClass( mContext, SingleChoices.class);
					startActivity(intent);					
				}

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
				view = mInflater.inflate(R.layout.paper_multi_choices_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
				holder.selected = (CheckBox)view.findViewById(R.id.list_select);
				holder.index = (TextView)view.findViewById(R.id.list_index);
				holder.choiceDesc = (TextView)view.findViewById(R.id.list_choiceDesc);
				
				final int p = position;
				map.put(position, view);
				
				holder.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						
						CheckBox cb = (CheckBox)buttonView;
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
			holder.selected.setChecked(mChecked.get(position));
			holder.index.setText(choice.getChoiceIndex());
			holder.choiceDesc.setText(choice.getChoiceDesc());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	CheckBox selected;
    	TextView index;
    	TextView choiceDesc;
    }
}
