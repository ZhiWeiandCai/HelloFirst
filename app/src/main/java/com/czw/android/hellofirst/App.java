package com.czw.android.hellofirst;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import com.czw.android.hellofirst.model.Constants;
import com.czw.android.hellofirst.util.LogHelper;
import com.czw.android.hellofirst.util.Utils;

import java.lang.reflect.Field;

public class App extends Application {

	private static final String TAG = "App";
	private static App sAppInstance;

	public static App getInstance() {
		return sAppInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sAppInstance = this;
		init();
	}

	private void init() {
		Utils.mkDir(Constants.ROOT_PATH_EXT);
		//获取屏幕分辨率（可用作填充内容的区域）
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		Constants.DENSITY = displayMetrics.density;
		Constants.DESIRED_WIDTH = displayMetrics.widthPixels;
		Constants.DESIRED_HEIGHT = displayMetrics.heightPixels;
		TypedValue tv = new TypedValue();
		if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
				true)) {
			Constants.ACTIONBAR_HEIGHT = TypedValue.complexToDimensionPixelSize(
					tv.data, getResources().getDisplayMetrics());
		}
		Constants.STATUSBAR_HEIGHT = getStatusBarHeight();
		Constants.SCREEN_WIDTH = Constants.DESIRED_WIDTH;
		Constants.SCREEN_HEIGHT = Constants.DESIRED_HEIGHT + getNavigationBarHeight();
		LogHelper.i(TAG, "width:" + Constants.DESIRED_WIDTH + ",height:" + Constants.DESIRED_HEIGHT
				+ ",actionBarHeight:" + Constants.ACTIONBAR_HEIGHT +
				",statusBarheight:" + Constants.STATUSBAR_HEIGHT
				+ ",density:" + Constants.DENSITY +
				",navigationBarHeight:" + getNavigationBarHeight());
	}

	private int getStatusBarHeight(){
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			int sbar = getResources().getDimensionPixelSize(x);
			return sbar;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	private int getNavigationBarHeight() {
		Resources resources = getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}

	/**
	 * 判断网络是否连接
	 * @return 0未连接， 1移动网络， 2wifi
	 */
	public int getNetworkConnectionType(){
		final ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null/* || !connectivityManager.getBackgroundDataSetting()*/) {
			return 0;
		}

		final NetworkInfo wifi =connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo mobile =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(wifi.isAvailable()){
			return 2;
		}else if(mobile.isAvailable()) {
			return 1;
		} else{
			return 0;
		}
	}

	private Toast mToast;

	public void showToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public void showToast(int resId){
		try{
			if(mToast == null){
				mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
			}else{
				mToast.setText(resId);
				mToast.setDuration(Toast.LENGTH_SHORT);
			}
			mToast.show();
		}catch(Exception e){
			e.printStackTrace();
			try{
				if(mToast != null){
					mToast.cancel();
					mToast = null;
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}

}
