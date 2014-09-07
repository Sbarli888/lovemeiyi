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
 * 版本检测，自动更新
 * 
 * 1.通过Url检测更新 2.下载并安装更新 3.删除临时路径
 * 
 */
public class ApkAutoUpdate {

	private static final String TAG = ApkAutoUpdate.class.getSimpleName();

	// 调用更新的Activity
	public Activity activity = null;

	// 当前版本号
	public int versionCode = 0;

	// 当前版本名称
	public String versionName = "";

	// 服务器端版本信息
	public int versionCodeServer = 0;

	public String versionNameServer = "";

	// 文件当前路径
	private String currentFilePath = "";

	// 安装包文件临时路径
	private String currentTempFilePath = "";

	// 获得文件扩展名字符串
	private String fileEx = "";

	// 获得文件名字符串
	private String fileNa = "";

	// apk在服务器端的地址
	private String strURL = Constants.APK_BASE_URL;
	private ProgressDialog dialog;

	private URLConnection conn;
	private int FileLength;
	private int DownedFileLength = 0;

	public ICallBackToMainPage CallBackObject = null;// 引用首页回调对象

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					dialog.setMax(FileLength);
					Log.d("文件长度----------->", dialog.getMax() + "");
					break;
				case 1:
					dialog.setProgress(DownedFileLength);
					break;
				case 2:
					ViewUtils.showToast(activity.getApplicationContext(),
							"下载完成");
					break;
				default:
					break;
				}
			}
		}
	};

	/**
	 * 
	 * 构造方法，获得当前版本信息
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
	 * 更新版本
	 */
	public boolean update() {
		if (!NetworkManager.isAvailable(activity)) {
			ViewUtils.showToast(activity.getApplicationContext(),
					"当前无网络信号，请开启网络后重试!", Toast.LENGTH_SHORT);
			return false;
		}

		// 获得当前版本
		getCurrentVersion();
		// 获取服务器端版本号
		getApkVersion();
		// 与本地版本号对比(注意：字符串比较相等调用equals函数)
		if ((versionCode == versionCodeServer)
				&& versionName.equals(versionNameServer)) {
			Log.d(TAG, "version is the newest!");
			showNewestDialog();
			return false;
		}

		// 弹出对话框，由用户决定是否更新
		showUpdateDialog();
		return true;
	}

	public boolean update_home() {
		// 获得当前版本
		getCurrentVersion();
		// 获取服务器端版本号
		getApkVersion();
		// 与本地版本号对比(注意：字符串比较相等调用equals函数)
		if ((versionCode == versionCodeServer)
				&& versionName.equals(versionNameServer)) {
			Log.d(TAG, "Apk is the newest version");
			return true;
		}
		// 弹出对话框，由用户决定是否更新
		showUpdateDialog();
		return false;
	}

	/**
	 * 弹出对话框，说明当前是最新版本
	 */
	public void showNewestDialog() {
		ViewUtils.showAlertDialog(activity, "提示", "你当前的版本已经是最新版本").show();
	}
	
	/**
	 * 弹出对话框，选择是否需要更新版本
	 */
	public void showUpdateDialog() {

		ViewUtils.showAlertDialog(activity, "发现新版本", "是否更新？",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Log.d(TAG, "++++++++++++++++++++" + UpdateService.class.getName());
						if (!ServiceUtils.isServiceRunning(activity, UpdateService.class.getName())) {
							//开启更新服务
							Intent updateIntent = new Intent(activity.getApplicationContext(), UpdateService.class);
							updateIntent.putExtra("titleId", R.string.app_name);
							updateIntent.putExtra("apkUrl", strURL);
							activity.startService(updateIntent);
						}
						
						if (CallBackObject != null) {
							CallBackObject.natigateToMainPage();
						}
						
//						// 通过地址下载文件
//						downloadTheFile(strURL);
//						// 显示更新状态，进度条
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
	 * 显示更新状态，进度条
	 */

	public void showWaitDialog() {
		// TODO: 如何显示Kb/Kb格式
		dialog = new ProgressDialog(activity);
		dialog.setTitle("客户端更新");
		dialog.setMessage("正在下载, 请稍等...");
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
	 * 截取文件名称并执行下载
	 * 
	 * 
	 * @param strPath
	 */

	private void downloadTheFile(final String strPath) {
		// 获得文件文件扩展名字符串
		fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length())
				.toLowerCase();
		// 获得文件文件名字符串
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
						// 执行下载
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
	 * 执行新版本进行下载，并安装
	 * 
	 * 
	 * @param strPath
	 * 
	 * @throws Exception
	 */

	private void doDownloadTheFile(String strPath) throws Exception {

		Log.i(TAG, "getDataSource()");

		// 判断strPath是否为网络地址

		if (!URLUtil.isNetworkUrl(strPath)) {

			Log.i(TAG, "服务器地址错误！");

		} else {

			URL myURL = new URL(strPath);

			conn = myURL.openConnection();

			conn.connect();

			InputStream is = conn.getInputStream();

			if (is == null) {

				throw new RuntimeException("stream is null");

			}

			SdCardUtil.init();
			// 生成一个临时文件
			Log.d(TAG, "path = " + SdCardUtil.APK_UPDATE_PATH + fileNa + "."
					+ fileEx);
			File path = new File(SdCardUtil.APK_UPDATE_PATH);
			File myTempFile = File.createTempFile(fileNa, "." + fileEx, path);

			// 安装包文件临时路径

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

			// 打开文件
			openFile(myTempFile);

			try {

				is.close();

			} catch (Exception ex) {

				Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);

			}

		}

	}

	/**
	 * 打开文件进行安装
	 * 
	 * @param f
	 */

	private void openFile(File f) {

		Intent intent = new Intent();

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setAction(android.content.Intent.ACTION_VIEW);

		// 获得下载好的文件类型

		String type = getMIMEType(f);

		// 打开各种类型文件
		intent.setDataAndType(Uri.fromFile(f), type);

		// 安装
		activity.startActivity(intent);

	}

	/**
	 * 
	 * 删除临时路径里的安装包
	 */

	public void delFile() {
		Log.i(TAG, "The TempFile(" + currentTempFilePath + ") was deleted.");
		File myFile = new File(currentTempFilePath);
		if (myFile.exists()) {
			myFile.delete();
		}
	}

	/**
	 * 获得下载文件的类型
	 * 
	 * @param f
	 *            文件名称
	 * @return 文件类型
	 */

	private String getMIMEType(File f) {

		String type = "";

		// 获得文件名称

		String fName = f.getName();

		// 获得文件扩展名

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
