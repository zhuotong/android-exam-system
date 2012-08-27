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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.dream.eexam.base.R;
import com.dream.eexam.server.DataUtil;
import com.msxt.client.model.Examination.Choice;

public class MultiChoices extends BaseQuestion {
	
	public final static String LOG_TAG = "MultiChoices";

	//components statement
	TextView questionTV = null;
	ListView listView;
	
	//data statement
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	public void loadComponents(){
		homeTV = (TextView)findViewById(R.id.homeTV);
		remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		submitTV = (TextView)findViewById(R.id.submitTV);
		imgDownArrow = (ImageView) findViewById(R.id.imgDownArrow);
//		questionIndex = (TextView)findViewById(R.id.questionIndex);
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
		StringBuffer timeSB = new StringBuffer();
		if(lMinutes<10) timeSB.append("0");
		timeSB.append(String.valueOf(lMinutes));
		timeSB.append(String.valueOf(":"));
		if(lSeconds<10) timeSB.append("0");
		timeSB.append(String.valueOf(lSeconds));
		
		remainingTime.setText(timeSB.toString());
		
		//set exam header(Right)
		submitTV.setText("Submit");
        submitTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "submitTV.onClick()...");
		    	int waitQuestions = examQuestionSum - examAnsweredQuestionSum;
				if (waitQuestions> 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
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
//        String questionIndexDesc = "Question "+ String.valueOf(cQuestionIndex) +"/"+ String.valueOf(examQuestionSum);
//        questionIndex.setText(questionIndexDesc);
//		questionIndex.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.putExtra("ccIndex", String.valueOf(cCatalogIndex));
//				intent.putExtra("cqIndex", String.valueOf(cQuestionIndex));
//				if(questionTypes[0].equals(cQuestionType)){
//					intent.putExtra("questionType", "Multi Select");
//					intent.setClass( getBaseContext(), MultiChoices.class);
//				}else if(questionTypes[1].equals(cQuestionType)){
//					intent.putExtra("questionType", "Single Select");
//					intent.setClass( getBaseContext(), SingleChoices.class);
//				}
//				finish();
//				startActivity(intent);
//			}
//		});
		
		imgDownArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
		
        //set catalog bar(Center) 
		catalogsTV.setText(String.valueOf(cCatalogIndex)+". " + cCatalog.getDesc() + 
				"(Q" + String.valueOf(cQuestionIndexOfExam)+" - " + "Q" + String.valueOf(cQuestionIndexOfExam+queSumOfCCatalog-1)+")");
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
        setContentView(R.layout.paper_multi_choices);
        mContext = getApplicationContext();

        loadComponents();
        setHeader();
		
        String questionHint = "Q"+String.valueOf(cQuestionIndexOfExam)+" (Score:"+String.valueOf(cQuestion.getScore())+")\n";
        Log.i(LOG_TAG, "questionHint:"+questionHint);

        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setMovementMethod(ScrollingMovementMethod.getInstance()); 
        questionTV.setText(questionHint+ "\n"+ cQuestion.getContent());
        questionTV.setTextColor(Color.BLACK);	
        
        //set List
        listView = (ListView)findViewById(R.id.lvChoices);
        adapter = new MyListAdapter(cChoices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		CheckBox cb = (CheckBox)view.findViewById(R.id.list_select);
        		cb.setChecked(!cb.isChecked());
				//set answer
		    	//clear answer first
		    	listItemID.clear();
		    	answerLabels.setLength(0);
				setAnswer();
				if(answerLabels.length()==0){
					clearAnswer(mContext,cCatalogIndex,cQuestionIndex);
				}else{
					saveAnswer(mContext,cCatalogIndex,cQuestionIndex,cQuestion.getId(),answerLabels.toString());
				}
			}      	
        });
        listView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				detector.onTouchEvent(arg1);
				return false;
			}
		});
        
        setFooter();
        
        //set GestureDetector
        detector = new GestureDetector((OnGestureListener) this);
    }
    
    public void setFooter(){
    	//set preBtn
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveDirect = -1;
				move2NewQuestion();
			}
		});
		//set completedSeekBar
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
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
				move2NewQuestion();
			}
		});
    }
    
    //save answer if not empty 
    public void move2NewQuestion(){
		if (listItemID.size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
			builder.setMessage("Answer this question late?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
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
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		super.onTouch(v,event);
		return detector.onTouchEvent(event);
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		super.onFling(e1,e2,velocityX,velocityY);
		Log.i(LOG_TAG, "onFling()...");	
        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
        	Log.i(LOG_TAG,"Move Left");
			moveDirect = -1;
			move2NewQuestion();
        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
        	Log.i(LOG_TAG,"Move Right");
			moveDirect = 1;
			move2NewQuestion();
        }
		return false;
	}
    
    public void setAnswer(){
    	Log.i(LOG_TAG, "setAnswer()...");
    	//get selection choice and assembly to string
		for (int i = 0; i < adapter.mChecked.size(); i++) {
			if (adapter.mChecked.get(i)) {
				Choice choice = adapter.choices.get(i);
				listItemID.add(String.valueOf(choice.getIndex()));
				if(i>0){
					answerLabels.append(",");
				}
				answerLabels.append(String.valueOf(choice.getLabel()));
			}
		}
		Log.i(LOG_TAG, "setAnswer().");
    }
    
    class MyListAdapter extends BaseAdapter{
    	List<Boolean> mChecked = new ArrayList<Boolean>();
    	List<Choice> choices = new ArrayList<Choice>();
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<Choice> choices){
    		this.choices = choices;
    		for(int i=0;i<choices.size();i++){
    			Choice choice = choices.get(i);
				if (answerLabels.indexOf(String.valueOf(choice.getLabel())) != -1) {
					mChecked.add(true);
				}else{
					mChecked.add(false);
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

						
						//set answer
				    	//clear answer first
				    	listItemID.clear();
				    	answerLabels.setLength(0);
						setAnswer();
						if(answerLabels.length()==0){
							clearAnswer(mContext,cCatalogIndex,cQuestionIndex);
						}else{
							saveAnswer(mContext,cCatalogIndex,cQuestionIndex,cQuestion.getId(),answerLabels.toString());
						}
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
			holder.selected.setText(choicesLabels[position]);
			holder.index.setText(choice.getLabel());
			holder.choiceDesc.setText(choice.getContent());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	CheckBox selected;
    	TextView index;
    	TextView choiceDesc;
    }


}
