package com.dream.eexam.crash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dream.eexam.util.ActivityStackControlUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author user
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	public static final String TAG = "CrashHandler";
	
	//系统默认的UncaughtException处理类 
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	//CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	
	//程序的Context对象	
	private Context mContext;
	
	//用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	//用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			//退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
			
			ActivityStackControlUtil.finishProgram();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "Sorry,please restart the application!", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		
		//收集设备参数信息 
		collectDeviceInfo(mContext);
		
		//保存日志文件 
//		Log.e(TAG,"SDCard state:"+ getSDCardState());
		
		//获取信息
		String crashInfoString =  getCrashInfo(ex);
		
		//print error message
		Log.e(TAG, crashInfoString);
		
		//保存到手机
		String fileName = saveCrashInfo2File(crashInfoString);
		if(fileName!=null) Log.e(TAG, "Log file name : "+fileName);
		
		//发送到邮箱
//		sendCrashInfo2Mail(crashInfoString);
//		doSendLogFile(fileName);
		return true;
	}
	
	/*public String getSDCardState(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_BAD_REMOVAL.equals(state)){
			return "MEDIA_BAD_REMOVAL";
		}else if(Environment.MEDIA_CHECKING.equals(state)){
			return "MEDIA_CHECKING";
		}else if(Environment.MEDIA_MOUNTED.equals(state)){
			return "MEDIA_MOUNTED";
		}else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			return "MEDIA_MOUNTED_READ_ONLY";
		}else if(Environment.MEDIA_NOFS.equals(state)){
			return "MEDIA_NOFS";
		}else if(Environment.MEDIA_REMOVED.equals(state)){
			return "MEDIA_REMOVED";
		}else if(Environment.MEDIA_SHARED.equals(state)){
			return "MEDIA_SHARED";
		}else if(Environment.MEDIA_UNMOUNTABLE.equals(state)){
			return "MEDIA_UNMOUNTABLE";
		}else if(Environment.MEDIA_UNMOUNTED.equals(state)){
			return "MEDIA_UNMOUNTED";
		}
		return null;
	}*/
	
	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return	返回文件名称,便于将文件传送到服务器
	 */
	
	private String getCrashInfo(Throwable ex){
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		
		return sb.toString();
	}
	
	private String saveCrashInfo2File(String crashInfo) {
//		
//		StringBuffer sb = new StringBuffer();
//		for (Map.Entry<String, String> entry : infos.entrySet()) {
//			String key = entry.getKey();
//			String value = entry.getValue();
//			sb.append(key + "=" + value + "\n");
//		}
//		
//		Writer writer = new StringWriter();
//		PrintWriter printWriter = new PrintWriter(writer);
//		ex.printStackTrace(printWriter);
//		Throwable cause = ex.getCause();
//		while (cause != null) {
//			cause.printStackTrace(printWriter);
//			cause = cause.getCause();
//		}
//		printWriter.close();
//		String result = writer.toString();
//		sb.append(result);
		try {
			 long timestamp = System.currentTimeMillis();  
	            String time = formatter.format(new Date());  
	            String fileName = "crash-" + time + "-" + timestamp + ".log";  
	            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
//	                String path = "/sdcard/crash/";  
	                String path = Environment.getExternalStorageDirectory().getPath()+File.separator +"crash";  
	                File dir = new File(path);  
	                if (!dir.exists()) {  
	                    dir.mkdirs();  
	                }  
	                FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);  
	                fos.write(crashInfo.getBytes());  
	                fos.close();  
	            }  
	            return fileName;  
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param crashInfo
	 * @return
	 */
/*	private void sendCrashInfo2Mail(String crashInfo) {
		Intent sendIntent=new Intent(Intent.ACTION_SEND);  
		
		sendIntent.setData(Uri.parse("mailto:tangqi1101@gmail.com"));
		
//        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tangqi1101@gmail.com"});  
        sendIntent.putExtra(Intent.EXTRA_TEXT, crashInfo);  
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"exception");  
//        sendIntent.setType("message/rfc822");  
        mContext.startActivity(sendIntent);  
	}*/
	
	public void doSendLogFile(String fileName) {
//	    String fileName = "myFileName.txt";

	    String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
	    String myDir = externalStorageDirectory + "/crash/";  // the file will be in myDir
	    Uri uri = Uri.parse("file://" + myDir + fileName);
	    Intent i = new Intent(Intent.ACTION_SEND);
//	    try {
//	        myFileHandle.close(); // you may want to be sure that the file is closed correctly
//	    } catch (IOException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//	    }
	    
	    i.setType("message/rfc822"); 
	    i.putExtra(Intent.EXTRA_EMAIL, new String[] { "tangqi1101@gmail.com" });
	    i.putExtra(Intent.EXTRA_SUBJECT, "the subject text");
	    i.putExtra(Intent.EXTRA_TEXT, "extra text body");
//	    Log.i(getClass().getSimpleName(), "logFile=" + uri);
	    i.putExtra(Intent.EXTRA_STREAM, uri);

	    try {
	    	mContext.startActivity(Intent.createChooser(i, "Send mail..."));
	    } catch (android.content.ActivityNotFoundException ex) {
	        Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT) .show();
	    }
	}
	
}
