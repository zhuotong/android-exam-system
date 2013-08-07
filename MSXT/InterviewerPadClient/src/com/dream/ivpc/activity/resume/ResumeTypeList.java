package com.dream.ivpc.activity.resume;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.adapter.ResumeTypeAdapter;
import com.dream.ivpc.model.ResumeTypeBean;
import com.dream.ivpc.util.NetWorkUtil;
import com.markupartist.android.widget.PullToRefreshListView;

public class ResumeTypeList extends ListActivity{
	public final static String LOG_TAG = "ResumeTypeList";
	
	ImageButton closeIB;
	
	Context mContext;
	PullToRefreshListView listView;
	ResumeTypeAdapter adapter;
	List<ResumeTypeBean> resumeTypeList = new ArrayList<ResumeTypeBean>();
    ProgressBar progressBar;
    int mShortAnimationDuration = 200;
    
	public void loadData(){
		resumeTypeList.clear();
		resumeTypeList.add(new ResumeTypeBean("Picture",true));
		resumeTypeList.add(new ResumeTypeBean("PDF",true));
		resumeTypeList.add(new ResumeTypeBean("Html",true));
	}
	
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.resume_type_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        
        mContext = getApplicationContext();
        
        closeIB = (ImageButton) findViewById(R.id.closeIB);
        closeIB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
		listView = (PullToRefreshListView) getListView();
		// Set a listener to be invoked when the list should be refreshed.
		listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
		});
		
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		
        		Log.i(LOG_TAG,"arg2:"+String.valueOf(arg2));
        		
        		ResumeTypeBean bean = resumeTypeList.get(arg2-1);
        		if(bean.isAvailable()){
        			String type = bean.getTypeName();
        			
        			Log.i(LOG_TAG,"type:"+type);
        			
        			if("Picture".equalsIgnoreCase(type)){
        				loadPicture();
        			}
        			if("PDF".equalsIgnoreCase(type)){
        				loadPdf("admin","tangqi");
        			}
        			if("Html".equalsIgnoreCase(type)){
        				loadHtml();
        			}  
        		}
			}      	
        });
        
        if(NetWorkUtil.isNetworkAvailable(mContext)){
        	new GetDataTask().execute();
        }else{
        	Toast.makeText(mContext, "Your network is not available!", Toast.LENGTH_LONG).show();
        }
    }
   
    public void loadPicture(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ResumePicture.class);
		startActivity(intent);     	
    }
    
    public void loadPdf(String adminFolder, String candidateFolder){
		String basePath = Environment.getExternalStorageDirectory()
				.getPath();
		String pdfPath = basePath + File.separator + "interviewer"
				+ File.separator + adminFolder 
				+ File.separator + candidateFolder 
				+ File.separator+ "resume.pdf";
		
		Uri uri = Uri.parse(pdfPath);
		Intent intent = new Intent(mContext, MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		startActivity(intent);
    }
    
    public void loadHtml(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ResumeWebView.class);
		startActivity(intent); 
    }
	   
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
                loadData();
            } catch (InterruptedException e) {
            }
            return null;
        }
        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if(resumeTypeList!=null&&resumeTypeList.size()>0){
            	adapter = new ResumeTypeAdapter(resumeTypeList,mContext);
                setListAdapter(adapter); 
                
                // Call onRefreshComplete when the list has been refreshed.
                listView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                
            }else{
            	Toast.makeText(mContext, "Can not get any data!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
