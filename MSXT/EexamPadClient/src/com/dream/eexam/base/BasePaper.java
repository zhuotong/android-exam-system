package com.dream.eexam.base;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.dream.eexam.model.QuestionProgress;

public class BasePaper extends BaseActivity {

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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

	@Override
	public void ShowDialog(String msg) {
		// TODO Auto-generated method stub
		super.ShowDialog(msg);
	}

	@Override
	public QuestionProgress getQuestionProgress(
			SharedPreferences sharedPreferences) {
		// TODO Auto-generated method stub
		return super.getQuestionProgress(sharedPreferences);
	}

	@Override
	public void saveQuestionProgress(SharedPreferences sharedPreferences,
			QuestionProgress qp) {
		// TODO Auto-generated method stub
		super.saveQuestionProgress(sharedPreferences, qp);
	}

}
