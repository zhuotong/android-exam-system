package com.dream.eexam.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.dream.eexam.base.R;
import com.dream.eexam.server.DataParseUtil;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.FileUtil;
import com.dream.eexam.util.SPUtil;
import com.msxt.client.model.Examination.Choice;
import com.msxt.client.model.SubmitSuccessResult;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.WebServerProxy;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.server.ServerProxy.STATUS;

public class MultiChoices extends BaseQuestion {
	
	public final static String LOG_TAG = "MultiChoices";

	//components statement
	TextView questionTV = null;
	ListView listView;
	
	//data statement
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	void loadComponents(){
		imgHome = (ImageView) findViewById(R.id.imgHome);
		catalogsTL = (TableLayout)findViewById(R.id.catalogsTL);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		imgDownArrow = (ImageView) findViewById(R.id.imgDownArrow);
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		pendQueNumber = (Button) findViewById(R.id.pendQueNumber);
		backArrow = (ImageView)findViewById(R.id.backArrow);
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);   	
    	nextArrow = (ImageView)findViewById(R.id.nextArrow);
	}
	
	void setHeader(){
		imgHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
				builder.setMessage(mContext.getResources().getString(R.string.warning_go_home))
						.setCancelable(false)
						.setPositiveButton(mContext.getResources().getString(R.string.warning_go_home_yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										goHome(mContext);
									}
								})
						.setNegativeButton(mContext.getResources().getString(R.string.warning_go_home_cancel),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										dialog.cancel();
									}
								});
				builder.show();
			}
		});
		
		catalogsTL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "catalogsTL.onClick()...");
				showWindow(v);
			}
		});
		
        //set catalog bar(Center) 
		catalogsTV.setText(String.valueOf(cCatalogIndex)+". "+ cCatalog.getDesc() + 
				"(Q" + String.valueOf(cCatalog1stQuestionIndex)+" - " + "Q" + String.valueOf(cCataloglastQuestionIndex)+")");
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paper_multi_choices);
        mContext = getApplicationContext();

        loadComponents();
        setHeader();
		
		String questionHint = "Q"
			+ String.valueOf(cQuestionIndexOfExam)
			+ " ("
			+ mContext.getResources().getString(
					R.string.msg_question_score) + ":"
			+ String.valueOf(cQuestion.getScore()) + ")\n";
        Log.i(LOG_TAG, "questionHint:"+questionHint);

        //set question text
        questionTV = (TextView)findViewById(R.id.questionTV);
        questionTV.setMovementMethod(ScrollingMovementMethod.getInstance()); 
        questionTV.setText(questionHint+ "\n"+ cQuestion.getContent());
        questionTV.setTextColor(Color.BLACK);
//        questionTV.setBackgroundColor(Color.argb(0, 0, 255, 0));
        
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
        		
        		TextView tvIndex = (TextView)view.findViewById(R.id.list_index);
        		TextView tvCD = (TextView)view.findViewById(R.id.list_choiceDesc);
        		if(cb.isChecked()){
        			tvIndex.setTextColor(Color.YELLOW);
        			tvCD.setTextColor(Color.YELLOW);
        		}else{
        			tvIndex.setTextColor(Color.WHITE);
        			tvCD.setTextColor(Color.WHITE);        			
        		}
        		
				//set answer
		    	//clear answer first
		    	listItemID.clear();
		    	answerLabels.setLength(0);
				setAnswer();
				
				updateAllData();
			}      	
        });
        listView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});
        
        setFooter();
        
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.i(LOG_TAG, "onDestroy()...");
		if(timerTask!=null){
			timerTask.cancel();
		}
		if(timer!=null){
			timer.cancel();
		}
	}

	void setFooter(){
    	//set preBtn
    	
        if(cQuestionIndexOfExam == 1){
        	Drawable firstQuestion = getResources().getDrawable(R.drawable.ic_first_question_64);
        	backArrow.setImageDrawable(firstQuestion);
        }else{
            backArrow.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				moveDirect = -1;
    				move2NewQuestion();
    			}
    		});        	
        }
        
        //set text view pending[count]
		pendQueNumber.setText(mContext.getResources().getString(R.string.label_tv_waiting)+"("+Integer.valueOf(pendQuestions.size())+")");
		pendQueNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(pendQuestions.size()>0){
    				finish();
    				Log.i(LOG_TAG, "----------Go to Pending Question!-----------------");
    				go2PendingQuestions(mContext);
    				Log.i(LOG_TAG, "--------------------------------------------");
				}else{
					ShowDialog(mContext.getResources().getString(R.string.dialog_note),
							mContext.getResources().getString(R.string.message_tv_no_question));	
				}
				
			}
		});        	

		setRemainingTime();
		
		//set completedSeekBar
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"%");

        
		//set exam header(Right)
		submitBtn.setText(mContext.getResources().getString(R.string.label_tv_submit));
        submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "submitTV.onClick()...");
		    	int waitQuestions = examQuestionSum - examAnsweredQuestionSum;
				if (waitQuestions> 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
					builder.setMessage(String.valueOf(waitQuestions)+ " " + mContext.getResources().getString(R.string.msg_submiting_warning)+"...")
							.setCancelable(false)
							.setPositiveButton(mContext.getResources().getString(R.string.msg_submiting_warning_yes),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											new SubmitAnswerTask().execute(exam.getId());
										}
									})
							.setNegativeButton(mContext.getResources().getString(R.string.msg_submiting_warning_cancel),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int id) {
											dialog.cancel();
										}
									});
					builder.show();
				} else {
					new SubmitAnswerTask().execute(exam.getId());
				}
			}
		});
        
       //set nextBtn
        if(cQuestionIndexOfExam == examQuestionSum){
        	Drawable lastQuestion = getResources().getDrawable(R.drawable.ic_last_question_64);
        	nextArrow.setImageDrawable(lastQuestion);
        }else{
            nextArrow.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				
    				moveDirect = 1;
    				move2NewQuestion();
    			}
    		});        	
        }

    }
    
    public void move2NewQuestion(){
		if (listItemID.size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
			builder.setMessage(mContext.getResources().getString(R.string.warning_answer_later))
					.setCancelable(false)
					.setPositiveButton(mContext.getResources().getString(R.string.warning_answer_later_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									gotoNewQuestion(mContext,cCatalogIndex,cQuestionIndex,moveDirect);
								}
							})
					.setNegativeButton(mContext.getResources().getString(R.string.warning_answer_later_cancel),
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
    
    public void setAnswer(){
    	Log.i(LOG_TAG, "setAnswer()...");
    	//get selection choice and assembly to string
    	List<Boolean> mCheckedList = adapter.mChecked;
    	List<Choice> choices = adapter.choices;
		for (int i = 0; i < mCheckedList.size(); i++) {
			if (mCheckedList.get(i)) {
				Choice choice = choices.get(i);
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
    	LayoutInflater mInflater;
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
    		
    		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		
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
//			View view;
			ViewHolder holder = null;
			
			if (convertView == null) {
				Log.i(LOG_TAG,"position1 = "+position);
//				view = mInflater.inflate(R.layout.paper_multi_choices_item, null);
				
				convertView = mInflater.inflate(R.layout.paper_multi_choices_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
				holder.selected = (CheckBox)convertView.findViewById(R.id.list_select);
				holder.index = (TextView)convertView.findViewById(R.id.list_index);
				holder.choiceDesc = (TextView)convertView.findViewById(R.id.list_choiceDesc);
				
				final int p = position;
				map.put(position, convertView);
				
				holder.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						CheckBox cb = (CheckBox)buttonView;
						mChecked.set(p, cb.isChecked());
						
				    	//clear answer first
				    	listItemID.clear();
				    	answerLabels.setLength(0);
						setAnswer();

						updateAllData();
					}
				});
				convertView.setTag(holder);
			}else{
//				Log.i(LOG_TAG,"position2 = "+position);
//				convertView = map.get(position);
				holder = (ViewHolder)convertView.getTag();
			}
			
			Choice choice = choices.get(position);
			
			holder.selected.setChecked(mChecked.get(position));
			holder.index.setText(choice.getLabel());
			holder.choiceDesc.setText(choice.getContent());
			
    		if(holder.selected.isChecked()){
    			holder.index.setTextColor(Color.YELLOW);
    			holder.choiceDesc.setTextColor(Color.YELLOW);
    		}else{
    			holder.index.setTextColor(Color.WHITE);
    			holder.choiceDesc.setTextColor(Color.WHITE);   			
    		}
			
			return convertView;
		}
    	
    }
    
    static class ViewHolder{
    	CheckBox selected;
    	TextView index;
    	TextView choiceDesc;
    }
    
    class SubmitAnswerTask extends AsyncTask<String, Void, String> {
    	String examId;
    	Map<String, String> answers;
    	
    	ProgressDialog progressDialog;
    	ServerProxy proxy;
    	Result submitResult;
    	
    	
    	@Override
    	protected void onPreExecute() {
    		Log.i(LOG_TAG, "onPreExecute() called");
    		String displayMessage =  mContext.getResources().getString(R.string.msg_submiting);
    		progressDialog = ProgressDialog.show(MultiChoices.this, null, displayMessage, true, true);
    		submitBtn.setEnabled(false);
    	}
    	
        @Override
		protected String doInBackground(String... urls) {
        	Log.i(LOG_TAG, "doInBackground()...");
        	examId = urls[0];
        	try {
				proxy =  WebServerProxy.Factroy.getCurrrentInstance();
				DatabaseUtil dbUtil = new DatabaseUtil(mContext);
				dbUtil.open();
				answers =  getAllAnswers(dbUtil);
				dbUtil.close();
				
				Log.i(LOG_TAG, "proxy.submitAnswer..."+examId);
				submitResult = proxy.submitAnswer(examId,answers);
			} catch (SQLException e) {
				progressDialog.dismiss();
			} catch (Exception e) {
				progressDialog.dismiss();
			}
			return null;
		}

        @Override
        protected void onPostExecute(String result) {
        	progressDialog.dismiss();
        	submitBtn.setEnabled(true);
        	if(submitResult==null || submitResult.getStatus() == STATUS.ERROR){
    			//save answer to local
    			AlertDialog.Builder builder = new AlertDialog.Builder(MultiChoices.this);
    			builder.setMessage(mContext.getResources().getString(R.string.warning_save_answer_local))
    					.setCancelable(false)
    					.setPositiveButton(mContext.getResources().getString(R.string.warning_save_answer_local_yes),
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,int id) {
    									String path = SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences);
    								    String examid = exam.getId();
    									saveAnswer2Local(answers,path,examid);
    									//save user status and exam status
    									saveExamStatusAfterSubmitted();
    									go2ExamResult(mContext);
    								}
    							})
    					.setNegativeButton(mContext.getResources().getString(R.string.warning_save_answer_local_cancel),
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,int id) {
    									dialog.cancel();
    								}
    							});
    			builder.show();
			}else{
        		//get result file name
           		String resultFileName = FileUtil.RESULT_FILE_PREFIX + exam.getId() + FileUtil.FILE_SUFFIX_XML;
        		Log.i(LOG_TAG, "resultFileName: " + resultFileName);
    			
        		//save submit result file
        		FileUtil.saveFile(SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences), resultFileName, submitResult.getSuccessMessage());
        		SubmitSuccessResult succResult = DataParseUtil.getSubmitSuccessResult(submitResult);
        		
        		//Save Exam Score to sharedPreferences
        		SPUtil.save2SP(SPUtil.CURRENT_EXAM_SCORE, String.valueOf(succResult.getScore()), sharedPreferences);
        		
        		//save user status and exam status 
        		saveExamStatusAfterSubmitted();
        		
				//move question
        		go2ExamResult(mContext);
			}
        }
    }

	@Override
	void setRemainingTime() {
		Log.i(LOG_TAG, "setRemainingTime()...");
		String rTimeStr = getRemainingTime();
		if(rTimeStr!=null){
			Log.i(LOG_TAG, "rTimeStr():"+rTimeStr);
			remainingTime.setText(rTimeStr);
		}else{
			Log.i(LOG_TAG, "Time Out!");
			if(timerTask!=null){
				timerTask.cancel();
			}
			if(timer!=null){
				timer.cancel();
			}
    		new SubmitAnswerTask().execute(exam.getId());
		}
	}
}
