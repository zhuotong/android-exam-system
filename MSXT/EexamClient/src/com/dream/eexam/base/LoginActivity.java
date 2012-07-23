package com.dream.eexam.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.dream.eexam.util.SystemConfig;

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
	
	private String requestURL = null;
	private StringBuffer responseText = new StringBuffer();
//	MyTask myTask;
	
	
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

//        	//login server
//        	String id = idEt.getText().toString();
//        	String password = passwordET.getText().toString();
//        	
//        	responseText.setLength(0);
//        	requestURL = SystemConfig.getInstance().getPropertyValue("Server_URL")+"/msxt/runinterview/loginAction/login?loginName="+id+"&loginPassword="+password;
//        	new DownloadXmlTask().execute(requestURL);

			Intent intent = new Intent();
			intent.setClass( getBaseContext(), PapersActivity.class);
			startActivity(intent);

        }  
    };
 
    
    private boolean loadXmlFromNetwork(String urlString){
    	Log.i(LOG_TAG,"loadXmlFromNetwork...");
    	Log.i(LOG_TAG,"urlString:"+urlString);
    	boolean isSuccess = false;
    	InputStream stream = null;
        try {
            stream = downloadUrl(urlString);
            if (stream != null){
            	isSuccess = true;
            	responseText.append(inputStream2String(stream));
            	Log.i(LOG_TAG,"response_Text:" + responseText);
            	stream.close();
            }
        } catch (IOException e) {
        	Log.i(LOG_TAG,"IOException:" + e.getMessage());
        	
		} finally {

        }
		return isSuccess;
    }
    
	public static String inputStream2String(InputStream is) throws IOException {
		Log.i(LOG_TAG,"inputStream2String...");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
//			Log.i(LOG_TAG,String.valueOf(i));
		}
		
		return baos.toString();
	}
    
    private InputStream downloadUrl(String urlString){
        HttpURLConnection conn;
        InputStream stream = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        
	        // Starts the query
	        conn.connect();
	        stream = conn.getInputStream();
		} catch (IOException e) {
			Log.i(LOG_TAG,"IOException:" + e.getMessage());
		}

        return stream;
    }
    
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

    	ProgressDialog progressDialog;
    	
    	@Override
    	protected void onPreExecute() {
    		Log.i(LOG_TAG, "onPreExecute() called");
    		progressDialog = ProgressDialog.show(LoginActivity.this, null, "Login to server...", true, false); 
    	}
    	
        @Override
        protected String doInBackground(String... urls) {
             loadXmlFromNetwork(urls[0]);
             
/*             if(!isSuccess){
            	 progressDialog.dismiss();
            	 this.cancel(true);
             }*/
             return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            setContentView(R.layout.main);
            // Displays the HTML string in the UI via a WebView
//            WebView myWebView = (WebView) findViewById(R.id.webview);
//            myWebView.loadData(result, "text/html", null);
        	progressDialog.dismiss();
        	
        	if(responseText!=null){
        		ShowDialog(responseText.toString());
        	}else{
        		ShowDialog("No return data!");
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