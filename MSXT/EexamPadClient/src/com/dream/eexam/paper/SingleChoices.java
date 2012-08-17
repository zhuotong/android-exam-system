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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.base.R;
import com.msxt.client.model.Examination.Choice;

public class SingleChoices extends BaseQuestion {
	
	public final static String LOG_TAG = "SingleChoices";
	
	//components statement
	TextView questionTV = null;
	ListView listView;
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();

	public void loadComponents(){
		homeTV = (TextView)findViewById(R.id.homeTV);
		imgDownArrow = (ImageView) findViewById(R.id.imgDownArrow);
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		questionIndex = (TextView)findViewById(R.id.questionIndex);
		
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);
    	preBtn = (Button)findViewById(R.id.preBtn);
    	pendQueNumber = (TextView)findViewById(R.id.pendQueNumber);
		remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		submitTV = (TextView)findViewById(R.id.submitTV);
    	nextBtn = (Button)findViewById(R.id.nextBtn);
    	
	}
	
	public void setHeader(){
		//set exam header(Left)
		homeTV.setText("Home");
		//set exam header(Center)
		remainingTime.setText(String.valueOf(exam.getTime())+" mins");
		//set exam header(Right)
		submitTV.setText("Submit");
        submitTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "submitTV.onClick()...");
		    	int waitQuestions = examQuestionSum - examAedQuestionSum;
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
        String questionIndexDesc = "Question "+ String.valueOf(cQuestionIndex) +"/"+ String.valueOf(examQuestionSum);
        questionIndex.setText(questionIndexDesc);
		questionIndex.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(cCatalogIndex));
				intent.putExtra("cqIndex", String.valueOf(cQuestionIndex));
				if(questionTypes[0].equals(cQuestionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( getBaseContext(), MultiChoices.class);
				}else if(questionTypes[1].equals(cQuestionType)){
					intent.putExtra("questionType", "Single Select");
					intent.setClass( getBaseContext(), SingleChoices.class);
				}
				finish();
				startActivity(intent);
			}
		});
		
		
        //set catalog bar(Center) 
		catalogsTV.setText(String.valueOf(cCatalogIndex)+". "+ cCatalog.getDesc() + 
				"(Q" + String.valueOf(cQuestionIndex)+" - " + "Q" + String.valueOf(cQuestionIndex+queSumOfCCatalog-1)+")");
		catalogsTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
		
		imgDownArrow.setOnClickListener(new View.OnClickListener() {
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
				intent.putExtra("ccIndex", String.valueOf(cCatalogIndex));
				intent.putExtra("cqIndex", String.valueOf(cQuestionIndex));
				if(questionTypes[0].equals(cQuestionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( getBaseContext(), PendQuestions.class);
				}else if(questionTypes[1].equals(cQuestionType)){
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
        setHeader();
        
        String questionHint = "Q "+String.valueOf(cQuestion.getIndex())+" (Score:"+String.valueOf(cQuestion.getScore())+")";
        Log.i(LOG_TAG, "questionHint:"+questionHint);
    	
        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setText(questionHint+ "\n"+cQuestion.getContent());
        questionTV.setTextColor(Color.BLACK);
        
        //set List
        listView = (ListView)findViewById(R.id.lvChoices);
        adapter = new MyListAdapter(cChoices);
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
        		
        		if(answerLabels.length()==0){
					clearAnswer(mContext,cCatalogIndex,cQuestionIndex);
				}else{
					saveAnswer(mContext,cCatalogIndex,cQuestionIndex,cQuestion.getId(),answerLabels.toString());
				}
			}      	
        });
        setFooter();
    }
    
    public void clearOldAnswer(){
    	Log.i(LOG_TAG, "clearOldAnswer()...");
    	
		listItemID.clear();
    	answerLabels.setLength(0);
    	
		//initial all items background color
		for(int i=0;i<cChoices.size();i++){
			RadioButton aRb =(RadioButton)adapter.getView(i, null, null).findViewById(R.id.radioButton);
			aRb.setChecked(false);
			adapter.mChecked.set(i, false);
			Log.i(LOG_TAG, String.valueOf(i)+":"+"false");
		} 
		
		Log.i(LOG_TAG, "clearOldAnswer().");

    }
    
    public void setAnswer(int location,boolean isChecked){
    	Log.i(LOG_TAG, "setAnswer()...");
    	String label = adapter.choices.get(location).getLabel();
    	if(isChecked){
    		listItemID.add(label);
    		answerLabels.append(label);
    	}else{
    		listItemID.clear();
    		answerLabels.setLength(0);
    	}
		Log.i(LOG_TAG, "answerString:"+answerLabels.toString());
		Log.i(LOG_TAG, "setAnswer().");
    }
    
    public void setFooter(){
    	//set preBtn
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveDirect = -1;
				relocationQuestion();
			}
		});
		//set completedSeekBar
		int per = 100 * examAedQuestionSum/examQuestionSum;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"%");
		//set nextBtn
        nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveDirect = 1;
				relocationQuestion();
			}
		});
    }
    
    //save answer if not empty 
    public void relocationQuestion(){
    	//clear answer first
    	listItemID.clear();
    	answerLabels.setLength(0);

    	//get selection choice and assembly to string
		
		//get selection
		for (int i = 0; i < adapter.mChecked.size(); i++) {
			if (adapter.mChecked.get(i)) {
				Choice choice = adapter.choices.get(i);
				listItemID.add(String.valueOf(choice.getLabel()));
				if(i>0){
					answerLabels.append(",");
				}
				answerLabels.append(String.valueOf(choice.getLabel()));
			}
		}
		
		if (answerLabels.length() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(SingleChoices.this);
			builder.setMessage("Answer this question late?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									clearAnswer(mContext,cCatalogIndex,cQuestionIndex);
									gotoNewQuestion(mContext,cCatalogIndex,cQuestionIndex,moveDirect);
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
			gotoNewQuestion(mContext,cCatalogIndex,cQuestionIndex,moveDirect);
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
				if (answerLabels.indexOf(String.valueOf(choice.getLabel())) != -1) {
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
			holder.radioButton.setText(choicesLabels[position]);
			holder.index.setText(choice.getLabel());
			holder.choiceDesc.setText(choice.getContent());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	RadioButton radioButton;
    	TextView index;
    	TextView choiceDesc;
    }
}
