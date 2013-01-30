package com.dream.eexam.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.dream.eexam.util.SPUtil;
import com.dream.eexam.util.TimeDateUtil;
import com.msxt.client.server.ServerProxy;
import com.msxt.client.server.WebServerProxy;
import com.msxt.client.server.ServerProxy.Result;
import com.msxt.client.server.ServerProxy.STATUS;
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
	public static String LOGIN_RESULT_FILE;
	
	String saveHost = null;
	EditText idEt = null;
	EditText passwordET = null;
	String saveId = null;
	String savePassword = null;
	Button loginBtn = null;
	Button settingBtn = null;
	
	//user home of save file
	String userHome = null;
	
	private Context mContext;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = getApplicationContext();
        
        saveHost = SPUtil.getFromSP(SPUtil.SP_KEY_HOST, sharedPreferences);
        
        idEt = (EditText) this.findViewById(R.id.idEt);
        saveId = SPUtil.getFromSP(SPUtil.SP_KEY_ID, sharedPreferences);
		if(saveId!=null||!"".equals(saveId)){
			idEt.setText(saveId);
		}
		
		passwordET = (EditText) this.findViewById(R.id.passwordET);
		savePassword = SPUtil.getFromSP(SPUtil.SP_KEY_PWD, sharedPreferences);
		if(savePassword!=null||!"".equals(savePassword)){
			passwordET.setText(savePassword);
		}
		
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(loginListener);
		
		settingBtn = (Button) this.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new View.OnClickListener() {  
	        @Override  
	        public void onClick(View v) { 
	        	//go to examList page
	        	Intent intent = new Intent();
				intent.setClass( LoginActivity.this, SettingActivity.class);
				startActivity(intent);  	
	        }  
	    });
		
    }

    //define login listener
    View.OnClickListener loginListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Button lBtn = (Button)v;
        	lBtn.setEnabled(false);

        	LOGIN_RESULT_FILE = "lgresult"+TimeDateUtil.getCurrentDate()+".xml";
        	
        	userHome = Environment.getExternalStorageDirectory().getPath() 
        	+ File.separator + getResources().getString(R.string.app_file_home)
        	+ File.separator + idEt.getText().toString();
        	
        	SPUtil.save2SP(SPUtil.SP_KEY_USER_HOME, userHome, sharedPreferences);
        	
        	String examPath = SPUtil.getFromSP(SPUtil.SP_KEY_EXAM_PATH, sharedPreferences);
        	String examFile = SPUtil.getFromSP(SPUtil.SP_KEY_EXAM_FILE, sharedPreferences);
        	String examStatus = SPUtil.getFromSP(SPUtil.SP_KEY_EXAM_STATUS, sharedPreferences);
        	
        	boolean firstTimeFlag = new File(examPath+File.separator+examFile).exists();
        	boolean examStartFlag = SPUtil.SP_VALUE_EXAM_STATUS_START.equals(examStatus);
        	
        	if(!examStartFlag){//exam is not start
        		//go to start exam page
            	String id = idEt.getText().toString();
            	String password = passwordET.getText().toString();
        		SPUtil.save2SP(SPUtil.SP_KEY_ID, id, sharedPreferences);
        		SPUtil.save2SP(SPUtil.SP_KEY_PWD, password, sharedPreferences);
            	if (getWifiIP() != null && getWifiIP().trim().length() > 0 && !getWifiIP().trim().equals("0.0.0.0")){
            		new LoginTask().execute(new String[]{id,password});
            	}else{
            		ShowDialog(mContext.getResources().getString(R.string.dialog_note),
            				mContext.getResources().getString(R.string.msg_network_error));
            	}         		
        	}else{//exam is start  
        		if(!firstTimeFlag){//user has login before
    	        	//go to continue exam page
    	        	Intent intent = new Intent();
        			intent.putExtra("loginResultFile", LOGIN_RESULT_FILE);
        			intent.putExtra("loginResultFilePath", userHome);
        			intent.setClass( mContext, ExamContinue.class);
        			startActivity(intent);            		       			
        		}else{//user has not login before
            		ShowDialog(mContext.getResources().getString(R.string.dialog_note),
            				mContext.getResources().getString(R.string.msg_user_in_exam_error));
        		}
        	}
        }
    };
    
    //define login task
    private class LoginTask extends AsyncTask<String, Void, String> {
    	ProgressDialog progressDialog;
    	ServerProxy proxy;
    	Result loginResult;
    	
    	@Override
    	protected void onPreExecute() {
    		Log.i(LOG_TAG, "onPreExecute() called");
    		progressDialog = ProgressDialog.show(LoginActivity.this, null,mContext.getResources().getString(R.string.msg_login_server), true, false); 
    	}
    	
        @Override
		protected String doInBackground(String... urls) {
        	Integer port = Integer.valueOf(mContext.getResources().getString(R.string.port));
        	proxy =  WebServerProxy.Factroy.createInstance(saveHost, port);
    		loginResult = proxy.login(urls[0], urls[1]);
			return null;
		}

        @Override
        protected void onPostExecute(String result) {
        	progressDialog.dismiss();
        	loginBtn.setEnabled(true);

    		if( loginResult.getStatus() == STATUS.ERROR ) {
    			ShowDialog(mContext.getResources().getString(R.string.dialog_note),
    					loginResult.getErrorMessage());
        	} else {
        		//save login result file
        		saveFile(userHome, LOGIN_RESULT_FILE, loginResult.getSuccessMessage());
        		
        		SPUtil.save2SP(SPUtil.SP_KEY_LOGIN_FILE_PATH, userHome, sharedPreferences);
        		SPUtil.save2SP(SPUtil.SP_KEY_LOGIN_FILE, LOGIN_RESULT_FILE, sharedPreferences);
        		
        		try{
    		    	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    		        ByteArrayInputStream is = new ByteArrayInputStream( loginResult.getSuccessMessage().getBytes() );
    		        Document doc = db.parse( is );
    		        is.close();
    		        
    		        Element root = doc.getDocumentElement();
    		        String status = root.getElementsByTagName("status").item(0).getTextContent();
    		        if( status.equals("failed") ) {
    		        	String desc = root.getElementsByTagName("desc").item(0).getTextContent();
    		        	ShowDialog(mContext.getResources().getString(R.string.dialog_note),
    		        			desc);
    		        } else {
    		        	String conversation = root.getElementsByTagName( "conversation" ).item(0).getTextContent();
    		        	proxy.setConversationId( conversation );
    		        	
    		        	//get exam status 
    		        	String examStatus = SPUtil.getFromSP(SPUtil.SP_KEY_EXAM_STATUS, sharedPreferences);
    		        	if(examStatus == null){
        		        	//go to exam List pa
        		        	Intent intent = new Intent();
        	    			intent.setClass( mContext, ExamStart.class);
        	    			startActivity(intent);     		        		
    		        	}else if(SPUtil.SP_VALUE_EXAM_STATUS_START.equals(examStatus)){
        		        	//go to continue exam page
        		        	Intent intent = new Intent();
        	    			intent.setClass( mContext, ExamContinue.class);
        	    			startActivity(intent);   
    		        	}else if(SPUtil.SP_VALUE_EXAM_STATUS_END.equals(examStatus)){
    		        		ShowDialog(mContext.getResources().getString(R.string.dialog_note),"Your Exam is completed");
    		        	}
    		        }
        		} catch (Exception e) {
        			ShowDialog(mContext.getResources().getString(R.string.dialog_note),"Invalid XML Data");
    			}
        	}
        }
    }


}