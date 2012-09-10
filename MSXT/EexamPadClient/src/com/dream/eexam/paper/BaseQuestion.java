package com.dream.eexam.paper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.GroupAdapter;
import com.dream.eexam.base.R;
import com.dream.eexam.model.CatalogInfo;
import com.dream.eexam.server.DataUtil;
import com.dream.eexam.server.FileUtil;
import com.dream.eexam.util.DatabaseUtil;
import com.msxt.client.model.Examination;
import com.msxt.client.model.Examination.Catalog;
import com.msxt.client.model.Examination.Choice;
import com.msxt.client.model.Examination.Question;

public class BaseQuestion extends BaseActivity implements OnDoubleTapListener, OnGestureListener,OnTouchListener{
	
	public final static String LOG_TAG = "BaseQuestion";
	
	//set exam header(
	protected TextView homeTV = null;//(Left)
//	protected TextView remainingTimeLabel = null;//(Center 1)
	protected TextView remainingTime = null;	//(Center 2)	
	protected TextView submitTV = null;//(Right)
	protected ImageView imgDownArrow = null;
	//set catalog bar
	protected TextView catalogsTV = null;
	protected TextView currentTV = null;
	protected TextView waitTV = null;
	//set exam footer
	protected SeekBar completedSeekBar= null;
	protected TextView completedPercentage = null;
	protected TextView pendQueNumber = null;
//	protected Button preBtn = null;
//	protected Button nextBtn = null;
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
    	exam = DataUtil.getExam(examStream);
    	//load exam related data
    	examQuestionSum = DataUtil.getExamQuestionSum(exam);
    	cataLogs = exam.getCatalogs();
    	cCatalog = cataLogs.get(cCatalogIndex-1);
    	cCatalog1stQuestionIndex = DataUtil.getFirstQuestionIndexOfCatalog(exam, cCatalogIndex);
    	cQuestion = DataUtil.getQuestionByCidQid(exam, cCatalogIndex, cQuestionIndex);
    	cQuestionIndexOfExam = DataUtil.getQuestionExamIndex(exam, cQuestion.getId());
    	queSumOfCCatalog = cCatalog.getQuestions().size();
    	cCataloglastQuestionIndex = cCatalog1stQuestionIndex+queSumOfCCatalog-1;
    	cChoices = cQuestion.getChoices();
    	
    	loadPendingQuestions(dbUtil);
    	loadCatalogInfos(dbUtil);
    }
    
    public void loadPendingQuestions(DatabaseUtil dbUtil){
    	//load pending questions
//    	dbUtil.open();
    	Cursor cursor = null;
		for(Catalog catalog: cataLogs){
			List<Question> questions = catalog.getQuestions();
			for(Question question: questions){
				cursor = dbUtil.fetchAnswer(catalog.getIndex(),question.getIndex());
				if(cursor.moveToNext()){
					continue;
				}
				pendQuestions.add(question);
			}
		}
		cursor.close();
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
					DataUtil.getFirstQuestionIndexOfCatalog(exam, catalog.getIndex()),totalQuestions,answeredQuetions));
			catalogCursor.close();
		}
		catalogCursor.close();
    }
	
    public void loadAnswerHistory(int cid,int qid,DatabaseUtil dbUtil){
    	Log.i(LOG_TAG, "loadAnswer()...");
		Log.i(LOG_TAG, "cid = " + cid + " qid = " + qid);
		
		//load label(s) of answer
    	
    	dbUtil.open();
    	Cursor cursor = dbUtil.fetchAnswer(cid,qid);
		if (cursor != null && cursor.moveToNext()) {
			Log.i(LOG_TAG, "find answer...");
			Log.i(LOG_TAG, "cid: " + cursor.getInt(0) + " qid " + cursor.getInt(1)+ " qid_str " + cursor.getString(2) + " answer " + cursor.getString(3));
    		answerLabels.setLength(0);
    		answerLabels.append(cursor.getString(3));
		}
		
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
		examFilePath = Environment.getExternalStorageDirectory().getPath()+ File.separator + getResources().getString(R.string.app_file_home);
		examFileName = getResources().getString(R.string.exam_file_name);
		
		Bundle bundle = this.getIntent().getExtras();
		cQuestionType  = bundle.getString("questionType");
		cCatalogIndex  = Integer.valueOf(bundle.getString("ccIndex"));
		cQuestionIndex  = Integer.valueOf(bundle.getString("cqIndex"));
		
		Log.i(LOG_TAG, "cQuestionIndex:" + String.valueOf(cQuestionIndex));
		Log.i(LOG_TAG, "cCatalogIndex:" + String.valueOf(cCatalogIndex));
		
		//load old data from database
		DatabaseUtil dbUtil = new DatabaseUtil(this);
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
		
		//set load count down time(MM:SS)
		setLoadCDTime();
		
		//save catalog and question cursor to local SharedPreferences
		saveccIndexcqIndex(cCatalogIndex,cQuestionIndex);
		
	}
	
	protected void setLoadCDTime(){
		long currentTime = Calendar.getInstance().getTimeInMillis();
		sharedPreferences = this.getSharedPreferences("eexam",MODE_PRIVATE);
		long starttime = sharedPreferences.getLong("starttime", 0);
		long cosumeTime = (currentTime - starttime)/1000;//second
	    long examTime = exam.getTime() * 60;//second
	    if(cosumeTime>examTime){
	    	ShowDialog("Exam Time Out!");
	    }else{
	    	long leftTime = examTime - cosumeTime;
	    	lMinutes = Integer.valueOf((int)(leftTime/60));
	    	lSeconds = Integer.valueOf((int)(leftTime - lMinutes * 60));
	    }
	}
	
	protected void setCountDownTime() {
		if (lMinutes == 0) {
			if (lSeconds == 0) {
				remainingTime.setText("Time out !");
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
		
		//set exam progress text
		completedPercentage.setText(String.valueOf(per)+"%");
		
		//set pending questions
		pendQuestions.clear();
		loadPendingQuestions(dbUtil);
		pendQueNumber.setText("Pending("+Integer.valueOf(pendQuestions.size())+")");
		
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
		Question newQuestion = DataUtil.getNewQuestionByCidQid(exam, cid, qid,diret);
		
		if(newQuestion!=null){
			String newQuestionType = newQuestion.getType();
			if(newQuestionType!=null){
				finish();
				
				//move question
				Intent intent = new Intent();
				intent.putExtra("ccIndex",String.valueOf(DataUtil.getCidByQid(exam, newQuestion.getId())));
				intent.putExtra("cqIndex",String.valueOf(newQuestion.getIndex()));
				intent.putExtra("questionType", newQuestionType);
				
				if(questionTypes[0].equals(newQuestionType)){
					intent.setClass( context, MultiChoices.class);
				}else if(questionTypes[1].equals(newQuestionType)){
					intent.setClass( context, SingleChoices.class);
				}
				
				startActivity(intent);
			}			
		}else{
			if(diret==1){
				ShowDialog("This question is the last question in Exam!");
			}else if(diret==-1){
				ShowDialog("This question is the first question in Exam!");
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
//    	DatabaseUtil dbUtil = new DatabaseUtil(this);
//    	dbUtil.open();
    	dbUtil.deleteAnswer(cid,qid);
//    	dbUtil.close();
    	Log.i(LOG_TAG, "end clearAnswer().");
    }
	
	public void saveAnswer(DatabaseUtil dbUtil,Integer cid,Integer qid,String qidStr,String answers){
    	Log.i(LOG_TAG, "saveAnswer()...");
//    	DatabaseUtil dbUtil = new DatabaseUtil(context);
//    	dbUtil.open();
    	Cursor cursor = dbUtil.fetchAnswer(cid,qid);
    	if(cursor != null && cursor.moveToNext()){
    		Log.i(LOG_TAG, "updateAnswer("+cid+","+qid+","+answers+")");
    		dbUtil.updateAnswer(cid,qid,qidStr,answers);
    	}else{
    		Log.i(LOG_TAG, "createAnswer("+cid+","+qid+","+answers+")");
    		dbUtil.createAnswer(cid,qid,qidStr,answers);
    	}
//    	dbUtil.close();
    	Log.i(LOG_TAG, "saveAnswer().");
    }

	public void submitAnswer(){
//		getAllAnswers();
		Log.i(LOG_TAG, "submitAnswer()...");
//		ShowDialog("Submit Success!");
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
			GroupAdapter groupAdapter = new GroupAdapter(this, catalogInfos);
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
			
			GroupAdapter groupAdapter = new GroupAdapter(this, catalogInfos);
			lv_group.setAdapter(groupAdapter);
			
			popupWindow.dismiss();
		}

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);//window will dismiss once touch out of it
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//window will dismiss once click back 
//		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		int xPos = windowManager.getDefaultDisplay().getWidth() / 3 ;
//		Log.i(LOG_TAG, "xPos:" + xPos);

		popupWindow.showAsDropDown(parent,0, 0);
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
				Question nQuestion = DataUtil.getQuestionByCidQid(exam, info.getIndex(),1);
				String nQuestionType = nQuestion.getType();
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(info.getIndex()));
				intent.putExtra("cqIndex", String.valueOf(nQuestion.getIndex()));
				if(questionTypes[0].equals(nQuestionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( mContext, MultiChoices.class);
				}else if(questionTypes[1].equals(nQuestionType)){
					intent.putExtra("questionType", "Single Select");
					intent.setClass( mContext, SingleChoices.class);
				}else{
					ShowDialog("Wrong Question Type: " + cQuestionType);
				}
				finish();
				startActivity(intent);
			}
		});
	}
	
	
	//----------------------------------define fling move------------------------------------
	GestureDetector detector = null;
	protected int verticalMinDistance = 5;
	protected int horizontalMinDistance = 5;
    protected int minVelocity         = 0;
    
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
