package com.dream.eexam.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.GroupAdapter;
import com.dream.eexam.base.R;
import com.dream.eexam.model.CatalogBean;
import com.dream.eexam.model.CatalogInfo;
import com.dream.eexam.model.ExamDetailBean;
import com.dream.eexam.model.Question;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SystemConfig;
import com.dream.eexam.util.XMLParseUtil;

public class BaseQuestion extends BaseActivity implements OnDoubleTapListener, OnGestureListener,OnTouchListener{
	
	public final static String LOG_TAG = "BaseQuestion";
	
	//set exam header(
	protected TextView homeTV = null;//(Left)
	protected TextView remainingTimeLabel = null;//(Center 1)
	protected TextView remainingTime = null;	//(Center 2)	
	protected TextView submitTV = null;//(Right)
	
	//set catalog bar
	protected TextView catalogsTV = null;
	protected TextView currentTV = null;
	protected TextView waitTV = null;

	//set exam footer
	protected SeekBar completedSeekBar= null;
	protected TextView completedPercentage = null;
	protected TextView pendQueNumber = null;
	protected Button preBtn = null;
	protected TextView questionIndex = null;
	protected Button nextBtn = null;
	
	protected InputStream inputStream;
	
	//common data
	protected String questionType;//questionType
	protected Integer currentCatalogIndex;//current catalog index
	protected Integer currentQuestionIndex;//current question index
	
	protected ExamDetailBean detailBean;//paperBean
	protected List<Question> pendQuestions = new ArrayList<Question>();
	protected Question question;
	protected Integer totalQuestions;
	protected Integer answeredQuestions;
	
	protected List<CatalogBean> cataLogList = new ArrayList<CatalogBean>();//cataLogList
	protected List<CatalogInfo> catalogNames = new ArrayList<CatalogInfo>();//catalog names

	protected Integer direction = 0;//move direction(1 move next, 0 move previous)
	protected Integer questionSize = 0;//question size for current catalog
	protected Integer comQuestionSize = 0;//completed question size for current catalog
	
	protected StringBuffer answerString = new StringBuffer();//answer ids
	protected Context mContext;
	
	protected String downloadExamFile = null;
	protected String downloadExamFilePath = null;
	
	protected String questionTypeM;
	protected String questionTypeS;
	
    public void loadAnswer(){
    	Log.i(LOG_TAG, "loadAnswer()...");
		Log.i(LOG_TAG, "currentCatalogIndex = " + currentCatalogIndex
				+ " currentQuestionIndex = " + currentQuestionIndex);
    	DatabaseUtil dbUtil = new DatabaseUtil(this);
    	dbUtil.open();
    	Cursor cursor = dbUtil.fetchAnswer(currentCatalogIndex,currentQuestionIndex);
		if (cursor != null && cursor.moveToNext()) {
			Log.i(LOG_TAG, "find answer...");
			Log.i(LOG_TAG,
					"cid: " + cursor.getInt(0) + " qid "
							+ cursor.getInt(1) + " answer " + cursor.getString(2));
    		answerString.setLength(0);
    		answerString.append(cursor.getString(2));
		}
		int sum = 0;
		cursor = dbUtil.fetchAnswer(currentCatalogIndex);
		while(cursor.moveToNext()){
			Integer qid = cursor.getInt(1);
			String answers = cursor.getString(2);
			if(qid!=null && answers!= null && answers.length()>0){
				sum++;
			}
		}
		comQuestionSize = sum;
		Log.i(LOG_TAG,"comQuestionSize:"+ String.valueOf(comQuestionSize));
    	cursor.close();
    	dbUtil.close();  
    	Log.i(LOG_TAG, "end loadAnswer().");
    }
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		questionTypeM = SystemConfig.getInstance().getPropertyValue("Question_Type_Multi_Select");
		questionTypeS = SystemConfig.getInstance().getPropertyValue("Question_Type_Single_Select");
		
		//get demoSessionStr and save to string array
		Bundle bundle = this.getIntent().getExtras();
		String questionType  = bundle.getString("questionType");
		String ccIndex  = bundle.getString("ccIndex");
		String cqIndex  = bundle.getString("cqIndex");
		
		if(ccIndex!=null){
			this.questionType = questionType;
		}
		if(ccIndex!=null){
			currentCatalogIndex = Integer.valueOf(ccIndex);
		}
		if(cqIndex!=null){
			currentQuestionIndex = Integer.valueOf(cqIndex);
		}
		saveccIndexcqIndex(Integer.valueOf(cqIndex),Integer.valueOf(cqIndex));
    	
		try {
	    	FileInputStream inputStream = getExamStream();
	    	detailBean = XMLParseUtil.readExamination(inputStream);
	    	//set catalog list
	    	cataLogList = detailBean.getCatalogs();
	    	//set catalog questionSize
			if(cataLogList!=null&&cataLogList.size()>0){
				CatalogBean bean = cataLogList.get(currentCatalogIndex-1);
			    questionSize = bean.getQuestions().size();
			    Log.i(LOG_TAG,"questionSize:"+ String.valueOf(questionSize));
			}
			//set question
			question = detailBean.getQuestionByCidQid(currentCatalogIndex, currentQuestionIndex);
			totalQuestions = detailBean.getTotalQuestions();

			//load pending questions
	    	DatabaseUtil dbUtil = new DatabaseUtil(this);
	    	dbUtil.open();
	    	Cursor cursor = null;
			if(cataLogList!=null&&cataLogList.size()>0){
				for(CatalogBean catalogBean: cataLogList){
					List<Question> questions = catalogBean.getQuestions();
					for(Question question: questions){
						cursor = dbUtil.fetchAnswer(catalogBean.getIndex(),question.getIndex());
						if(cursor.moveToNext()){
							continue;
						}
						pendQuestions.add(question);
					}
				}
			}
			cursor.close();
			dbUtil.close();
			
		} catch (Exception e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		
		DatabaseUtil dbUtil = new DatabaseUtil(this);
		dbUtil.open();
		//-------------------get data-----------------------
		Cursor cursor;
		String catalogDesc;
		int sum;
		//set groups
		for(CatalogBean catalogBean: cataLogList){
			cursor = dbUtil.fetchAnswer(catalogBean.getIndex());
			sum = 0;
			while(cursor.moveToNext()){
				Integer qid = cursor.getInt(1);
				String answers = cursor.getString(2);
				if(qid!=null && answers!= null && answers.length()>0){
					sum++;
				}
			}
			cursor.close();
			catalogDesc = catalogBean.getDesc()+"(Total:"+catalogBean.getQuestions().size()+","+"Finished:"+String.valueOf(sum)+")";
			Log.i(LOG_TAG, "catalog: "+catalogDesc);
			
			catalogNames.add(new CatalogInfo(catalogBean.getIndex(),catalogBean.getDesc(),catalogBean.getQuestions().size(),sum));
		}
		
		//get answered questions
    	answeredQuestions = dbUtil.fetchAllAnswersCount();
    	Log.i(LOG_TAG, "answeredQuestions:" + String.valueOf(answeredQuestions));	
    	
    	//close db
		dbUtil.close();
		
		
		
	}

	public void submitAnswer(){
		Log.i(LOG_TAG, "submitAnswer()...");
	}
	/**
	 * 
	 * @return
	 */
	public FileInputStream getExamStream(){
		downloadExamFilePath = Environment.getExternalStorageDirectory().getPath()+ File.separator + "eExam";
		downloadExamFile = SystemConfig.getInstance().getPropertyValue("Download_Exam");
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(downloadExamFilePath+ File.separator+downloadExamFile));
		} catch (FileNotFoundException e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		return inputStream;
	}
	
    public void clearAnswer(Context context,Integer cid,Integer qid){
    	Log.i(LOG_TAG, "clearAnswer()...");
    	
    	Log.i(LOG_TAG, "(cid="+String.valueOf(cid)+",qid="+String.valueOf(qid)+")");
    	DatabaseUtil dbUtil = new DatabaseUtil(this);
    	dbUtil.open();
    	dbUtil.deleteAnswer(cid,qid);
    	dbUtil.close();
    	
    	Log.i(LOG_TAG, "end clearAnswer().");
    }
	
	public void saveAnswer(Context context,Integer cid,Integer qid,String answers){
    	Log.i(LOG_TAG, "saveAnswer()...");
    	
    	DatabaseUtil dbUtil = new DatabaseUtil(context);
    	dbUtil.open();
    	Cursor cursor = dbUtil.fetchAnswer(cid,qid);
    	if(cursor != null && cursor.moveToNext()){
    		Log.i(LOG_TAG, "updateAnswer("+cid+","+qid+","+answers+")");
    		dbUtil.updateAnswer(cid,qid,"("+ answers+")");
    	}else{
    		Log.i(LOG_TAG, "createAnswer("+cid+","+qid+","+answers+")");
    		dbUtil.createAnswer(cid,qid, "("+ answers+")");
    	}
    	dbUtil.close();
    	
    	Log.i(LOG_TAG, "saveAnswer().");
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	
	protected PopupWindow popupWindow;
	protected ListView lv_group;
	protected Integer pressedItemIndex = -1;
	protected View popupView;
	
	protected void showWindow(View parent) {
		Log.i(LOG_TAG, "showWindow()...");
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			popupView = layoutInflater.inflate(R.layout.catalog_info_list, null);
			lv_group = (ListView) popupView.findViewById(R.id.lvGroup);
//			groups = new ArrayList<String>();
			
			Log.i(LOG_TAG, "pressedItemIndex:"+pressedItemIndex);
			GroupAdapter groupAdapter = new GroupAdapter(this, catalogNames);
			lv_group.setAdapter(groupAdapter);
			int ppH = Integer.valueOf(getResources().getString(R.string.popup_window_height));
			int ppW = Integer.valueOf(getResources().getString(R.string.popup_window_width));
			popupWindow = new PopupWindow(popupView,ppH, ppW);
		}else{
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
				
//				catalogsTV.setText(cataLogList.get(position).getDesc());
				
				inputStream =  getExamStream();
				String questionType = null;
				try {
					 Log.i(LOG_TAG, "Go to catalog "+String.valueOf((position+1))+" question 1");
					 questionType = XMLParseUtil.readQuestionType(inputStream,(position+1), 1);
					 
					 Log.i(LOG_TAG, "questionType " + questionType);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.i(LOG_TAG, e.getMessage());
				}
				
				Intent intent = new Intent();
				intent.putExtra("ccIndex", String.valueOf(position+1));
				intent.putExtra("cqIndex", String.valueOf(1));
				if(questionTypeM.equals(questionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( mContext, MultiChoices.class);
				}else if(questionTypeS.equals(questionType)){
					intent.putExtra("questionType", "Single Select");
					intent.setClass( mContext, SingleChoices.class);
				}else{
					ShowDialog("Wrong Question Type: " + questionType);
				}
				finish();
				startActivity(intent);
				
			}
		});
	}

	//----------------------------------------------------------------------
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
