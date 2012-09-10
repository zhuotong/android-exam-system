package com.dream.eexam.base;

import com.dream.eexam.util.ActivityStackControlUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends BaseActivity {

	public final static String LOG_TAG = "ResultActivity";
	
	TextView scoreTV = null;
	Button quitBtn = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
		
		Bundle bundle = this.getIntent().getExtras();
		String score  = bundle.getString("score");
		
        scoreTV = (TextView) this.findViewById(R.id.scoreTV);
        scoreTV.setText(score);
        
		quitBtn = (Button) this.findViewById(R.id.quitBtn);
		quitBtn.setOnClickListener(quitListener);
		
    }

    View.OnClickListener quitListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
		    finish();
		    ActivityStackControlUtil.finishProgram();
		    System.exit(0);
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