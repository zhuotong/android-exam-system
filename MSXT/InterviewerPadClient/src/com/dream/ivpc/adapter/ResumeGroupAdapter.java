package com.dream.ivpc.adapter;

import com.dream.ivpc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ResumeGroupAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private static final int[] ids = { R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
			R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.icecream };

	public ResumeGroupAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ids.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.candidate_resume_group_item, null);
		}
		((ImageView) convertView.findViewById(R.id.imgView)).setImageResource(ids[position]);
		return convertView;
	}
}
