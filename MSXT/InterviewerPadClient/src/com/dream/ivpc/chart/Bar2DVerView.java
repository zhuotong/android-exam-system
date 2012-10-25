package com.dream.ivpc.chart;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class Bar2DVerView extends BaseChartView {

	public Bar2DVerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawChart(SurfaceHolder sfh, List<Chart> chartList,
			Integer page, Integer screenSize) {
		//get canvas and paint
		Canvas canvas = sfh.lockCanvas();
		Paint mPaint = new Paint();
		
		//draw bar List 
		for(Chart chart:chartList){
			Bar2D bar2D = (Bar2D)chart;
			Coordinate startC = bar2D.getStartC();
			Coordinate endC = bar2D.getEndC();
			
			mPaint.setColor(Color.parseColor(bar2D.getBgColor()));
			
			canvas.drawRect(startC.getCoordinateX(),startC.getCoordinateY(),endC.getCoordinateX(),endC.getCoordinateY(), mPaint);
		}
		
		canvas.save();
		
		//finish
		canvas.restore();
		sfh.unlockCanvasAndPost(canvas);
	}



}
