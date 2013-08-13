package com.dream.eexam.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.adapter.CatalogAdapter;
import com.dream.eexam.adapter.ChoiceAdapter;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.R;
import com.dream.eexam.model.CatalogInfo;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import com.dream.exam.bean.Catalog;
import com.dream.exam.bean.Exam;
import com.dream.exam.bean.ExamParse;
import com.dream.exam.bean.ExamXMLParse;
import com.dream.exam.bean.Question;

public class ChoiceQuestion extends BaseActivity {
	public final static String LOG_TAG = "ChoiceQuestion";
	
	TextView questionTV;
	Exam exam;
	Question question;
	
	ListView listView;
	ChoiceAdapter adapter;
	
	public void loadData(){
		//get exam
    	String home = SPUtil.getFromSP(SPUtil.CURRENT_APP_HOME, sharedPreferences);
    	String userId = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
    	String examId = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_ID, sharedPreferences);
    	FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(home+ File.separator+userId+ File.separator+examId));
			exam = ExamXMLParse.parseExam(inputStream);
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG,e.getMessage());
		}
		
		//get question
//		int cId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_CATALOG_ID, sharedPreferences);
//    	int qId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_QUESTON_ID, sharedPreferences);
    	question = ExamParse.getQuestion(exam, 1, 1);
	}
	
	TableLayout catalogsTL;
	ImageView backArrow;
	ImageView nextArrow;
	
	void loadComponents(){
		catalogsTL = (TableLayout)findViewById(R.id.catalogsTL);
		catalogsTL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "catalogsTL.onClick()...");
				showWindow(v);
			}
		});
		backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeQuestion(1,1);
			}
		});  
		nextArrow = (ImageView)findViewById(R.id.nextArrow);
		nextArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeQuestion(1,2);
			}
		}); 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_list);
		mContext = getApplicationContext();
		
		loadData();
		loadComponents();
		
		String questionHint = "Q"
				+ String.valueOf(question.getIndex())
				+ " ("
				+ mContext.getResources().getString(
						R.string.msg_question_score) + ":"
				+ String.valueOf(question.getScore()) + ")\n";
		
        questionTV = (TextView) findViewById(R.id.questionTV);
        questionTV.setMovementMethod(ScrollingMovementMethod.getInstance()); 
        questionTV.setText(questionHint + question.getContent());
        
        adapter = new ChoiceAdapter(mContext,question.getChoices(),question.getType(),"");
        listView = (ListView)findViewById(R.id.choicesListView);
        listView.setAdapter(adapter);
	}
	
	public void changeQuestion(int cId,int qId){
		String questionHint = "Q"
				+ String.valueOf(question.getIndex())
				+ " ("
				+ mContext.getResources().getString(
						R.string.msg_question_score) + ":"
				+ String.valueOf(question.getScore()) + ")\n";
		question = ExamParse.getQuestion(exam,cId, qId);
		questionTV.setText(questionHint + question.getContent());
		adapter = new ChoiceAdapter(mContext,question.getChoices(),question.getType(),"");
		listView.setAdapter(adapter);
	}

	protected PopupWindow popupWindow;
	protected ListView lv_group;
	protected Integer pressedItemIndex = -1;
	protected View popupView;
	protected List<CatalogInfo> catalogInfos = new ArrayList<CatalogInfo>();
	
    public void loadCatalogInfos(DatabaseUtil dbUtil){
		//load catalog list menu
		Cursor catalogCursor = null;
		int answeredQuetions =0;
		//set catalog menus
		for(Catalog catalog: exam.getCatalogs()){
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
			catalogInfos.add(new CatalogInfo(catalog.getIndex(),catalog.getDesc(),1,totalQuestions,answeredQuetions));
			catalogCursor.close();
		}
    }
    
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
//				Log.i(LOG_TAG, "onItemClick()...");
//				//initial all items background color
//        		for(int i=0;i<adapterView.getChildCount();i++){
//        			View item = adapterView.getChildAt(i);
//        		}
//				view.setBackgroundColor(getRequestedOrientation());
//				pressedItemIndex = position;
//				Log.i(LOG_TAG, "pressedItemIndex:" + pressedItemIndex);
//				if (popupWindow != null) {
//					popupWindow.dismiss();
//				}
//				CatalogInfo info = catalogInfos.get(position);
//				Question nQuestion = DataParseUtil.getQuestionByCidQid(exam, info.getIndex(),1);
//				QUESTION_TYPE nQuestionType = nQuestion.getType();
//				finish();
//				Log.i(LOG_TAG, "----------Start a New Exam!-----------------");
//				SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.EXAM_STATUS_START_GOING, sharedPreferences);
//				go2QuestionByType(nQuestionType,mContext);
//				saveQuestionMovePara(info.getIndex(),nQuestion.getIndex(),nQuestionType,sharedPreferences);
//				Log.i(LOG_TAG, "--------------------------------------------");
			}
		});
	}
}
