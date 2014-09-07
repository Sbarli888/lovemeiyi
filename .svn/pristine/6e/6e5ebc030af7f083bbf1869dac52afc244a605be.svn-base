package com.huawei.ptn;

import com.huawei.ptn.config.ConfigManager;
import com.huawei.ptn.service.ImageCopyService;
import com.huawei.ptn.util.ApkAutoUpdate;
import com.huawei.ptn.util.NetworkManager;
import com.huawei.ptn.util.ViewUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {

	private boolean isNetworkAvailable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.welcome);
		ImageView iv = (ImageView) findViewById(R.id.welcome_logo);

		//启动各类后台service
		appStartService();
		
		// 设置淡入淡出的动画效果
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		// 设置动画的持续时间为3s
		aa.setDuration(3000);
		// 开始播放动画
		iv.startAnimation(aa);

		// 当动画执行完后，启动MainActivity
		aa.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {

				if (isNetworkAvailable) {
					// ToMainPage由ApkAutoUpdate对象回调
					ApkAutoUpdate m_ApkAutoUpdate = new ApkAutoUpdate(
							WelcomeActivity.this, new CallBackToMainPage());
					Boolean isNewVersion = m_ApkAutoUpdate.update_home();
					if (isNewVersion) {
						ToMainPage();
					}

				}

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationStart(Animation animation) {

			}
		});
	}

	private void checkNetworkState() {
		if (!NetworkManager.isAvailable(this)) {
			ViewUtils
					.showAlertDialog(
							this,
							getResources().getString(R.string.network_service),
							getResources().getString(
									R.string.network_unavailable_desc),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(
											"android.settings.WIRELESS_SETTINGS");
									startActivity(intent);
								}
							}, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									ViewUtils.showToast(WelcomeActivity.this,
											R.string.network_service_back);
								}
							}).show();
			isNetworkAvailable = false;
			return;
		}
		isNetworkAvailable = true;
		return;
	}

	private void ToMainPage() {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		boolean guideShowed = ConfigManager
				.getBoolean(ConfigManager.GUIDE_SHOWED);
		String lastLoadVersion = ConfigManager
				.getString(ConfigManager.LAST_LOAD_VERSION);
		if (!guideShowed || (lastLoadVersion == null)
				|| (!lastLoadVersion.equalsIgnoreCase(Build.CLIENT_VERSION))) {
			intent.setClass(this, NewerGuideActivity.class);
			ConfigManager.putBoolean(ConfigManager.GUIDE_SHOWED, true);
			ConfigManager.putString(ConfigManager.LAST_LOAD_VERSION, Build.CLIENT_VERSION);
		} else {
			intent.setClass(this, MainActivity.class);
		}

		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkNetworkState();
	}

	public interface ICallBackToMainPage {
		void natigateToMainPage();
	}

	class CallBackToMainPage implements ICallBackToMainPage {
		public void natigateToMainPage() {
			ToMainPage();
		}
	}
	
	private void appStartService(){
		startImageCopyServ();
	}
	
	private void startImageCopyServ(){
		Intent intent = new Intent(WelcomeActivity.this,ImageCopyService.class);
		startService(intent);
	}
	
}
