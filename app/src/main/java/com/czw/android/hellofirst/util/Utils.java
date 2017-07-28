package com.czw.android.hellofirst.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.czw.android.hellofirst.model.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 工具类，里面有一些工具函数
 * @author czw
 * <br>2016-04-23
 */
public class Utils {

	/**
	 * 创建一个目录
	 * @param dirString
	 */
	public static void mkDir(String dirString) {
		File file = new File(dirString);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 初始化屏幕宽高（这个方法待研究）
	 */
	public static void initScreenWAndH(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		Constants.SCREEN_WIDTH = dm.widthPixels;
		Constants.SCREEN_HEIGHT = dm.heightPixels;
	}

	/**
	 * 删除文件及其子文件
	 * @param file
	 */
	public static void deleteFiles(File file) {
		File[] files = file.listFiles();
		//递归删除所有文件
		if (files != null) {
			for (File file2 : files) {
				deleteFiles(file2);
			}
		}
		file.delete();
	}

	public static byte[] chartobyte(char[] charArray) {
		byte[] byteArray = new byte[charArray.length * 2];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[2 * i] = (byte) (charArray[i] & 0xff00 >> 8);
			byteArray[2*i + 1] = (byte) (charArray[i] & 0xff);
		}
		return byteArray;
	}

	public static char[] bytetochar(byte[] byteArray) {
		char[] charArray = new char[byteArray.length / 2];
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = (char) (byteArray[i*2] & 0xff << 8 | byteArray[i*2 + 1] & 0xff);
		}
		return charArray;
	}

	public static int px2dip(Context context, int px) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5f);
	}

	public static int dip2px(Context context, int dip) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (dip * density + 0.5f);
	}

	@SuppressLint("NewApi")
	public static boolean saveJSONObjectToPath(boolean bEnCode, String path,
											   JSONObject json) {
		try (FileOutputStream os = new FileOutputStream(path))
		{
			byte[] source = null;
			source = json.toString().getBytes("utf-8");
			if (source != null) {
				os.write(source, 0, source.length);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@SuppressLint("NewApi")
	public static JSONObject getJSONObjectFromPath(boolean bEnCode, String path) {
		String rawJson = null;
		byte[] buffer = new byte[1024];
		int length = 0;
		JSONObject jsonObject = null;
		StringBuilder stringBuilder = new StringBuilder();
		try (FileInputStream is = new FileInputStream(path))
		{
			while ((length = is.read(buffer)) != -1) {
				stringBuilder.append(new String(buffer));
			}
			rawJson = stringBuilder.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			jsonObject = new JSONObject(rawJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

}
