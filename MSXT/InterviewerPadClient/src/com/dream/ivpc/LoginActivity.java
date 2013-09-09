package com.dream.ivpc;

import java.io.File;
import java.io.InputStream;
import com.dream.ivpc.R;
import com.dream.ivpc.bean.LoginResultBean;
import com.dream.ivpc.custom.CustomDialog;
import com.dream.ivpc.server.ParseResult;
import com.dream.ivpc.util.FileUtil;
import com.dream.ivpc.util.NetWorkUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
	CustomDialog cusDialog = null;
	
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
		        	String idStr = idEt.getText().toString();
		        	String passwordStr = passwordET.getText().toString();
		        	if(idStr!=null && passwordStr!=null){
		        		new LoginTask().execute(new String[]{idStr,passwordStr});
		        	}else{
		        		ShowDialog("Warning","Id or Password Can not be empty!");
		        	}
		        }else{
		        	Toast.makeText(mContext, "Your network is not available!", Toast.LENGTH_LONG).show();
		        }
			}
		});
		
		settingBtn = (Button) this.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//start
				LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				final View view = inflater.inflate(R.layout.login_admin, null);
				
				new AlertDialog.Builder(LoginActivity.this)
						.setTitle("Admin Login").setView(view)
						.setPositiveButton("Login",new DialogInterface.OnClickListener() {
					     @Override
						public void onClick(DialogInterface arg0, int arg1) {
							EditText nameEditText = (EditText) view.findViewById(R.id.editText1);
							String username = nameEditText.getText().toString();
							EditText passEditText = (EditText) view.findViewById(R.id.editText2);
							String userpass = passEditText.getText().toString();
							
							if("admin".equalsIgnoreCase(nameEditText.getText().toString()) && "admin".equalsIgnoreCase(passEditText.getText().toString()) ){
								go2SettingActivity();
							}else{
								ShowDialog("Warning","Fail to login!");
							}
							
							Toast.makeText(LoginActivity.this, "Login:"+username+"&"+userpass, Toast.LENGTH_LONG).show();
						}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(LoginActivity.this, "Login canceled!", Toast.LENGTH_LONG).show();
					}
				}).show();	
				
			}
		});		

		
    }
 
	private class LoginTask extends AsyncTask<String, Void, String> {
    	InputStream inputStream;
    	LoginResultBean bean;
		
		@Override
		protected void onPreExecute() {
			cusDialog = new CustomDialog(LoginActivity.this,R.style.custom_dialog_style);
			//set alpha
			Window wd = cusDialog.getWindow();
			WindowManager.LayoutParams lp = wd.getAttributes();
			lp.alpha = 0.8f;
			wd.setAttributes(lp);
			cusDialog.show();

		}
		@Override
		protected String doInBackground(String... urls) {
			try {
				Thread.sleep(2000);
				
//				GetData getData = GetDateImp.getInstance();
//				bean = getData.login(urls[0], urls[1]);
				
				inputStream = FileUtil.getFileInputStream(getPath("admin"));
				
			} catch (Exception e) {
				Log.e(LOG_TAG, "erro message:" + e.getMessage());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			cusDialog.dismiss();
			
			//get result from local disk
			if(inputStream!=null){
				LoginResultBean loginResult = ParseResult.parseLoginResult(inputStream);
				if(loginResult.isSuccess()){
					go2CandiateList();
					Toast.makeText(mContext, "Login Success! Name:"+  loginResult.getUserName()+" token:"+loginResult.getToken(), Toast.LENGTH_LONG).show();
				}else{
					ShowDialog("Warning",loginResult.getError_code()+":"+loginResult.getError_desc());
				}
			}else{
				ShowDialog("Warning","Fail to login!");
			}
			
//			if(bean!=null){
//				if(bean.isSuccess()){
//					go2CandiateList();
//				}else{
//					ShowDialog("Warning","Fail to login!");
//				}
//			}else{
//				ShowDialog("Warning","Fail to login!");
//			}
		}
	}
	
	public void go2CandiateList(){
    	Intent intent = new Intent();
		intent.setClass( mContext, CandidateList.class);
		startActivity(intent);  		
	}
	
	public void go2SettingActivity(){
    	Intent intent = new Intent();
		intent.setClass( mContext, SettingActivity.class);
		startActivity(intent);  		
	}
	
	public String getPath(String admin) {
		String basePath = Environment.getExternalStorageDirectory() + "/interviewer";
		return basePath + File.separator + admin  + File.separator + "login_result.xml";
	}
	
}
