package com.dream.eexam.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.dream.eexam.adapter.CatalogAdapter;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.R;
import com.dream.eexam.base.ResultActivity;
import com.dream.eexam.model.CatalogInfo;
import com.dream.eexam.server.DataParseUtil;
import com.dream.eexam.server.FileUtil;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import com.msxt.client.model.Examination;
import com.msxt.client.model.SubmitSuccessResult;
import com.msxt.client.model.Examination.Catalog;
import com.msxt.client.model.Examination.Choice;
import com.msxt.client.model.Examination.Question;
import com.msxt.client.model.QUESTION_TYPE;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.server.ServerProxy.STATUS;
import com.msxt.client.server.WebServerProxy;

public class BaseQuestion extends BaseActivity{
	
	public final static String LOG_TAG = "BaseQuestion";
	
	//set exam header(
	protected ImageView imgHome = null;
	protected TableLayout catalogsTL = null;
	protected TextView catalogsTV = null;
	protected TextView currentTV = null;
	
	//set exam footer
	protected TextView remainingTime = null;	
	protected TextView submitTV = null;
	protected ImageView imgDownArrow = null;
	protected TextView waitTV = null;
	protected SeekBar completedSeekBar= null;
	protected TextView completedPercentage = null;
	protected TextView pendQueNumber = null;
	protected ImageView backArrow;
	protected ImageView nextArrow;
	
	protected InputStream inputStream;

	//static data
	protected String[] questionTypes;
	protected String[] choicesLabels;
	
	//data store in local database(question and answers)
	protected StringBuffer answerLabels = new StringBuffer();//answer labels
	protected Integer queSumOfCCatalog = 0;//question sum for current catalog
	protected Integer aQueSumOfCCatalog = 0;//answered question sum for current catalog
	
	protected Context mContext;
	
	//page data
	protected String cQuestionType;//current questionType
	protected int cCatalogIndex;//current catalog index
	
	protected int cCatalog1stQuestionIndex;//current 1st question index
	protected int cCataloglastQuestionIndex;//current last question index
	
	protected int cQuestionIndex;//question index in catalog
	protected int cQuestionIndexOfExam;//question index in exam
	protected String examFileName = null;
	protected String examFilePath = null;
	protected int moveDirect = 0;
	
	//-------------exam data---------------
	protected Examination exam;
	protected int examQuestionSum;
	protected int examAnsweredQuestionSum;
	protected List<Catalog> cataLogs = new ArrayList<Catalog>();
	protected Catalog cCatalog;
	protected Question cQuestion;
	protected List<Choice> cChoices;
	protected List<Question> pendQuestions = new ArrayList<Question>();
	protected List<CatalogInfo> catalogInfos = new ArrayList<CatalogInfo>();
	protected TimerTask  timerTask;
	protected Timer timer;
	protected Integer lMinutes = 0;
	protected Integer lSeconds = 0;
	
    public void loadDownLoadExam(DatabaseUtil dbUtil){
    	FileInputStream examStream = FileUtil.getExamStream(examFilePath,examFileName);
    	//load exam data
    	exam = DataParseUtil.getExam(examStream);
    	//load exam related data
    	examQuestionSum = DataParseUtil.getExamQuestionSum(exam);
    	cataLogs = exam.getCatalogs();
    	cCatalog = cataLogs.get(cCatalogIndex-1);
    	cCatalog1stQuestionIndex = DataParseUtil.getFirstQuestionIndexOfCatalog(exam, cCatalogIndex);
    	cQuestion = DataParseUtil.getQuestionByCidQid(exam, cCatalogIndex, cQuestionIndex);
    	cQuestionIndexOfExam = DataParseUtil.getQuestionExamIndex(exam, cQuestion.getId());
    	queSumOfCCatalog = cCatalog.getQuestions().size();
    	cCataloglastQuestionIndex = cCatalog1stQuestionIndex+queSumOfCCatalog-1;
    	cChoices = cQuestion.getChoices();
    	
    	loadPendingQuestions(dbUtil);
    	loadCatalogInfos(dbUtil);
    }
    
    public void loadPendingQuestions(DatabaseUtil dbUtil){
    	//load pending questions
    	Cursor cursor = null;
		for(Catalog catalog: cataLogs){
			List<Question> questions = catalog.getQuestions();
			for(Question question: questions){
				cursor = dbUtil.fetchAnswer(catalog.getIndex(),question.getIndex());
				if(cursor.moveToNext()){
					continue;
				}
				cursor.close();
				pendQuestions.add(question);
			}
		}
    }
    
    public void loadCatalogInfos(DatabaseUtil dbUtil){
		//load catalog list menu
		Cursor catalogCursor = null;
		int answeredQuetions;
		//set catalog menus
		for(Catalog catalog: cataLogs){
			catalogCursor = dbUtil.fetchAnswer(catalog.getIndex());
			answeredQuetions = 0;
			while(catalogCursor.moveToNext()){
				Integer qid = catalogCursor.getInt(1);
				String answers = catalogCursor.getString(3);
				if(qid!=null && answers!= null && answers.length()>0){
					answeredQuetions++;
				}
			}
			List<Question> qList = catalog.getQuestions();
			Integer totalQuestions = qList.size();

			catalogInfos.add(new CatalogInfo(catalog.getIndex(),catalog.getDesc(),
					DataParseUtil.getFirstQuestionIndexOfCatalog(exam, catalog.getIndex()),totalQuestions,answeredQuetions));
			catalogCursor.close();
		}
    }
	
    public void loadAnswerHistory(int cid,int qid,DatabaseUtil dbUtil){
    	Log.i(LOG_TAG, "loadAnswer()...");
		Log.i(LOG_TAG, "cid = " + cid + " qid = " + qid);
		//load label(s) of answer
    	
    	Cursor cursor = dbUtil.fetchAnswer(cid,qid);
		if (cursor != null && cursor.moveToNext()) {
			Log.i(LOG_TAG, "find answer...");
			Log.i(LOG_TAG, "cid: " + cursor.getInt(0) + " qid " + cursor.getInt(1)+ " qid_str " + cursor.getString(2) + " answer " + cursor.getString(3));
    		answerLabels.setLength(0);
    		answerLabels.append(cursor.getString(3));
		}
		cursor.close();
		
		//load answered question number of current catalog
		int sum = 0;
		cursor = dbUtil.fetchAnswer(cid);
		while(cursor.moveToNext()){
			Integer aQid = cursor.getInt(1);
			String aLabels = cursor.getString(2);
			if(aQid!=null && aLabels!= null && aLabels.length()>0){
				sum++;
			}
		}
		aQueSumOfCCatalog = sum;
		Log.i(LOG_TAG,"aQueSumOfCCatalog:"+ String.valueOf(aQueSumOfCCatalog));
    	cursor.close();
    	
    	examAnsweredQuestionSum = dbUtil.fetchAllAnswersCount();
    	
    	Log.i(LOG_TAG, "end loadAnswer().");
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i(LOG_TAG, "------------------------------onCreate()---------------------------------");
		
		questionTypes = getResources().getStringArray(R.array.question_types);
		choicesLabels = getResources().getStringArray(R.array.display_choice_label);
		
		//set file home and file
		examFilePath = SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences);
		examFileName = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_FILE_NAME, sharedPreferences);
		Log.i(LOG_TAG, "examFilePath:" + examFilePath);
		Log.i(LOG_TAG, "examFileName:" + examFileName);
		
		Bundle bundle = this.getIntent().getExtras();
		cQuestionType  = bundle.getString("questionType");
		cCatalogIndex  = Integer.valueOf(bundle.getString("ccIndex"));
		cQuestionIndex  = Integer.valueOf(bundle.getString("cqIndex"));
		
		Log.i(LOG_TAG, "cQuestionIndex:" + String.valueOf(cQuestionIndex));
		Log.i(LOG_TAG, "cCatalogIndex:" + String.valueOf(cCatalogIndex));
		
		//load old data from database
		DatabaseUtil dbUtil = new DatabaseUtil(this);
		dbUtil.open();
		loadAnswerHistory(cCatalogIndex,cQuestionIndex,dbUtil);
		loadDownLoadExam(dbUtil);//load exam with local file
		dbUtil.close();
		
		//set time task to set count down time 
		timerTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		};
		timer = new Timer();
		timer.schedule(timerTask,0,1000);
		
		// load count down time(MM:SS)
		setLoadCDTime();
		
		//save catalog and question cursor to local SharedPreferences
		saveccIndexcqIndex(cCatalogIndex,cQuestionIndex);
		
	}
	
	//set load countdown time
	protected void setLoadCDTime(){
		long currentTime = Calendar.getInstance().getTimeInMillis();
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		long starttime = sharedPreferences.getLong(SPUtil.CURRENT_EXAM_START_TIME, 0);
		long cosumeTime = (currentTime - starttime)/1000;//second
	    long examTime = exam.getTime() * 60;//second
	    
	    //time out
	    if(cosumeTime>examTime){
	    	ShowDialog(mContext.getResources().getString(R.string.dialog_note),
	    			"Exam Time Out!");
	    	
	    	//todo, submit answer
//	    	new SubmitAnswerTask().execute(exam.getId());
	    }else{
	    	long leftTime = examTime - cosumeTime;
	    	lMinutes = Integer.valueOf((int)(leftTime/60));
	    	lSeconds = Integer.valueOf((int)(leftTime - lMinutes * 60));
	    }
	}
	
	protected Result submitExam(String examId){
		ServerProxy proxy =  WebServerProxy.Factroy.getCurrrentInstance();
    	DatabaseUtil dbUtil = new DatabaseUtil(mContext);
    	dbUtil.open();
    	Map<String, String> answers =  getAllAnswers(dbUtil);
    	dbUtil.close();
		Result submitResult = proxy.submitAnswer(examId,answers);
		return submitResult;
	}
	
	class SubmitAnswerTask extends AsyncTask<String, Void, String> {
		Result submitResult;
		
    	@Override
    	protected void onPreExecute() {
    		Toast.makeText(mContext, "Exam time out, system will auto submit!", Toast.LENGTH_LONG).show();
    	}
    	
		@Override
		protected String doInBackground(String... arg0) {
			submitResult = submitExam(arg0[0]);
			return null;
		}
		
        @Override
        protected void onPostExecute(String result) {
        	if( submitResult.getStatus() == STATUS.ERROR ) {
        		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
            	dbUtil.open();
            	Map<String, String> answers =  getAllAnswers(dbUtil);
            	dbUtil.close();
            	
            	String path = SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences);
			    String examid = exam.getId();
			    
				saveAnswer2Local(answers,path,examid);
				SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.STATUS_START_SUBMIT_LOCAL, sharedPreferences);
				Toast.makeText(mContext, "Fail to submit to server, save answers to local!", Toast.LENGTH_LONG).show();
        		
        	} else{
        		SubmitSuccessResult succResult = DataParseUtil.getSubmitSuccessResult(submitResult);
        		//Save Exam Score to sharedPreferences
        		SPUtil.save2SP(SPUtil.CURRENT_EXAM_SCORE, String.valueOf(succResult.getScore()), sharedPreferences);
        		SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.STATUS_START_TIMEOUT_SUBMIT, sharedPreferences);
				//move question
				Intent intent = new Intent();
				intent.setClass( getBaseContext(), ResultActivity.class);
        	}
        }
		
	}
	
	/**
	 * save question index you current view 
	 * @param cqIndex
	 */
	public void saveccIndexcqIndex(Integer ccIndex,Integer cqIndex){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("ccIndex", ccIndex);  
		editor.putInt("cqIndex", cqIndex); 
		editor.commit();
		Log.i(LOG_TAG,"saveccIndexcqIndex()...");
		Log.i(LOG_TAG,"ccIndex="+String.valueOf(ccIndex));
		Log.i(LOG_TAG,"cqIndex="+String.valueOf(cqIndex));
	}
	
	protected void setCountDownTime() {
		if (lMinutes == 0) {
			if (lSeconds == 0) {
				remainingTime.setText("Time out !");
				
				//todo something
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				if (timerTask != null) {
					timerTask = null;
				}
			}else {
				lSeconds--;
				if (lSeconds >= 10) {
					remainingTime.setText("0"+lMinutes + ":" + lSeconds);
				}else {
					remainingTime.setText("0"+lMinutes + ":0" + lSeconds);
				}
			}
		}else {
			if (lSeconds == 0) {
				lSeconds =59;
				lMinutes--;
				if (lMinutes >= 10) {
					remainingTime.setText(lMinutes + ":" + lSeconds);
				}else {
					remainingTime.setText("0"+lMinutes + ":" + lSeconds);
				}
			}else {
				lSeconds--;
				if (lSeconds >= 10) {
					if (lMinutes >= 10) {
						remainingTime.setText(lMinutes + ":" + lSeconds);
					}else {
						remainingTime.setText("0"+lMinutes + ":" + lSeconds);
					}
				}else {
					if (lMinutes >= 10) {
						remainingTime.setText(lMinutes + ":0" + lSeconds);
					}else {
						remainingTime.setText("0"+lMinutes + ":0" + lSeconds);
					}
				}
			}
		}
	}
	
	public void updateAllData(){
		Log.i(LOG_TAG, "updateAllData()...");
		
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
		
		//save answers
		if(answerLabels.length()==0){
			clearAnswer(dbUtil,cCatalogIndex,cQuestionIndex);
		}else{
			saveAnswer(dbUtil,cCatalogIndex,cQuestionIndex,cQuestion.getId(),answerLabels.toString());
		}
		
		//set exam progress bar
		examAnsweredQuestionSum = dbUtil.fetchAllAnswersCount();
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
		
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		
/*		TypedArray colors = getResources().obtainTypedArray(R.array.completed_seekbar_colors);
		switch (per/25){
		case 0:completedSeekBar.setBackgroundColor(colors.getColor(0, 0));break;
		case 1:completedSeekBar.setBackgroundColor(colors.getColor(1, 0));break;
		case 2:completedSeekBar.setBackgroundColor(colors.getColor(2, 0));break;
		case 3:completedSeekBar.setBackgroundColor(colors.getColor(3, 0));break;
		}*/
		
		//set exam progress text
		completedPercentage.setText(String.valueOf(per)+"%");
		
		//set pending questions
		pendQuestions.clear();
		loadPendingQuestions(dbUtil);
		pendQueNumber.setText(mContext.getResources().getString(R.string.label_tv_waiting)+"("+Integer.valueOf(pendQuestions.size())+")");
		
		//set catalog list
		catalogInfos.clear();
		loadCatalogInfos(dbUtil);
		
		dbUtil.close();
		
		Log.i(LOG_TAG, "updateAllData().");
	}
	

	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 0:setCountDownTime();break;
				case 1:updateAllData();break;
			}
		}
	};
	
    //go to new question page
    public void gotoNewQuestion(Context context,int cid, int qid, int diret){
    	Log.i(LOG_TAG, "gotoNewQuestion()...");
		Question newQuestion = DataParseUtil.getNewQuestionByCidQid(exam, cid, qid,diret);
		
		if(newQuestion!=null){
//			String newQuestionType = newQuestion.getType();
			QUESTION_TYPE newQuestionType = newQuestion.getType();
			
			if(newQuestionType!=null){
				finish();
				
				//move question
				Intent intent = new Intent();
				intent.putExtra("ccIndex",String.valueOf(DataParseUtil.getCidByQid(exam, newQuestion.getId())));
				intent.putExtra("cqIndex",String.valueOf(newQuestion.getIndex()));
				
				
				if(QUESTION_TYPE.MULTIPLE_CHOICE.equals(newQuestionType)){
					intent.putExtra("questionType", questionTypes[0]);
					intent.setClass( context, MultiChoices.class);
				}else if(QUESTION_TYPE.SINGLE_CHOICE.equals(newQuestionType)){
					intent.putExtra("questionType", questionTypes[1]);
					intent.setClass( context, SingleChoices.class);
				}
				
				startActivity(intent);
			}			
		}else{
			if(diret==1){
				ShowDialog(mContext.getResources().getString(R.string.dialog_note),
						"This question is the last question in Exam!");
			}else if(diret==-1){
				ShowDialog(mContext.getResources().getString(R.string.dialog_note),
						"This question is the first question in Exam!");
			}
		}

    }

    public Map<String, String> getAllAnswers(DatabaseUtil dbUtil){
    	Log.w(LOG_TAG, "getAllAnswers()...");
    	
    	Map<String, String> answers = new HashMap<String,String>();
    	Cursor cursor = dbUtil.fetchAllAnswers();
    	while(cursor.moveToNext()){
			String qidStr = cursor.getString(2);
			String answer = cursor.getString(3);
			
			if(qidStr!=null && answer!= null && answer.length()>0){
				answers.put(qidStr, answer.replaceAll(",", ""));
				Log.w(LOG_TAG, qidStr+":"+answer.replaceAll(",", ""));
			}
		}
    	cursor.close();
    	
    	return answers;
    }
    
    public void clearAnswer(DatabaseUtil dbUtil,Integer cid,Integer qid){
    	Log.i(LOG_TAG, "clearAnswer()...");
    	Log.i(LOG_TAG, "(cid="+String.valueOf(cid)+",qid="+String.valueOf(qid)+")");
    	dbUtil.deleteAnswer(cid,qid);
    	Log.i(LOG_TAG, "end clearAnswer().");
    }
	
	public void saveAnswer(DatabaseUtil dbUtil,Integer cid,Integer qid,String qidStr,String answers){
    	Log.i(LOG_TAG, "saveAnswer()...");
    	Cursor cursor = dbUtil.fetchAnswer(cid,qid);
    	if(cursor != null && cursor.moveToNext()){
    		Log.i(LOG_TAG, "updateAnswer("+cid+","+qid+","+answers+")");
    		dbUtil.updateAnswer(cid,qid,qidStr,answers);
    	}else{
    		Log.i(LOG_TAG, "createAnswer("+cid+","+qid+","+answers+")");
    		dbUtil.createAnswer(cid,qid,qidStr,answers);
    	}
    	Log.i(LOG_TAG, "saveAnswer().");
    }

	public void submitAnswer(){
		Log.i(LOG_TAG, "submitAnswer()...");
	}
	
	public void saveExamStatus(){
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("exam_status", "end");
		editor.commit();		
	}
	
	//save answer 2 local SD card by user id
	public void saveAnswer2Local(Map<String, String> answer, String path,
			String examId) {
		StringBuffer content = new StringBuffer();

		Iterator<Entry<String, String>> it = answer.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			content.append(key + ";" + value + "\n");
		}

		FileOutputStream foutput;
		try {
			foutput = new FileOutputStream(path + File.separator + examId+ "_answer.txt");
			foutput.write(content.toString().getBytes());
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage());
		}

	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(LOG_TAG, "------------------------------onDestroy()---------------------------------");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(LOG_TAG, "------------------------------onRestart()---------------------------------");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.i(LOG_TAG, "------------------------------onResume()---------------------------------");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(LOG_TAG, "------------------------------onStart()---------------------------------");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(LOG_TAG, "------------------------------onStop()---------------------------------");
	}

	//----------------------------------define popupWindow-----------------------------
	protected PopupWindow popupWindow;
	protected ListView lv_group;
	protected Integer pressedItemIndex = -1;
	protected View popupView;
	
	protected void showWindow(View parent) {
		Log.i(LOG_TAG, "showWindow()...");
		if (popupWindow == null) {
			//get components
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			popupView = layoutInflater.inflate(R.layout.catalog_info_list, null);
			lv_group = (ListView) popupView.findViewById(R.id.lvGroup);
			
			//get latest data and display in items
			DatabaseUtil dbUtil = new DatabaseUtil(mContext);
			dbUtil.open();
			catalogInfos.clear();
			loadCatalogInfos(dbUtil); 
			dbUtil.close();
			CatalogAdapter groupAdapter = new CatalogAdapter(this, catalogInfos);
			lv_group.setAdapter(groupAdapter);
			
			int ppH = Integer.valueOf(getResources().getString(R.string.popup_window_height));
			int ppW = Integer.valueOf(getResources().getString(R.string.popup_window_width));
			
			popupWindow = new PopupWindow(popupView,ppH, ppW);
		}else{
			//get latest data and display in items
			DatabaseUtil dbUtil = new DatabaseUtil(mContext);
			dbUtil.open();
			catalogInfos.clear();
			loadCatalogInfos(dbUtil);
			dbUtil.close();
			
			CatalogAdapter groupAdapter = new CatalogAdapter(this, catalogInfos);
			lv_group.setAdapter(groupAdapter);
			
			popupWindow.dismiss();
		}

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);//window will dismiss once touch out of it
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//window will dismiss once click back 
//		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		int xPos = windowManager.getDefaultDisplay().getWidth() / 3 ;
//		Log.i(LOG_TAG, "xPos:" + xPos);

		popupWindow.showAsDropDown(parent,0, 20);
		
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				Log.i(LOG_TAG, "onItemClick()...");
				//initial all items background color
        		for(int i=0;i<adapterView.getChildCount();i++){
        			View item = adapterView.getChildAt(i);
        			item.setBackgroundColor(getResources().getColor(R.color.catalog_menu_bg));
        		}
				view.setBackgroundColor(getRequestedOrientation());
				pressedItemIndex = position;
				Log.i(LOG_TAG, "pressedItemIndex:" + pressedItemIndex);
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				
				CatalogInfo info = catalogInfos.get(position);
				Question nQuestion = DataParseUtil.getQuestionByCidQid(exam, info.getIndex(),1);
				QUESTION_TYPE nQuestionType = nQuestion.getType();
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(info.getIndex()));
				intent.putExtra("cqIndex", String.valueOf(nQuestion.getIndex()));
				if(QUESTION_TYPE.MULTIPLE_CHOICE.equals(nQuestionType)){
					intent.putExtra("questionType", questionTypes[0]);
					intent.setClass( mContext, MultiChoices.class);
				}else if(QUESTION_TYPE.SINGLE_CHOICE.equals(nQuestionType)){
					intent.putExtra("questionType", questionTypes[1]);
					intent.setClass( mContext, SingleChoices.class);
				}else{
					ShowDialog(mContext.getResources().getString(R.string.dialog_note),
							"Wrong Question Type: " + cQuestionType);
				}
				finish();
				startActivity(intent);
			}
		});
	}

	
}
