package com.dream.eexam.paper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dream.eexam.base.R;
import com.dream.eexam.server.DataParseUtil;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.FileUtil;
import com.dream.eexam.util.SPUtil;
import com.msxt.client.model.QUESTION_TYPE;
import com.msxt.client.model.SubmitSuccessResult;
import com.msxt.client.model.Examination.Question;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.WebServerProxy;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.server.ServerProxy.STATUS;

public class PendQuestions extends BaseQuestion {
	
	public final static String LOG_TAG = "PendQuestions";

	GridView gridList;
    String qType = null;
    String qid = null;
	
	//data statement
	PendQueListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	void loadComponents(){
		imgHome = (ImageView) findViewById(R.id.imgHome);
		
		//header components
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		
		//footer components
		backArrow = (ImageView)findViewById(R.id.backArrow);
		pendQueNumber = (TextView)findViewById(R.id.pendQueNumber);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);   	
    	
		submitTV = (TextView)findViewById(R.id.submitTV);
		nextArrow = (ImageView)findViewById(R.id.nextArrow);
    	
	}
	
	void setHeader(){
		
		imgHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PendQuestions.this);
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
		
        //set catalog bar(Center) 
		catalogsTV.setText(String.valueOf(cCatalogIndex)+". "+
				cCatalog.getDesc() + 
				"(Q" + String.valueOf(cQuestionIndex)+" - " + "Q" + String.valueOf(cQuestionIndex+queSumOfCCatalog-1)+")");
		catalogsTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_questions);
        mContext = getApplicationContext();
        loadComponents();
        setHeader();
        //set List
        gridList = (GridView)findViewById(R.id.gridview);
        adapter = new PendQueListAdapter(pendQuestions);
        gridList.setAdapter(adapter);
        gridList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {

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
    	backArrow.setVisibility(View.INVISIBLE);
		pendQueNumber.setVisibility(View.INVISIBLE);
		
		setRemainingTime();
		
		//set completedSeekBar
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"%");
		
		submitTV.setVisibility(View.INVISIBLE);
		nextArrow.setVisibility(View.INVISIBLE);
    }
    
    class PendQueListAdapter extends BaseAdapter{
    	List<Question> questions = new ArrayList<Question>();
    	private LayoutInflater mInflater;
    	
    	public PendQueListAdapter(List<Question> questions){
    		this.questions = questions;
    		mInflater = LayoutInflater.from(mContext);  
    	}

		@Override
		public int getCount() {
			return questions.size();
		}

		@Override
		public Object getItem(int position) {
			return questions.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(LOG_TAG,"getView()..."+" position="+position);
        	
			ViewHolder holder;  
            if (convertView == null){  
                holder = new ViewHolder();  
                convertView = mInflater.inflate(R.layout.pending_questions_item, null);  
                holder.questionBtn = (Button) convertView.findViewById(R.id.questionBtn);  
                convertView.setTag(holder);  
            }else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            
            Question question = questions.get(position);
            qid = question.getId();
            
            holder.questionBtn.setText(String.valueOf(DataParseUtil.getQuestionExamIndex(exam, qid)));  
            holder.questionBtn.setOnClickListener(new Button.OnClickListener() {
    			public void onClick(View v) {
    				Log.i(LOG_TAG,"onClick()...");
    				
    				Button sButton = (Button)v;
    	            String indexInExam = sButton.getText().toString();
    	            Question nQuestion = DataParseUtil.getQuestionByIndexInExam(exam, Integer.valueOf(indexInExam));
    	            
    				//move question
    				Intent intent = new Intent();
    				intent.putExtra(SPUtil.CURRENT_EXAM_CATALOG, String.valueOf(DataParseUtil.getCidByQid(exam, nQuestion.getId())));
    				intent.putExtra(SPUtil.CURRENT_EXAM_INDEX_IN_CATA, String.valueOf(nQuestion.getIndex()));
    				
    				if(QUESTION_TYPE.MULTIPLE_CHOICE.equals(nQuestion.getType())){
    					intent.putExtra("questionType", questionTypes[0]);
    					intent.setClass( mContext, MultiChoices.class);
    				}else if(QUESTION_TYPE.SINGLE_CHOICE.equals(nQuestion.getType())){
    					intent.putExtra("questionType", questionTypes[1]);
    					intent.setClass( mContext, SingleChoices.class);
    				}else{
    					ShowDialog(mContext.getResources().getString(R.string.dialog_note),"Invalid qeustion type!");
    				}
    				finish();
    				startActivity(intent);
    			}
    		});
            return convertView; 
		}
    	
    }
    
    static class ViewHolder{
    	Button questionBtn;
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
    		progressDialog = ProgressDialog.show(PendQuestions.this, null, displayMessage, true, true);
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
        	
        	if( submitResult!=null && submitResult.getStatus() == STATUS.SUCCESS ) {
        		
        		//get result file name
           		String resultFileName = FileUtil.RESULT_FILE_PREFIX + exam.getId() + FileUtil.FILE_SUFFIX_XML;
        		Log.i(LOG_TAG, "resultFileName: " + resultFileName);
        		
        		//save submit result file
    			FileUtil fu = new FileUtil();
        		fu.saveFile(SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences), resultFileName, submitResult.getSuccessMessage());
        		
        		//parse submit success result 
        		SubmitSuccessResult succResult = DataParseUtil.getSubmitSuccessResult(submitResult);
        		
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
    			AlertDialog.Builder builder = new AlertDialog.Builder(PendQuestions.this);
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
