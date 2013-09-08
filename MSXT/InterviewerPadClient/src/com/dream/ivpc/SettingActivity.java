package com.dream.ivpc;

import java.io.File;
import com.dream.ivpc.R;
import com.dream.ivpc.custom.CustomDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends BaseActivity {
	public final static String LOG_TAG = "LoginActivity";
	
	String saveHost = null;
	EditText idEt = null;
	EditText passwordET = null;
	String saveId = null;
	String savePassword = null;
	Button loginBtn = null;
	Button settingBtn = null;
	String loginResultFile = null;
	String loginResultFilePath = null;
	Context mContext;
	
	ProgressDialog myDialog = null;
	CustomDialog cusDialog = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        mContext = getApplicationContext();
        
        saveHost = sharedPreferences.getString("host", null);
        
        idEt = (EditText) this.findViewById(R.id.idEt);
		saveId = sharedPreferences.getString("id", null);
		if(saveId!=null||!"".equals(saveId)){
			idEt.setText(saveId);
		}
		
		passwordET = (EditText) this.findViewById(R.id.passwordET);
		savePassword = sharedPreferences.getString("password", null);
		if(savePassword!=null||!"".equals(savePassword)){
			passwordET.setText(savePassword);
		}
		
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		        
			}
		});
		
		settingBtn = (Button) this.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		    	Intent intent = new Intent();
				intent.setClass( mContext, LoginActivity.class);
				startActivity(intent);  
			}
		});
		
    }
	
	public void go2CandiateList(){
    	Intent intent = new Intent();
		intent.setClass( mContext, CandidateList.class);
		startActivity(intent);  		
	}
	
	public String getPath(String admin) {
		String basePath = Environment.getExternalStorageDirectory() + "/interviewer";
		return basePath + File.separator + admin  + File.separator + "login_result.xml";
	}
	
}
