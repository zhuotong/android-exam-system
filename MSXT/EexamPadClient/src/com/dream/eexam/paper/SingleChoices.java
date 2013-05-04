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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dream.eexam.base.R;
import com.dream.eexam.model.ChoiceExt;
import com.dream.eexam.paper.MultiChoices.ViewHolder;
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
	MyListAdapter myAdapter;
	List<ChoiceExt> choiceExtList = new ArrayList<ChoiceExt>();
	List<String> listItemID = new ArrayList<String>();

	void loadComponents(){
		imgHome = (ImageView) findViewById(R.id.imgHome);
		catalogsTL = (TableLayout)findViewById(R.id.catalogsTL);
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		imgDownArrow = (ImageView) findViewById(R.id.imgDownArrow);
    	pendQueNumber = (Button)findViewById(R.id.pendQueNumber);//TextView[Pending([count])]
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
        
//      List<Boolean> mCheckedList = new ArrayList<Boolean>();
//		for (Choice choice: cChoices) {
//			if (answerLabels.indexOf(String.valueOf(choice.getLabel())) != -1) {
//				mCheckedList.add(true);
//			} else {
//				mCheckedList.add(false);
//			}
//		}
        
		for(Choice choice:cChoices){
			ChoiceExt choiceExt = new ChoiceExt();
			choiceExt.setActualLabel(choice.getActualLabel());
			choiceExt.setContent(choice.getContent());
			choiceExt.setIndex(choice.getIndex());
			choiceExt.setLabel(choice.getLabel());
			if (answerLabels.indexOf(String.valueOf(choice.getLabel())) != -1) {
				choiceExt.setChecked(true);
			}else{
				choiceExt.setChecked(false);
			}
			choiceExtList.add(choiceExt);
		}
		
        myAdapter = new MyListAdapter(choiceExtList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		Log.i(LOG_TAG, "=================onItemClick()...===============");
        		
        		//get old status
        		RadioButton cb = (RadioButton)view.findViewById(R.id.radioButton);
        		boolean oldStatus = cb.isChecked();
        		Log.i(LOG_TAG, "before clear:"+String.valueOf(oldStatus));
        		
        		changeList(arg2,!oldStatus);
				//reSetAnswer
				reSetAnswer();
				updateAllData();
				
				myAdapter.notifyDataSetChanged();
				
        		//clear answer first
//        		clearOldAnswer();
//        		Log.i(LOG_TAG, "after clear:"+String.valueOf(cb.isChecked()));
//        		cb.setChecked(!oldStatus);
//        		
//        		//set text color for selected item
//        		TextView tvIndex = (TextView)view.findViewById(R.id.index);
//        		TextView tvCD = (TextView)view.findViewById(R.id.choiceDesc);
//        		if(cb.isChecked()){
//        			tvIndex.setTextColor(Color.YELLOW);
//        			tvCD.setTextColor(Color.YELLOW);
//        		}else{
//        			tvIndex.setTextColor(Color.WHITE);
//        			tvCD.setTextColor(Color.WHITE);        			
//        		}
//        		
//        		Log.i(LOG_TAG, "after set:"+String.valueOf(cb.isChecked()));
//        		setAnswer(arg2,cb.isChecked());
//        		
//				updateAllData();
				
				
				
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
    
//    void clearOldAnswer(){
//    	Log.i(LOG_TAG, "clearOldAnswer()...");
//		listItemID.clear();
//    	answerLabels.setLength(0);
//		//initial all items background color
//		for(int i=0;i<cChoices.size();i++){
//			RadioButton aRb =(RadioButton)myAdapter.getView(i, null, null).findViewById(R.id.radioButton);
//			aRb.setChecked(false);
//			myAdapter.mChecked.set(i, false);
//			Log.i(LOG_TAG, String.valueOf(i)+":"+"false");
//		} 
//		Log.i(LOG_TAG, "clearOldAnswer().");
//    }
    
//    void clearOldAnswer(int checkedIndex){
//    	Log.i(LOG_TAG, "clearOldAnswer()...");
//		listItemID.clear();
//    	answerLabels.setLength(0);
//		//initial all items background color
//		for(int i=0;i<cChoices.size();i++){
//			
//			RadioButton aRb =(RadioButton)myAdapter.getView(i, null, null).findViewById(R.id.radioButton);
//			if (i == checkedIndex){
//				aRb.setChecked(!aRb.isChecked());
//				myAdapter.mChecked.set(i, aRb.isChecked());	
//			}else{
//				aRb.setChecked(false);
//				myAdapter.mChecked.set(i, false);				
//			}
//			Log.i(LOG_TAG, String.valueOf(i)+":"+"false");
//		} 
//		Log.i(LOG_TAG, "clearOldAnswer().");
//
//    }
    
//    void setAnswer(int location,boolean isChecked){
//    	Log.i(LOG_TAG, "setAnswer()...");
//    	String label = myAdapter.choices.get(location).getLabel();
//    	if(isChecked){
//    		listItemID.add(label);
//    		answerLabels.append(label);
//    	}else{
//    		listItemID.clear();
//    		answerLabels.setLength(0);
//    	}
//		Log.i(LOG_TAG, "answerString:"+answerLabels.toString());
//		Log.i(LOG_TAG, "setAnswer().");
//    }
    
    public void reSetAnswer(){
    	Log.i(LOG_TAG, "-----------reSetAnswer()...------------");
    	
    	Log.i(LOG_TAG, "Clear answerItemList and answerLabels first...");
    	
    	listItemID.clear(); 
    	answerLabels.setLength(0);
    	
		int i=0;
		for(ChoiceExt ce:choiceExtList){
			if(ce.isChecked()){
				listItemID.add(String.valueOf(ce.getIndex()));
				if(i++>0){
					answerLabels.append(",");
				}
				answerLabels.append(ce.getLabel());
			}
		}
		
		Log.i(LOG_TAG, "answerLabels={" + answerLabels.toString()+"}");
		
		Log.i(LOG_TAG, "-----------reSetAnswer() end.-----------");
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
//		for (int i = 0; i < myAdapter.mChecked.size(); i++) {
//			if (myAdapter.mChecked.get(i)) {
//				Choice choice = myAdapter.choices.get(i);
//				listItemID.add(String.valueOf(choice.getLabel()));
//				if(i>0){
//					answerLabels.append(",");
//				}
//				answerLabels.append(String.valueOf(choice.getLabel()));
//			}
//		}
		
		for(ChoiceExt ce:choiceExtList){
			if(ce.isChecked()){
				listItemID.add(String.valueOf(ce.getLabel()));
				answerLabels.append(ce.getLabel());
				break;
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
    
    public void changeList(int p,boolean isChecked){
    	Log.i(LOG_TAG, "------------changeList()...-------------");
    	Log.i(LOG_TAG, "will change index "+String.valueOf(p)+" in choiceExtList to "+String.valueOf(isChecked));
    	
    	Log.i(LOG_TAG, "Before Change:");
    	for(ChoiceExt ext:choiceExtList){
			Log.i(LOG_TAG, String.valueOf(ext.getIndex()) + " " + ext.getLabel() + " "+ String.valueOf(ext.isChecked()) );
		}
    	
    	Log.i(LOG_TAG, "After Change:");
		int i=0;
		for(ChoiceExt ext:choiceExtList){
			ChoiceExt nExt = new ChoiceExt();
			nExt.setIndex(ext.getIndex());
			nExt.setActualLabel(ext.getActualLabel());
			nExt.setLabel(ext.getLabel());
			nExt.setContent(ext.getContent());
			nExt.setChecked(i==p?isChecked:false);
			choiceExtList.set(i, nExt);
			i++;
		}
		
		for(ChoiceExt ext:choiceExtList){
			Log.i(LOG_TAG, String.valueOf(ext.getIndex()) + " " + ext.getLabel() + " "+ String.valueOf(ext.isChecked()) );
		}
		
		Log.i(LOG_TAG, "------------changeList() End------------");
    }
    
    class MyListAdapter extends BaseAdapter{
    	List<ChoiceExt> choiceExtList = new ArrayList<ChoiceExt>();
    	LayoutInflater mInflater; 
    	
    	public MyListAdapter(List<ChoiceExt> choiceExtList){
    		Log.i(LOG_TAG,"MyListAdapter()...");
    		this.choiceExtList = choiceExtList;
    		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}

		@Override
		public int getCount() {
			return choiceExtList.size();
		}

		@Override
		public Object getItem(int position) {
			return choiceExtList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			View view;
			ViewHolder holder = null;
			
			if (convertView == null)  {
				convertView = mInflater.inflate(R.layout.paper_single_choices_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
				holder.radioButton = (RadioButton)convertView.findViewById(R.id.radioButton);
				holder.index = (TextView)convertView.findViewById(R.id.index);
				holder.choiceDesc = (TextView)convertView.findViewById(R.id.choiceDesc);
				
				final int p = position;
//				map.put(position, view);
				
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
						
						Log.i(LOG_TAG, "-----------------------holder.selected.setOnClickListener...-----------------------");
						RadioButton rb = (RadioButton)v;
						
						changeList(p,rb.isChecked());
						reSetAnswer();
						updateAllData();
						
						myAdapter.notifyDataSetChanged();
						Log.i(LOG_TAG, "-----------------------holder.selected.setOnClickListener End!-----------------------");
					}
				});
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			ChoiceExt choiceExt = choiceExtList.get(position);
			holder.radioButton.setChecked(choiceExt.isChecked());
			holder.index.setText(choiceExt.getLabel());
			holder.choiceDesc.setText(choiceExt.getContent());
			
    		if(choiceExt.isChecked()){
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
				progressDialog.dismiss();
			} catch (Exception e){
				progressDialog.dismiss();
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
