package com.dream.ivpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dream.ivpc.model.ChoiceBean;
import com.dream.ivpc.model.QuestionBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoiceListAdapter extends BaseAdapter {
	Context mContext;
	List<ChoiceBean> choiceList = new ArrayList<ChoiceBean>();
	HashMap<Integer,View> map = new HashMap<Integer,View>(); 
	
	public ChoiceListAdapter(List<ChoiceBean> choiceList,Context mContext){
		this.choiceList = choiceList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return choiceList.size();
	}

	@Override
	public Object getItem(int position) {
		return choiceList.get(position);
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
			view = mInflater.inflate(R.layout.candidate_info_examdetail_item, null);
			holder = new ViewHolder();

			//set 3 component 
			holder.index = (TextView)view.findViewById(R.id.index);
			holder.catalog = (TextView)view.findViewById(R.id.catalog);
			
			map.put(position, view);
			view.setTag(holder);
		}else{
			view = map.get(position);
			holder = (ViewHolder)view.getTag();
		}
		
		ChoiceBean bean = choiceList.get(position);
		
		holder.label.setText(bean.getLabel());
		holder.content.setText(bean.getContent());
		
		return view;
	}
	
	static class ViewHolder{
		TextView label;
		TextView content;
	}
	



}
