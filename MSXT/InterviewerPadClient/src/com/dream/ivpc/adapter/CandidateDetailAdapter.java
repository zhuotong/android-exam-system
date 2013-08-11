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
import com.dream.ivpc.activity.report.ExamRptList;
import com.dream.ivpc.activity.resume.ResumeTypeList;
import com.dream.ivpc.model.DetailBean;

public class CandidateDetailAdapter extends BaseAdapter{
	List<DetailBean> detailList = new ArrayList<DetailBean>();
	Context mContext;
	LayoutInflater mInflater;
	
	public CandidateDetailAdapter(List<DetailBean> detailList,Context mContext){
		this.detailList = detailList;
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return detailList.size();
	}

	@Override
	public Object getItem(int position) {
		return detailList.get(position);
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
//			holder.detailTV = (TextView)convertView.findViewById(R.id.detailDesc);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final int choosePostion = position;
		DetailBean bean = detailList.get(position);
		
		holder.detailIV.setBackgroundResource(bean.getImageId());
		holder.detailIV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (choosePostion) {
					case 0:chooseResume();break;
					case 1:chooseExamRpt();break;
					case 2:checkInterviewHistory();break;
					case 3:submitInterviewResult();break;
				}
			}
		});
		
//		holder.detailTV.setText(bean.getName());
		return convertView;
	}
	

	static class ViewHolder{
		ImageView detailIV;
//		TextView detailTV;
	}
	
    public void chooseResume(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ResumeTypeList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);      	
    }
    
    public void chooseExamRpt(){
    	Intent intent = new Intent();
		intent.setClass( mContext, ExamRptList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);  
    }

    public void checkInterviewHistory(){
    	
    }
    
    public void submitInterviewResult(){
    	
    }
}
