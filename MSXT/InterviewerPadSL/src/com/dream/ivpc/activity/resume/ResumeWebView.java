package com.dream.ivpc.activity.resume;

import com.dream.ivpc.PageChange;
import com.dream.ivpc.R;
import com.dream.ivpc.activity.CandidateBase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

public class ResumeWebView extends CandidateBase {
	
	private final static String URL = "http://www.google.com.hk/";
	private WebView resumeWV;
	
    View.OnClickListener ocLister = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	finish();
        	switch(v.getId()){
        		case (R.id.customBack):PageChange.go2CandidateDeatil(mContext);break;
        		case (R.id.imgLogout):PageChange.logout(mContext);break;
        	}
        }  
    };
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.resume_webview);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title2);
        
		mContext = getApplicationContext();
		((ImageView) findViewById(R.id.customBack)).setOnClickListener(ocLister);
		((ImageView) findViewById(R.id.imgLogout)).setOnClickListener(ocLister);
		
		resumeWV = (WebView) this.findViewById(R.id.resumeWV);
		resumeWV.loadUrl(URL);
		resumeWV.setVisibility(View.VISIBLE);
		
//        new LoadResume().execute();
	}
	
	private class LoadResume extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
//                progressBar.setVisibility(View.GONE);
                resumeWV.loadUrl(URL);
                resumeWV.getSettings().setJavaScriptEnabled(true);
            } catch (InterruptedException e) {
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            
        }
    }
}
