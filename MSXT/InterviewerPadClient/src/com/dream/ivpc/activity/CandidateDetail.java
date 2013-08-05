package com.dream.ivpc.activity;

import java.io.File;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.activity.report.CandidateInfoExamRpt;
import com.dream.ivpc.activity.resume.ResumeWebView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CandidateDetail extends BaseActivity {
	public final static String LOG_TAG = "CandidateDetail";
	
	ImageView imgGoBack = null;
	TextView canInfoTV;
	
	ImageView resumeBtn = null;
	ImageView reportBtn = null;
	ImageView webViewBtn = null;
	
	Context mContext;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate);
        mContext = getApplicationContext();

        imgGoBack = (ImageView) this.findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(goBackListener);
        
        canInfoTV = (TextView) this.findViewById(R.id.candidateInfo);
		Bundle bundle = this.getIntent().getExtras();
		String name  = bundle.getString("name");
		String position  = bundle.getString("position");
		if(name!=null&&position!=null){
			canInfoTV.setText(name+"("+position+")");
		}
        
		resumeBtn = (ImageView) this.findViewById(R.id.imageView1);
		resumeBtn.setOnClickListener(resumeListener);
		
		reportBtn = (ImageView) this.findViewById(R.id.imageView2);
		reportBtn.setOnClickListener(reportListener);
	
		webViewBtn = (ImageView) this.findViewById(R.id.imageView3);
		webViewBtn.setOnClickListener(webViewListener);
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Intent intent = new Intent();
			intent.setClass( mContext, CandidateList.class);
			startActivity(intent);  
        }  
    };
    
    View.OnClickListener resumeListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
			String basePath = Environment.getExternalStorageDirectory()
					.getPath();
			String pdfPath = basePath + File.separator + "interviewer"
					+ File.separator + "tangqi" + File.separator
					+ "tangqi_resume.pdf";
			
			Uri uri = Uri.parse(pdfPath);
			Intent intent = new Intent(mContext, MuPDFActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(uri);
			startActivity(intent);
        }  
    };
    
    View.OnClickListener reportListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Intent intent = new Intent();
			intent.setClass( mContext, CandidateInfoExamRpt.class);
			startActivity(intent);  
        }  
    };
    
    View.OnClickListener webViewListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Intent intent = new Intent();
			intent.setClass( mContext, ResumeWebView.class);
			startActivity(intent);  
        }  
    };
    
}
