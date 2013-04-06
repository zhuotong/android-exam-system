package com.dream.eexam.base;

import com.dream.eexam.util.SPUtil;
import com.dream.eexam.util.ValidateUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingServer extends SettingBase {
	TextView valiMessageTV = null;
	String[] valiMessageArray = null;
	EditText hostET = null;
	EditText portET = null;
	
	Button saveHostBtn = null;
	Button savePortBtn = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_server);
        mContext = getApplicationContext();
        
        setHeader((ImageView)findViewById(R.id.imgHome));
		setFooter((Button) findViewById(R.id.server_setting));
		
        valiMessageTV = (TextView) this.findViewById(R.id.valiMessage);
        valiMessageArray = getResources().getStringArray(R.array.msg_settings_invalid);
        
        //set host if exist
        hostET = (EditText) this.findViewById(R.id.hostET);
        String saveHost = sharedPreferences.getString(SPUtil.SP_KEY_HOST, null);
		hostET.setText(saveHost!=null?saveHost:"");

        //set host if exist
		portET = (EditText) this.findViewById(R.id.portET);
        String savePort = sharedPreferences.getString(SPUtil.SP_KEY_PORT, null);
        portET.setText(savePort!=null?savePort:"");
		
		saveHostBtn = (Button) this.findViewById(R.id.saveHostBtn);
		saveHostBtn.setOnClickListener(saveListener);

		savePortBtn = (Button) this.findViewById(R.id.savePortBtn);
		savePortBtn.setOnClickListener(saveListener);

    }
    
    View.OnClickListener saveListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	switch(v.getId()){
        		case R.id.saveHostBtn:saveHost();break;
        		case R.id.savePortBtn:savePort();
        	}
        }  
    };
    
    private void saveHost(){
    	String host = hostET.getText().toString();
    	if(ValidateUtil.validateIP4(host)){
			SPUtil.save2SP(SPUtil.SP_KEY_HOST, host, sharedPreferences);
			Toast.makeText(mContext, "Host is saved!", Toast.LENGTH_SHORT).show();
    	}else{
    		valiMessageTV.setVisibility(View.VISIBLE);
    		valiMessageTV.setText(valiMessageArray[0]);
    	}
    }
    
    private void savePort(){
    	String host = hostET.getText().toString();
		SPUtil.save2SP(SPUtil.SP_KEY_HOST, host, sharedPreferences);
		Toast.makeText(mContext, "Port is saved!", Toast.LENGTH_SHORT).show();
    }
}
