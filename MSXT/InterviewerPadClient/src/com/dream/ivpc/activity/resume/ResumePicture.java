package com.dream.ivpc.activity.resume;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.viewflow.android.widget.CircleFlowIndicator;
import org.viewflow.android.widget.ViewFlow;

import com.dream.ivpc.CandidateBase;
import com.dream.ivpc.PageChange;
import com.dream.ivpc.R;
import com.dream.ivpc.adapter.ResumeGroupAdapter;
import com.dream.ivpc.model.ResumeBean;
import com.dream.ivpc.model.ResumePageBean;
import com.dream.ivpc.util.XMLParseUtil;
import com.dream.ivpc.util.FileUtil;
import com.dream.ivpc.util.ImageUtil;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ResumePicture extends CandidateBase {

	ImageButton closeIB;
//	ProgressDialog myDialog = null;
	ViewFlow viewFlow;
	CircleFlowIndicator indic;

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
		// setTitle(R.string.circle_title);
		
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.candidate_resume_group);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title2);
        
		mContext = getApplicationContext();
		
		((ImageView) findViewById(R.id.customBack)).setOnClickListener(ocLister);
		((ImageView) findViewById(R.id.imgLogout)).setOnClickListener(ocLister);
        
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
//		 viewFlow.setAdapter(new ResumeGroupAdapter(this), 5);

		indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
//		viewFlow.setFlowIndicator(indic);
		
		new LoadTask().execute(new String[] {});
	}

	/*
	 * If your min SDK version is < 8 you need to trigger the
	 * onConfigurationChanged in ViewFlow manually, like this
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
	}

	private class LoadTask extends AsyncTask<String, Void, String> {
		private List<Bitmap> bitmapList = new ArrayList<Bitmap>();
		private String message;
//		private ProgressBar progressBar;
		private boolean isSuccess;
		
		@Override
		protected void onPreExecute() {
			Log.i(LOG_TAG, "onPreExecute() called");
//			progressBar = (ProgressBar) findViewById(R.id.loading_resume);
//			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				// get image from local
				FileInputStream inputStream = FileUtil.getFileInputStream(getRptPath("admin","tangqi"));

				// get resume bean
				ResumeBean resume = XMLParseUtil.parseResume(inputStream);
				List<ResumePageBean> pageList = resume.getRpbList();

				// String encodedContent = null;
				for (ResumePageBean bean : pageList) {
					Bitmap bitmap = ImageUtil.decodeBase64(bean.getContent().toString());
					bitmapList.add(bitmap);
				}

				isSuccess = true;

			} catch (Exception e) {
//				message = "fail";
				Log.e(LOG_TAG, message + ":" + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
//			progressBar.setVisibility(View.GONE);

			if(isSuccess){
				viewFlow.setAdapter(new ResumeGroupAdapter(mContext, bitmapList),bitmapList.size());
				viewFlow.setFlowIndicator(indic);				
			}else{
				Toast.makeText(ResumePicture.this, "Can not get data!", Toast.LENGTH_SHORT).show();
			}
			
		}

	}
	
	public String getRptPath(String admin,String candidate) {
		String basePath = Environment.getExternalStorageDirectory() + "/interviewer";
		return basePath + File.separator + admin + File.separator + candidate + File.separator + "resume.xml";
	}
}
