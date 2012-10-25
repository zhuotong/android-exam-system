package com.dream.ivpc;

import java.util.ArrayList;
import java.util.List;

import com.dream.ivpc.chart.Bar2D;
import com.dream.ivpc.chart.Bar2DVerView;
import com.dream.ivpc.chart.Chart;
import com.dream.ivpc.chart.Coordinate;

import android.os.Bundle;
import android.util.Log;

public class CandidateInfoExamRpt extends CandidateInfoBase {
	public final static String LOG_TAG = "CandidateInfoExamRpt";
	
	Bar2DVerView bar2DVerView;
	List<Chart> chartList;
	
	public void getChartData(){
		chartList = new ArrayList<Chart>();
		Coordinate c1 = new Coordinate(100,100);
		Coordinate c2 = new Coordinate(200,200);
		chartList.add(new Bar2D(c1,c2));
		
		Coordinate c3 = new Coordinate(100,100);
		Coordinate c4 = new Coordinate(200,200);
		chartList.add(new Bar2D(c3,c4));
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_info_examrpt);
        
        getChartData();
        
		//get bar2DVerView and set bar2DVerView
		bar2DVerView = (Bar2DVerView) this.findViewById(R.id.bar2DVerView);
//		bar2DVerView.setLongClickable(true);
		bar2DVerView.setSaveEnabled(false);
		bar2DVerView.setChartList(chartList);
//		bar2DVerView.setPage(1);
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
