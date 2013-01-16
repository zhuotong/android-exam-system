package com.dream.ivpc;

import org.viewflow.android.widget.CircleFlowIndicator;
import org.viewflow.android.widget.ViewFlow;
import com.dream.ivpc.adapter.ResumeGroupAdapter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CandidateResumeGroup extends CandidateInfoBase {

	private ViewFlow viewFlow;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTitle(R.string.circle_title);
		setContentView(R.layout.candidate_resume_group);
		
		mContext = getApplicationContext();

		setHeader((TextView)findViewById(R.id.candidateInfo));
		setFooter((Button) findViewById(R.id.resume));

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(new ResumeGroupAdapter(this), 5);
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
	}
	/* If your min SDK version is < 8 you need to trigger the onConfigurationChanged in ViewFlow manually, like this */	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
	}
}
