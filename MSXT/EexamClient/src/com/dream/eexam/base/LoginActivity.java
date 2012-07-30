package com.dream.eexam.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.dream.eexam.util.SystemConfig;
import com.dream.eexam.util.XMLParseUtil;

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

public class LoginActivity extends BaseActivity {

	public final static String LOG_TAG = "LoginActivity";
	
	private EditText idEt = null;
	private EditText passwordET = null;
	private Button loginBtn = null;
	
	private String requestURL = null;
	private StringBuffer responseText = new StringBuffer();
	
	private String path;
	private String fileName;
	private String status;
//	private boolean isSuccess = false;
	
	private Context mContext;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = getApplicationContext();
 
		idEt = (EditText) this.findViewById(R.id.idEt);
		passwordET = (EditText) this.findViewById(R.id.passwordET);
		
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(loginListener);
    }

    View.OnClickListener loginListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) {  
        	String id = idEt.getText().toString();
        	String password = passwordET.getText().toString();
        	responseText.setLength(0);
        	requestURL = SystemConfig.getInstance().getPropertyValue("Server_URL")+"/msxt/runinterview/loginAction/login?loginName="+id+"&loginPassword="+password;
        	new DownloadXmlTask().execute(requestURL);
        }  
    };
    
    private void loadXmlFromNetwork(String urlString){
    	Log.i(LOG_TAG,"loadXmlFromNetwork...");
    	Log.i(LOG_TAG,"urlString:"+urlString);
    	InputStream inputStream = null;
        try {
        	//get stream
//        	inputStream = downloadUrl(urlString);
            inputStream = LoginActivity.class.getClassLoader().getResourceAsStream("login_result.xml");
        	path = Environment.getExternalStorageDirectory().getPath()+File.separator +"eExam";  
        	fileName = "login_result.xml";  
        	responseText.append(inputStream2String(inputStream));
        	
        	//save stream to file
        	saveFile(path, fileName, responseText.toString());

        	//get stream from stream
//        	InputStream inputStream2 =  PapersActivity.class.getClassLoader().getResourceAsStream(path+ File.separator+fileName);
        	FileInputStream inputStream2 = new FileInputStream(new File(path+ File.separator+fileName));
            status = XMLParseUtil.readLoginResult(inputStream2);
            inputStream2.close();
        } catch (IOException e) {
        	Log.i(LOG_TAG,"IOException:" + e.getMessage());
		} catch (Exception e) {
			Log.i(LOG_TAG,"Exception:" + e.getMessage());
		} finally {

        }
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
	
	private void saveFile(String path, String fileName,String content) {
		Log.i(LOG_TAG,"saveFile...");
		Log.i(LOG_TAG,"path:"+path);
		Log.i(LOG_TAG,"fileName:"+fileName);
		Log.i(LOG_TAG,"content:"+content);
		try {
	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
	            File dir = new File(path);  
	            if (!dir.exists()) {  
	                dir.mkdirs();  
	            }  
	            FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);  
	            fos.write(content.getBytes());  
	            fos.close();  
	        }  
		} catch (Exception e) {
			Log.e(LOG_TAG, "an error occured while writing file...", e);
		}
		Log.i(LOG_TAG,"saveFile end.");
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
        	progressDialog.dismiss();
        	/*if(responseText!=null){
        		ShowDialog(responseText.toString());
        	}else{
        		ShowDialog("No return data!");
        	}*/
        	
        	if("success".equals(status)){
    			Intent intent = new Intent();
    			intent.setClass( mContext, PapersActivity.class);
    			intent.putExtra("path", path);
    			intent.putExtra("fileName", fileName);
    			startActivity(intent);        		
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