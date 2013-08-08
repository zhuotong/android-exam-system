package com.dream.ivpc.custom;

import com.dream.ivpc.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class CustomDialog extends ProgressDialog {
	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dialog);
	}

	public static CustomDialog show(Context context) {
		CustomDialog dialog = new CustomDialog(context,R.style.custom_dialog_style);
		dialog.show();
		return dialog;
	}
}
