package com.dream.eexam.adapter;

import java.util.List;

import com.dream.eexam.base.R;
import com.dream.eexam.model.CatalogInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * dropdown list of catalogs
 * @author Timothy
 *
 */

public class CatalogAdapter extends BaseAdapter {
	private static final String TAG = "GroupAdapter";
	private Context context;
	private List<CatalogInfo> list;
	
	public CatalogAdapter(Context context, List<CatalogInfo> list) {
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
			convertView=LayoutInflater.from(context).inflate(R.layout.catalog_info_list_item, null);
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
		Integer qQuestionIndex = bean.getfQuestionIndex();
		Integer totalQuestion = bean.getTotalQuesions();
		Integer lQuestionIndex = qQuestionIndex + totalQuestion - 1;
		
		Log.i(TAG, "indexStr="+indexStr+" desc="+desc+" qQuestionIndex="+ 
				String.valueOf(qQuestionIndex)+" lQuestionIndex="+String.valueOf(lQuestionIndex));
		
		holder.groupItem.setText(indexStr+". " + desc +"(Q"+String.valueOf(qQuestionIndex)+" - Q"+ String.valueOf(lQuestionIndex)+ ")");
		
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
