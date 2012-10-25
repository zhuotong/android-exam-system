package com.dream.ivpc.chart;

import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class BaseChartView extends SurfaceView implements SurfaceHolder.Callback{
	private final static String LOG_TAG = "BaseChartView";
	
	public SurfaceHolder mHolder;
	public Integer screenSize;
	public List<Chart> chartList;
	public Integer page;
	
	public final static float BAR_RATIO = 0.8f;
	
	public BaseChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(LOG_TAG, "Bar2DVerView(Context context, AttributeSet attrs)...");
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	public void setScreenSize(Integer screenSize) {
		this.screenSize = screenSize;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	public Integer getPage() {
		return page;
	}
	
	public List<Chart> getChartList() {
		return chartList;
	}

	public void setChartList(List<Chart> chartList) {
		this.chartList = chartList;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		Log.i(LOG_TAG, "surfaceChanged()...");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(LOG_TAG, "surfaceCreated()...");
		drawChart(mHolder,chartList,page,screenSize);
		Log.i(LOG_TAG, "surfaceCreated() END.");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(LOG_TAG, "surfaceDestroyed()...");
	}
	
	public abstract void drawChart(SurfaceHolder sfh,List<Chart> chartList,Integer page,Integer screenSize);
}