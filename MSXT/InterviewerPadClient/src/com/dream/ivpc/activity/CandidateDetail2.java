package com.dream.ivpc.activity;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.activity.report.ExamRptList;
import com.dream.ivpc.activity.resume.ResumeTypeList;
import com.dream.ivpc.activity.resume.ResumeWebView;
import com.dream.ivpc.adapter.CandidateDetailAdapter;
import com.dream.ivpc.chart.Chart;
import com.dream.ivpc.chart.Circle;
import com.dream.ivpc.chart.Coordinate;
import com.dream.ivpc.chart.TimeLineView;
import com.dream.ivpc.model.DetailBean;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CandidateDetail2 extends BaseActivity {
	public final static String LOG_TAG = "CandidateDetail";
	
	ImageView imgGoBack = null;
	TextView canInfoTV;
	CandidateDetailAdapter adapter;
	List<DetailBean> detailList = new ArrayList<DetailBean>();
	Context mContext;
	
	public void loadCandidateDetail(){
        //set candidate infor value
		Bundle bundle = this.getIntent().getExtras();
		String name  = bundle.getString("name");
		String position  = bundle.getString("position");
		String phase  = bundle.getString("phase");
		((TextView) this.findViewById(R.id.nameTV)).setText(name);
		((TextView) this.findViewById(R.id.positionTV)).setText(position);
		((TextView) this.findViewById(R.id.phaseTV)).setText(phase);	
		
		ImageView imgResume = (ImageView) this.findViewById(R.id.imgResume);
		imgResume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		    	Intent intent = new Intent();
				intent.setClass( mContext, ResumeTypeList.class);
				startActivity(intent);  
			}
		});
	}
	
//	TimeLineView timelineView;
//	List<Chart> chartList;
//	Coordinate c1;
//	Coordinate c2;
//	public void loadTimeLineData(){
//		chartList = new ArrayList<Chart>();
//		c1 = new Coordinate(75,0);
//		c2 = new Coordinate(85,750);
//		Circle circle1 = new Circle("#00C66E",20,80,100);
//		Circle circle2 = new Circle("#00C66E",20,80,300);
//		Circle circle3 = new Circle("#FFE255",20,80,500);
//		chartList.add(circle1);
//		chartList.add(circle2);
//		chartList.add(circle3);
//	}
	
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
        
        //set candidate detail
        loadCandidateDetail();
		
		//get bar2DVerView and set bar2DVerView
//		loadTimeLineData();
//		timelineView = (TimeLineView) this.findViewById(R.id.timelineView);
//		timelineView.setSaveEnabled(false);
//		timelineView.setBarC1(c1);
//		timelineView.setBarC2(c2);
//		timelineView.setChartList(chartList);
        
		//load GridView
//        GridView gridview = (GridView) findViewById(R.id.gridview);
        
        ListView listview = (ListView) findViewById(R.id.listview);
        String[] descs = this.getResources().getStringArray(R.array.candidate_detail_descs);
        int[] imgIds = new int[]{R.drawable.detail_btn2_selector,R.drawable.detail_btn3_selector,R.drawable.detail_btn4_selector};
        for(int i=0;i<descs.length;i++){
        	detailList.add(new DetailBean(descs[i],imgIds[i]));
        }
        
        adapter =  new CandidateDetailAdapter(detailList,mContext);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new ItemClickListener());
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
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			switch(arg2){
				 case 0:checkExamRpt();break;
				 case 1:checkInterviewHistory();break;
				 case 2:submitInterviewResult();break;
			}
		}
    }
    
    public void checkExamRpt(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ExamRptList.class);
		startActivity(intent);  
    }

    public void checkInterviewHistory(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ResumeWebView.class);
		startActivity(intent); 
    }
    
    public void submitInterviewResult(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ResumeWebView.class);
		startActivity(intent); 
    }
    
    
}
