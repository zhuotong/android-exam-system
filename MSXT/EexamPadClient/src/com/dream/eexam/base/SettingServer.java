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
		
		saveHostBtn = (Button) this.findViewById(R.id.saveBtn);
		saveHostBtn.setOnClickListener(saveListener);

		savePortBtn = (Button) this.findViewById(R.id.clearBtn);
		savePortBtn.setOnClickListener(saveListener);

    }
    
    View.OnClickListener saveListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	switch(v.getId()){
        		case R.id.saveBtn:save();break;
        		case R.id.clearBtn:clear();
        	}
        }  
    };
    
    private void save(){
    	String host = hostET.getText().toString();
    	String port = portET.getText().toString();
    	if(ValidateUtil.validateIP4(host)){
			SPUtil.save2SP(SPUtil.SP_KEY_HOST, host, sharedPreferences);
			SPUtil.save2SP(SPUtil.SP_KEY_PORT, port, sharedPreferences);
			Toast.makeText(mContext, "Host And Port is saved!", Toast.LENGTH_SHORT).show();
    	}else{
    		valiMessageTV.setVisibility(View.VISIBLE);
    		valiMessageTV.setText(valiMessageArray[0]);
    	}
    }
    
    private void clear(){
    	hostET.getText().clear();
    	portET.getText().clear();
		Toast.makeText(mContext, "Host And Port is cleared, please input again", Toast.LENGTH_SHORT).show();
    }
}
