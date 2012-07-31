package com.dream.eexam.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.dream.eexam.base.R;
import com.dream.eexam.model.Choice;
import com.dream.eexam.model.Question;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.XMLParseUtil;

public class MultiChoices extends BaseQuestion {
	
	public final static String LOG_TAG = "MultiChoices";

	//components statement
	TextView questionTV = null;
	ListView listView;
	Button preBtn;
	Button nextBtn;
	
	//data statement
	Question question;	
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	public void loadHeader(){
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		currentTV = (TextView)findViewById(R.id.header_tv_current);
		waitTV = (TextView)findViewById(R.id.header_tv_waiting);
	}
	
	public void setHeader(){
		//set question text
		
		remainingTime.setText("Time Remaining: "+String.valueOf(detailBean.getTime())+" mins");

		catalogsTV.setText(questionType);
		catalogsTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
    	
    	currentTV.setText("Q "+String.valueOf(currentQuestionIndex)+" of "+String.valueOf(questionSize));
        
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
		
        //get question
        FileInputStream inputStream = null;
        try {
        	inputStream = new FileInputStream(new File(downloadExamFilePath+ File.separator+downloadExamFile));
			question = XMLParseUtil.readQuestion(inputStream,currentCatalogIndex, currentQuestionIndex);
		} catch (Exception e) {
			Log.i(LOG_TAG, e.getMessage());
		}
        
        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setText(question.getContent());
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
			}      	
        });
        
        preBtn = (Button)findViewById(R.id.preBtn);
        preBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = -1;
				relocationQuestion();
			}
		});
        
        nextBtn = (Button)findViewById(R.id.nextBtn);
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
		
		if (listItemID.size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
			builder.setMessage("Answer this question late?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									clearAnswer();
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
			
			saveAnswer();
	    	gotoNewQuestion();
		}
		
    }
    
    //go to next or previous question
    public void gotoNewQuestion(){
    	Log.i(LOG_TAG, "gotoNewQuestion()...");
    	
    	InputStream inputStream =  MultiChoices.class.getClassLoader().getResourceAsStream("sample_paper.xml");
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
    
    public void clearAnswer(){
    	Log.i(LOG_TAG, "clearAnswer()...");
    	
    	DatabaseUtil dbUtil = new DatabaseUtil(this);
    	dbUtil.open();
    	dbUtil.deleteAnswer(currentCatalogIndex,currentQuestionIndex);
    	dbUtil.close();
    	
    	Log.i(LOG_TAG, "end clearAnswer().");
    }
    
    public void saveAnswer(){
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
    }
    
    
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
