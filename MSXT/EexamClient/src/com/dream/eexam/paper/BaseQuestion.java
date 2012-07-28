package com.dream.eexam.paper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.eexam.base.BaseActivity;
import com.dream.eexam.base.GroupAdapter;
import com.dream.eexam.base.R;
import com.dream.eexam.model.CatalogBean;
import com.dream.eexam.model.PaperBean;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.XMLParseUtil;

public class BaseQuestion extends BaseActivity{
	
	public final static String LOG_TAG = "BaseQuestion";
	
	//set exam header
	protected TextView remainingTime = null;
	
	//set question sub header
	protected TextView catalogsTV = null;
	protected TextView currentTV = null;
	protected TextView waitTV = null;
	
	protected InputStream inputStream;
	
	//common data
	protected String questionType;//questionType
	protected Integer currentCatalogIndex;//current catalog index
	protected Integer currentQuestionIndex;//current question index
	
	protected PaperBean paperBean;//paperBean
	protected List<CatalogBean> cataLogList = new ArrayList<CatalogBean>();//cataLogList
	protected List<String> catalogNames = new ArrayList<String>();//catalog names

	protected Integer direction = 0;//move direction(1 move next, 0 move previous)
	protected Integer questionSize = 0;//question size for current catalog
	protected Integer comQuestionSize = 0;//completed question size for current catalog
	
	protected StringBuffer answerString = new StringBuffer();//answer ids
	protected Context mContext;
	
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
		
        inputStream =  BaseQuestion.class.getClassLoader().getResourceAsStream("sample_paper.xml");
        try {
        	//get paperBean
        	paperBean = XMLParseUtil.readPaperBean(inputStream);
        	inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//set cataLogList
		cataLogList = paperBean.getCatalogBeans();
		
		//set groups
		for(CatalogBean bean: cataLogList){
			String catalogDesc = bean.getDesc()+"("+bean.getQuestions().size()+")";
			Log.i(LOG_TAG, "catalog: "+catalogDesc);
			catalogNames.add(catalogDesc);
		}
		
		//set questionSize
		if(cataLogList!=null&&cataLogList.size()>0){
			CatalogBean bean = cataLogList.get(currentCatalogIndex-1);
		    questionSize = bean.getQuestions().size();
		    Log.i(LOG_TAG,"questionSize:"+ String.valueOf(questionSize));
		}
		
		
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
			popupView = layoutInflater.inflate(R.layout.group_list, null);
			lv_group = (ListView) popupView.findViewById(R.id.lvGroup);
//			groups = new ArrayList<String>();
			
			Log.i(LOG_TAG, "pressedItemIndex:"+pressedItemIndex);
			GroupAdapter groupAdapter = new GroupAdapter(this, catalogNames);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(popupView,200, 500);
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
        			item.setBackgroundColor(Color.parseColor("#4C4C4C"));
        		}
				view.setBackgroundColor(Color.parseColor("#D5E43C"));
				pressedItemIndex = position;
				Log.i(LOG_TAG, "pressedItemIndex:" + pressedItemIndex);
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				
//				catalogsTV.setText(cataLogList.get(position).getDesc());
				
				inputStream =  BaseQuestion.class.getClassLoader().getResourceAsStream("sample_paper.xml");
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
				if("Choice:M".equals(questionType)){
					intent.putExtra("questionType", "Multi Select");
					intent.setClass( mContext, MultiChoices.class);
				}else if("Choice:S".equals(questionType)){
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
	
}
