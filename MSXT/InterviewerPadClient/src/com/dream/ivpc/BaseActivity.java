package com.dream.ivpc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class BaseActivity extends Activity {

	public final static String LOG_TAG = "BaseActivity";
	protected SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG,"onCreate()...");
		
		ActivityManage.add(this);
		
		String appName = this.getString(R.string.app_name);
		sharedPreferences = this.getSharedPreferences(appName,MODE_PRIVATE);
	}

	public void ShowDialog(String title,String msg) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
		}).show();
	}
	
	public String getWifiIP() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);// get wifi service
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = (ipAddress& 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF)+ "." + (ipAddress >> 24 & 0xFF);
		return ip;
	}
	
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
	    	if((System.currentTimeMillis()-exitTime) > 2000){
	    		Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.msg_exit_hint), Toast.LENGTH_SHORT).show();                                
	    		exitTime = System.currentTimeMillis();
	    	}else{
			    finish();
			    ActivityManage.finishProgram();
			    System.exit(0);
		    }
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
