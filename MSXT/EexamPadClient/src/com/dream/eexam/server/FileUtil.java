package com.dream.eexam.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.util.Log;

public class FileUtil {

	public final static String LOG_TAG = "FileUtil";
	
	public static FileInputStream getExamStream(String filePath,String fileName){
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(filePath+ File.separator+fileName));
		} catch (FileNotFoundException e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		return inputStream;
	}
}
