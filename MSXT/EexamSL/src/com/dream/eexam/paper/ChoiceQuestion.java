package com.dream.eexam.paper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.adapter.CatalogAdapter;
import com.dream.eexam.adapter.ChoiceAdapter;
import com.dream.eexam.sl.R;
import com.dream.eexam.model.CatalogInfo;
import com.dream.eexam.sl.BaseActivity;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import com.dream.exam.bean.Catalog;
import com.dream.exam.bean.Choice;
import com.dream.exam.bean.Exam;
import com.dream.exam.bean.ExamParse;
import com.dream.exam.bean.ExamXMLParse;
import com.dream.exam.bean.Question;

public class ChoiceQuestion extends BaseActivity {
	public final static String LOG_TAG = "ChoiceQuestion";
	
	//data
	Exam exam;
	public void loadExam(){
		//get exam
    	String home = SPUtil.getFromSP(SPUtil.CURRENT_APP_HOME, sharedPreferences);
//    	String userId = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
    	String examId = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_ID, sharedPreferences);
    	FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(home+ File.separator+"exam"+ File.separator+examId));
			exam = ExamXMLParse.parseExam(inputStream);
		} catch (FileNotFoundException e) {
			Log.e(LOG_TAG,e.getMessage());
		}
	}
	
	Question question;
	int cId;
	int qId;
	public void loadQuestion(){
		// get question
		cId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_CATALOG_ID,sharedPreferences);
		if (cId == 0) cId = 1;
		qId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_QUESTON_ID,sharedPreferences);
		if (qId == 0) qId = 1;
    	question = ExamParse.getQuestion(exam, cId, qId);		
	}
	
	StringBuilder answers = new StringBuilder();
	List<Question> pendQuestions = new ArrayList<Question>();
	public void loadAnswer(){
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
		Cursor cursor = dbUtil.fetchAnswer(cId,qId);
		answers.setLength(0);
		while(cursor.moveToNext()){
    		answers.append(cursor.getString(3));
		}
		Log.i(LOG_TAG, "answers:"+answers.toString());
    	//load pending questions
		pendQuestions.clear();
		for(Catalog catalog: exam.getCatalogs()){
			List<Question> questions = catalog.getQuestions();
			for(Question question: questions){
				Cursor cursor2 = dbUtil.fetchAnswer(catalog.getIndex(),question.getIndex());
				if(cursor2.moveToNext()){
					continue;
				}
				cursor2.close();
				pendQuestions.add(question);
			}
		}
		dbUtil.close();
    }
	
	public void resetQuestion(){
    	//set saved answer to choices
    	List<Choice> answerChoices = new ArrayList<Choice>();
    	for(Choice choice: question.getChoices()){
    		if(answers.indexOf(choice.getLabel()) != -1){
    			choice.setSelect(true);
    		}else{
    			choice.setSelect(false);
    		}
    		answerChoices.add(choice);
    	}
    	question.setChoices(answerChoices);
	}
	
	TableLayout catalogsTL;
	TextView catalogsTV = null;
	TextView questionTV;
	ListView listView;
	ChoiceAdapter choiceAdapter;
	ImageView backArrow;
	Button pendQueNumber = null;
	ImageView nextArrow;
	
	public void loadComponents(){
		//set header components
		catalogsTL = (TableLayout)findViewById(R.id.catalogsTL);
		catalogsTL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "catalogsTL.onClick()...");
				showWindow(v);
			}
		});
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		
        questionTV = (TextView) findViewById(R.id.questionTV);
        questionTV.setMovementMethod(ScrollingMovementMethod.getInstance()); 

        listView = (ListView)findViewById(R.id.choicesListView);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		List<Choice> choices = question.getChoices();
        		List<Choice> updChoices = new ArrayList<Choice>();
        		for(Choice choice:choices){
        			if(choice.getIndex() == (arg2+1)){
        				choice.setSelect(!choice.isSelect());
        			}else{
            			if(question.getType() == 2){// single choices
            				choice.setSelect(false);
            			}	
        			}
        			updChoices.add(choice);
        		}
        		question.setChoices(updChoices);
//        		choiceAdapter.refresh(updChoices);
        		
        		saveAnswer();
        		
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}      	
        });
        
        //set footer components
        backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(qId>1){
					changeQuestion(cId,qId-1);
				}
			}
		}); 			
        pendQueNumber = (Button) findViewById(R.id.pendQueNumber);
		pendQueNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(pendQuestions.size()>0){
    				go2PendingQuestion(mContext);
				}else{
					ShowDialog(mContext.getResources().getString(R.string.dialog_note),mContext.getResources().getString(R.string.message_tv_no_question));	
				}
			}
		}); 
		nextArrow = (ImageView)findViewById(R.id.nextArrow);
		nextArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int maxQuestion = ExamParse.getMaxQuestion(exam, cId);
				Log.i(LOG_TAG,"maxQuestion of catalog "+ String.valueOf(cId)+":"+String.valueOf(maxQuestion));
				if(qId< maxQuestion){
					changeQuestion(cId,qId+1);
				}
			}
		});
		
	}
	
	public void changeComponents(){
		if(exam.getCatalogs().size()>1){
			Catalog catalog = ExamParse.getCatalog(exam, cId);
			catalogsTV.setText(String.valueOf(cId)+". "+ catalog.getName() + 
					"(Q" + String.valueOf(1)+" - " + "Q" + String.valueOf(ExamParse.getMaxQuestion(exam, cId))+")");
		}else{
			//set catalogsTV invisible
		}
		
		//change question content
		String questionHint = "Q"
				+ String.valueOf(question.getIndex())
				+ " ("
				+ mContext.getResources().getString(
						R.string.msg_question_score) + ":"
				+ String.valueOf(question.getScore()) + ")\n";
		questionTV.setText(questionHint + question.getContent());
		
		//change choices
		if(choiceAdapter==null){
	        choiceAdapter = new ChoiceAdapter(mContext,question.getChoices(),question.getType(),answers.toString());
	        listView.setAdapter(choiceAdapter);
		}else{
			choiceAdapter.refresh(question.getChoices());
		}
		
		Drawable firstImg = getResources().getDrawable(R.drawable.ic_first_question_64);
		Drawable backImg = getResources().getDrawable(R.drawable.ic_back_64);
		backArrow.setImageDrawable(qId == 1?firstImg:backImg);
		
		String pendNumString = mContext.getResources().getString(R.string.label_tv_waiting)+"("+Integer.valueOf(pendQuestions.size())+")";
	    pendQueNumber.setText(pendNumString);
		
		Drawable lastImg = getResources().getDrawable(R.drawable.ic_last_question_64);
		Drawable nextImg = getResources().getDrawable(R.drawable.ic_next_64);
		nextArrow.setImageDrawable(qId == ExamParse.getMaxQuestion(exam, cId)?lastImg:nextImg);		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_list);
		mContext = getApplicationContext();
		loadExam();
		loadComponents();
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		Log.i(LOG_TAG,"onResume()...");
		loadQuestion();
		loadAnswer();
		resetQuestion();
		
		changeComponents();
	}

    public void saveAnswer(){
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
		List<Choice> choices = question.getChoices();
		answers.setLength(0);
		for(Choice choice:choices){
			if(choice.isSelect()){
				answers.append(choice.getLabel());
			}
		}
		//save answers
		if(answers.length()==0){
			dbUtil.deleteAnswer(cId, qId);
		}else{
			dbUtil.saveAnswer(dbUtil,cId,qId,question.getQuestionid(),answers.toString());
		}
		dbUtil.close();
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
		popupWindow.showAsDropDown(parent,0, 20);
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
//				//initial all items background color
        		for(int i=0;i<adapterView.getChildCount();i++){
        			View item = adapterView.getChildAt(i);
        		}
				view.setBackgroundColor(getRequestedOrientation());
				pressedItemIndex = position;
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				CatalogInfo info = catalogInfos.get(position);
				//change to first question of choose catalog
				if(info.getIndex()!=cId){
					changeQuestion(info.getIndex(),1);
				}
			}
		});
	}

	protected Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0){
				loadAnswer();
				//resetQuestion();
				changeComponents();
			}
		}
	};
	
	public void changeQuestion(int newCId,int newQId){
//		question = ExamParse.getQuestion(exam,newCId, newQId);
//		this.cId = newCId;
//		this.qId = newQId;
		SPUtil.save2SP(SPUtil.CURRENT_CATALOG_ID, newCId, sharedPreferences);
		SPUtil.save2SP(SPUtil.CURRENT_QUESTON_ID, newQId, sharedPreferences);
		
		loadQuestion();
		loadAnswer();
		resetQuestion();
		
		changeComponents();
	}
	
}
