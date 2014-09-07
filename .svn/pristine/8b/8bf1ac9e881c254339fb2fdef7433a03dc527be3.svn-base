package com.huawei.ptn.util;

import com.huawei.ptn.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

public class ViewUtils {

	public static void showAlertDialog(Context context, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.ok, null);
		builder.create().show();
	}

	public static AlertDialog showAlertDialog(Context context, String title,
			String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setPositiveButton(
						context.getResources().getString(R.string.ok),
						null)
				.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		return alertDialog;
	}
	
	public static AlertDialog showAlertDialog(Context context, String title,
			String message, DialogInterface.OnClickListener onClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setPositiveButton(
						context.getResources().getString(R.string.ok),
						onClickListener)
				.setNegativeButton(
						context.getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialogInterface,
									int paramInt) {
								dialogInterface.dismiss();
							}
						}).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		return alertDialog;
	}

	public static AlertDialog showAlertDialog(Context context, String title,
			String message, DialogInterface.OnClickListener onClickListenerP,
			DialogInterface.OnClickListener onClickListenerN) {
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setPositiveButton(
						context.getResources().getString(R.string.ok),
						onClickListenerP)
				.setNegativeButton(
						context.getResources().getString(R.string.cancel),
						onClickListenerN).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		return alertDialog;
	}

	public static void showToast(Context context, int resId) {
		showToast(context, resId, Toast.LENGTH_LONG);
	}

	public static void showToast(Context context, int resId, int duration) {
		Toast localToast = Toast.makeText(context, resId, duration);
		localToast.show();
	}

	public static void showToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_LONG);
	}

	public static void showToast(Context context, String text, int duration) {
		Toast localToast = Toast.makeText(context, text, duration);
		localToast.show();
	}
}
