package com.dream.ivpc.activity;


import java.io.File;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.dream.ivpc.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class TestActivity extends Activity {

	String pdfFile;
	Context oContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		
		oContext = this.getApplicationContext();
		
		Button testBtn  = (Button) findViewById(R.id.testBtn);
		
		String basePath = Environment.getExternalStorageDirectory().getPath();
		pdfFile = basePath + File.separator + "books" + File.separator
				+ "Android" + File.separator + "Develop" + File.separator
				+ "Android Users Guide.pdf";
		
		testBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Uri uri = Uri.parse(pdfFile);
				Intent intent = new Intent(oContext,MuPDFActivity.class);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(uri);
				startActivity(intent);
			}
		});
	}

}
