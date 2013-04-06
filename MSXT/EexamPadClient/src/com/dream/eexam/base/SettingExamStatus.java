package com.dream.eexam.base;

import com.dream.eexam.util.SPUtil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingExamStatus extends SettingBase {
	TextView spData = null;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.setting_examstatus);
	        mContext = getApplicationContext();
	        
	        setHeader((ImageView)findViewById(R.id.imgHome));
			setFooter((Button) findViewById(R.id.server_setting));
			
		spData = (TextView) this.findViewById(R.id.spData);
		spData.setText(SPUtil.getAllSPData(sharedPreferences)); 
	 }
}
