package com.dream.ivpc;

import java.util.ArrayList;
import java.util.List;
import com.dream.ivpc.model.CandiateBean;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CandidateList extends BaseActivity {
	
	ImageView imgHome = null;
	
	protected Context mContext;
	protected ListView listView;
	CandidateListAdapter adapter;

	public List<CandiateBean> getCandiateList(){
		List<CandiateBean> candiateList = new ArrayList<CandiateBean>();
		
		candiateList.add(new CandiateBean("2012-10-12 09:00","Timothy","Java Engineer"));
		candiateList.add(new CandiateBean("2012-10-12 09:00","Jack","Java Engineer"));
		candiateList.add(new CandiateBean("2012-10-12 09:00","Tom","Java Engineer"));
		
		candiateList.add(new CandiateBean("2012-10-13 09:00","LiLei","Java Designer"));
		candiateList.add(new CandiateBean("2012-10-13 09:00","Hanmeimei","Java Tester"));
		
		return candiateList;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_list);
        mContext = getApplicationContext();

		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(goHomeListener);
		
        //getCandiateList
        List<CandiateBean> candiateList = getCandiateList();
        
        //set List
        listView = (ListView)findViewById(R.id.candidate_list);
        adapter = new CandidateListAdapter(candiateList,mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
            	Intent intent = new Intent();
    			intent.setClass( mContext, CandidateInfoResume.class);
    			startActivity(intent);  
			}      	
        });

        
    }
    
    View.OnClickListener goHomeListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			goHome(mContext);
		}
	};
}
