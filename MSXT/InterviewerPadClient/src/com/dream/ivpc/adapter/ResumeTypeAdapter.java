package com.dream.ivpc.adapter;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.R;
import com.dream.ivpc.model.ResumeTypeBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResumeTypeAdapter extends BaseAdapter {
	Context mContext;
	List<ResumeTypeBean> resumeTypeList = new ArrayList<ResumeTypeBean>();
	LayoutInflater mInflater;
	
	public ResumeTypeAdapter(List<ResumeTypeBean> candiateList,Context mContext){
		this.resumeTypeList = candiateList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext); 
	}

	@Override
	public int getCount() {
		return resumeTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return resumeTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.resume_type_list_item, null);
			
			holder.index = (TextView)convertView.findViewById(R.id.index);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		ResumeTypeBean bean = resumeTypeList.get(position);
		
		holder.index.setText(String.valueOf(position+1));
		holder.name.setText(bean.getTypeName());
		
		return convertView;
	}
	

	static class ViewHolder{
		TextView index;
		TextView name;
	}

}
