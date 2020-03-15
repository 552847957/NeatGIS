package gisluq.lib.Util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 跟App相关的辅助类
 * 
 * @author zhy
 * 
 */
public class AppUtils {

	/**
	 * 应用功能完全正常运行的所有权限
	 */
	public static final String[] REQUIRED_ALL_PERMISSIONS = new String[] {
			Manifest.permission.INTERNET,//互联网
			Manifest.permission.ACCESS_NETWORK_STATE,//网络状态
			Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入存储
			Manifest.permission.ACCESS_FINE_LOCATION,//位置信息
			Manifest.permission.ACCESS_WIFI_STATE,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.CAMERA //相机
	};


	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");

	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}





}
