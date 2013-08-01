package com.dream.ivpc.adapter;

import java.util.ArrayList;
import java.util.HashMap;
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
	Context mContext;
	List<CandiateBean> candiateList = new ArrayList<CandiateBean>();
	HashMap<Integer,View> map = new HashMap<Integer,View>(); 
	
	public CandidateListAdapter(List<CandiateBean> candiateList,Context mContext){
		this.candiateList = candiateList;
		this.mContext = mContext;
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
		View view;
		ViewHolder holder = null;
		
		if (map.get(position) == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mInflater.inflate(R.layout.candidate_list_item, null);
			holder = new ViewHolder();
			//set 3 component 
			holder.index = (TextView)view.findViewById(R.id.index);
			holder.time = (TextView)view.findViewById(R.id.time);
			holder.position = (TextView)view.findViewById(R.id.position);
			holder.name = (TextView)view.findViewById(R.id.name);
			
//			final int p = position;
			map.put(position, view);
			view.setTag(holder);
		}else{
			view = map.get(position);
			holder = (ViewHolder)view.getTag();
		}
		
		CandiateBean bean = candiateList.get(position);
		
		holder.index.setText(String.valueOf(position+1));
		holder.time.setText(bean.getTime());
		holder.position.setText(bean.getPosition());
		holder.name.setText(bean.getName());
		
		return view;
	}
	

	static class ViewHolder{
		TextView index;
		TextView time;
		TextView position;
		TextView name;
	}

}
