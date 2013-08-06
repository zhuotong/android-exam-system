package com.dream.ivpc.activity.resume;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.dream.ivpc.BaseActivity;
import com.dream.ivpc.R;

@Deprecated 
public class ResumePdfLoad extends BaseActivity {
//	String pdfFile;
	Context oContext;
	ProgressBar loadingPB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);

		oContext = this.getApplicationContext();
		loadingPB = (ProgressBar) findViewById(R.id.loadingPB);
//		new DownLoadTask().execute(new String[] {});
	}

	private class DownLoadTask extends AsyncTask<String, Void, String> {
		private String pdfPath;
		private boolean dowloadSucess = false;

		@Override
		protected void onPreExecute() {
			Log.i(LOG_TAG, "onPreExecute() called");
			loadingPB.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				// download code
				Thread.sleep(3000);
				
				String basePath = Environment.getExternalStorageDirectory()
						.getPath();
				pdfPath = basePath + File.separator + "interviewer"
						+ File.separator + "tangqi" + File.separator
						+ "tangqi_resume.pdf";
				dowloadSucess = true;
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dowloadSucess) {
				Uri uri = Uri.parse(pdfPath);
				Intent intent = new Intent(oContext, MuPDFActivity.class);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(uri);
				startActivity(intent);
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		new DownLoadTask().execute(new String[] {});
	}
	

}
