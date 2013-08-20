package com.dream.ivpc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

public class FileUtil {
	public final static String LOG_TAG = "FileUtil";
	
	private String SDPATH;
	
	private int FILESIZE = 4 * 1024; 
	
	public String getSDPATH(){
		return SDPATH;
	}
	
	public FileUtil(){
		//得到当前外部存储设备的目录( /SDCARD )
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}
	
	/**
	 * 在SD卡上创建文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException{
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * 在SD卡上创建目录
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName){
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}
	
	/**
	 * 判断SD卡上的文件夹是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];
			while((input.read(buffer)) != -1){
				output.write(buffer);
			}
			output.flush();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public static FileInputStream getFileInputStream(String filePath,String fileName){
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
	
	public static FileInputStream getFileInputStream(String filePath){
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(filePath));
		} catch (FileNotFoundException e) {
			Log.i(LOG_TAG,e.getMessage());
		}
		Log.i(LOG_TAG, "getExamStream() end.");
		return inputStream;
	}

}

