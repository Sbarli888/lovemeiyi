package com.huawei.ptn.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.huawei.ptn.MainActivity;
import com.huawei.ptn.MyApplication;
import com.huawei.ptn.util.SdCardUtil;
import com.huawei.ptn.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class UpdateService extends Service {

	private static final String TAG = UpdateService.class.getSimpleName();

	// 标题
	private int titleId = 0;

	// 文件存储
	private File updateDir = null;
	private File updateFile = null;

	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;

	// 通知栏跳转Intent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;

	// 下载状态
	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAILED = 1;

	private final static int CONNECTION_TIMEOUT = 10000;
	private final static int READ_TIMEOUT = 20000;
	private final static int BUFFER_SIZE = 4096;

	// 服务器端版本信息
	public int versionCodeServer = 0;
	public String versionNameServer = "";
	// apk在服务器端的地址
	private String apkUrl = null;

	private RemoteViews contentView = null;

	private Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				// 点击安装PendingIntent
				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				updatePendingIntent = PendingIntent.getActivity(
						UpdateService.this, 0, installIntent, 0);

				updateNotification.defaults = Notification.DEFAULT_SOUND;
				updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				updateNotification.contentView.setViewVisibility(
						R.id.update_notification_progressblock, View.GONE);
				updateNotification.contentView.setTextViewText(
						R.id.update_notification_percent, "下载完成,请点击安装！");
				updateNotification.contentIntent = updatePendingIntent;
				// updateNotification.setLatestEventInfo(UpdateService.this,
				// "爱美衣", "下载完成，点击安装", updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);

				// 停止服务
				UpdateService.this.stopSelf();
				break;
			case DOWNLOAD_FAILED:
				updateNotification.defaults = Notification.DEFAULT_SOUND;
				updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				updateNotification.contentView.setViewVisibility(
						R.id.update_notification_progressblock, View.GONE);
				updateNotification.contentView.setTextViewText(
						R.id.update_notification_percent, "下载失败！");
				updateNotification.contentIntent = updatePendingIntent;
				updateNotificationManager.notify(0, updateNotification);
				UpdateService.this.stopSelf();
				break;
			default:
				UpdateService.this.stopSelf();
				break;
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MyApplication.getInstance().addService(this);
		contentView = new RemoteViews(this.getPackageName(),
				R.layout.update_notification);
		Log.d(TAG, this.getPackageName());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "+++++++++++++++onStartCommand++++++++++++++++");
		
		// 获取传过来的titleid
		titleId = intent.getIntExtra("titleId", 0);
		apkUrl = intent.getStringExtra("apkUrl");

		// 创建文件
		if (hasSDCard()) {
			updateDir = new File(SdCardUtil.APK_UPDATE_PATH);
			updateFile = new File(updateDir.getPath(), getResources()
					.getString(titleId) + ".apk");
		}

		updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//自定义Notification必须使用如下构造函数，使用无参数构造函数将显示不了
		updateNotification = new Notification(R.drawable.ic_launcher,
				getResources().getString(R.string.app_name), System.currentTimeMillis());
		//updateNotification = new Notification(); 
		updateNotification.flags |= Notification.FLAG_ONGOING_EVENT;

		// 设置下载过程中，点击通知栏，回到主界面
		updateIntent = new Intent(this, MainActivity.class);
		updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,
				0);
		
		contentView.setTextViewText(R.id.update_notification_percent, "0%");
		contentView.setProgressBar(
				com.huawei.ptn.R.id.update_notification_progressbar, 100, 0,
				false);

		updateNotification.contentView = contentView;
		updateNotification.contentIntent = updatePendingIntent;
		// 发出通知
		updateNotificationManager.notify(0, updateNotification);
		Log.d(TAG, "onStartCommand end");
		// 开启新线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会被阻塞
		new Thread(new updateRunnable()).start();

		return super.onStartCommand(intent, flags, startId);
	}

	private boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	class updateRunnable implements Runnable {

		Message message = updateHandler.obtainMessage();

		public void run() {
			message.what = DOWNLOAD_COMPLETE;
			try {
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}

				long downloadSize = downloadUpdateFile(apkUrl, updateFile);
				if (downloadSize > 0) {
					Log.d(TAG, "downloadSize = " + downloadSize);
					updateHandler.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.what = DOWNLOAD_FAILED;
				updateHandler.sendMessage(message);
			}

		}

	}

	public long downloadUpdateFile(String downloadUrl, File saveFile)
			throws Exception {
		Log.d(TAG, "downloadUrl = " + downloadUrl);
		Log.d(TAG, "saveFile = " + saveFile.getAbsolutePath());
		int downloadCount = 0;
		int currentSize = 0;
		long totalSize = 0;
		int updateTotalSize = 0;

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setConnectTimeout(CONNECTION_TIMEOUT);
			httpConnection.setReadTimeout(READ_TIMEOUT);

			if (httpConnection.getResponseCode() == 404) {
				Log.e(TAG, "404");
				throw new Exception("failed");
			}

			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[BUFFER_SIZE];
			int readSize = 0;

			updateTotalSize = httpConnection.getContentLength();
			Log.d(TAG, "updateTotalSize=" + updateTotalSize);

			updateNotification.contentView.setViewVisibility(
					R.id.update_notification_progressblock, View.VISIBLE);
			while ((readSize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readSize);
				totalSize += readSize;
				// 为了防止频繁通知导致应用吃紧，百分比增加10才通知一次
				if ((downloadCount == 0)
						|| ((totalSize * 100) / updateTotalSize) - 10 > downloadCount) {
					Log.d(TAG, "downloadCount = " + downloadCount);
					downloadCount += 10;
					// updateNotification.setLatestEventInfo(UpdateService.this,
					// "正在下载", (int)totalSize*100/updateTotalSize + "%",
					// updatePendingIntent);
					updateNotification.contentView.setProgressBar(
							R.id.update_notification_progressbar, 100,
							(int) totalSize * 100 / updateTotalSize, false);
					updateNotification.contentView.setTextViewText(
							R.id.update_notification_percent, (int) totalSize
									* 100 / updateTotalSize + "%");
					updateNotificationManager.notify(0, updateNotification);
				}
			}
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}

	public void cancelNotification() {
		Log.d(TAG, "cancelNotification");
		updateNotificationManager.cancel(0);
	}

}
