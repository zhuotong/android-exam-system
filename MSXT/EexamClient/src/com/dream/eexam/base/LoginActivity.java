package com.dream.eexam.base;

import java.net.HttpURLConnection;
import java.net.URL;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

	public final static String LOG_TAG = "LoginActivity";
	
	private EditText idEt = null;
	private EditText passwordET = null;
	private Button loginBtn = null;
	
	MyTask myTask;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
 
		idEt = (EditText) this.findViewById(R.id.idEt);
		passwordET = (EditText) this.findViewById(R.id.passwordET);
		
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(loginListener);
    }

    
    View.OnClickListener loginListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) {  

        	//show waiting Dialog
        	myTask = new MyTask();
        	myTask.execute("");
        	
        	//login server
			String idEtText = idEt.getText().toString();
			String passwordETText = passwordET.getText().toString();
			boolean result = login(idEtText,passwordETText);
			if(result){
				myTask.cancel(true);
				finish();
				Intent intent = new Intent();
				intent.setClass( getBaseContext(), PapersActivity.class);
				startActivity(intent);
			}

        }  
    };
    
    /**
     * 
     * @param username
     * @param password
     */
    public boolean login(String username,String password){
    	boolean result = false;
		String urlStr="http://192.168.240.148:8080/";
//		String query = "username="+username+"&password="+password;
//		urlStr+=query;
		try{
			URL url =new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				result = true;
			}
			conn.disconnect();
		}catch(Exception e){
//			ShowDialog(e.getMessage());
		}
		return result;
	}
    
    /**
     * 
     * @author qtang
     *
     */
    private class MyTask extends AsyncTask<String, Integer, String> {
    	ProgressDialog progressDialog;  
    	@Override
    	protected void onPreExecute() {
    		Log.i(LOG_TAG, "onPreExecute()");
    		progressDialog = ProgressDialog.show(LoginActivity.this, null, "Login to server,please wait...", true, false); 
    	}
		@Override
		protected String doInBackground(String... params) {
			Log.i(LOG_TAG, "doInBackground(Params... params) called");
			try {
				Thread.sleep(10000);  
				return new String("");
			} catch (Exception e) {
				Log.e(LOG_TAG, e.getMessage());
			}
			return null;
		}
		@Override
    	protected void onProgressUpdate(Integer... progresses) {
			Log.i(LOG_TAG, "onProgressUpdate(Progress... progresses) called");
    	}
		
		@Override
		protected void onPostExecute(String result) {
			Log.i(LOG_TAG, "onPostExecute(String result) called");
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
				ShowDialog("Fail to login Server!");
			}
		}
		@Override
		protected void onCancelled() {
			Log.i(LOG_TAG, "onCancelled() called");
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		}
    }
    
    @Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


}