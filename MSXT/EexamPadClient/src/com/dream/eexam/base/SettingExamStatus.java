package com.dream.eexam.base;

import com.dream.eexam.util.SPUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingExamStatus extends SettingBase {
	TextView spData;
	Button clearBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_examstatus);
		mContext = getApplicationContext();

		setHeader((ImageView) findViewById(R.id.imgHome));
		setFooter((Button) findViewById(R.id.examstatus_setting));

		spData = (TextView) this.findViewById(R.id.spData);
		spData.setText(SPUtil.getAllSPData(sharedPreferences));
		
		clearBtn = (Button) this.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(clearListener);
	}
	
    View.OnClickListener clearListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	clearSP();
        	clearDB(mContext);
        	
        	ShowDialog(mContext.getResources().getString(R.string.dialog_note),
        			mContext.getResources().getString(R.string.msg_history_be_cleared));	
        }  
    }; 
    
    
}
