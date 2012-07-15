package com.dream.eexam.base;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

	public final static String LOG_TAG = "LoginActivity";
	
	private EditText idEt = null;
	private EditText passwordET = null;
	private Button loginBtn = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		
		idEt = (EditText) this.findViewById(R.id.idEt);
		passwordET = (EditText) this.findViewById(R.id.passwordET);
		
		loginBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass( getBaseContext(), PapersActivity.class);
//				startActivity(intent);
				
				String idEtText = idEt.getText().toString();
				String passwordETText = passwordET.getText().toString();
				
				login(idEtText,passwordETText);
			}			
		});
    }

    /**
     * 
     * @param username
     * @param password
     */
    public void login(String username,String password){
		//要访问的HttpServlet
		String urlStr="http://192.168.1.103:8080/msxt/login.xhtml?";
		//要传递的数据
		String query = "username="+username+"&password="+password;
		urlStr+=query;
		try{
			URL url =new URL(urlStr);
			//获得连接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			// 设置连接超时时间
			conn.setConnectTimeout(5 * 1000);
			// 开始连接
			conn.connect();
			// 判断请求是否成功
			if (conn.getResponseCode() == 200) {
				// 获取返回的数据
//				byte[] data = readStream(conn.getInputStream());
//				Log.i(LOG_TAG, "Get方式请求成功，返回数据如下：");
//				Log.i(LOG_TAG, new String(data, "UTF-8"));
				
				ShowDialog("Success!");
			} else {
//				Log.i(LOG_TAG, "Get方式请求失败");
				ShowDialog("Fail!");
			}
			// 关闭连接
			conn.disconnect();
		}catch(Exception e){
			ShowDialog(e.getMessage());
		}
	}
    
    @Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


}