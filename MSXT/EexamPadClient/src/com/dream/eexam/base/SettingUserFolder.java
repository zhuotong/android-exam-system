package com.dream.eexam.base;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

public class SettingUserFolder extends SettingBase {
	
	Button clearBtn = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_user_folder);
        mContext = getApplicationContext();
        
        setHeader((ImageView)findViewById(R.id.imgHome));
		setFooter((Button) findViewById(R.id.server_setting));
		

    }
    

}
