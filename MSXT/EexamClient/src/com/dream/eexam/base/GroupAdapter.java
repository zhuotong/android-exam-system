package com.dream.eexam.base;

import java.util.List;

import com.dream.eexam.model.CatalogInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {
	private static final String TAG = "GroupAdapter";
	private Context context;
	private List<CatalogInfo> list;
	
	public GroupAdapter(Context context, List<CatalogInfo> list) {
		Log.i(TAG, "GroupAdapter()...");
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Log.i(TAG, "getView()...");
		ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.group_list_item, null);
			holder=new ViewHolder();
			convertView.setTag(holder);
			
			holder.groupItem = (TextView) convertView.findViewById(R.id.groupItem);
			holder.groupSeekBar = (SeekBar) convertView.findViewById(R.id.groupSeekBar);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		CatalogInfo bean = list.get(position);
		
		String indexStr = String.valueOf(bean.getIndex());
		String desc = bean.getDesc();
		String queNumber = String.valueOf(bean.getQuestionNumber());
		Log.i(TAG, "indexStr="+indexStr+" desc="+desc+" queNumber="+queNumber);
		
		holder.groupItem.setText(indexStr+". " + desc +"("+queNumber+")");
		
		double percentage = bean.getComPercentage();
		Log.i(TAG, "percentage="+String.valueOf(percentage));
		
		holder.groupSeekBar.setThumb(null);
		holder.groupSeekBar.setVisibility(View.VISIBLE);
		holder.groupSeekBar.setMinimumHeight(5);
		
		holder.groupSeekBar.setProgress((int) (percentage*100) );
		holder.groupSeekBar.setEnabled(false);
		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
		SeekBar groupSeekBar;
	}

}
