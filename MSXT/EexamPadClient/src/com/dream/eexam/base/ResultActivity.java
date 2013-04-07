package com.dream.eexam.base;

import com.dream.eexam.util.ActivityManage;
import com.dream.eexam.util.SPUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    
    TextView examLeftTV = null;
    Button continueBtn = null;
	Button quitBtn = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        
        mContext = getApplicationContext();
		
		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(onClickListener);
		
		String score = SPUtil.getFromSP(SPUtil.CURRENT_EXAM_SCORE, sharedPreferences);
        scoreTV = (TextView) this.findViewById(R.id.yourScoreTV);
        scoreTV.setText(score);
        
        examLeftTV = (TextView) this.findViewById(R.id.examLeftTV);
        examLeftTV.setText("You have 1 exam paper remaining,please continue!");
 
		continueBtn = (Button) this.findViewById(R.id.continueBtn);
		continueBtn.setOnClickListener(onClickListener);
		quitBtn = (Button) this.findViewById(R.id.quitBtn);
		quitBtn.setOnClickListener(onClickListener);
		
    }
    
    View.OnClickListener onClickListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	switch(v.getId()){
        		case R.id.continueBtn: continueExam();break;
        		case R.id.quitBtn:quitExam();break;
        		case R.id.imgHome:goHome(mContext);
        	}
        }  
    };

    private void continueExam(){
    	//go to start exam page
    	Intent intent = new Intent();
		intent.setClass( mContext, ExamStart.class);
		startActivity(intent); 
    }
    
    private void quitExam(){
		AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
		builder.setMessage(mContext.getResources().getString(R.string.warning_quit_eexam))
				.setCancelable(false)
				.setPositiveButton(mContext.getResources().getString(R.string.warning_quit_eexam_yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
							    finish();
							    ActivityManage.finishProgram();
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


}