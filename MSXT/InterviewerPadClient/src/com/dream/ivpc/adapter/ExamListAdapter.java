package com.dream.ivpc.adapter;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.R;
import com.dream.ivpc.model.ExamBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExamListAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater mInflater;
	List<ExamBean> examList = new ArrayList<ExamBean>();
	
	public ExamListAdapter(List<ExamBean> choiceList,Context mContext){
		this.examList = choiceList;
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return examList.size();
	}

	@Override
	public Object getItem(int position) {
		return examList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.exam_rpt_list_item, null);
			holder = new ViewHolder();
			
			holder.index = (TextView)convertView.findViewById(R.id.index);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		ExamBean bean = examList.get(position);
		
		holder.index.setText(String.valueOf(position+1));
		holder.name.setText(bean.getExamName());
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView index;
		TextView name;
	}
	



}
