package com.dream.ivpc.adapter;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.R;
import com.dream.ivpc.bean.PendRoundBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CandidateListAdapter extends BaseAdapter {
	List<PendRoundBean> roundList = new ArrayList<PendRoundBean>();
	
	Context mContext;
	LayoutInflater mInflater;
	
	public CandidateListAdapter(List<PendRoundBean> candiateList,Context mContext){
		this.roundList = candiateList;
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
		
		PendRoundBean bean = roundList.get(position);
		
		holder.index.setText(String.valueOf(position+1));
		holder.time.setText(bean.getrTime());
		holder.position.setText(bean.getrAppPosition());
		holder.name.setText(bean.getrCandidate());
		holder.phase.setText(bean.getrName());
		
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
