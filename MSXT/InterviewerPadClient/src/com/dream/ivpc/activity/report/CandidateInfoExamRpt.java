package com.dream.ivpc.activity.report;

import java.util.ArrayList;
import java.util.List;

import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;
import com.dream.ivpc.chart.Bar2D;
import com.dream.ivpc.chart.Bar2DVerView;
import com.dream.ivpc.chart.Chart;
import com.dream.ivpc.chart.Coordinate;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class CandidateInfoExamRpt extends BaseActivity {
	
	public final static String LOG_TAG = "CandidateInfoExamRpt";
	
	ImageView imgGoBack = null;
	
	Bar2DVerView bar2DVerView;
	List<Chart> chartList;
//	Button examrpt;
	
	public void getChartData(){
		chartList = new ArrayList<Chart>();
		Coordinate c1 = new Coordinate(100,100);
		Coordinate c2 = new Coordinate(200,200);
		Bar2D bar1 = new Bar2D(c1,c2);
		bar1.setBgColor("#FF2200");
		chartList.add(bar1);
		
		Coordinate c3 = new Coordinate(300,100);
		Coordinate c4 = new Coordinate(400,300);
		Bar2D bar2 = new Bar2D(c3,c4);
		bar2.setBgColor("#FFAE00");
		chartList.add(bar2);
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_info_examrpt);
        
//        mContext = getApplicationContext();
        
//        setHeader((TextView)findViewById(R.id.candidateInfo),(ImageView)findViewById(R.id.imgGoBack));
//		setFooter((Button) findViewById(R.id.examrpt));
        
//        imgGoBack = (ImageView) findViewById(R.id.imgGoBack);
//        imgGoBack.setOnClickListener(goBackListener);
        
//        examrpt = (Button) findViewById(R.id.examrpt);
//        examrpt.setBackgroundResource(R.drawable.bg_footer_button_select);
        
        getChartData();
        
		//get bar2DVerView and set bar2DVerView
		bar2DVerView = (Bar2DVerView) this.findViewById(R.id.bar2DVerView);
		bar2DVerView.setSaveEnabled(false);
		bar2DVerView.setChartList(chartList);
    }

    @Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
