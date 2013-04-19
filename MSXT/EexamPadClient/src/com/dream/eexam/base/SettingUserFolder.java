package com.dream.eexam.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.dream.eexam.model.UserFolderBean;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingUserFolder extends SettingBase {
	
	Button clearBtn = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_user_folder);
        mContext = getApplicationContext();
        
        setHeader((ImageView)findViewById(R.id.imgHome));
		setFooter((Button) findViewById(R.id.userfolder_setting));

    }
    
    /*class MyListAdapter extends BaseAdapter{
    	List<Boolean> mChecked = new ArrayList<Boolean>();
    	List<UserFolderBean> ufList = new ArrayList<UserFolderBean>();
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<UserFolderBean> ufList){
    		this.ufList = ufList;
    		for(int i=0;i<ufList.size();i++){
    			mChecked.add(false);
    		}
    		
    	}

		@Override
		public int getCount() {
			return ufList.size();
		}

		@Override
		public Object getItem(int position) {
			return ufList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder = null;
			
			if (map.get(position) == null) {
				Log.i(LOG_TAG,"position1 = "+position);
				
				LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mInflater.inflate(R.layout.paper_multi_choices_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
				holder.selected = (CheckBox)view.findViewById(R.id.list_select);
				holder.index = (TextView)view.findViewById(R.id.list_index);
				holder.name = (TextView)view.findViewById(R.id.list_choiceDesc);
				
				final int p = position;
				map.put(position, view);
				
				holder.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						CheckBox cb = (CheckBox)buttonView;
						mChecked.set(p, cb.isChecked());
					}
				});
				view.setTag(holder);
			}else{
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			
			UserFolderBean bean = ufList.get(position);
			
			holder.selected.setChecked(mChecked.get(position));
			holder.index.setText(bean.getIndex());
			holder.name.setText(bean.getName());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	CheckBox selected;
    	TextView index;
    	TextView name;
    }*/
    

}
