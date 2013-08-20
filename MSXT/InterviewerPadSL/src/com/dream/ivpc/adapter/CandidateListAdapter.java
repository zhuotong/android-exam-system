package com.dream.ivpc.adapter;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.R;
import com.dream.ivpc.model.CandiateBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CandidateListAdapter extends BaseAdapter {
	List<CandiateBean> candiateList = new ArrayList<CandiateBean>();
	Context mContext;
	LayoutInflater mInflater;
	
	public CandidateListAdapter(List<CandiateBean> candiateList,Context mContext){
		this.candiateList = candiateList;
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return candiateList.size();
	}

	@Override
	public Object getItem(int position) {
		return candiateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			
			convertView = mInflater.inflate(R.layout.candidate_list_item, null);
			holder = new ViewHolder();
			//set 3 component 
			holder.index = (TextView)convertView.findViewById(R.id.index);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			holder.position = (TextView)convertView.findViewById(R.id.position);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.phase = (TextView)convertView.findViewById(R.id.phase);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		CandiateBean bean = candiateList.get(position);
		
		holder.index.setText(String.valueOf(position+1));
		holder.time.setText(bean.getTime());
		holder.position.setText(bean.getPosition());
		holder.name.setText(bean.getName());
		holder.phase.setText(bean.getPhase());
		
		return convertView;
	}
	

	static class ViewHolder{
		TextView index;
		TextView time;
		TextView position;
		TextView name;
		TextView phase;
	}

}
