package com.dream.eexam.base;

import java.util.ArrayList;
import java.util.List;

import com.dream.eexam.model.CatalogBean;
import com.dream.eexam.model.QuestionProgress;
import com.dream.eexam.util.ActivityStackControlUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class BaseActivity extends Activity {

	public final static String LOG_TAG = "BaseActivity";
	
	@Override
	public void finish() {
		Log.i(LOG_TAG,"finish()...");
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		ActivityStackControlUtil.add(this);
		
		//hide title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onDestroy() {
		Log.i(LOG_TAG,"onDestroy()...");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(LOG_TAG,"onPause()...");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i(LOG_TAG,"onRestart()...");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(LOG_TAG,"onResume()...");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.i(LOG_TAG,"onStart()...");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(LOG_TAG,"onStop()...");
		super.onStop();
	}
	
	public void ShowDialog(String msg) {
		new AlertDialog.Builder(this).setTitle("Note").setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
		}).show();
	}

	/**
	 * 
	 * @param sharedPreferences
	 * @return
	 */
	public QuestionProgress getQuestionProgress(SharedPreferences sharedPreferences){
		
		Integer currentQueIndex = sharedPreferences.getInt("currentQueIndex", 0);
		Integer quesCount = sharedPreferences.getInt("quesCount", 0);
		String completedQueIdsString = sharedPreferences.getString("completedQueIdsString", null);
		
		return new QuestionProgress(currentQueIndex,quesCount,completedQueIdsString);

	}

	/**
	 * 
	 * @param sharedPreferences
	 * @param qp
	 */
	public void saveQuestionProgress(SharedPreferences sharedPreferences,QuestionProgress qp){
		SharedPreferences.Editor editor = sharedPreferences.edit();
		
		Integer currentQueIndex = qp.getCurrentQueIndex();
		if(currentQueIndex!=null){
			editor.putInt("currentQueIndex", currentQueIndex);  
		}

		Integer quesCount = qp.getQuesCount();
		if(currentQueIndex!=null){
			editor.putInt("quesCount", quesCount);  
		}
		
		String completedQueIdsString = qp.getCompletedQueIdsString();
		if(currentQueIndex!=null){
			editor.putString("completedQueIdsString", completedQueIdsString);  
		}
		
		editor.commit();
	}

	protected List<CatalogBean> cataLogList = new ArrayList<CatalogBean>();
	protected PopupWindow popupWindow;
	protected ListView lv_group;
	protected List<String> groups;
	protected Integer pressedItemIndex = -1;
	protected View popupView;
	
	protected void showWindow(View parent) {
		Log.i(LOG_TAG, "showWindow()...");
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			popupView = layoutInflater.inflate(R.layout.group_list, null);
			lv_group = (ListView) popupView.findViewById(R.id.lvGroup);
			groups = new ArrayList<String>();
			groups.add("Catalog 1");
			groups.add("Catalog 2");
			groups.add("Catalog 3");
			groups.add("Catalog 4");	
			
			/*for(int i=0;i<cataLogList.size();i++){
				groups.add("Catalog " + String.valueOf(i+1));
			}*/
			
			Log.i(LOG_TAG, "pressedItemIndex:"+pressedItemIndex);
			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(popupView,150, 400);
		}else{
			popupWindow.dismiss();
		}

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);//window will dismiss once touch out of it
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//window will dismiss once click back 
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
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
//				priorityList.setText("Priority "+String.valueOf(position+1));
//				changePriority(position);
//				Log.i(LOG_TAG, "onItemClick() END");
			}
		});
	}
}
