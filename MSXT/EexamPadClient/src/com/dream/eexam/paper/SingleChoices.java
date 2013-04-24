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
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.dream.eexam.base.R;
import com.dream.eexam.server.DataParseUtil;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.FileUtil;
import com.dream.eexam.util.SPUtil;
import com.msxt.client.model.SubmitSuccessResult;
import com.msxt.client.model.Examination.Choice;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.WebServerProxy;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.server.ServerProxy.STATUS;

public class SingleChoices extends BaseQuestion {
	
	public final static String LOG_TAG = "SingleChoices";
	
	//components statement
	TextView questionTV = null;
	ListView listView;
	MyListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();

	void loadComponents(){
		imgHome = (ImageView) findViewById(R.id.imgHome);
		catalogsTL = (TableLayout)findViewById(R.id.catalogsTL);
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		imgDownArrow = (ImageView) findViewById(R.id.imgDownArrow);
    	pendQueNumber = (TextView)findViewById(R.id.pendQueNumber);//TextView[Pending([count])]
		remainingTime = (TextView)findViewById(R.id.remainingTime);//TextView[Time Value]
		submitBtn = (Button)findViewById(R.id.submitBtn);
		backArrow = (ImageView)findViewById(R.id.backArrow);
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);   	
    	nextArrow = (ImageView)findViewById(R.id.nextArrow);
	}
	
	void setHeader(){
		imgHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(SingleChoices.this);
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
        setContentView(R.layout.paper_single_choices);
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
        questionTV.setText(questionHint+ "\n"+cQuestion.getContent());
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
        		Log.i(LOG_TAG, "=================onItemClick()...===============");
        		
        		//get old status
        		RadioButton cb = (RadioButton)view.findViewById(R.id.radioButton);
        		boolean oldStatus = cb.isChecked();
        		Log.i(LOG_TAG, "before clear:"+String.valueOf(oldStatus));
        		
        		//clear answer first
        		clearOldAnswer();
        		Log.i(LOG_TAG, "after clear:"+String.valueOf(cb.isChecked()));
        		cb.setChecked(!oldStatus);
        		Log.i(LOG_TAG, "after set:"+String.valueOf(cb.isChecked()));
        		setAnswer(arg2,cb.isChecked());
        		
        		//send message
//				Message msg = new Message();
//				msg.what = 1;
//				handler.sendMessage(msg);
				updateAllData();
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
    
    void clearOldAnswer(){
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

    void clearOldAnswer(int checkedIndex){
    	Log.i(LOG_TAG, "clearOldAnswer()...");
		listItemID.clear();
    	answerLabels.setLength(0);
		//initial all items background color
		for(int i=0;i<cChoices.size();i++){
			
			RadioButton aRb =(RadioButton)adapter.getView(i, null, null).findViewById(R.id.radioButton);
			if (i == checkedIndex){
				aRb.setChecked(!aRb.isChecked());
				adapter.mChecked.set(i, aRb.isChecked());	
			}else{
				aRb.setChecked(false);
				adapter.mChecked.set(i, false);				
			}
			Log.i(LOG_TAG, String.valueOf(i)+":"+"false");
		} 
		Log.i(LOG_TAG, "clearOldAnswer().");

    }
    
    void setAnswer(int location,boolean isChecked){
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
        
    	//set catalog bar(Right) 
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
		
		//set remaining time
		setRemainingTime();
		
		//set completedSeekBar
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		
		//set exam header(Right)
		submitBtn.setText(mContext.getResources().getString(R.string.label_tv_submit));
        submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "submitTV.onClick()...");
		    	int waitQuestions = examQuestionSum - examAnsweredQuestionSum;
				if (waitQuestions> 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SingleChoices.this);
					builder.setMessage(String.valueOf(waitQuestions) + " " + mContext.getResources().getString(R.string.msg_submiting_warning)+"...")
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
		
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"%");
		
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
				
				holder.radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						Log.i(LOG_TAG, "---------------radioButton.onCheckedChanged---------------");
						
					}
					
				});
				holder.radioButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Log.i(LOG_TAG, "---------------radioButton.onClick---------------");
						//clear old answer
						clearOldAnswer(p);
		        		//get old status
						RadioButton rb = (RadioButton)v;
		        		//set answer
		        		setAnswer(p,rb.isChecked());
		        		
						updateAllData();
					}
				});
				view.setTag(holder);
			}else{
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			
			Choice choice = choices.get(position);
			holder.radioButton.setChecked(mChecked.get(position));
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
    
    class SubmitAnswerTask extends AsyncTask<String, Void, String> {
    	String examId;
    	ProgressDialog progressDialog;
    	ServerProxy proxy;
    	Result submitResult;
    	Map<String, String> answers;
    	
    	@Override
    	protected void onPreExecute() {
    		Log.i(LOG_TAG, "SubmitAnswerTask.onPreExecute() called");
    		
    		String displayMessage =  mContext.getResources().getString(R.string.msg_submiting);
    		progressDialog = ProgressDialog.show(SingleChoices.this, null, displayMessage, true, false);
    		submitBtn.setEnabled(false);
    	}
    	
        @Override
		protected String doInBackground(String... urls) {
        	Log.i(LOG_TAG, "SubmitAnswerTask.doInBackground()...");
        	
        	examId = urls[0];
        	
        	try {
				proxy =  WebServerProxy.Factroy.getCurrrentInstance();
				DatabaseUtil dbUtil = new DatabaseUtil(mContext);
				dbUtil.open();
				answers =  getAllAnswers(dbUtil);
				dbUtil.close();
				
				Log.i(LOG_TAG, "proxy.submitAnswer..."+examId);
				
				submitResult = proxy.submitAnswer(examId,answers);
			} catch (SQLException se) {
				Log.i(LOG_TAG, se.getMessage());
				progressDialog.dismiss();
			} catch (Exception e){
//				Log.i(LOG_TAG, e.getMessage());
				progressDialog.dismiss();
				Toast.makeText(mContext, "Error happens when submit Answer!", Toast.LENGTH_LONG).show();
				
				finish();
				goHome(mContext);
			}
			return null;
		}

        @Override
        protected void onPostExecute(String result) {
        	Log.i(LOG_TAG, "SubmitAnswerTask.onPostExecute()...");
        	progressDialog.dismiss();
        	submitBtn.setEnabled(true);

        	if(submitResult!= null && submitResult.getStatus() == STATUS.SUCCESS ) {
        		
        		String resultFileName = FileUtil.RESULT_FILE_PREFIX + exam.getId() + FileUtil.FILE_SUFFIX_XML;
        		Log.i(LOG_TAG, "resultFileName: " + resultFileName);
        		
    			FileUtil.saveFile(SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences), resultFileName, submitResult.getSuccessMessage());
    			
        		SubmitSuccessResult succResult = DataParseUtil.getSubmitSuccessResult(submitResult);
        		SPUtil.save2SP(SPUtil.CURRENT_EXAM_SCORE, String.valueOf(succResult.getScore()), sharedPreferences);

        		//save user status and exam status 
        		saveExamStatusAfterSubmitted();
        		
				//move question
        		go2ExamResult(mContext);
        		
        	}else {
    			
    			//save answer to local
    			AlertDialog.Builder builder = new AlertDialog.Builder(SingleChoices.this);
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
