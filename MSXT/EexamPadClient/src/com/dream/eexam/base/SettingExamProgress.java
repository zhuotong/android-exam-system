package com.dream.eexam.base;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.dream.eexam.util.DatabaseUtil;

public class SettingExamProgress extends SettingBase {
	TextView dbData ;
	Button clearBtn;

	public String getDBData(Context mContext){
		DatabaseUtil dbUtil = new DatabaseUtil(mContext);
		dbUtil.open();
		StringBuffer sb = new StringBuffer();
    	Cursor cursor = dbUtil.fetchAllAnswers();
    	while(cursor.moveToNext()){
    		int cid = cursor.getInt(0);
    		int qid = cursor.getInt(1);
			String qidStr = cursor.getString(2);
			String answer = cursor.getString(3);
			sb.append(String.valueOf(cid)+";" + String.valueOf(qid)+";"+qidStr+";"+answer+"\n");
		}
    	cursor.close();
    	dbUtil.close();
    	return sb.toString();
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_exam_answer);
		mContext = getApplicationContext();

		setHeader((ImageView) findViewById(R.id.imgHome));
		setFooter((Button) findViewById(R.id.examprogress_setting));

		dbData = (TextView) this.findViewById(R.id.dbData);
		dbData.setText(getDBData(mContext));

		clearBtn = (Button) this.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(clearListener);
	}
	
    View.OnClickListener clearListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) { 
        	clearSP();
        	clearDB(mContext);
        	
        	ShowDialog(mContext.getResources().getString(R.string.dialog_note),
        			mContext.getResources().getString(R.string.msg_history_be_cleared));	
        }  
    }; 
	
 /*   class MyListAdapter extends BaseAdapter{
    	List<ExamProgress> epList = new ArrayList<ExamProgress>();
		HashMap<Integer,View> map = new HashMap<Integer,View>(); 
    	
    	public MyListAdapter(List<ExamProgress> epList){
    		this.epList = epList;
    	}

		@Override
		public int getCount() {
			return epList.size();
		}

		@Override
		public Object getItem(int position) {
			return epList.get(position);
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
				holder.seq = (TextView)view.findViewById(R.id.list_select);
				holder.cId = (TextView)view.findViewById(R.id.list_index);
				holder.qId = (TextView)view.findViewById(R.id.list_choiceDesc);
				holder.qIdStr = (TextView)view.findViewById(R.id.list_choiceDesc);
				holder.answer = (TextView)view.findViewById(R.id.list_choiceDesc);
				final int p = position;
				map.put(position, view);
				view.setTag(holder);
			}else{
				view = map.get(position);
				holder = (ViewHolder)view.getTag();
			}
			ExamProgress ep = epList.get(position);
			holder.seq.setText(ep.getSeq());
			holder.cId.setText(ep.getcId());
			holder.qId.setText(ep.getqId());
			holder.qIdStr.setText(ep.getqIdStr());
			holder.answer.setText(ep.getAnswer());
			return view;
		}
    	
    }
    
    static class ViewHolder{
    	TextView seq;
    	TextView cId;
    	TextView qId;
    	TextView qIdStr;
    	TextView answer;
    }*/
	

	
	
}
