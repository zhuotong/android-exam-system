package com.dream.ivpc.activity;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.activity.report.ExamRptList;
import com.dream.ivpc.activity.resume.ResumeTypeList;
import com.dream.ivpc.adapter.CandidateDetailAdapter;
import com.dream.ivpc.model.DetailBean;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CandidateDetail2 extends BaseActivity {
	public final static String LOG_TAG = "CandidateDetail";
	
	ImageView imgGoBack = null;
	TextView canInfoTV;
	
	CandidateDetailAdapter adapter;
	List<DetailBean> detailList = new ArrayList<DetailBean>();
	
	Context mContext;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_detail);
        mContext = getApplicationContext();

        //set header
        imgGoBack = (ImageView) this.findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(goBackListener);
        canInfoTV = (TextView) this.findViewById(R.id.candidateInfo);
        canInfoTV.setText("Candidate Detail");
        
        //set candidate infor value
		Bundle bundle = this.getIntent().getExtras();
		String name  = bundle.getString("name");
		String position  = bundle.getString("position");
		String phase  = bundle.getString("phase");
		((TextView) this.findViewById(R.id.nameTV)).setText(name);
		((TextView) this.findViewById(R.id.positionTV)).setText(position);
		((TextView) this.findViewById(R.id.phaseTV)).setText(phase);
        
		//load GridView
        GridView gridview = (GridView) findViewById(R.id.gridview);
        String[] descs = this.getResources().getStringArray(R.array.candidate_detail_descs);
        int[] imgIds = new int[]{R.drawable.detail_btn1_selector,R.drawable.detail_btn2_selector,R.drawable.detail_btn3_selector,R.drawable.detail_btn4_selector};
        
        for(int i=0;i<descs.length;i++){
        	detailList.add(new DetailBean(descs[i],imgIds[i]));
        }
        
        adapter =  new CandidateDetailAdapter(detailList,mContext);
        gridview.setAdapter(adapter);
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
//			HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
//			setTitle((String)item.get("ItemText"));
//			
//			switch(arg2){
//				 case 0:checkResume();break;
//				 case 1:checkExamRpt();break;
//				 case 2:checkInterviewHistory();break;
//				 case 3:submitInterviewResult();break;
//			}
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
