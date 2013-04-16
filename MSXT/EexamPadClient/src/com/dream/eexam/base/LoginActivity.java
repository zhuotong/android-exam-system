package com.dream.eexam.base;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dream.eexam.util.FileUtil;
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
	
	EditText idEt = null;
	EditText passwordET = null;
	Button loginBtn = null;
	Button settingBtn = null;
	
	String saveHost = null;
	String savePort = null;
	String currentUserId = null;
	String currentUserPwd = null;
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
        	int examStatus = SPUtil.getIntegerFromSP(SPUtil.CURRENT_EXAM_STATUS, sharedPreferences);
        	
        	//If Current Exam User is empty, or Current Exam User not empty, but completed
        	//Then Connect server to login with a new User
        	if(StringUtil.isEmpty(currentUserId) || (!StringUtil.isEmpty(currentUserId) && examStatus == SPUtil.EXAM_STATUS_END)){
    			//clear last exam first,start a new exam
    			SPUtil.clearUserSP(sharedPreferences);
    			SPUtil.clearExamSP(sharedPreferences);
    			clearDB(mContext);
    			
            	if (getWifiIP() != null && getWifiIP().trim().length() > 0 && !getWifiIP().trim().equals("0.0.0.0")){
            		Log.i(LOG_TAG,"start connect server...");
            		new LoginTask().execute(new String[]{loginUserId,loginUserPwd});
            	}else{
            		ShowDialog(mContext.getResources().getString(R.string.dialog_note),
            				mContext.getResources().getString(R.string.msg_network_error));
            	}
        	}else{
        		if(loginUserId.equals(currentUserId)){
        			switch(examStatus){
	    				case SPUtil.EXAM_STATUS_NOT_START:go2ExamStart(mContext);break;//Go to ExamStart Page
	    				case SPUtil.EXAM_STATUS_START_GOING:go2ExamContinue(mContext);break;//Go to ExamContinue Page
	    				case SPUtil.EXAM_STATUS_START_GOING_OBSOLETE:go2ExamContinue(mContext);break;//Go to ExamContinue Page
	    				case SPUtil.EXAM_STATUS_START_PENDING_NEW:go2ExamStart(mContext);break;//Go to ExamStart Page
	    				case SPUtil.EXAM_STATUS_END:go2ExamResult(mContext);break;//Go to ExamResult Page
        			}
        		}else{
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
    	String loginUserId;
    	String loginUserPwd;
    	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(LoginActivity.this, null,mContext.getResources().getString(R.string.msg_login_server), true, false); 
    	}
    	
        @Override
		protected String doInBackground(String... urls) {
        	Log.i(LOG_TAG,"LoginTask.doInBackground()...");
        	loginUserId = urls[0];
        	loginUserPwd = urls[1];
        	proxy =  WebServerProxy.Factroy.createInstance(saveHost, Integer.valueOf(savePort));
    		loginResult = proxy.login(loginUserId, loginUserPwd);           	
			return null;
		}

        @Override
        protected void onPostExecute(String result) {
        	progressDialog.dismiss();
        	loginBtn.setEnabled(true);

    		if( loginResult.getStatus() == STATUS.ERROR ) {//Login failed
    			ShowDialog(mContext.getResources().getString(R.string.dialog_note),
    					loginResult.getErrorMessage());
        	} else {//Login successfully
        		
        		//define login result file name
//        		LOGIN_RESULT_FILE = idEt.getText().toString() + TimeDateUtil.getCurrentDate() + ".xml";
        		String loginResultFile = FileUtil.LOGIN_FILE_PREFIX + TimeDateUtil.getCurrentDate()+FileUtil.FILE_SUFFIX_XML;
        		
        		//save login file
        		FileUtil fu = new FileUtil();
        		fu.deleteFolder(new File(userHome));
        		fu.saveFile(userHome, loginResultFile, loginResult.getSuccessMessage());
        		
        		//save login result file
        		SPUtil.save2SP(SPUtil.CURRENT_USER_LOGIN_FILE_NAME, loginResultFile, sharedPreferences);
        		
        		//parse login result
        		try{
    		    	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    		        ByteArrayInputStream is = new ByteArrayInputStream( loginResult.getSuccessMessage().getBytes() );
    		        Document doc = db.parse( is );
    		        is.close();
    		        
    		        Element root = doc.getDocumentElement();
    		        String status = root.getElementsByTagName("status").item(0).getTextContent();
    		        
    		        if( status.equals("failed") ) {
    		        	String desc = root.getElementsByTagName("desc").item(0).getTextContent();
    		        	ShowDialog(mContext.getResources().getString(R.string.dialog_note),desc);
    		        } else {//Login successfully
    		        	String conversation = root.getElementsByTagName( "conversation" ).item(0).getTextContent();
    		        	proxy.setConversationId( conversation );
    		        	
    		        	//exam ready but not start
    		        	go2ExamStart(mContext);
    		        	SPUtil.save2SP(SPUtil.CURRENT_EXAM_STATUS, SPUtil.EXAM_STATUS_NOT_START, sharedPreferences);
    	        		//save new exam to sharedPreferences
    	        		SPUtil.save2SP(SPUtil.CURRENT_USER_ID, loginUserId, sharedPreferences);
    	        		SPUtil.save2SP(SPUtil.CURRENT_USER_PWD, loginUserPwd, sharedPreferences);
    	        		SPUtil.save2SP(SPUtil.CURRENT_USER_HOME, userHome, sharedPreferences);
    		        }
        		} catch (Exception e) {
        			ShowDialog(mContext.getResources().getString(R.string.dialog_note),"Invalid XML Data");
    			}
        	}
        }
    }


}