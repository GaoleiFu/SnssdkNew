package com.devdroid.snssdknew.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 集中与应用本身相关的方法
 * @author lishen
 */
public class SnssdkUtil {
	private static String sChannel;
	/**
	 * 获取桌面渠道号的方法
	 * @param ctx
	 * @return
	 */
	public static String getChannel(Context ctx) {
		if (sChannel == null) {
			try {
				ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				sChannel = appInfo.metaData.getString("channel");
			} catch (Exception e) {
				sChannel = "";
			}
		}
		return sChannel;
	}

	/**
	 * 是否为官方包<br>
	 * @param ctx
	 * @return
	 */
	public static boolean isOfficial(Context ctx) {
		return "200".equals(getChannel(ctx));
	}

	/**
	 * 获取版本名
	 * 
	 * @return
	 */
	public static String getVersionName(Context context) {
		String name = "";
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			name = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int code = 0;
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			code = info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 获取语言包请求的product id <li>
	 * 产品id请查看后台：http://pbasi18n01.rmz.gomo.com:8088/admin
	 *
	 * @return
	 */
	public static String getLangProductID() {
		return "1004";
	}

}
