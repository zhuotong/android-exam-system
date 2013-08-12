package com.dream.eexam.base;

import com.dream.eexam.base.R;
import com.dream.eexam.util.SPUtil;
import com.dream.eexam.util.StringUtil;
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
	

	public void systemSetting(){
		SPUtil.save2SP("tangqi1", "1234",sharedPreferences);
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        systemSetting();
        
        idEt = (EditText) this.findViewById(R.id.idEt);
//        idEt.setText(currentUserId==null?"":currentUserId);
		
		passwordET = (EditText) this.findViewById(R.id.passwordET);
//		passwordET.setText(currentUserPwd==null?"":currentUserPwd);
		
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new View.OnClickListener() {  
	        @Override  
	        public void onClick(View v) {
	        	if(emptyValidate(idEt,passwordET)){
	        		ShowDialog("Warning","UserId or password can not be empty! ");
	        	}else{
		        	if(passValidate(idEt.getText().toString(),passwordET.getText().toString())){
			        	Intent intent = new Intent();
						intent.setClass( LoginActivity.this, ExamStart.class);
						startActivity(intent); 		   
						SPUtil.save2SP(SPUtil.CURRENT_USER_ID, idEt.getText().toString(), sharedPreferences);
		        	}else{
		        		ShowDialog("Warning","Wrong userId or password!");
		        	}	        		
	        	}
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
    
    public boolean emptyValidate(EditText userIdTV,EditText userPassTV){
    	if(userIdTV.getText() == null || userPassTV.getText() == null){
    		return true;
    	}
    	if(StringUtil.isEmpty(userIdTV.getText().toString()) || StringUtil.isEmpty(userPassTV.getText().toString()))  {
    		return true;
    	}else{
    		return false;
    	}
    }
 
    public boolean passValidate(String userId,String userPass){
    	String rightPass = SPUtil.getFromSP(userId, sharedPreferences);
    	if(rightPass.equals(userPass)){
    		return true;
    	}else{
    		return false;
    	}
    }


}