package com.dream.ivpc;

import java.io.FileInputStream;
import java.util.List;

import com.dream.ivpc.model.ResumePicBean;
import com.dream.ivpc.util.DataParseUtil;
import com.dream.ivpc.util.FileUtil;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CandidateResumeXML extends CandidateInfoBase {
		private final static String LOG_TAG = "CandidateResumeXML";
	    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/interviewer/";
	    private final static String ALBUM_NAME = "sample_resume.xml";
	    
	    private ImageView imageView;
	    private ProgressDialog myDialog = null;
	    private Bitmap bitmap;
//	    private String filePath = "http://ww2.sinaimg.cn/large/8161d11bjw1dym8g5uzmdj.jpg";
	    private String fileName = "test.jpg";
	    private String message;
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.candidate_info_resume);
	        mContext = getApplicationContext();
	        //set header
	        setHeader((TextView)findViewById(R.id.candidateInfo));
	        
	        
	        imageView = (ImageView)findViewById(R.id.imgSource);
            myDialog = ProgressDialog.show(CandidateResumeXML.this, "Download File...", "Please Wait!", true);
            new Thread(saveFileRunnable).start();
	    }

	    private Runnable saveFileRunnable = new Runnable(){
	        @Override
	        public void run() {
	            try {
//		        	byte[] data = ImageUtil.getImage(filePath);      
//		            if(data!=null)      
//		            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap      
//		            ImageUtil.saveFile(bitmap,ALBUM_PATH, fileName);
		            
		            FileInputStream inputStream =  FileUtil.getFileInputStream(ALBUM_PATH, ALBUM_NAME);
		            List<ResumePicBean> resumeList = DataParseUtil.parseResume(inputStream);
		            StringBuffer encodedContent = null;
		            for(ResumePicBean bean: resumeList){
		            	Log.i(LOG_TAG,"Resume Page "+String.valueOf(bean.getIndex()));
		            	if(bean.getIndex() == 1){
		            		encodedContent = bean.getContent();
		            	}
		            }
		            byte[] decodedString = Base64.decode(encodedContent.toString(), Base64.DEFAULT);
		            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		            imageView.setImageBitmap(decodedByte);
	                message = "success";
	            }  catch (Exception e) {
	            	message = "fail";
					e.printStackTrace();
				}
	            messageHandler.sendMessage(messageHandler.obtainMessage());
	        }
	            
	    };
	    
	    //set picture
	    private Handler messageHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            myDialog.dismiss();
	            imageView.setImageBitmap(bitmap);
	            Toast.makeText(CandidateResumeXML.this, message, Toast.LENGTH_SHORT).show();
	        }
	    };
	}
