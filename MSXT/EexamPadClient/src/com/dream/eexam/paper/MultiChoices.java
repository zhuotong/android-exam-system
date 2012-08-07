package com.dream.eexam.paper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.dream.eexam.base.R;
import com.dream.eexam.model.Choice;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.XMLParseUtil;

public class MultiChoices extends BaseQuestion {
	
	public final static String LOG_TAG = "MultiChoices";

	//components statement
	TextView questionHintTV = null;
	TextView questionTV = null;

	ListView listView;
	
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
//		currentTV = (TextView)findViewById(R.id.header_tv_current);
		waitTV = (TextView)findViewById(R.id.header_tv_waiting);
	}
	
	public void setHeader(){
		//set exam header(Left)
		homeTV.setText("Home");
		
		//set exam header(Center)
		remainingTimeLabel.setText("Time Remaining: ");
		remainingTime.setText(String.valueOf(detailBean.getTime())+" mins");
		
		//set completedSeekBar
		int per = 100 * answeredQuestions/totalQuestions;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"% finished");
		
		//set exam header(Right)
		submitTV.setText("Submit");
        submitTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "submitTV.onClick()...");
				
		    	int waitQuestions = totalQuestions - answeredQuestions;
				 
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
    	
//    	currentTV.setText("Q "+String.valueOf(currentQuestionIndex)+" -- "+"Q "+String.valueOf(questionSize));
        
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
/*        questionHintTV = (TextView)findViewById(R.id.questionHintTV);
        questionHintTV.setText(questionHint);
        questionHintTV.setTextColor(Color.BLACK);	*/

        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setText(questionHint+ "\n"+ question.getContent());
        questionTV.setTextColor(Color.BLACK);	
        
        //set List
        listView = (ListView)findViewById(R.id.lvChoices);
        adapter = new MyListAdapter(question.getChoices());
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
		    	answerString.setLength(0);
				setAnswer();
				if(answerString.length()==0){
					clearAnswer(mContext,currentCatalogIndex,currentQuestionIndex);
				}else{
					saveAnswer(mContext,currentCatalogIndex,currentQuestionIndex,answerString.toString());
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
        
        loadFooter();
        setFooter();
        
        //set GestureDetector
        detector = new GestureDetector((OnGestureListener) this);
    
    }
    
    public void loadFooter(){
    	preBtn = (Button)findViewById(R.id.preBtn);
    	questionIndex = (TextView)findViewById(R.id.questionIndex);
    	nextBtn = (Button)findViewById(R.id.nextBtn);
    }
    
    public void setFooter(){
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = -1;
				relocationQuestion();
			}
		});
        
        String questionIndexDesc = "Question "+ String.valueOf(currentQuestionIndex) +"/"+ String.valueOf(totalQuestions);
        questionIndex.setText(questionIndexDesc);
        
        nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = 1;
				relocationQuestion();
			}
		});
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
			direction = -1;
			relocationQuestion();
        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
        	Log.i(LOG_TAG,"Move Right");
			direction = 1;
			relocationQuestion();
        }
		return false;
	}
	
    //save answer if not empty 
    public void relocationQuestion(){
    	
		if (listItemID.size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
			builder.setMessage("Answer this question late?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
//									clearAnswer();
									gotoNewQuestion();
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
//			saveAnswer();
	    	gotoNewQuestion();
		}
		
    }
    
    //go to next or previous question
    public void gotoNewQuestion(){
    	Log.i(LOG_TAG, "gotoNewQuestion()...");
    	
    	InputStream inputStream =  getExamStream();
		String questionType = null;
		try {
			 questionType = XMLParseUtil.readQuestionType(inputStream,currentCatalogIndex,currentQuestionIndex+direction);
			 inputStream.close();
		} catch (Exception e) {
			Log.i(LOG_TAG, e.getMessage());
		}
		
		if(questionType!=null){
			//move question
			Intent intent = new Intent();
			intent.putExtra("ccIndex", String.valueOf(currentCatalogIndex));
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
    
/*    public void clearAnswer(){
    	Log.i(LOG_TAG, "clearAnswer()...");
    	
    	DatabaseUtil dbUtil = new DatabaseUtil(this);
    	dbUtil.open();
    	dbUtil.deleteAnswer(currentCatalogIndex,currentQuestionIndex);
    	dbUtil.close();
    	
    	Log.i(LOG_TAG, "end clearAnswer().");
    }*/
 
    public void setAnswer(){
    	Log.i(LOG_TAG, "setAnswer()...");
    	
    	//get selection choice and assembly to string
		for (int i = 0; i < adapter.mChecked.size(); i++) {
			if (adapter.mChecked.get(i)) {
				Choice choice = adapter.choices.get(i);
				listItemID.add(String.valueOf(choice.getChoiceIndex()));
				if(i>0){
					answerString.append(",");
				}
				answerString.append(String.valueOf(choice.getChoiceIndex()));
			}
		}
		
		Log.i(LOG_TAG, "setAnswer().");
    }
    
    /*public void saveAnswer(){
    	Log.i(LOG_TAG, "saveAnswer()...");
    	
    	DatabaseUtil dbUtil = new DatabaseUtil(this);
    	dbUtil.open();
    	Cursor cursor = dbUtil.fetchAnswer(currentCatalogIndex,currentQuestionIndex);
    	if(cursor != null && cursor.moveToNext()){
    		Log.i(LOG_TAG, "updateAnswer("+currentCatalogIndex+","+currentQuestionIndex+","+answerString.toString()+")");
    		dbUtil.updateAnswer(currentCatalogIndex,currentQuestionIndex,"("+ answerString.toString()+")");
    	}else{
    		Log.i(LOG_TAG, "createAnswer("+currentCatalogIndex+","+currentQuestionIndex+","+answerString.toString()+")");
    		dbUtil.createAnswer(currentCatalogIndex,currentQuestionIndex, "("+ answerString.toString()+")");
    	}
    	
    	dbUtil.close();
    	
    	Log.i(LOG_TAG, "saveAnswer().");
    }*/
    
    
    class MyListAdapter extends BaseAdapter{
    	List<Boolean> mChecked = new ArrayList<Boolean>();
    	List<Choice> choices = new ArrayList<Choice>();
    	
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<Choice> choices){
//    		choices = new ArrayList<Choice>();
    		this.choices = choices;
    		for(int i=0;i<choices.size();i++){
    			Choice choice = choices.get(i);
				if (answerString.indexOf(String.valueOf(choice.getChoiceIndex())) != -1) {
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
				    	answerString.setLength(0);
						setAnswer();
						if(answerString.length()==0){
							clearAnswer(mContext,currentCatalogIndex,currentQuestionIndex);
						}else{
							saveAnswer(mContext,currentCatalogIndex,currentQuestionIndex,answerString.toString());
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
			holder.index.setText(choice.getChoiceLabel());
			holder.choiceDesc.setText(choice.getChoiceContent());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	CheckBox selected;
    	TextView index;
    	TextView choiceDesc;
    }
}
