package com.dream.eexam.paper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
import com.dream.eexam.util.XMLParseUtil;

public class BaseQuestion extends BaseActivity{
	
	public final static String LOG_TAG = "BaseQuestion";
	
	protected InputStream inputStream;
	protected PaperBean paperBean;
	protected List<CatalogBean> cataLogList = new ArrayList<CatalogBean>();
	
	protected String questionType;
	protected Integer currentCatalogIndex;
	protected Integer currentQuestionIndex;
	protected Integer direction = 0;
	
	protected TextView catalogsTV = null;
	
	protected Context mContext;
	
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
        	paperBean = XMLParseUtil.readPaperBean(inputStream);
        	cataLogList = paperBean.getCatalogBeans();
			for(CatalogBean bean: cataLogList){
				String catalogDesc = bean.getDesc()+"("+bean.getQuestions().size()+")";
				Log.i(LOG_TAG, "catalog: "+catalogDesc);
				groups.add(catalogDesc);
			}
        	inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	protected List<String> groups = new ArrayList<String>();;
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
			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
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
