package com.dream.eexam.base;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import com.dream.eexam.util.SPUtil;
import com.dream.eexam.util.ValidateUtil;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingServer extends SettingBase {
	public final static String LOG_TAG = "SettingServer";
	
	TextView valiMessageTV = null;
	String[] valiMessageArray = null;
	EditText hostET = null;
	EditText portET = null;
	
	Button saveHostBtn = null;
	Button testBtn = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(LOG_TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_server);
        mContext = getApplicationContext();
        
        setHeader((ImageView)findViewById(R.id.imgHome));
		setFooter((Button) findViewById(R.id.server_setting));
		
        valiMessageTV = (TextView) this.findViewById(R.id.valiMessage);
        valiMessageArray = getResources().getStringArray(R.array.msg_settings_invalid);
        
        //set host if exist
        hostET = (EditText) this.findViewById(R.id.hostET);
        String saveHost = sharedPreferences.getString(SPUtil.SP_KEY_HOST, null);
		hostET.setText(saveHost!=null?saveHost:"");

        //set host if exist
		portET = (EditText) this.findViewById(R.id.portET);
        String savePort = sharedPreferences.getString(SPUtil.SP_KEY_PORT, null);
        portET.setText(savePort!=null?savePort:"");
		
		saveHostBtn = (Button) this.findViewById(R.id.saveBtn);
		saveHostBtn.setOnClickListener(btnOnClickListener);

		testBtn = (Button) this.findViewById(R.id.testBtn);
		testBtn.setOnClickListener(btnOnClickListener);

    }
    
    View.OnClickListener btnOnClickListener = new View.OnClickListener() {  
        @Override  
        public void onClick(View v) {
        	switch(v.getId()){
        		case R.id.saveBtn:save();break;
        		case R.id.testBtn:testConnect();
        	}
        }  
    };
    
    private void save(){
    	String host = hostET.getText().toString();
    	String port = portET.getText().toString();
    	if(ValidateUtil.validateIP4(host)){
			SPUtil.save2SP(SPUtil.SP_KEY_HOST, host, sharedPreferences);
			SPUtil.save2SP(SPUtil.SP_KEY_PORT, port, sharedPreferences);
			Toast.makeText(mContext, "Host And Port is saved!", Toast.LENGTH_SHORT).show();
    	}else{
    		valiMessageTV.setVisibility(View.VISIBLE);
    		valiMessageTV.setText(valiMessageArray[0]);
    	}
    	goHome(mContext);
    }
    
    private void testConnect(){
    	if (getWifiIP() != null && getWifiIP().trim().length() > 0 && !getWifiIP().trim().equals("0.0.0.0")){
    		String host = hostET.getText().toString();
    		String port = portET.getText().toString();
    		new TestTask().execute(new String[]{host,port});
    	}else{
    		ShowDialog(mContext.getResources().getString(R.string.dialog_note),
    				mContext.getResources().getString(R.string.msg_network_error));
    	}
    }
    
    private class TestTask extends AsyncTask<String, Void, String> {
    	ProgressDialog progressDialog;
    	boolean isConnect = false;
    	
    	@Override
    	protected void onPreExecute() {
    		progressDialog = ProgressDialog.show(SettingServer.this, null,mContext.getResources().getString(R.string.msg_wait_test_connect), true, false); 
    	}
    	
		@Override
		protected String doInBackground(String... arg0) {
			InetAddress remoteAddr = null;
			int port = Integer.valueOf(arg0[1]);
			try {
				remoteAddr = InetAddress.getByName(arg0[0]);
				isConnect = isAddressAvailable(remoteAddr,port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		 @Override
	     protected void onPostExecute(String result) {
			 progressDialog.cancel();
			 if(isConnect){
				 ShowDialog(getResources().getString(R.string.dialog_note),"Connect Successfully!");
			 }else{
				 ShowDialog(getResources().getString(R.string.dialog_warning),"Connect Failed!");
			 }
		 }
    }
    
    boolean isAddressAvailable(InetAddress remoteAddr, int port) {
    	boolean isAvailable;
		String retIP = null;
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> localAddrs = ni.getInetAddresses();
				while (localAddrs.hasMoreElements()) {
					InetAddress localAddr = localAddrs.nextElement();
					if (isReachable(localAddr, remoteAddr, port, 5000)) {
						retIP = localAddr.getHostAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			Log.i(LOG_TAG,"Error occurred while listing all the local network addresses.");
		}
		if (retIP == null) {
			Log.i(LOG_TAG,"NULL reachable local IP is found!");
			isAvailable = false;
		} else {
			Log.i(LOG_TAG,"Reachable local IP is found, it is " + retIP);
			isAvailable = true;
		}
		return isAvailable;
	}

	boolean isReachable(InetAddress localInetAddr, InetAddress remoteInetAddr,int port, int timeout) {
		boolean isReachable = false;
		Socket socket = null;
		try {
			socket = new Socket();
			// 端口号设置为 0 表示在本地挑选一个可用端口进行连接
			SocketAddress localSocketAddr = new InetSocketAddress(localInetAddr, 0);
			socket.bind(localSocketAddr);
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(remoteInetAddr, port);
			socket.connect(endpointSocketAddr, timeout);
			Log.i(LOG_TAG,"SUCCESS - connection established! " +
					"Local: "+ localInetAddr.getHostAddress() + 
					" remote: "+ remoteInetAddr.getHostAddress() + " port" + port);
			isReachable = true;
		} catch (IOException e) {
			Log.i(LOG_TAG,"FAILRE - CAN not connect! Local: "
					+ localInetAddr.getHostAddress() + " remote: "
					+ remoteInetAddr.getHostAddress() + " port" + port);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					Log.i(LOG_TAG,"Error occurred while closing socket..");
				}
			}
		}
		return isReachable;
	}

}
