package com.dream.eexam.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
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
import com.dream.eexam.base.ResultActivity;
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
	
	public void loadComponents(){
		imgHome = (ImageView) findViewById(R.id.imgHome);
		catalogsTL = (TableLayout)findViewById(R.id.catalogsTL);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		submitTV = (TextView)findViewById(R.id.submitTV);
		imgDownArrow = (ImageView) findViewById(R.id.imgDownArrow);
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		pendQueNumber = (TextView)findViewById(R.id.pendQueNumber);
		backArrow = (ImageView)findViewById(R.id.backArrow);
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);   	
    	nextArrow = (ImageView)findViewById(R.id.nextArrow);
	}
	
	public void setHeader(){
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
        questionTV.setBackgroundColor(Color.argb(0, 0, 255, 0));
        
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
				
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
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
    
    public void setFooter(){
    	//set preBtn
    	
        if(cQuestionIndexOfExam == 1){
        	Drawable firstQuestion = getResources().getDrawable(R.drawable.first_question_64);
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
    				Intent intent = new Intent();
    				intent.putExtra("ccIndex", String.valueOf(cCatalogIndex));
    				intent.putExtra("cqIndex", String.valueOf(cQuestionIndex));
    				intent.putExtra("questionType", cQuestionType);
    				if(questionTypes[0].equals(cQuestionType)){
    					intent.setClass( getBaseContext(), PendQuestions.class);
    				}else if(questionTypes[1].equals(cQuestionType)){
    					intent.setClass( getBaseContext(), PendQuestions.class);
    				}
    				finish();
    				startActivity(intent);   					
				}else{
					ShowDialog(mContext.getResources().getString(R.string.dialog_note),
							mContext.getResources().getString(R.string.message_tv_no_question));	
				}
			}
		});        	

        //set remaining time
		StringBuffer timeSB = new StringBuffer();
		if(lMinutes<10) timeSB.append("0");
		timeSB.append(String.valueOf(lMinutes));
		timeSB.append(String.valueOf(":"));
		if(lSeconds<10) timeSB.append("0");
		timeSB.append(String.valueOf(lSeconds));
		remainingTime.setText(timeSB.toString());
		
		//set completedSeekBar
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"%");

        
		//set exam header(Right)
		submitTV.setText(mContext.getResources().getString(R.string.label_tv_submit));
        submitTV.setOnClickListener(new View.OnClickListener() {
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
        	Drawable lastQuestion = getResources().getDrawable(R.drawable.last_question_64);
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
    
    //save answer if not empty 
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
						
				    	//clear answer first
				    	listItemID.clear();
				    	answerLabels.setLength(0);
						setAnswer();

						//send message
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
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
    		submitTV.setEnabled(false);
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
				Log.i(LOG_TAG, e.getMessage());
				progressDialog.dismiss();
			}
        	
			return null;
		}

        @Override
        protected void onPostExecute(String result) {
        	progressDialog.dismiss();
        	submitTV.setEnabled(true);
        	
        	if( submitResult!=null && submitResult.getStatus() == STATUS.SUCCESS ) {
        		
           		String resultFileName = FileUtil.RESULT_FILE_PREFIX + exam.getId() + FileUtil.FILE_SUFFIX;
        		Log.i(LOG_TAG, "resultFileName: " + resultFileName);
        		
    			FileUtil fu = new FileUtil();
        		fu.saveFile(SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences), resultFileName, submitResult.getSuccessMessage());
        		
        		SubmitSuccessResult succResult = DataParseUtil.getSubmitSuccessResult(submitResult);
        		
        		//Save Exam Score to sharedPreferences
        		Log.i(LOG_TAG, "Exam " + exam.getId() + " Submitted Successfully!");
        		
        		//Save Update Exam Remaining Count to sharedPreferences
        		String examCountBefore = SPUtil.getFromSP(SPUtil.CURRENT_USER_EXAM_REMAINING_COUNT, sharedPreferences);
        		Log.i(LOG_TAG, "Exam Count Before Submitted: " +  examCountBefore);
        		
        		int remainingExamCount = Integer.valueOf(examCountBefore);
        		SPUtil.save2SP(SPUtil.CURRENT_USER_EXAM_REMAINING_COUNT, String.valueOf(remainingExamCount-1), sharedPreferences);
        		
        		String examCountAfter = SPUtil.getFromSP(SPUtil.CURRENT_USER_EXAM_REMAINING_COUNT, sharedPreferences);
        		Log.i(LOG_TAG, "Exam Count After Submitted: " +  examCountAfter);
        		
        		//Save Update Exam Submitted IDs to sharedPreferences
        		SPUtil.append2SP(SPUtil.CURRENT_EXAM_SUBMITTED_IDS, exam.getId(), sharedPreferences);
        		
        		//Save Exam Score to sharedPreferences
        		SPUtil.save2SP(SPUtil.CURRENT_EXAM_SCORE, String.valueOf(succResult.getScore()), sharedPreferences);
        		
        		//Save Exam Status
        		SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.EXAM_STATUS_START_PENDING_NEW, sharedPreferences);
        		
				//move question
        		go2ExamResult(mContext);
        		
        	}else{
    			
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
    									
    									SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.EXAM_STATUS_START_PENDING_NEW, sharedPreferences);
    								}
    							})
    					.setNegativeButton(mContext.getResources().getString(R.string.warning_save_answer_local_cancel),
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,int id) {
    									dialog.cancel();
    								}
    							});
    			builder.show();
    			
        	} 
        }
    }



}
