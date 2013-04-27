package com.dream.eexam.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.dream.eexam.model.UserFolderBean;
import com.dream.eexam.util.FileUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingUserFolder extends SettingBase {
	public final static String LOG_TAG = "SettingUserFolder";
	
	
	public static String CRASH_FOLDER = "crash";
	
	Button deleteBtn = null;
	ListView listView;
	MyListAdapter adapter;
	
	List<String> ufSelectedList = new ArrayList<String>();
	String appHome;
	
	public List<UserFolderBean> loadufbList(){
		List<UserFolderBean> ufbList = new ArrayList<UserFolderBean>();
		appHome = Environment.getExternalStorageDirectory().getPath()+File.separator + getResources().getString(R.string.app_file_home);
		List<String> ufList = FileUtil.getFolderList(new File(appHome));
		int i=1;
		for(String uf:ufList){
			Log.i(LOG_TAG,"uf "+String.valueOf(i) +":"+uf);
			if(!CRASH_FOLDER.equalsIgnoreCase(uf)){
				ufbList.add(new UserFolderBean(i++,uf));
			}
		}
		return ufbList;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_user_folder);
        mContext = getApplicationContext();
        
        setHeader((ImageView)findViewById(R.id.imgHome));
//		setFooter((Button) findViewById(R.id.userfolder_setting));
		
        listView = (ListView)findViewById(R.id.user_folder_list);
        adapter = new MyListAdapter(loadufbList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {
        		CheckBox cb = (CheckBox)view.findViewById(R.id.list_select);
        		cb.setChecked(!cb.isChecked());
				//set answer
		    	//clear answer first
        		ufSelectedList.clear();
				setSeletedUserFolder();
			}      	
        });
        listView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});
        
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) { 
    			//save answer to local
    			AlertDialog.Builder builder = new AlertDialog.Builder(SettingUserFolder.this);
    			builder.setMessage(mContext.getResources().getString(R.string.msg_del_user_folder) +"\n" + getSeletedUserFolder())
    					.setCancelable(false)
    					.setPositiveButton(mContext.getResources().getString(R.string.warning_save_answer_local_yes),
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,int id) {
    									deleteSelectedUserFolder();
    									resetList();
    								}
    							})
    					.setNegativeButton(mContext.getResources().getString(R.string.warning_save_answer_local_cancel),
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,int id) {
    									dialog.cancel();
    								}
    							});
    			builder.show();
            }  
        });

    }
    
    public void setSeletedUserFolder(){
    	Log.i(LOG_TAG, "setSeletedUserFolder()...");
    	
    	List<Boolean> mCheckedList = adapter.mChecked;
    	List<UserFolderBean> ufList = adapter.ufbList;
		for (int i = 0; i < mCheckedList.size(); i++) {
			Log.i(LOG_TAG, "mChecked "+String.valueOf(i) + ":" + String.valueOf(mCheckedList.get(i)));
			if (mCheckedList.get(i)) {
				UserFolderBean bean = ufList.get(i);
				ufSelectedList.add(bean.getName());
			}
		}
    }
    
    public String getSeletedUserFolder(){
    	Log.i(LOG_TAG, "getSeletedUserFolder()...");
    	
    	StringBuilder sb = new StringBuilder();
    	int i=0;
    	for(String ufSelected: ufSelectedList){
    		if(i==0){
    			sb.append(ufSelected);
    		}else{
    			sb.append(","+ufSelected);
    		}
    	}
    	return sb.toString();
    }
    
    public void deleteSelectedUserFolder(){
    	Log.i(LOG_TAG, "deleteSelectedUserFolder()...");
    	for(String ufSelected: ufSelectedList){
    		String path = appHome+File.separator+ ufSelected;
    		File ufFile = new File(appHome+File.separator+ ufSelected);
    		if(ufFile.isDirectory()){
    			Log.i(LOG_TAG, path +" will be deleted...");
//    			ufFile.delete();
    			FileUtil.deleteDirectory(path);
    		}
    	}    	
    }
    
    public void resetList(){
        adapter = new MyListAdapter(loadufbList());
        listView.setAdapter(adapter);
    }
    
    class MyListAdapter extends BaseAdapter{
    	List<Boolean> mChecked = new ArrayList<Boolean>();
    	List<UserFolderBean> ufbList = new ArrayList<UserFolderBean>();
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
		
    	public MyListAdapter(List<UserFolderBean> ufList){
    		this.ufbList = ufList;
    		for(int i=0;i<ufList.size();i++){
    			mChecked.add(false);
    		}
    		
    	}

		@Override
		public int getCount() {
			return ufbList.size();
		}

		@Override
		public Object getItem(int position) {
			return ufbList.get(position);
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
				
				LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mInflater.inflate(R.layout.setting_user_folder_item, null);
				holder = new ViewHolder();
				
				//set 3 component 
				holder.selected = (CheckBox)view.findViewById(R.id.list_select);
				holder.index = (TextView)view.findViewById(R.id.list_index);
				holder.name = (TextView)view.findViewById(R.id.list_name);
				final int p = position;
				map.put(position, view);
				holder.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
						
						CheckBox cb = (CheckBox)buttonView;
						mChecked.set(p, cb.isChecked());
						
						Log.i(LOG_TAG,"cb " + String.valueOf(p)+" is checked, change to: " + String.valueOf(cb.isChecked()));
						
						ufSelectedList.clear();
						setSeletedUserFolder();
						
					}
				});
				view.setTag(holder);
			}else{
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			
			UserFolderBean bean = ufbList.get(position);
			
			holder.selected.setChecked(mChecked.get(position));
			holder.index.setText(String.valueOf(bean.getIndex()));
			holder.name.setText(bean.getName());
			
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	CheckBox selected;
    	TextView index;
    	TextView name;
    }
    

}
