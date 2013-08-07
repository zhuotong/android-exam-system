package com.dream.ivpc.activity;

import java.io.File;
import java.io.FileInputStream;

import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.model.LoginResult;
import com.dream.ivpc.util.FileUtil;
import com.dream.ivpc.util.NetWorkUtil;
import com.dream.ivpc.util.XMLParseUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
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
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
		        if(NetWorkUtil.isNetworkAvailable(mContext)){
		        	new LoginTask().execute();
		        }else{
		        	Toast.makeText(mContext, "Your network is not available!", Toast.LENGTH_LONG).show();
		        }
			}
		});
		
		settingBtn = (Button) this.findViewById(R.id.settingBtn);
		
    }
    
    
	private class LoginTask extends AsyncTask<String, Void, String> {
		private boolean succFlag = false;
		@Override
		protected void onPreExecute() {
			myDialog = ProgressDialog.show(LoginActivity.this, "Login...","Please Wait!", true);
		}
		@Override
		protected String doInBackground(String... urls) {
			try {
				Thread.sleep(2000);
				succFlag = true;
			} catch (Exception e) {
				Log.e(LOG_TAG, "erro message:" + e.getMessage());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			myDialog.dismiss();
			if (succFlag) {
				FileInputStream inputStream = FileUtil.getFileInputStream(getPath("admin"));
				LoginResult lResult = XMLParseUtil.parseLoginResult(inputStream);
				if(lResult.isSuccess()){
					go2CandiateList();
				}else{
					ShowDialog("Warning","Fail to Login!");
				}
			}else{
				
			}
		}
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
