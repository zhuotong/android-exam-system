package com.dream.eexam.base;

import com.dream.eexam.sl.R;
import com.dream.eexam.sl.R.id;
import com.dream.eexam.sl.R.layout;
import com.dream.eexam.sl.R.string;
import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.SPUtil;
import com.dream.eexam.util.TimeDateUtil;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingExamStatus extends SettingBase {
	TextView userInfoTV;
	TextView examInfoTV;
	TextView examAnswerTV;
	Button clearBtn;
	
	public String getUserInfo(){
		StringBuilder sb = new StringBuilder();

		sb.append(mContext.getResources().getString(R.string.label_setting_user_id)+":\t");
		String userId = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
		sb.append(userId==null? "":userId);
		sb.append("\n");
		
		sb.append(mContext.getResources().getString(R.string.label_setting_user_password)+":\t");
		String userPassword = SPUtil.getFromSP(SPUtil.CURRENT_USER_PWD, sharedPreferences);
		sb.append(userPassword==null? "":userPassword);
		sb.append("\n");
		
		sb.append(mContext.getResources().getString(R.string.label_setting_user_file_home)+":\t");
		String userFileHome = SPUtil.getFromSP(SPUtil.CURRENT_USER_HOME, sharedPreferences);
		sb.append(userFileHome==null? "":userFileHome);
		sb.append("\n");
		
		sb.append(mContext.getResources().getString(R.string.label_setting_user_completed_exam)+":\t");
		String userCompletedExam = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_SUBMITTED_IDS, sharedPreferences);
		sb.append(userCompletedExam==null? "":userCompletedExam);
		return sb.toString();
	}

	public String getExamInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append(mContext.getResources().getString(R.string.label_setting_exam_file_name));
		String examFileName = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_FILE_NAME,sharedPreferences);
		sb.append(examFileName == null ? "" : examFileName);
		sb.append("\n");

		sb.append(mContext.getResources().getString(R.string.label_setting_exam_name));
		String examName = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_NAME,sharedPreferences);
		sb.append(examName == null ? "" : examName);
		sb.append("\n");
		
		sb.append(mContext.getResources().getString(R.string.label_setting_exam_start_time));
		long examStartTime = SPUtil.getLongFromSP(SPUtil.CURRENT_EXAM_START_TIME,sharedPreferences);
		if(examStartTime != 0){
			sb.append(TimeDateUtil.transferTime2Str(examStartTime));
		}
		sb.append("\n");

		sb.append(mContext.getResources().getString(R.string.label_setting_exam_status));
		int examStatus = SPUtil.getIntegerFromSP(SPUtil.CURRENT_EXAM_STATUS,sharedPreferences);
		switch(examStatus){
			case 1:sb.append("Exam Not Start");break;
			case 2:sb.append("Exam Start Going");break;
			case 3:sb.append("Exam Start Going Obsolete");break;
			case 4:sb.append("EXAM_Start Pending New");break;
			case 5:sb.append("Exam End");
			default:sb.append("");
		}
		
		sb.append("\n");

		sb.append(mContext.getResources().getString(R.string.label_setting_exam_current_catalog));
		int examCurrentCatalog = SPUtil.getIntegerFromSP(SPUtil.CURRENT_EXAM_CATALOG, sharedPreferences);
		sb.append(examCurrentCatalog == 0 ? "" : String.valueOf(examCurrentCatalog));
		sb.append("\n");

		sb.append(mContext.getResources().getString(R.string.label_setting_exam_current_index_in_catalog));
		int examCurrentIndexInCatalog = SPUtil.getIntegerFromSP(SPUtil.CURRENT_EXAM_INDEX_IN_CATA, sharedPreferences);
		sb.append(examCurrentIndexInCatalog == 0 ? "": String.valueOf(examCurrentCatalog));
		sb.append("\n");

		sb.append(mContext.getResources().getString(R.string.label_setting_exam_exam_score));
		String examScore = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_SCORE,sharedPreferences);
		String scoreStr = (examScore == null) ? "" : examScore; 
		sb.append(scoreStr);

		return sb.toString();
	}
	
	public String getDBData(Context mContext){
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
		StringBuffer sb = new StringBuffer();
    	Cursor cursor = dbUtil.fetchAllAnswers();
    	while(cursor.moveToNext()){
    		int cid = cursor.getInt(0);
    		int qid = cursor.getInt(1);
			String qidStr = cursor.getString(2);
			String answer = cursor.getString(3);
			sb.append(String.valueOf(cid)+";" + String.valueOf(qid)+";"+qidStr+";"+answer+"\n");
		}
    	cursor.close();
    	dbUtil.close();
    	return sb.toString();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_exam_status);
		mContext = getApplicationContext();

		setHeader((ImageView) findViewById(R.id.imgHome));

		userInfoTV = (TextView) this.findViewById(R.id.userInfoTV);
		examInfoTV = (TextView) this.findViewById(R.id.examInfoTV);
		examAnswerTV = (TextView) this.findViewById(R.id.examAnswerTV);
		
		setData();
		
		clearBtn = (Button) this.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(clearListener);
	}
	
	void setData(){
		userInfoTV.setText(getUserInfo());
		examInfoTV.setText(getExamInfo());
		examAnswerTV.setText(getDBData(mContext));
	}
	
    View.OnClickListener clearListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	clearSP();
        	clearDB(mContext);
        	
        	ShowDialog(mContext.getResources().getString(R.string.dialog_note),
        			mContext.getResources().getString(R.string.msg_history_be_cleared));
        	
        	setData(); 
        }  
    }; 
    
    
}
