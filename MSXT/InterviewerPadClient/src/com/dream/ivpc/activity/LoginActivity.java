package com.dream.ivpc.activity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.custom.CustomDialog;
import com.dream.ivpc.model.LoginResult;
import com.dream.ivpc.util.FileUtil;
import com.dream.ivpc.util.NetWorkUtil;
import com.dream.ivpc.util.XMLParseUtil;
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
	
	private static final String SERVER = "192.168.1.105";
	private static final String PORT = "8080";
	private static final String LOGIN_URI = "/msxt2/RequestDispatchServlet/interviewRunAction/interviewerLogin";
	
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
		        	new LoginTask().execute(new String[]{"test","test"});
		        }else{
		        	Toast.makeText(mContext, "Your network is not available!", Toast.LENGTH_LONG).show();
		        }
			}
		});
		
		settingBtn = (Button) this.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//		    	Intent intent = new Intent();
//				intent.setClass( mContext, SettingActivity.class);
//				startActivity(intent);  
				
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
							
							Toast.makeText(LoginActivity.this, "Login:"+username+"&"+userpass, Toast.LENGTH_LONG).show();
						}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast.makeText(LoginActivity.this, "Login canceled!", Toast.LENGTH_LONG).show();
					}
				}).show();	
				
				//end
				
			}
		});		

		
    }
 
	private class LoginTask extends AsyncTask<String, Void, String> {
		private boolean succFlag = false;
		HttpURLConnection conn = null;
    	InputStream inputStream;
		
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
				
//				URL loginURL = new URL("http://" + SERVER + ":" + PORT + LOGIN_URI);
//				conn = (HttpURLConnection)loginURL.openConnection();
//				conn.setDoOutput(true);
//				conn.setUseCaches(false);
//				conn.setConnectTimeout(10000);
//				conn.setRequestMethod( "POST" );
//				conn.connect();
//				OutputStream os = conn.getOutputStream();
//				os.write( ("loginName="+urls[0]+"&loginPassword=" + urls[1]).getBytes("utf-8") );
//				os.close();
//				inputStream = conn.getInputStream();
				
				inputStream = FileUtil.getFileInputStream(getPath("admin"));
				succFlag = true;
				
				
			} catch (Exception e) {
				Log.e(LOG_TAG, "erro message:" + e.getMessage());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
//			myDialog.dismiss();
			cusDialog.dismiss();
			
			if(inputStream!=null){
				LoginResult lResult = XMLParseUtil.parseLoginResult(inputStream);
				if(lResult.isSuccess()){
					go2CandiateList();
					Toast.makeText(mContext, "Login Success! Name:"+  lResult.getUserName()+" token:"+lResult.getToken(), Toast.LENGTH_LONG).show();
				}else{
					ShowDialog("Warning",lResult.getError_code()+":"+lResult.getError_desc());
				}
			}else{
				ShowDialog("Warning","Fail to login!");
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
