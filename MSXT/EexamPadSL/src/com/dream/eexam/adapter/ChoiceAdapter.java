package com.dream.eexam.adapter;

import java.util.List;

import com.dream.eexam.base.R;
import com.dream.exam.bean.Choice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * dropdown list of catalogs
 * @author Timothy
 *
 */

public class ChoiceAdapter extends BaseAdapter {
	private static final String TAG = "GroupAdapter";
	private Context context;
	private List<Choice> choiceList;
	private int qustionType;
	private String answers;
	
	public void refresh(List<Choice> choiceList) {
		this.choiceList = choiceList;
		notifyDataSetChanged();
	}

	public ChoiceAdapter(Context context, List<Choice> list,int questionType,String answers) {
		Log.i(TAG, "GroupAdapter()...");
		this.context = context;
		this.choiceList = list;
		this.qustionType = questionType;
		this.answers = answers;
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
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		Log.i(TAG, "getView()...");
		ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.choice_list_item, null);
			
			holder=new ViewHolder();
			convertView.setTag(holder);
			
			holder.chooseIV = (ImageView) convertView.findViewById(R.id.chooseIV);
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.content = (TextView) convertView.findViewById(R.id.content);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		Choice choice = choiceList.get(position);
		
		holder.chooseIV.setBackgroundResource(choice.isSelect() ? R.drawable.checkboxon_64: R.drawable.checkboxoff_64);
		holder.label.setText(choice.getLabel());
		holder.content.setText(choice.getContent());
		
		return convertView;
	}

	static class ViewHolder {
		ImageView chooseIV;
		TextView label;
		TextView content;
	}

}
