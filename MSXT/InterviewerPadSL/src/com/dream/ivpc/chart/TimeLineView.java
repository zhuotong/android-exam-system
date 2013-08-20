package com.dream.ivpc.chart;

import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class TimeLineView extends BaseChartView {

	Coordinate barC1;
	Coordinate barC2;
	
	public TimeLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawChart(SurfaceHolder sfh, List<Chart> chartList,
			Integer page, Integer screenSize) {
		//get canvas and paint
		Canvas canvas = sfh.lockCanvas();
		canvas.drawColor(Color.parseColor("#E8E8E8"));
		
		Paint mPaint = new Paint();
		
//		//draw bar List 
//		for(Chart chart:chartList){
//			Bar2D bar2D = (Bar2D)chart;
//			Coordinate startC = bar2D.getStartC();
//			Coordinate endC = bar2D.getEndC();
//			mPaint.setColor(Color.parseColor(bar2D.getBgColor()));
//			canvas.drawRect(startC.getCoordinateX(),startC.getCoordinateY(),endC.getCoordinateX(),endC.getCoordinateY(), mPaint);
//		}
//		
		mPaint.setColor(Color.parseColor("#00B4C6"));
		canvas.drawRect(barC1.getCoordinateX(),barC1.getCoordinateY(),barC2.getCoordinateX(),barC2.getCoordinateY(), mPaint);
		
		for(Chart chart: chartList){
			Circle circ = (Circle)chart;
			mPaint.setColor(Color.parseColor(circ.getBgColor()));
			canvas.drawCircle(circ.getCdx(), circ.getCdy(), circ.getRadius(), mPaint);
		}
		
		canvas.save();
		
		//finish
		canvas.restore();
		sfh.unlockCanvasAndPost(canvas);
	}


	public void setBarC1(Coordinate barC1) {
		this.barC1 = barC1;
	}

	public void setBarC2(Coordinate barC2) {
		this.barC2 = barC2;
	}
	
	



}
