package com.dream.ivpc;

import java.util.ArrayList;
import java.util.List;

import com.dream.ivpc.adapter.ResumeAdapter;
import com.dream.ivpc.model.ResumeBean;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class CandidateInfoResume extends CandidateInfoBase {
	public final static String LOG_TAG = "CandidateInfoResume";
	
	ImageView imgGoBack = null;
	Button resume;
	
	List<ResumeBean> resumeInfo = new ArrayList<ResumeBean>();
	ListView listView;
	ResumeAdapter adapter;	

	public List<ResumeBean> getResumeInfo(){
		List<ResumeBean> resumeInfo = new ArrayList<ResumeBean>();
		resumeInfo.add(new ResumeBean("Name", "Timothy"));
		resumeInfo.add(new ResumeBean("Gender", "Male"));
		resumeInfo.add(new ResumeBean("Age", "30"));
		resumeInfo.add(new ResumeBean("Education", "Bachelor"));
		return resumeInfo;
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_info_resume);
        mContext = getApplicationContext();
        
        imgGoBack = (ImageView) findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(goBackListener);
        
        resume = (Button) findViewById(R.id.resume);
        resume.setBackgroundResource(R.drawable.bg_footer_button_select);
        
        resumeInfo = getResumeInfo();
/*		Collections.sort(resumeInfo,new Comparator<ResumeBean>(){  
            public int compare(ResumeBean arg0, ResumeBean arg1) {  
                return Integer.valueOf(arg0.getLabel()).compareTo(Integer.valueOf(arg1.getLabel()));  
            }  
        });*/
        
        //set List
        listView = (ListView)findViewById(R.id.resume_info);
        adapter = new ResumeAdapter(resumeInfo,mContext);
        listView.setAdapter(adapter);

    }
  
}
