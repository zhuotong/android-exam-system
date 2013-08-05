package com.dream.ivpc.activity.resume;

import com.dream.ivpc.R;
import com.dream.ivpc.activity.CandidateBase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class ResumeWebView extends CandidateBase {
	
	private final static String URL = "http://www.google.com.hk/";
	private WebView resumeWV;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_webview);
		
		mContext = getApplicationContext();
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
