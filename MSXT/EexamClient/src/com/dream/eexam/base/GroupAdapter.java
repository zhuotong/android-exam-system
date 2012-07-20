package com.dream.eexam.base;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {
	private static final String TAG = "GroupAdapter";
	private Context context;
	private List<String> list;

	public GroupAdapter(Context context, List<String> list) {
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
			holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.groupItem.setText(list.get(position));
		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
	}

}
