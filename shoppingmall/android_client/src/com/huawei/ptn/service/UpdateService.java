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

	// ����
	private int titleId = 0;

	// �ļ��洢
	private File updateDir = null;
	private File updateFile = null;

	// ֪ͨ��
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;

	// ֪ͨ����תIntent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;

	// ����״̬
	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAILED = 1;

	private final static int CONNECTION_TIMEOUT = 10000;
	private final static int READ_TIMEOUT = 20000;
	private final static int BUFFER_SIZE = 4096;

	// �������˰汾��Ϣ
	public int versionCodeServer = 0;
	public String versionNameServer = "";
	// apk�ڷ������˵ĵ�ַ
	private String apkUrl = null;

	private RemoteViews contentView = null;

	private Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				// �����װPendingIntent
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
						R.id.update_notification_percent, "�������,������װ��");
				updateNotification.contentIntent = updatePendingIntent;
				// updateNotification.setLatestEventInfo(UpdateService.this,
				// "������", "������ɣ������װ", updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);

				// ֹͣ����
				UpdateService.this.stopSelf();
				break;
			case DOWNLOAD_FAILED:
				updateNotification.defaults = Notification.DEFAULT_SOUND;
				updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				updateNotification.contentView.setViewVisibility(
						R.id.update_notification_progressblock, View.GONE);
				updateNotification.contentView.setTextViewText(
						R.id.update_notification_percent, "����ʧ�ܣ�");
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
		
		// ��ȡ��������titleid
		titleId = intent.getIntExtra("titleId", 0);
		apkUrl = intent.getStringExtra("apkUrl");

		// �����ļ�
		if (hasSDCard()) {
			updateDir = new File(SdCardUtil.APK_UPDATE_PATH);
			updateFile = new File(updateDir.getPath(), getResources()
					.getString(titleId) + ".apk");
		}

		updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//�Զ���Notification����ʹ�����¹��캯����ʹ���޲������캯������ʾ����
		updateNotification = new Notification(R.drawable.ic_launcher,
				getResources().getString(R.string.app_name), System.currentTimeMillis());
		//updateNotification = new Notification(); 
		updateNotification.flags |= Notification.FLAG_ONGOING_EVENT;

		// �������ع����У����֪ͨ�����ص�������
		updateIntent = new Intent(this, MainActivity.class);
		updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,
				0);
		
		contentView.setTextViewText(R.id.update_notification_percent, "0%");
		contentView.setProgressBar(
				com.huawei.ptn.R.id.update_notification_progressbar, 100, 0,
				false);

		updateNotification.contentView = contentView;
		updateNotification.contentIntent = updatePendingIntent;
		// ����֪ͨ
		updateNotificationManager.notify(0, updateNotification);
		Log.d(TAG, "onStartCommand end");
		// �������߳����أ����ʹ��Serviceͬ�����أ��ᵼ��ANR���⣬Service����Ҳ�ᱻ����
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
				// Ϊ�˷�ֹƵ��֪ͨ����Ӧ�óԽ����ٷֱ�����10��֪ͨһ��
				if ((downloadCount == 0)
						|| ((totalSize * 100) / updateTotalSize) - 10 > downloadCount) {
					Log.d(TAG, "downloadCount = " + downloadCount);
					downloadCount += 10;
					// updateNotification.setLatestEventInfo(UpdateService.this,
					// "��������", (int)totalSize*100/updateTotalSize + "%",
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
