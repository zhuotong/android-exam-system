package com.dream.eexam.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Environment;
import android.util.Log;

public class FileUtil {

	public final static String LOG_TAG = "FileUtil";
	
	public static FileInputStream getExamStream(String filePath,String fileName){
		
		Log.i(LOG_TAG, "getExamStream()...");
		
		Log.i(LOG_TAG, "filePath:"+filePath);
		Log.i(LOG_TAG, "fileName:"+fileName);
		
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(filePath+ File.separator+fileName));
		} catch (FileNotFoundException e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		
		Log.i(LOG_TAG, "getExamStream() end.");
		return inputStream;
	}
	
	
	public void deleteFolder(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					this.deleteFolder(files[i]);
				}
			}
			file.delete();
		} 
	}
	
	public void saveFile(String path, String fileName,String content) {
		try {
	        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
	            File dir = new File(path);  
	            if (!dir.exists()) {  
	                dir.mkdirs();  
	            } 
	            FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);  
	            fos.write(content.getBytes());  
	            fos.close();  
	        }  
			
		} catch (Exception e) {
			Log.e(LOG_TAG, "an error occured while writing file...", e);
		}
		Log.i(LOG_TAG,"saveFile end.");
	}
}
