package com.huawei.ptn.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.huawei.ptn.R;
import com.huawei.ptn.WelcomeActivity.ICallBackToMainPage;
import com.huawei.ptn.common.Constants;
import com.huawei.ptn.service.UpdateService;

import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

/**
 * 
 * �汾��⣬�Զ�����
 * 
 * 1.ͨ��Url������ 2.���ز���װ���� 3.ɾ����ʱ·��
 * 
 */
public class ApkAutoUpdate {

	private static final String TAG = ApkAutoUpdate.class.getSimpleName();

	// ���ø��µ�Activity
	public Activity activity = null;

	// ��ǰ�汾��
	public int versionCode = 0;

	// ��ǰ�汾����
	public String versionName = "";

	// �������˰汾��Ϣ
	public int versionCodeServer = 0;

	public String versionNameServer = "";

	// �ļ���ǰ·��
	private String currentFilePath = "";

	// ��װ���ļ���ʱ·��
	private String currentTempFilePath = "";

	// ����ļ���չ���ַ���
	private String fileEx = "";

	// ����ļ����ַ���
	private String fileNa = "";

	// apk�ڷ������˵ĵ�ַ
	private String strURL = Constants.APK_BASE_URL;
	private ProgressDialog dialog;

	private URLConnection conn;
	private int FileLength;
	private int DownedFileLength = 0;

	public ICallBackToMainPage CallBackObject = null;// ������ҳ�ص�����

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					dialog.setMax(FileLength);
					Log.d("�ļ�����----------->", dialog.getMax() + "");
					break;
				case 1:
					dialog.setProgress(DownedFileLength);
					break;
				case 2:
					ViewUtils.showToast(activity.getApplicationContext(),
							"�������");
					break;
				default:
					break;
				}
			}
		}
	};

	/**
	 * 
	 * ���췽������õ�ǰ�汾��Ϣ
	 * 
	 * @param activity
	 */
	public ApkAutoUpdate(Activity activity) {
		this.activity = activity;
	}

	public ApkAutoUpdate(Activity activity, ICallBackToMainPage obj) {
		this.activity = activity;
		this.CallBackObject = obj;
	}

	/**
	 * 
	 * ���°汾
	 */
	public boolean update() {
		if (!NetworkManager.isAvailable(activity)) {
			ViewUtils.showToast(activity.getApplicationContext(),
					"��ǰ�������źţ��뿪�����������!", Toast.LENGTH_SHORT);
			return false;
		}

		// ��õ�ǰ�汾
		getCurrentVersion();
		// ��ȡ�������˰汾��
		getApkVersion();
		// �뱾�ذ汾�ŶԱ�(ע�⣺�ַ����Ƚ���ȵ���equals����)
		if ((versionCode == versionCodeServer)
				&& versionName.equals(versionNameServer)) {
			Log.d(TAG, "version is the newest!");
			showNewestDialog();
			return false;
		}

		// �����Ի������û������Ƿ����
		showUpdateDialog();
		return true;
	}

	public boolean update_home() {
		// ��õ�ǰ�汾
		getCurrentVersion();
		// ��ȡ�������˰汾��
		getApkVersion();
		// �뱾�ذ汾�ŶԱ�(ע�⣺�ַ����Ƚ���ȵ���equals����)
		if ((versionCode == versionCodeServer)
				&& versionName.equals(versionNameServer)) {
			Log.d(TAG, "Apk is the newest version");
			return true;
		}
		// �����Ի������û������Ƿ����
		showUpdateDialog();
		return false;
	}

	/**
	 * �����Ի���˵����ǰ�����°汾
	 */
	public void showNewestDialog() {
		ViewUtils.showAlertDialog(activity, "��ʾ", "�㵱ǰ�İ汾�Ѿ������°汾").show();
	}
	
	/**
	 * �����Ի���ѡ���Ƿ���Ҫ���°汾
	 */
	public void showUpdateDialog() {

		ViewUtils.showAlertDialog(activity, "�����°汾", "�Ƿ���£�",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Log.d(TAG, "++++++++++++++++++++" + UpdateService.class.getName());
						if (!ServiceUtils.isServiceRunning(activity, UpdateService.class.getName())) {
							//�������·���
							Intent updateIntent = new Intent(activity.getApplicationContext(), UpdateService.class);
							updateIntent.putExtra("titleId", R.string.app_name);
							updateIntent.putExtra("apkUrl", strURL);
							activity.startService(updateIntent);
						}
						
						if (CallBackObject != null) {
							CallBackObject.natigateToMainPage();
						}
						
//						// ͨ����ַ�����ļ�
//						downloadTheFile(strURL);
//						// ��ʾ����״̬��������
//						showWaitDialog();
					}
				}, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (CallBackObject != null) {
							CallBackObject.natigateToMainPage();
						}
					}
				}).show();

	}

	/**
	 * ��ʾ����״̬��������
	 */

	public void showWaitDialog() {
		// TODO: �����ʾKb/Kb��ʽ
		dialog = new ProgressDialog(activity);
		dialog.setTitle("�ͻ��˸���");
		dialog.setMessage("��������, ���Ե�...");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.show();
	}

	public void getCurrentVersion() {
		try {
			PackageInfo info = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0);
			this.versionCode = info.versionCode;
			this.versionName = info.versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.toString());
		}
	}

	/**
	 * 
	 * ��ȡ�ļ����Ʋ�ִ������
	 * 
	 * 
	 * @param strPath
	 */

	private void downloadTheFile(final String strPath) {
		// ����ļ��ļ���չ���ַ���
		fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length())
				.toLowerCase();
		// ����ļ��ļ����ַ���
		fileNa = strURL.substring(strURL.lastIndexOf("/") + 1,
				strURL.lastIndexOf("."));
		try {
			if (strPath.equals(currentFilePath)) {
				doDownloadTheFile(strPath);
			}
			currentFilePath = strPath;
			new Thread(new Runnable() {
				public void run() {
					try {
						// ִ������
						doDownloadTheFile(strPath);
					} catch (Exception e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * ִ���°汾�������أ�����װ
	 * 
	 * 
	 * @param strPath
	 * 
	 * @throws Exception
	 */

	private void doDownloadTheFile(String strPath) throws Exception {

		Log.i(TAG, "getDataSource()");

		// �ж�strPath�Ƿ�Ϊ�����ַ

		if (!URLUtil.isNetworkUrl(strPath)) {

			Log.i(TAG, "��������ַ����");

		} else {

			URL myURL = new URL(strPath);

			conn = myURL.openConnection();

			conn.connect();

			InputStream is = conn.getInputStream();

			if (is == null) {

				throw new RuntimeException("stream is null");

			}

			SdCardUtil.init();
			// ����һ����ʱ�ļ�
			Log.d(TAG, "path = " + SdCardUtil.APK_UPDATE_PATH + fileNa + "."
					+ fileEx);
			File path = new File(SdCardUtil.APK_UPDATE_PATH);
			File myTempFile = File.createTempFile(fileNa, "." + fileEx, path);

			// ��װ���ļ���ʱ·��

			currentTempFilePath = myTempFile.getAbsolutePath();

			FileOutputStream fos = new FileOutputStream(myTempFile);

			byte buf[] = new byte[128];

			FileLength = conn.getContentLength();
			Log.d(TAG, "FileLength=" + FileLength);

			Message message0 = new Message();
			message0.what = 0;
			handler.sendMessage(message0);

			while (DownedFileLength < FileLength) {

				int numread = is.read(buf);
				DownedFileLength += numread;
				fos.write(buf, 0, numread);

				//Log.d("-------->", DownedFileLength + "");

				Message message1 = new Message();
				message1.what = 1;
				handler.sendMessage(message1);
			}

			Log.i(TAG, "getDataSource() Download ok...");
			Message message2 = new Message();
			message2.what = 2;
			handler.sendMessage(message2);

			dialog.cancel();
			dialog.dismiss();

			// ���ļ�
			openFile(myTempFile);

			try {

				is.close();

			} catch (Exception ex) {

				Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);

			}

		}

	}

	/**
	 * ���ļ����а�װ
	 * 
	 * @param f
	 */

	private void openFile(File f) {

		Intent intent = new Intent();

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setAction(android.content.Intent.ACTION_VIEW);

		// ������غõ��ļ�����

		String type = getMIMEType(f);

		// �򿪸��������ļ�
		intent.setDataAndType(Uri.fromFile(f), type);

		// ��װ
		activity.startActivity(intent);

	}

	/**
	 * 
	 * ɾ����ʱ·����İ�װ��
	 */

	public void delFile() {
		Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");
		File myFile = new File(currentTempFilePath);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * ��������ļ�������
	 * 
	 * @param f
	 *            �ļ�����
	 * @return �ļ�����
	 */

	private String getMIMEType(File f) {

		String type = "";

		// ����ļ�����

		String fName = f.getName();

		// ����ļ���չ��

		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {

			type = "audio";

		} else if (end.equals("3gp") || end.equals("mp4")) {

			type = "video";

		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")

		|| end.equals("jpeg") || end.equals("bmp")) {

			type = "image";

		} else if (end.equals("apk")) {

			type = "application/vnd.android.package-archive";

		} else {

			type = "*";

		}

		if (end.equals("apk")) {

		} else {

			type += "/*";

		}

		return type;
	}

	private void getApkVersion() {
		try {
			String content = HttpUtils.getRequest(Constants.APK_VERSION);
			System.out.println("ApkVersion : " + content);
			if (content != null) {
				JSONObject jsonobject = new JSONObject(content);
				parse(jsonobject);
			} else {
				Log.i(TAG, "ApkVersion from server is null");
			}
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}

	private void parse(JSONObject obj) {
		try {
			versionCodeServer = obj.getInt("VersionCode");
			versionNameServer = obj.getString("VersionName");
			strURL += obj.getString("ApkPath");

			Log.d(TAG, "versionCodeServer = " + versionCodeServer);
			Log.d(TAG, "versionNameServer = " + versionNameServer);
			Log.d(TAG, "strURL = " + strURL);

		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}

}
