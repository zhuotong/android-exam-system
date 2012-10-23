package com.dream.ivpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	ImageView timeSortIcon = null;
	ImageView positionSortIcon = null;
	ImageView nameSortIcon = null;
	
	int timeSortFlag = -1;
	int positionSortFlag = -1;
	int nameSortFlag = -1;
	
	protected Context mContext;
	protected ListView listView;
	CandidateListAdapter adapter;
	List<CandiateBean> candiateList;
	
	public List<CandiateBean> getCandiateList(){
		List<CandiateBean> candiateList = new ArrayList<CandiateBean>();
		
		candiateList.add(new CandiateBean("2012-10-12 08:00","Java Engineer","Timothy"));
		candiateList.add(new CandiateBean("2012-10-12 09:00","Java Engineer","Jack"));
		candiateList.add(new CandiateBean("2012-10-12 10:00","Java Engineer","Tom"));
		
		candiateList.add(new CandiateBean("2012-10-13 08:00","Java Designer","LiLei"));
		candiateList.add(new CandiateBean("2012-10-13 09:00","Java Tester","Hanmeimei"));

		candiateList.add(new CandiateBean("2012-10-14 08:00","Java Engineer","Robin"));
		candiateList.add(new CandiateBean("2012-10-14 09:00","Java Engineer","Calvin"));
		
		candiateList.add(new CandiateBean("2012-10-15 08:00","Java Engineer","Charlie"));
		candiateList.add(new CandiateBean("2012-10-16 09:00","Java Tester","Grace"));
		
		candiateList.add(new CandiateBean("2012-10-16 09:00","Java Designer","Amy"));
		candiateList.add(new CandiateBean("2012-10-16 10:00","Java Tester","Michael"));
		return candiateList;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_list);
        mContext = getApplicationContext();

		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(goHomeListener);
		
		timeSortIcon = (ImageView) findViewById(R.id.timeSortIcon);
		positionSortIcon = (ImageView) findViewById(R.id.positionSortIcon);
		nameSortIcon = (ImageView) findViewById(R.id.nameSortIcon);
		
		timeSortIcon.setOnClickListener(sortListener);
		positionSortIcon.setOnClickListener(sortListener);
		nameSortIcon.setOnClickListener(sortListener);
		
        //getCandiateList
        candiateList = getCandiateList();
		Collections.sort(candiateList,new Comparator<CandiateBean>(){  
            public int compare(CandiateBean arg0, CandiateBean arg1) {  
                return arg1.getTime().compareTo(arg0.getTime());  
            }  
        });
		timeSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
        
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
	
    View.OnClickListener sortListener = new View.OnClickListener(){
		@Override
		public void onClick(View v) {
//			ImageView view = (ImageView)v;
			switch(v.getId()){
				case(R.id.timeSortIcon): sortByTime(); break;
				case(R.id.positionSortIcon):sortByPosition();break;
				case(R.id.nameSortIcon):sortByName();
			}
			
	        adapter = new CandidateListAdapter(candiateList,mContext);
	        listView.setAdapter(adapter);
			
		}
	};
	
	public void sortByTime(){
		switch(timeSortFlag){
			case -1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg0.getTime().compareTo(arg1.getTime());  
		            }  
		        });	
				timeSortFlag = 1;	
				timeSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg1.getTime().compareTo(arg0.getTime());  
		            }  
		        });
				timeSortFlag = -1;
				timeSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
		
		positionSortIcon.setImageDrawable(null);
		nameSortIcon.setImageDrawable(null);
	}
	
	public void sortByPosition(){
		switch(positionSortFlag){
			case -1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg0.getPosition().compareTo(arg1.getPosition());  
		            }  
		        });	
				positionSortFlag = 1;	
				positionSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg1.getPosition().compareTo(arg0.getPosition());  
		            }  
		        });
				positionSortFlag = -1;
				positionSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
	}
	
	public void sortByName(){
		switch(nameSortFlag){
			case -1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg0.getName().compareTo(arg1.getName());  
		            }  
		        });	
				nameSortFlag = 1;	
				nameSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.up_32));
				break;
			case 1:
				Collections.sort(candiateList,new Comparator<CandiateBean>(){  
		            public int compare(CandiateBean arg0, CandiateBean arg1) {  
		                return arg1.getName().compareTo(arg0.getName());  
		            }  
		        });
				nameSortFlag = -1;
				nameSortIcon.setImageDrawable(getResources().getDrawable(R.drawable.down_32));
		}
	}

}
