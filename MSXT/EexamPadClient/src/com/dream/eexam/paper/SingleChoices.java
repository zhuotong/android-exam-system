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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.base.R;
import com.dream.eexam.model.Choice;

public class SingleChoices extends BaseQuestion {
	
	public final static String LOG_TAG = "SingleChoices";
	
	//components statement
	TextView questionTV = null;
	ListView listView;
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();

	public void loadComponents(){
		homeTV = (TextView)findViewById(R.id.homeTV);
		remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		submitTV = (TextView)findViewById(R.id.submitTV);
		
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);
		pendQueNumber = (TextView)findViewById(R.id.pendQueNumber);
		
    	preBtn = (Button)findViewById(R.id.preBtn);
    	questionIndex = (TextView)findViewById(R.id.questionIndex);
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
					AlertDialog.Builder builder = new AlertDialog.Builder(SingleChoices.this);
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
        setContentView(R.layout.paper_single_choices);
        mContext = getApplicationContext();
        
        loadComponents();
        loadAnswer();
        setHeader();
        
        String questionHint = "Q "+String.valueOf(question.getIndex())+" (Score:"+String.valueOf(question.getScore())+")";
        Log.i(LOG_TAG, "questionHint:"+questionHint);
    	
        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setText(questionHint+ "\n"+question.getContent());
        questionTV.setTextColor(Color.BLACK);
        
        //set List
        listView = (ListView)findViewById(R.id.lvChoices);
        adapter = new MyListAdapter(choices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		Log.i(LOG_TAG, "=================onItemClick()...===============");
        		
        		//get old status
        		RadioButton cb = (RadioButton)view.findViewById(R.id.radioButton);
        		boolean oldStatus = cb.isChecked();
        		Log.i(LOG_TAG, "oldStatus:"+String.valueOf(oldStatus));
        		
        		clearOldAnswer();
        		cb.setChecked(!oldStatus);
        		
        		Log.i(LOG_TAG, "after check...");
        		Log.i(LOG_TAG, "newStatus:"+cb.isChecked());
        		
        		setAnswer(arg2,cb.isChecked());
        		
        		if(answerString.length()==0){
					clearAnswer(mContext,currentCatalogIndex,currentQuestionIndex);
				}else{
					saveAnswer(mContext,currentCatalogIndex,currentQuestionIndex,question.getQuestionId(),answerString.toString());
				}
			}      	
        });
        setFooter();
    }
    
    public void clearOldAnswer(){
    	Log.i(LOG_TAG, "clearOldAnswer()...");
    	
		listItemID.clear();
    	answerString.setLength(0);
    	
		//initial all items background color
		for(int i=0;i<choices.size();i++){
			RadioButton aRb =(RadioButton)adapter.getView(i, null, null).findViewById(R.id.radioButton);
			aRb.setChecked(false);
			adapter.mChecked.set(i, false);
			Log.i(LOG_TAG, String.valueOf(i)+":"+"false");
		} 
		
		Log.i(LOG_TAG, "clearOldAnswer().");

    }
    
    public void setAnswer(int location,boolean isChecked){
    	Log.i(LOG_TAG, "setAnswer()...");
    	
    	//get selection choice and assembly to string
//		for (int i = 0; i < adapter.mChecked.size(); i++) {
//			if (adapter.mChecked.get(i)) {
//				
//				Choice choice = adapter.choices.get(i);
//				listItemID.add(String.valueOf(choice.getChoiceLabel()));
//				
//				if(i>0){
//					answerString.append(",");
//				}
//				answerString.append(String.valueOf(choice.getChoiceLabel()));
//			}
			
//		}
    	String label = adapter.choices.get(location).getChoiceLabel();
    	if(isChecked){
    		listItemID.add(label);
    		answerString.append(label);
    	}else{
    		listItemID.clear();
    		answerString.setLength(0);
    	}
    	
		
		Log.i(LOG_TAG, "answerString:"+answerString.toString());
		
		Log.i(LOG_TAG, "setAnswer().");
    }
    
    public void setFooter(){
    	
    	//set preBtn
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = -1;
				relocationQuestion();
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
				relocationQuestion();
			}
		});
    }
    
    //save answer if not empty 
    public void relocationQuestion(){
    	//clear answer first
    	listItemID.clear();
    	answerString.setLength(0);

    	//get selection choice and assembly to string
		
		//get selection
		for (int i = 0; i < adapter.mChecked.size(); i++) {
			if (adapter.mChecked.get(i)) {
				Choice choice = adapter.choices.get(i);
				listItemID.add(String.valueOf(choice.getChoiceLabel()));
				if(i>0){
					answerString.append(",");
				}
				answerString.append(String.valueOf(choice.getChoiceLabel()));
			}
		}
		
		if (answerString.length() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(SingleChoices.this);
			builder.setMessage("Answer this question late?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									clearAnswer(mContext,currentCatalogIndex,currentQuestionIndex);
									gotoNewQuestion(mContext,direction);
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
			gotoNewQuestion(mContext,direction);
		}
		
    }
    
    class MyListAdapter extends BaseAdapter{
    	List<Boolean> mChecked = new ArrayList<Boolean>();
    	List<Choice> choices = new ArrayList<Choice>();
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<Choice> choices){
    		
    		Log.i(LOG_TAG,"MyListAdapter()...");
    		
    		this.choices = choices;
			for (int i = 0; i < choices.size(); i++) {
				Choice choice = choices.get(i);
				if (answerString.indexOf(String.valueOf(choice.getChoiceLabel())) != -1) {
					mChecked.add(true);
					Log.i(LOG_TAG,String.valueOf(i)+":"+"true");
				} else {
					mChecked.add(false);
					Log.i(LOG_TAG,String.valueOf(i)+":"+"false");
				}
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
				LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mInflater.inflate(R.layout.paper_single_choices_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
				holder.radioButton = (RadioButton)view.findViewById(R.id.radioButton);
				holder.index = (TextView)view.findViewById(R.id.index);
				holder.choiceDesc = (TextView)view.findViewById(R.id.choiceDesc);
				
				final int p = position;
				map.put(position, view);
				
				holder.radioButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i(LOG_TAG,"==========pressed item position = "+String.valueOf(p)+"===========");
						
						
//						RadioButton cb = (RadioButton)v;
//						boolean oldStatus = cb.isChecked();
//						Log.i(LOG_TAG,"oldStatus:"+String.valueOf(oldStatus));
//						
//						clearOldAnswer();
//						
//						cb.setChecked(!oldStatus);
//						Log.i(LOG_TAG,"after setChecked");
//						
//						Log.i(LOG_TAG,"newStatus:"+String.valueOf(cb.isChecked()));
//						mChecked.set(p,cb.isChecked());
//						
//		        		setAnswer(p,cb.isChecked());
//		        		
//		        		if(answerString.length()==0){
//							clearAnswer(mContext,currentCatalogIndex,currentQuestionIndex);
//						}else{
//							saveAnswer(mContext,currentCatalogIndex,currentQuestionIndex,question.getQuestionId(),answerString.toString());
//						}
		        		
		        		Log.i(LOG_TAG,"=====================");
					}
				});
				view.setTag(holder);
			}else{
//				Log.i(LOG_TAG,"position2 = "+position);
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			
			Choice choice = choices.get(position);
			holder.radioButton.setChecked(mChecked.get(position));
			holder.radioButton.setText(choicesLabel[position]);
			
			holder.index.setText(choice.getChoiceLabel());
			holder.choiceDesc.setText(choice.getChoiceContent());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	RadioButton radioButton;
    	TextView index;
    	TextView choiceDesc;
    }
}
