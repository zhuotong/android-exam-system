package com.dream.ivpc;

import com.dream.ivpc.R;
import com.dream.ivpc.custom.CustomDialog;
import com.dream.ivpc.util.SPUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends BaseActivity {
	public final static String LOG_TAG = "LoginActivity";
	
	EditText hostEt = null;
	String saveHost = null;
	
	EditText portET = null;
	String savePort = null;
	
	Button saveBtn = null;
	Button cancelBtn = null;
	
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
        setContentView(R.layout.setting);
        mContext = getApplicationContext();
        
        hostEt = (EditText) this.findViewById(R.id.hostEt);
        saveHost = SPUtil.getFromSP(SPUtil.SP_KEY_HOST,sharedPreferences);
		if(saveHost!=null||!"".equals(saveHost)){
			hostEt.setText(saveHost);
		}
		
		portET = (EditText) this.findViewById(R.id.portET);
		savePort = SPUtil.getFromSP(SPUtil.SP_KEY_PORT,sharedPreferences);
		if(savePort!=null||!"".equals(savePort)){
			portET.setText(savePort);
		}
		
		saveBtn = (Button) this.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SPUtil.save2SP(SPUtil.SP_KEY_HOST, hostEt.getText().toString(), sharedPreferences);
				SPUtil.save2SP(SPUtil.SP_KEY_PORT, portET.getText().toString(), sharedPreferences);
				
				ShowDialog("Message","Host and Port saved successfully!");
				
				PageChange.logout(mContext);
			}
		});
		
		cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//		    	Intent intent = new Intent();
//				intent.setClass( mContext, LoginActivity.class);
//				startActivity(intent);  
				
				PageChange.logout(mContext);
			}
		});
		
    }
	
}
