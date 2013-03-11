package com.dream.eexam.paper;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dream.eexam.base.R;
import com.dream.eexam.server.DataParseUtil;
import com.msxt.client.model.QUESTION_TYPE;
import com.msxt.client.model.Examination.Question;

public class PendQuestions extends BaseQuestion {
	
	public final static String LOG_TAG = "PendQuestions";

	GridView gridList;
	
    String qType = null;
    String qid = null;
	
	//data statement
	PendQueListAdapter adapter;
	List<String> listItemID = new ArrayList<String>();
	Integer indexInExam;
	
	public void loadComponents(){
		//header components
		catalogsTV = (TextView)findViewById(R.id.header_tv_catalogs);
		
		//footer components
		backArrow = (ImageView)findViewById(R.id.backArrow);
		pendQueNumber = (TextView)findViewById(R.id.pendQueNumber);
		remainingTime = (TextView)findViewById(R.id.remainingTime);
		
		completedSeekBar = (SeekBar) findViewById(R.id.completedSeekBar);
		completedPercentage = (TextView)findViewById(R.id.completedPercentage);   	
    	
		submitTV = (TextView)findViewById(R.id.submitTV);
		nextArrow = (ImageView)findViewById(R.id.nextArrow);
    	
	}
	
	public void setHeader(){
        //set catalog bar(Center) 
		catalogsTV.setText(String.valueOf(cCatalogIndex)+". "+
				cCatalog.getDesc() + 
				"(Q" + String.valueOf(cQuestionIndex)+" - " + "Q" + String.valueOf(cQuestionIndex+queSumOfCCatalog-1)+")");
		catalogsTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_questions);
        mContext = getApplicationContext();
        loadComponents();
        setHeader();
        //set List
        gridList = (GridView)findViewById(R.id.gridview);
        adapter = new PendQueListAdapter(pendQuestions);
        gridList.setAdapter(adapter);
        gridList.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int arg2,
					long arg3) {

			}      	
        });
        setFooter();
    }
    
    public void setFooter(){
    	
    	backArrow.setVisibility(View.INVISIBLE);
		pendQueNumber.setVisibility(View.INVISIBLE);
		StringBuffer timeSB = new StringBuffer();
		if(lMinutes<10) timeSB.append("0");
		timeSB.append(String.valueOf(lMinutes));
		timeSB.append(String.valueOf(":"));
		if(lSeconds<10) timeSB.append("0");
		timeSB.append(String.valueOf(lSeconds));
		remainingTime.setText(timeSB.toString());
		
		//set completedSeekBar
		int per = 100 * examAnsweredQuestionSum/examQuestionSum;
		completedSeekBar.setThumb(null);
		completedSeekBar.setProgress(per);
		completedSeekBar.setEnabled(false);
		//set completedSeekBar label
		completedPercentage.setText(String.valueOf(per)+"%");
		
		submitTV.setVisibility(View.INVISIBLE);
		nextArrow.setVisibility(View.INVISIBLE);
    }
    
    class PendQueListAdapter extends BaseAdapter{
    	List<Question> questions = new ArrayList<Question>();
    	private LayoutInflater mInflater;
    	
    	public PendQueListAdapter(List<Question> questions){
    		this.questions = questions;
    		mInflater = LayoutInflater.from(mContext);  
    	}

		@Override
		public int getCount() {
			return questions.size();
		}

		@Override
		public Object getItem(int position) {
			return questions.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(LOG_TAG,"getView()..."+" position="+position);
        	
			ViewHolder holder;  
            if (convertView == null){  
                holder = new ViewHolder();  
                convertView = mInflater.inflate(R.layout.pending_questions_item, null);  
                holder.questionBtn = (Button) convertView.findViewById(R.id.questionBtn);  
                convertView.setTag(holder);  
            }else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            
            Question question = questions.get(position);
            qid = question.getId();
            
            holder.questionBtn.setText(String.valueOf(DataParseUtil.getQuestionExamIndex(exam, qid)));  
            holder.questionBtn.setOnClickListener(new Button.OnClickListener() {
    			public void onClick(View v) {
    				Log.i(LOG_TAG,"onClick()...");
    				
    				Button sButton = (Button)v;
    	            String indexInExam = sButton.getText().toString();
    	            Question nQuestion = DataParseUtil.getQuestionByIndexInExam(exam, Integer.valueOf(indexInExam));
    	            
    				//move question
    				Intent intent = new Intent();
    				intent.putExtra("ccIndex", String.valueOf(DataParseUtil.getCidByQid(exam, nQuestion.getId())));
    				intent.putExtra("cqIndex", String.valueOf(nQuestion.getIndex()));
    				
    				if(QUESTION_TYPE.MULTIPLE_CHOICE.equals(nQuestion.getType())){
    					intent.putExtra("questionType", questionTypes[0]);
    					intent.setClass( mContext, MultiChoices.class);
    				}else if(QUESTION_TYPE.SINGLE_CHOICE.equals(nQuestion.getType())){
    					intent.putExtra("questionType", questionTypes[1]);
    					intent.setClass( mContext, SingleChoices.class);
    				}else{
    					ShowDialog(mContext.getResources().getString(R.string.dialog_note),"Invalid qeustion type!");
    				}
    				finish();
    				startActivity(intent);
    			}
    		});
            return convertView; 
		}
    	
    }
    
    static class ViewHolder{
    	Button questionBtn;
    }
}
