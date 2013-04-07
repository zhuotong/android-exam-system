package com.dream.eexam.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dream.eexam.server.FileUtil;
import com.dream.eexam.util.SPUtil;
import com.dream.eexam.util.StringUtil;
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

/**
 * Login Page
 * @author Timothy
 *
 */

public class LoginActivity extends BaseActivity {

	public final static String LOG_TAG = "LoginActivity";
	public static String LOGIN_RESULT_FILE = "lgresult"+TimeDateUtil.getCurrentDate()+".xml";;
	
	String saveHost = null;
	String savePort = null;
	
	EditText idEt = null;
	EditText passwordET = null;
	
	String currentUserId = null;
	String currentUserPwd = null;
	
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
        
        currentUserId = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
        currentUserPwd = SPUtil.getFromSP(SPUtil.CURRENT_USER_PWD, sharedPreferences);
        
        idEt = (EditText) this.findViewById(R.id.idEt);
        idEt.setText(currentUserId==null?"":currentUserId);
		
		passwordET = (EditText) this.findViewById(R.id.passwordET);
		passwordET.setText(currentUserPwd==null?"":currentUserPwd);
		
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(loginListener);
		
		settingBtn = (Button) this.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new View.OnClickListener() {  
	        @Override  
	        public void onClick(View v) {
//            	String id = idEt.getText().toString();
//            	String password = passwordET.getText().toString();
//	        	String settingId = getResources().getString(R.string.setting_id);
//	        	String settingPwd = getResources().getString(R.string.setting_password);
//	        	if(settingId.endsWith(id)&&settingPwd.equals(password)){
		        	//go to examList page
		        	Intent intent = new Intent();
					intent.setClass( LoginActivity.this, SettingServer.class);
					startActivity(intent);  	        		
//	        	}else{
//		        	ShowDialog(mContext.getResources().getString(R.string.dialog_note),
//		        			mContext.getResources().getString(R.string.msg_warning_setting));
//	        	}
//	
	        }  
	    });
		
    }

    //define login listener
    View.OnClickListener loginListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 

        	Log.i(LOG_TAG,"Login Button on Click...");
        	
        	String loginUserId  = idEt.getText().toString();
        	String loginUserPwd  = passwordET.getText().toString();

        	if(StringUtil.isEmpty(loginUserId)|| StringUtil.isEmpty(loginUserPwd)){
    			ShowDialog(mContext.getResources().getString(R.string.dialog_warning),
    					mContext.getResources().getString(R.string.msg_userid_password_empty_error));
    			return;
        	}
        	
        	Log.i(LOG_TAG,"loginUserId:"+loginUserId);
        	Log.i(LOG_TAG,"loginUserPwd:"+loginUserPwd);
        	
        	//get user home
        	userHome  = Environment.getExternalStorageDirectory().getPath()+File.separator + getResources().getString(R.string.app_file_home)
					+File.separator + loginUserId;
        	Log.i(LOG_TAG,"userHome:"+userHome);
 
        	//server is not set
        	saveHost = SPUtil.getFromSP(SPUtil.SP_KEY_HOST, sharedPreferences);
        	savePort = SPUtil.getFromSP(SPUtil.SP_KEY_PORT, sharedPreferences);
        	if(StringUtil.isEmpty(saveHost) && StringUtil.isEmpty(savePort)){
    			ShowDialog(mContext.getResources().getString(R.string.dialog_warning),
    					mContext.getResources().getString(R.string.msg_host_port_empty_error));
    			return;
        	}
        	
        	String currentUserId  = SPUtil.getFromSP(SPUtil.CURRENT_USER_ID, sharedPreferences);
        	String currentExamStatus = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_STATUS, sharedPreferences);
        	Log.i(LOG_TAG,"currentUserId:"+currentUserId);
        	Log.i(LOG_TAG,"currentExamStatus:"+currentExamStatus);
        	
        	
        	if(loginUserId.equals(currentUserId)){
        		Log.i(LOG_TAG,"---------------------currentUserId=loginUserId ---------------------");
        		
            	if(currentExamStatus==null){//user first login
            		Log.i(LOG_TAG,"currentExamStatus==null.");
            		
        			//clear last exam first,start a new exam
        			SPUtil.clearUserSP(sharedPreferences);
        			
        			//start a new exam
            		SPUtil.save2SP(SPUtil.CURRENT_USER_ID, loginUserId, sharedPreferences);
            		SPUtil.save2SP(SPUtil.CURRENT_USER_PWD, loginUserPwd, sharedPreferences);
            		SPUtil.save2SP(SPUtil.CURRENT_USER_HOME, userHome, sharedPreferences);
            		
                	if (getWifiIP() != null && getWifiIP().trim().length() > 0 && !getWifiIP().trim().equals("0.0.0.0")){
                		new LoginTask().execute(new String[]{loginUserId,loginUserPwd});
                	}else{
                		ShowDialog(mContext.getResources().getString(R.string.dialog_note),
                				mContext.getResources().getString(R.string.msg_network_error));
                	} 
            	}else if(SPUtil.STATUS_LOGIN_NOT_START.equals(currentExamStatus)){//user login before but not start exam ("0")
            		Log.i(LOG_TAG,"currentExamStatus=STATUS_LOGIN_NOT_START.");
            		
    	        	//go to start exam page directly
    	        	Intent intent = new Intent();
        			intent.setClass( mContext, ExamStart.class);
        			startActivity(intent);            		       			
            	}else if(SPUtil.STATUS_START_NOT_TIMEOUT_NOT_SUBMIT.equals(currentExamStatus)){ //("1")
            		Log.i(LOG_TAG,"currentExamStatus=STATUS_START_NOT_TIMEOUT_NOT_SUBMIT.");
            		
    	        	//go to continue exam page
    	        	Intent intent = new Intent();
        			intent.setClass( mContext, ExamContinue.class);
        			startActivity(intent);            		       			
            	}else if(SPUtil.STATUS_START_NOT_TIMEOUT_SUBMIT.equals(currentExamStatus) || SPUtil.STATUS_START_TIMEOUT_SUBMIT.equals(currentExamStatus) ){//("2","4")
            		if(SPUtil.STATUS_START_NOT_TIMEOUT_SUBMIT.equals(currentExamStatus)){
            			Log.i(LOG_TAG,"currentExamStatus=STATUS_START_NOT_TIMEOUT_SUBMIT.");
            		}else{
            			Log.i(LOG_TAG,"currentExamStatus=STATUS_START_TIMEOUT_SUBMIT.");
            		}
            		
    	        	//go to exam result page
    	        	Intent intent = new Intent();
        			intent.setClass( mContext, ResultActivity.class);
        			startActivity(intent); 
            	}else{
            		
            	}
        		
        	}else{
        		Log.i(LOG_TAG,"---------------------currentUserId != loginUserId ---------------------");
        		
        		//Exam is in progress
        		if(SPUtil.STATUS_START_NOT_TIMEOUT_NOT_SUBMIT.equals(currentExamStatus)||SPUtil.STATUS_START_TIMEOUT_NOT_SUBMIT.equals(currentExamStatus)){
            		ShowDialog(mContext.getResources().getString(R.string.dialog_note),
            				mContext.getResources().getString(R.string.msg_user_in_exam_error));
        		}else{
        			//clear last exam first,start a new exam
        			SPUtil.clearUserSP(sharedPreferences);
        			
            		//save new exam to sharedPreferences
            		SPUtil.save2SP(SPUtil.CURRENT_USER_ID, loginUserId, sharedPreferences);
            		SPUtil.save2SP(SPUtil.CURRENT_USER_PWD, loginUserPwd, sharedPreferences);
            		SPUtil.save2SP(SPUtil.CURRENT_USER_HOME, userHome, sharedPreferences);
            		
                	if (getWifiIP() != null && getWifiIP().trim().length() > 0 && !getWifiIP().trim().equals("0.0.0.0")){
                		new LoginTask().execute(new String[]{loginUserId,loginUserPwd});
                	}else{
                		ShowDialog(mContext.getResources().getString(R.string.dialog_note),
                				mContext.getResources().getString(R.string.msg_network_error));
                	}
        			
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
//        	Integer port = Integer.valueOf(mContext.getResources().getString(R.string.port));
        	proxy =  WebServerProxy.Factroy.createInstance(saveHost, Integer.valueOf(savePort));
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
        		//define login result file name
        		LOGIN_RESULT_FILE = idEt.getText().toString() + TimeDateUtil.getCurrentDate() + ".xml";
        		
        		//save login file
        		FileUtil fu = new FileUtil();
        		fu.deleteFolder(new File(userHome));
        		fu.saveFile(userHome, LOGIN_RESULT_FILE, loginResult.getSuccessMessage());
        		
        		//save login result file
        		SPUtil.save2SP(SPUtil.CURRENT_LOGIN_FILE_NAME, LOGIN_RESULT_FILE, sharedPreferences);
        		
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
    		        	
    		        	//clear last exam
    		        	SPUtil.clearExamSP(sharedPreferences);
    		        	//clear last exam progress
    		        	clearDB(mContext);
    		        	
    		        	//exam ready but not start
    		        	SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.STATUS_LOGIN_NOT_START, sharedPreferences);
    		        	
    		        	//go to exam List pa
    		        	Intent intent = new Intent();
    	    			intent.setClass( mContext, ExamStart.class);
    	    			startActivity(intent);   
    		        }
        		} catch (Exception e) {
        			ShowDialog(mContext.getResources().getString(R.string.dialog_note),"Invalid XML Data");
    			}
        	}
        }
    }


}