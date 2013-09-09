package com.dream.ivpc;

import android.content.Context;
import android.content.Intent;


public class PageChange {

	public static void logout(Context context){
		Intent intent = new Intent();
		intent.setClass( context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static void go2CandidateList(Context context){
		Intent intent = new Intent();
		intent.setClass( context, CandidateList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static void go2CandidateDeatil(Context context){
		Intent intent = new Intent();
		intent.setClass( context, CandidateDetail.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
}
