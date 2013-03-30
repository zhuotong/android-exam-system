package com.dream.ivpc;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.viewflow.android.widget.CircleFlowIndicator;
import org.viewflow.android.widget.ViewFlow;
import com.dream.ivpc.adapter.ResumeGroupAdapter;
import com.dream.ivpc.model.ResumeBean;
import com.dream.ivpc.model.ResumePageBean;
import com.dream.ivpc.util.DataParseUtil;
import com.dream.ivpc.util.FileUtil;
import com.dream.ivpc.util.ImageUtil;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CandidateResumeGroup extends CandidateInfoBase {
	
	private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/interviewer/";
	private final static String ALBUM_NAME = "sample_resume.xml";
	
	private ProgressDialog myDialog = null;
	private ViewFlow viewFlow;
	private CircleFlowIndicator indic;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTitle(R.string.circle_title);
		setContentView(R.layout.candidate_resume_group);
		
		mContext = getApplicationContext();

		setHeader((TextView)findViewById(R.id.candidateInfo));
		setFooter((Button) findViewById(R.id.resume));

        myDialog = ProgressDialog.show(CandidateResumeGroup.this, "Download File...", "Please Wait!", true);      
        new LoadTask().execute(new String[]{});
      
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
//		viewFlow.setAdapter(new ResumeGroupAdapter(this), 5);
		
		indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
//		viewFlow.setFlowIndicator(indic);
	}
	/* If your min SDK version is < 8 you need to trigger the onConfigurationChanged in ViewFlow manually, like this */	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
	}
	
	 private class LoadTask extends AsyncTask<String, Void, String>{
	    	private List<Bitmap> bitmapList =new ArrayList<Bitmap>();
	    	private String message;
	    	
	    	@Override
	    	protected void onPreExecute() {
	    		Log.i(LOG_TAG, "onPreExecute() called");
	    	}
	    	
	        @Override
			protected String doInBackground(String... urls) {
	        	try {
					//get image from local
		            FileInputStream inputStream =  FileUtil.getFileInputStream(ALBUM_PATH, ALBUM_NAME);
		            
		            //get resume bean
		            ResumeBean resume = DataParseUtil.parseResume(inputStream);
		            List<ResumePageBean> pageList = resume.getRpbList();
		            
//		            String encodedContent = null;
		            for(ResumePageBean bean: pageList){
//		            	if(bean.getIndex() == 1){
//		            		Log.i(LOG_TAG,"Resume Page: "+String.valueOf(bean.getIndex()));
//		            		encodedContent = bean.getContent().toString();
//							Log.i(LOG_TAG, "Resume encodedContent:" + encodedContent);
//							
//							break;
//		            	}
//			            ImageUtil.saveAsFileWriter(bean.getContent().toString(), ALBUM_PATH+File.separator+"base64(2).txt");
			            Bitmap bitmap = ImageUtil.decodeBase64(bean.getContent().toString());
			            bitmapList.add(bitmap);
		            }

		            message = "success";
					
				} catch (Exception e) {
					message = "fail";
					Log.e(LOG_TAG, message+":"+e.getMessage());
				} 
				return null;
			}
			
	        @Override
	        protected void onPostExecute(String result){
	        	myDialog.dismiss();
	        	
//	        	if("success".equals(message)&&bitmap!=null){
//	        		imageView.setImageBitmap(bitmap);
//	        	}
	        	viewFlow.setAdapter(new ResumeGroupAdapter(mContext,bitmapList), bitmapList.size());
	        	viewFlow.setFlowIndicator(indic);
	        	
	        	Toast.makeText(CandidateResumeGroup.this, message, Toast.LENGTH_SHORT).show();
	        }
	    	
	    }
}
