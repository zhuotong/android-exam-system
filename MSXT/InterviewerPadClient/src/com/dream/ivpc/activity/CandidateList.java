package com.dream.ivpc.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.adapter.CandidateListAdapter;
import com.dream.ivpc.model.CandiateBean;
import com.dream.ivpc.util.NetWorkUtil;
import com.markupartist.android.widget.PullToRefreshListView;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CandidateList extends ListActivity {
	
	ImageView imgHome = null;
	ImageView timeSortIcon = null;
	ImageView positionSortIcon = null;
	ImageView nameSortIcon = null;
	
	int timeSortFlag = -1;
	int positionSortFlag = -1;
	int nameSortFlag = -1;
	
	Context mContext;
	PullToRefreshListView listView;
	CandidateListAdapter adapter;
	List<CandiateBean> candiateList = new ArrayList<CandiateBean>();
    ProgressBar progressBar;
    int mShortAnimationDuration = 200;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_list);
        mContext = getApplicationContext();

		imgHome = (ImageView) findViewById(R.id.imgHome);
		timeSortIcon = (ImageView) findViewById(R.id.timeSortIcon);
		positionSortIcon = (ImageView) findViewById(R.id.positionSortIcon);
		nameSortIcon = (ImageView) findViewById(R.id.nameSortIcon);
		timeSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		
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
        		CandiateBean bean = candiateList.get(arg2);
            	Intent intent = new Intent();
//    			intent.putExtra("name", bean.getName());
//    			intent.putExtra("position", bean.getPosition());
    			intent.setClass( mContext, CandidateDetail.class);
    			startActivity(intent); 
    			
//        		String basePath = Environment.getExternalStorageDirectory().getPath();
//        		String pdfFile = basePath + File.separator + "books" + File.separator
//        				+ "Android" + File.separator + "Develop" + File.separator
//        				+ "Android Users Guide.pdf";
//        		openPdf(pdfFile,mContext);
			}      	
        });
        
        if(NetWorkUtil.isNetworkAvailable(mContext)){
        	new GetDataTask().execute();
        }else{
        	Toast.makeText(mContext, "Your network is not available!", Toast.LENGTH_LONG).show();
        }
    }
    
    public void openPdf(String pdfFile,Context mContext){
		Uri uri = Uri.parse(pdfFile);
		Intent intent = new Intent(mContext,MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		startActivity(intent);
    }
    
//    View.OnClickListener goHomeListener = new View.OnClickListener(){
//		@Override
//		public void onClick(View v) {
//			goHome(mContext);
//		}
//	};
	
/*    View.OnClickListener sortListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case(R.id.timeSortIcon): sortByTime(); break;
				case(R.id.positionSortIcon):sortByPosition();break;
				case(R.id.nameSortIcon):sortByName();
			}
	        adapter = new CandidateListAdapter(candiateList,mContext);
	        listView.setAdapter(adapter);
			
		}
	};*/
	
	public void loadData(){
		candiateList.add(new CandiateBean("2012-10-12 08:00","Java Engineer","Timothy Qi"));
		candiateList.add(new CandiateBean("2012-10-12 09:00","Java Engineer","Jack Zhangsan"));
		candiateList.add(new CandiateBean("2012-10-12 10:00","Java Engineer","Tom"));
		candiateList.add(new CandiateBean("2012-10-13 08:00","Java Designer","LiLei"));
		candiateList.add(new CandiateBean("2012-10-13 09:00","Java Tester","Hanmeimei"));
		candiateList.add(new CandiateBean("2012-10-14 08:00","Java Engineer","Robin"));
		candiateList.add(new CandiateBean("2012-10-14 09:00","Java Engineer","Calvin"));
		candiateList.add(new CandiateBean("2012-10-15 08:00","Java Engineer","Charlie"));
		candiateList.add(new CandiateBean("2012-10-16 09:00","Java Tester","Grace"));
		candiateList.add(new CandiateBean("2012-10-16 09:00","Java Designer","Amy"));
		candiateList.add(new CandiateBean("2012-10-16 10:00","Java Tester","Michael"));
		candiateList.add(new CandiateBean("2013-10-16 09:00","Java Designer","Amy"));
		candiateList.add(new CandiateBean("2013-10-16 10:00","Java Tester","Michael"));
		candiateList.add(new CandiateBean("2013-10-16 09:00","Java Designer","Amy"));
		candiateList.add(new CandiateBean("2013-10-16 10:00","Java Tester","Michael"));
		candiateList.add(new CandiateBean("2013-10-16 09:00","Java Designer","Amy"));
		candiateList.add(new CandiateBean("2013-10-16 10:00","Java Tester","Michael"));	
		
		Collections.sort(candiateList,new Comparator<CandiateBean>(){  
            public int compare(CandiateBean arg0, CandiateBean arg1) {  
                return arg1.getTime().compareTo(arg0.getTime());  
            }  
        });
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
            if(candiateList!=null&&candiateList.size()>0){
            	adapter = new CandidateListAdapter(candiateList,mContext);
                setListAdapter(adapter); 
                
                // Call onRefreshComplete when the list has been refreshed.
                listView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                
            }else{
            	Toast.makeText(mContext, "Can not get any data!", Toast.LENGTH_LONG).show();
            }
        }
    }
	
	
	public void sortEvent(View view){
		switch(view.getId()){
			case(R.id.timeSortTable): sortByTime(); break;
			case(R.id.positionSortTable):sortByPosition();break;
			case(R.id.nameSortTable):sortByName();
		}
	
	    adapter = new CandidateListAdapter(candiateList,mContext);
	    listView.setAdapter(adapter);
	}
	
	public void sortByTime(){
		switch(timeSortFlag){
			case -1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg0.getTime().compareTo(arg1.getTime());  
		            }  
		        });	
				timeSortFlag = 1;	
				timeSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg1.getTime().compareTo(arg0.getTime());  
		            }  
		        });
				timeSortFlag = -1;
				timeSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		positionSortIcon.setImageDrawable(null);
		nameSortIcon.setImageDrawable(null);
	}
	
	public void sortByPosition(){
		switch(positionSortFlag){
			case -1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg0.getPosition().compareTo(arg1.getPosition());  
		            }  
		        });	
				positionSortFlag = 1;	
				positionSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg1.getPosition().compareTo(arg0.getPosition());  
		            }  
		        });
				positionSortFlag = -1;
				positionSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		timeSortIcon.setImageDrawable(null);
		nameSortIcon.setImageDrawable(null);
	}
	
	public void sortByName(){
		switch(nameSortFlag){
			case -1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg0.getName().compareTo(arg1.getName());  
		            }  
		        });	
				nameSortFlag = 1;	
				nameSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg1.getName().compareTo(arg0.getName());  
		            }  
		        });
				nameSortFlag = -1;
				nameSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		timeSortIcon.setImageDrawable(null);
		positionSortIcon.setImageDrawable(null);
	}

}
