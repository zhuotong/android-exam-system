package com.dream.ivpc;

import java.io.IOException;
import com.dream.ivpc.util.ImageUtil;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

	public class CandidateResumePNG extends CandidateInfoBase {
//		private final static String TAG = "CandidateResumePNG";
	    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_test/";
	    private ImageView imageView;
	    private ProgressDialog myDialog = null;
	    private Bitmap bitmap;
	    private String filePath = "http://ww2.sinaimg.cn/large/8161d11bjw1dym8g5uzmdj.jpg";
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
            myDialog = ProgressDialog.show(CandidateResumePNG.this, "Download File...", "Please Wait!", true);
            new Thread(saveFileRunnable).start();
	    }

	    private Runnable saveFileRunnable = new Runnable(){
	        @Override
	        public void run() {
	            try {
		        	byte[] data = ImageUtil.getImage(filePath);      
		            if(data!=null)      
		            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap      
		            ImageUtil.saveFile(bitmap,ALBUM_PATH, fileName);
	                message = "success";
	            } catch (IOException e) {
	                message = "fail";
	                e.printStackTrace();
	            } catch (Exception e) {
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
	            Toast.makeText(CandidateResumePNG.this, message, Toast.LENGTH_SHORT).show();
	        }
	    };
	}
