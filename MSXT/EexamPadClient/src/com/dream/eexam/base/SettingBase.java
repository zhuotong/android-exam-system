package com.dream.eexam.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SettingBase extends BaseActivity {

	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	protected void setHeader(ImageView view){
		view.setOnClickListener(goBackListener);
    }

	protected void setFooter(Button button){
		button.setBackgroundColor(R.drawable.button_title_select);
    }
	
	public void go2SettingServer(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, SettingServer.class);
		startActivity(intent); 
	}
	
	public void go2SettingExamStatus(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, SettingExamStatus.class);
		startActivity(intent); 
	}
	
	public void go2SettingExamProgress(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, SettingExamProgress.class);
		startActivity(intent); 
	}

	public void go2SettingUserFolder(View view){
		Intent intent = new Intent();
		intent.setClass( mContext, SettingUserFolder.class);
		startActivity(intent); 
	}
	
    View.OnClickListener goBackListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	//go to login page
        	Intent intent = new Intent();
			intent.setClass(mContext, LoginActivity.class);
			startActivity(intent);    
        }  
    };
}
