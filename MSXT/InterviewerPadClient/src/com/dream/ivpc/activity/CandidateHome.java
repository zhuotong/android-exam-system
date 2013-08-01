package com.dream.ivpc.activity;

import com.dream.ivpc.R;
import com.dream.ivpc.activity.report.CandidateInfoExamDetail;
import com.dream.ivpc.activity.report.CandidateInfoExamRpt;
import com.dream.ivpc.activity.resume.ResumePdf;
import com.dream.ivpc.activity.resume.ResumePicture;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class CandidateHome extends TabActivity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main);
		setTabs();
	}

	private void setTabs() {
		addTab("Resume Picture", R.drawable.ic_compose, ResumePicture.class);
		addTab("Resume Pdf", R.drawable.ic_compose, ResumePdf.class);
		addTab("Report", R.drawable.ic_compose, CandidateInfoExamRpt.class);
		addTab("Exam", R.drawable.ic_compose, CandidateInfoExamDetail.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}
