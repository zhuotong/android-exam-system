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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import com.dream.eexam.sl.R;
import com.dream.eexam.sl.BaseActivity;
=======
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import com.dream.eexam.adapter.CatalogAdapter;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.R;
>>>>>>> .r487
import com.dream.eexam.model.CatalogInfo;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import com.dream.exam.bean.Catalog;
import com.dream.exam.bean.Exam;
import com.dream.exam.bean.ExamParse;
import com.dream.exam.bean.ExamXMLParse;
import com.dream.exam.bean.Question;

public class PendQuestion extends BaseActivity {
	
	public final static String LOG_TAG = "PendQuestions";

//    String qType = null;
	PendQueListAdapter adapter;
	GridView gridList;
	
	Exam exam;
	int cId;
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
//		// get question
		cId = SPUtil.getIntegerFromSP(SPUtil.CURRENT_CATALOG_ID,sharedPreferences);
	}
	
	List<Question> pendQuestions = new ArrayList<Question>();
	public void loadAnswer(){
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
    	//load pending questions
		pendQuestions.clear();
		for(Catalog catalog: exam.getCatalogs()){
			if(cId == catalog.getIndex()){
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
		}
		dbUtil.close();		
	}
	
	TableLayout catalogsTL;
	TextView catalogsTV;
	ImageView imgClose;
	public void loadComponents(){
		catalogsTL = (TableLayout)findViewById(R.id.pendCataTL);
		catalogsTL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(LOG_TAG, "catalogsTL.onClick()...");
				showWindow(v);
			}
		});
		catalogsTV = (TextView)findViewById(R.id.pendCatasTV);
		if(exam.getCatalogs().size()>1){
			Catalog catalog = ExamParse.getCatalog(exam, cId);
			catalogsTV.setText(String.valueOf(cId)+". "+ catalog.getName() + 
					"(Q" + String.valueOf(1)+" - " + "Q" + String.valueOf(ExamParse.getMaxQuestion(exam, cId))+")");
		}
		imgClose = (ImageView)findViewById(R.id.imgClose);
		imgClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pend_question);
        mContext = getApplicationContext();
        
        loadData();
        loadAnswer();
        loadComponents();
        
        //set List
        gridList = (GridView)findViewById(R.id.gridview);
        adapter = new PendQueListAdapter(pendQuestions);
        gridList.setAdapter(adapter);
        gridList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		goBack();
			}      	
        });
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
			ViewHolder holder;  
            if (convertView == null){  
                holder = new ViewHolder();  
                convertView = mInflater.inflate(R.layout.pend_question_item, null);  
                holder.questionBtn = (Button) convertView.findViewById(R.id.questionBtn);  
                convertView.setTag(holder);  
            }else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            Question question = questions.get(position);
            final int qId = question.getIndex();
            holder.questionBtn.setText(String.valueOf(qId));  
            holder.questionBtn.setOnClickListener(new Button.OnClickListener() {
    			public void onClick(View v) {
    				SPUtil.save2SP(SPUtil.CURRENT_CATALOG_ID, cId, sharedPreferences);
    				SPUtil.save2SP(SPUtil.CURRENT_QUESTON_ID, qId, sharedPreferences);
    				goBack();
    			}
    		});
            return convertView; 
		}
    }
    
    static class ViewHolder{
    	Button questionBtn;
    }
    
    public void goBack(){
		finish();
		go2ChoiceQuestion(mContext);
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
				
			}
		});
	}
	
}
