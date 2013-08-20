package com.dream.ivpc;

import android.content.Context;
import android.content.Intent;

import com.dream.ivpc.activity.CandidateDetail2;
import com.dream.ivpc.activity.CandidateList;
import com.dream.ivpc.activity.LoginActivity;

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
		intent.setClass( context, CandidateDetail2.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
}
