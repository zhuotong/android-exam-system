package com.dream.eexam.base;

import java.util.ArrayList;
import java.util.List;

import com.dream.eexam.model.Paper;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class PapersActivity extends BaseActivity{
	private static final String LOG_TAG = "PapersActivity";
	
	MyAdapter myAdapter;
	private Context mContext;  
    private GridView mGridView; 
    private List<Paper> papers = new ArrayList<Paper>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papers);
        
        mContext = this;
        
        for(int i=1;i<10;i++){
        	papers.add(new Paper(i));
        }
        
		mGridView = (GridView) findViewById(R.id.gridview);
		myAdapter = new MyAdapter();
		mGridView.setAdapter(myAdapter);  
		mGridView.setOnItemClickListener(new ItemClickListener());
    }
    
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the // click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3){// The row id of the item that was clicked
			Log.i(LOG_TAG,"onItemClick");
			Log.i(LOG_TAG,"arg2="+arg2);
			Log.i(LOG_TAG,"arg3="+arg3);
			
		}
	}
    
    //define MayAdapter
    class MyAdapter extends BaseAdapter{  
        private LayoutInflater mInflater;  
          
        public MyAdapter(){  
            mInflater = LayoutInflater.from(mContext);  
        }  
  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return papers.size();  
        }  
  
        @Override  
        public Object getItem(int arg0) {  
            // TODO Auto-generated method stub  
            return papers.get(arg0);  
        }  
  
        @Override  
        public long getItemId(int arg0) {  
            // TODO Auto-generated method stub  
            return arg0;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
        	Log.i(LOG_TAG,"getView()..."+" position="+position);
        	
        	ViewHold holder;  
            if (convertView == null){  
                holder = new ViewHold();  
                convertView = mInflater.inflate(R.layout.papers_item, null);  
                holder.paperBtn = (Button) convertView.findViewById(R.id.paperBtn); 
                holder.paperTV = (TextView) convertView.findViewById(R.id.paperTV);  
                convertView.setTag(holder);  
            }else {  
                holder = (ViewHold) convertView.getTag();  
            }  
            
//            holder.paperBtn.setText("Paper "+position);  
            holder.paperTV.setText("Paper "+position);
            
            final int p = position;
            holder.paperBtn.setOnClickListener(new Button.OnClickListener() {
    			public void onClick(View v) {
    				sendAnswertoServer(p);
    			}
    		});
            
            return convertView;  
        }  
          
    }  
      
    class ViewHold{  
    	Button paperBtn;  
    	TextView paperTV; 
    } 

    public void sendAnswertoServer(Integer answerIndex){

    }
}
