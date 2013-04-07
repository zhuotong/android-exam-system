package com.dream.eexam.base;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.dream.eexam.util.DatabaseUtil;

public class SettingExamProgress extends SettingBase {
	 TextView dbData = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_examserver);
		mContext = getApplicationContext();

		setHeader((ImageView) findViewById(R.id.imgHome));
		setFooter((Button) findViewById(R.id.examprogress_setting));

		dbData = (TextView) this.findViewById(R.id.dbData);
		dbData.setText(getDBData(mContext));

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
	
	
}
