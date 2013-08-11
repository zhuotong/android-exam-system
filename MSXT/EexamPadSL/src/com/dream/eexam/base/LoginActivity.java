package com.dream.eexam.base;

import com.dream.eexam.base.R;
import com.dream.eexam.util.SPUtil;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Login Page
 * @author Timothy
 *
 */

public class LoginActivity extends BaseActivity {

	public final static String LOG_TAG = "LoginActivity";
	
	EditText idEt = null;
	EditText passwordET = null;
	Button loginBtn = null;
	Button settingBtn = null;
	
	String saveHost = null;
	String savePort = null;
	String currentUserId = null;
	String currentUserPwd = null;
	String userHome = null;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        
        currentUserId = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
        currentUserPwd = SPUtil.getFromSP(SPUtil.CURRENT_USER_PWD, sharedPreferences);
        
        idEt = (EditText) this.findViewById(R.id.idEt);
        idEt.setText(currentUserId==null?"":currentUserId);
		
		passwordET = (EditText) this.findViewById(R.id.passwordET);
		passwordET.setText(currentUserPwd==null?"":currentUserPwd);
		
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new View.OnClickListener() {  
	        @Override  
	        public void onClick(View v) {
	        	Intent intent = new Intent();
				intent.setClass( LoginActivity.this, ExamStart.class);
				startActivity(intent); 	        	
	        }  
	    });
		
		settingBtn = (Button) this.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new View.OnClickListener() {  
	        @Override  
	        public void onClick(View v) {
		        	Intent intent = new Intent();
					intent.setClass( LoginActivity.this, SettingMain.class);
					startActivity(intent);  	        		
	        }  
	    });
    }
 


}