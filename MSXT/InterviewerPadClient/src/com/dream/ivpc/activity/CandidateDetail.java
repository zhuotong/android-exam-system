package com.dream.ivpc.activity;

import java.util.ArrayList;
import java.util.HashMap;
import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.activity.report.ExamRptList;
import com.dream.ivpc.activity.report.ExamRptPicture;
import com.dream.ivpc.activity.resume.ResumeTypeList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
        setContentView(R.layout.candidate_detail);
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
        
		//load GridView
        GridView gridview = (GridView) findViewById(R.id.gridview);
        String[] descs = this.getResources().getStringArray(R.array.candidate_detail_descs);
        
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<descs.length;i++){
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("ItemImage", R.drawable.resume2);
			map.put("ItemText", descs[i]);
        	lstImageItem.add(map);
        }
        
		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem,
				R.layout.candidate_detail_item, new String[] { "ItemImage","ItemText" },
				new int[] { R.id.detailImage, R.id.detailDesc });
        gridview.setAdapter(saImageItems);
        gridview.setOnItemClickListener(new ItemClickListener());
    }

    View.OnClickListener goBackListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	Intent intent = new Intent();
			intent.setClass( mContext, CandidateList.class);
			startActivity(intent);  
        }  
    };
    
    class  ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			setTitle((String)item.get("ItemText"));
			
			switch(arg2){
				 case 0:checkResume();break;
				 case 1:checkExamRpt();break;
				 case 2:checkInterviewHistory();break;
				 case 3:submitInterviewResult();break;
			}
		}
    }

    public void checkResume(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ResumeTypeList.class);
		startActivity(intent);      	
    }
    
    public void checkExamRpt(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ExamRptList.class);
		startActivity(intent);  
    }

    public void checkInterviewHistory(){
    	
    }
    
    public void submitInterviewResult(){
    	
    }
    
    
}
