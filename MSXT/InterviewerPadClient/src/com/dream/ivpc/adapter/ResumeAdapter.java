package com.dream.ivpc.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dream.ivpc.R;
import com.dream.ivpc.model.ResumeBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResumeAdapter extends BaseAdapter {
	
	static class ViewHolder{
		TextView label;
		TextView value;
	}
	
	Context mContext;
	List<ResumeBean> resume = new ArrayList<ResumeBean>();
	HashMap<Integer,View> map = new HashMap<Integer,View>(); 
	
	public ResumeAdapter(List<ResumeBean> resume,Context mContext){
		this.resume = resume;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return resume.size();
	}

	@Override
	public Object getItem(int position) {
		return resume.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder = null;
		
		if (map.get(position) == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mInflater.inflate(R.layout.candidate_info_resume_item, null);
			holder = new ViewHolder();
			
			holder.label = (TextView)view.findViewById(R.id.label);
			holder.value = (TextView)view.findViewById(R.id.value);
			
			map.put(position, view);
			view.setTag(holder);
		}else{
			view = map.get(position);
			holder = (ViewHolder)view.getTag();
		}
		
		ResumeBean bean = resume.get(position);
		
		holder.label.setText(bean.getLabel());
		holder.value.setText(bean.getValue());
		
		
		return view;
	}
	



}
