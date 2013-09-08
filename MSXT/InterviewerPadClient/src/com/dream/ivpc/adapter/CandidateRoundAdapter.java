package com.dream.ivpc.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dream.ivpc.R;
import com.dream.ivpc.activity.exam.ExamRptList;
import com.dream.ivpc.bean.Round;

public class CandidateRoundAdapter extends BaseAdapter{
	List<Round> roundList = new ArrayList<Round>();
	Context mContext;
	LayoutInflater mInflater;
	
	public CandidateRoundAdapter(List<Round> detailList,Context mContext){
		this.roundList = detailList;
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return roundList.size();
	}

	@Override
	public Object getItem(int position) {
		return roundList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			
			convertView = mInflater.inflate(R.layout.candidate_detail_item, null);
			holder = new ViewHolder();
			//set 3 component 
			holder.detailIV = (ImageView)convertView.findViewById(R.id.detailImage);
			holder.detailTV = (TextView)convertView.findViewById(R.id.detailDesc);
			holder.dateTV = (TextView)convertView.findViewById(R.id.dateTV);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final int choosePostion = position;
		Round bean = roundList.get(position);
		
//		holder.detailIV.setBackgroundResource(bean.getImageId());

		
		if("EXAM".equalsIgnoreCase(bean.getType())){
			holder.detailIV.setBackgroundResource(R.drawable.detail_btn2_selector);
		}else{
			holder.detailIV.setBackgroundResource(R.drawable.detail_btn3_selector);
		}
		
		holder.detailTV.setText(bean.getName());
		if(bean.isCompFlag()){
			holder.dateTV.setText(bean.getDoneTime());
		}else{
			holder.dateTV.setText(bean.getPlanTime());
		}
		
//		holder.detailIV.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				switch (choosePostion) {
//					case 0:chooseExamRpt();break;
//					case 1:checkInterviewHistory();break;
//					case 2:submitInterviewResult();break;
//				}
//			}
//		});
		
		return convertView;
	}
	

	static class ViewHolder{
		ImageView detailIV;
		TextView detailTV;
		TextView dateTV;
	}
	
    public void chooseExamRpt(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ExamRptList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);  
    }

    public void openExamReport(){
    	
    }
    
    public void openInterviewResult(){
    	
    }
}
