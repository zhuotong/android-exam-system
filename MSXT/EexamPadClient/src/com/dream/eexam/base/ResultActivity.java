package com.dream.eexam.base;

import com.dream.eexam.util.ActivityStackControlUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends BaseActivity {

	public final static String LOG_TAG = "ResultActivity";
	
    Context mContext;
	
    ImageView imgHome = null;
    TextView scoreTV = null;
	Button quitBtn = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        
        mContext = getApplicationContext();
		
		Bundle bundle = this.getIntent().getExtras();
		String score  = bundle.getString("score");
		
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(goHomeListener);
		
        scoreTV = (TextView) this.findViewById(R.id.yourScoreTV);
        scoreTV.setText(score);
        
		quitBtn = (Button) this.findViewById(R.id.quitBtn);
		quitBtn.setOnClickListener(quitListener);
		
    }

    View.OnClickListener quitListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
		    
			AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
			builder.setMessage(mContext.getResources().getString(R.string.warning_quit_eexam))
					.setCancelable(false)
					.setPositiveButton(mContext.getResources().getString(R.string.warning_quit_eexam_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
								    finish();
								    ActivityStackControlUtil.finishProgram();
								    System.exit(0);
								}
							})
					.setNegativeButton(mContext.getResources().getString(R.string.warning_quit_eexam_cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
			builder.show();
        }  
    };
    
    View.OnClickListener goHomeListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			goHome(mContext);
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