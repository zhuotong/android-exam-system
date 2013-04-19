package com.dream.eexam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

public class FileUtil {
	public final static String LOG_TAG = "FileUtil";
	public final static String LOGIN_FILE_PREFIX = "Login_";
	public final static String EXAM_FILE_PREFIX = "Exam_";
	public final static String RESULT_FILE_PREFIX = "Result_";
	public final static String ANSWER_FILE_PREFIX = "Answer_";
	
	public final static String FILE_SUFFIX_XML = ".xml";
	public final static String FILE_SUFFIX_TXT = ".txt";
	
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
	
	public static List<String> getFolderList(File parentFolder){
		List<String> folderList = new ArrayList<String>();
		if(parentFolder.exists()){
			File subFolders[] = parentFolder.listFiles();
			if(subFolders!=null && subFolders.length>0){
				for (int i = 0; i < subFolders.length; i++) {
					folderList.add(subFolders[i].getName());
				}				
			}
		}
		return folderList;
	}
}
