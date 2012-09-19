package com.dream.eexam.base;

import com.dream.eexam.util.DatabaseUtil;
import com.dream.eexam.util.ValidateUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends BaseActivity {

	public final static String LOG_TAG = "LoginActivity";
	
	Context mContext;
	TextView valiMessageTV = null;
	String[] valiMessageArray = null;
	EditText hostET = null;
	String saveHost = null;
	
	Button saveBtn = null;
	Button clearBtn = null;
	Button cancelBtn = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        
        mContext = getApplicationContext();
		
        valiMessageTV = (TextView) this.findViewById(R.id.valiMessage);
        valiMessageArray = getResources().getStringArray(R.array.msg_settings_invalid);
        
        hostET = (EditText) this.findViewById(R.id.hostET);
        saveHost = sharedPreferences.getString("host", null);
		if(saveHost!=null||!"".equals(saveHost)){
			hostET.setText(saveHost);
		}
		
		saveBtn = (Button) this.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(saveListener);
		
		clearBtn = (Button) this.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(clearListener);
		
		cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(saveListener);
		
    }

    View.OnClickListener saveListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
//        	Button lBtn = (Button)v;
//        	lBtn.setEnabled(false);
        	
        	String host = hostET.getText().toString();
        	
        	if(ValidateUtil.validateIP4(host)){
    			SharedPreferences.Editor editor = sharedPreferences.edit();
    			editor.putString("host", host);
    			editor.commit();		
    			
            	//go to login page
            	Intent intent = new Intent();
    			intent.setClass( SettingActivity.this, LoginActivity.class);
    			startActivity(intent);          		
        	}else{
        		valiMessageTV.setVisibility(View.VISIBLE);
        		valiMessageTV.setText(valiMessageArray[0]);
        	}

        }  
    };
    
    
    View.OnClickListener clearListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	SharedPreferences.Editor editor = sharedPreferences.edit(); 
        	editor.clear();
        	editor.commit();
        	
        	DatabaseUtil dbUtil = new DatabaseUtil(mContext);
        	dbUtil.open();
        	dbUtil.deleteAllAnswers();
        	dbUtil.close();
        	
        	ShowDialog(mContext.getResources().getString(R.string.msg_login_server));	
        }  
    };   
    

    
    View.OnClickListener cancelListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	
        	//go to login page
        	Intent intent = new Intent();
			intent.setClass( SettingActivity.this, LoginActivity.class);
			startActivity(intent);  	
        }  
    };
    
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