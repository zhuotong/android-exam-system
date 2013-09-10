package com.dream.ivpc.activity.exam;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.dream.ivpc.R;
import com.dream.ivpc.adapter.ExamListAdapter;
import com.dream.ivpc.model.ExamBean;
import com.dream.ivpc.server.DAOProxy;
import com.dream.ivpc.server.DAOProxyLocalImp;
import com.dream.ivpc.util.NetWorkUtil;

public class ExamRptList extends ListActivity {

	public final static String LOG_TAG = "CandidateExamList";
	Context mContext;
	ListView listView;
	ProgressBar progressBar;
	
	ExamListAdapter adapter;
	List<ExamBean> examList = new ArrayList<ExamBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.candidate_exam_list);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		
		mContext = getApplicationContext();
		
		ImageView closeIcon =  (ImageView) findViewById(R.id.closeIcon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
		listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,int arg2, long arg3) {
				ExamBean bean = examList.get(arg2);

		    	Intent intent = new Intent();
		    	intent.putExtra("examid", bean.getExamId());
		    	intent.putExtra("examname", bean.getExamName());
				intent.setClass( mContext, ExamRptPicture.class);
				startActivity(intent); 
			}
		});
		
        if(NetWorkUtil.isNetworkAvailable(mContext)){
        	new GetDataTask().execute();
        }else{
        	Toast.makeText(mContext, "Your network is not available!", Toast.LENGTH_LONG).show();
        }
	}

	private class GetDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
        		DAOProxy proxy = new DAOProxyLocalImp();
        		examList = proxy.getCandiateExamRptList(urls[0], urls[1]);
        		
            } catch (InterruptedException e) {
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(examList!=null&&examList.size()>0){
            	progressBar.setVisibility(View.GONE);
            	adapter = new ExamListAdapter(examList,mContext);
//                setListAdapter(adapter); 
                listView.setAdapter(adapter);
            }else{
            	Toast.makeText(mContext, "Can not get any data!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
