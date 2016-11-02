package com.devdroid.snssdknew.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Surface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;

/**
 * 设备基础数据信息类
 * 
 * @author huyong
 * 
 */
public class Machine {

	public final static int NETTYPE_MOBILE = 0; // 中国移动 //CHECKSTYLE IGNORE
	public final static int NETTYPE_UNICOM = 1; // 中国联通 //CHECKSTYLE IGNORE
	public final static int NETTYPE_TELECOM = 2; // 中国电信 //CHECKSTYLE IGNORE

	private final static String LEPHONEMODEL[] = { "3GW100", "3GW101", "3GC100", "3GC101", "K1" };

	private final static String M9BOARD[] = { "m9", "M9" };

	// 硬件加速
	public static final int LAYER_TYPE_NONE = 0x00000000;
	public static final int LAYER_TYPE_SOFTWARE = 0x00000001;
	public static final int LAYER_TYPE_HARDWARE = 0x00000002;
	public static boolean sLevelUnder3 = Build.VERSION.SDK_INT < 11; // 版本小于3.0  //CHECKSTYLE IGNORE
	private static Method sAcceleratedMethod = null;

	public static String getIMSI(Context context) {
		String simOperator = "000";
		try {
			if (context != null) {
				// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
				TelephonyManager manager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				simOperator = manager.getSimOperator();
			}
		} catch (Throwable e) {
			// TODO: handle exception
		}

		return simOperator;
	}

	/**
	 * <br>功能简述:获取Android ID的方法
	 * <br>功能详细描述:
	 * <br>注意:
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String androidId = "";
		if (context != null) {
			androidId = Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.ANDROID_ID);
		}
		return androidId;
	}

	/**
	 * 获取语言和国家地区的方法 格式:zh-CN
	 * 
	 * @return 当前国家区域及语言
	 */
	public static String language() {
		String ret = null;
		try {
			ret = String.format("%s-%s", Locale.getDefault().getLanguage(), Locale.getDefault()
					.getCountry());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalFormatException e) {
			e.printStackTrace();
		}
		return null == ret ? "error" : ret;
	}

	public static boolean isLephone() {
		final String model = Build.MODEL;
		if (model == null) {
			return false;
		}

		final int size = LEPHONEMODEL.length;
		for (int i = 0; i < size; i++) {
			if (model.equals(LEPHONEMODEL[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前网络是否可以使用
	 *
	 * @author huyong
	 * @param context
	 * @return
	 */
	public static boolean isNetworkOK(Context context) {
		boolean result = false;
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {
					result = true;
				}
			}
		}

		return result;
	}

	/**
	 * 判断当前运营商是否在指定数组内
	 *
	 * @param areaArray
	 * @return
	 */
	public static boolean isLocalAreaCodeMatch(Context context, String[] areaArray) {
		if (null == areaArray || areaArray.length == 0) {
			return false;
		}

		// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
		TelephonyManager manager = (TelephonyManager) context.getSystemService(
				Context.TELEPHONY_SERVICE);
		String simOperator = manager.getSimOperator();
		if (null == simOperator) {
			return false;
		}

		boolean ret = false;
		for (String areastring : areaArray) {
			if (null == areastring || areastring.length() == 0
					|| areastring.length() > simOperator.length()) {
				continue;
			} else {
				String subString = simOperator.substring(0, areastring.length());
				if (subString.equals(areastring)) {
					ret = true;
					break;
				} else {
					continue;
				}
			}
		}
		return ret;
	}

	public static boolean isCnUser(Context context) {
		boolean result = false;
		if (context != null) {
			// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			// SIM卡状态
			boolean simCardUnable = manager.getSimState() != TelephonyManager.SIM_STATE_READY;
			String simOperator = manager.getSimOperator();

			if (simCardUnable || TextUtils.isEmpty(simOperator)) {
				// 如果没有SIM卡的话simOperator为null，然后获取本地信息进行判断处理
				// 获取当前国家或地区，如果当前手机设置为简体中文-中国，则使用此方法返回CN
				String curCountry = Locale.getDefault().getCountry();
				if (curCountry != null && curCountry.contains("CN")) {
					// 如果获取的国家信息是CN，则返回TRUE
					result = true;
				} else {
					// 如果获取不到国家信息，或者国家信息不是CN
					result = false;
				}
			} else if (simOperator.startsWith("460")) {
				// 如果有SIM卡，并且获取到simOperator信息。
				/**
				 * 中国大陆的前5位是(46000) 中国移动：46000、46002 中国联通：46001 中国电信：46003
				 */
				result = true;
			}
		}

		return result;

	}

	/**
	 * 获取Android中的Linux内核版本号
	 *
	 */
	public static String getLinuxKernel() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("cat /proc/version");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (null == process) {
			return null;
		}

		// get the output line
		InputStream outs = process.getInputStream();
		InputStreamReader isrout = new InputStreamReader(outs);
		BufferedReader brout = new BufferedReader(isrout, 8 * 1024);  //CHECKSTYLE IGNORE
		String result = "";
		String line;

		// get the whole standard output string
		try {
			while ((line = brout.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!result.equals("")) {
			String keyword = "version ";
			int index = result.indexOf(keyword);
			line = result.substring(index + keyword.length());
			if (null != line) {
				index = line.indexOf(" ");
				return line.substring(0, index);
			}
		}
		return null;
	}

	static public int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.parseInt(Build.VERSION.SDK);
		} catch (NumberFormatException e) {
		}
		return version;
	}

	/**
	 * 获取网络类型
	 *
	 * @author huyong
	 * @param context
	 * @return 1 for 移动，2 for 联通，3 for 电信，-1 for 不能识别
	 */
	public static int getNetWorkType(Context context) {
		int netType = -1;
		// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simOperator = manager.getSimOperator();
		if (simOperator != null) {
			if (simOperator.startsWith("46000") || simOperator.startsWith("46002")) {
				// 因为移动网络编号46000下的IMSI已经用完，
				// 所以虚拟了一个46002编号，134/159号段使用了此编号
				// 中国移动
				netType = NETTYPE_MOBILE;
			} else if (simOperator.startsWith("46001")) {
				// 中国联通
				netType = NETTYPE_UNICOM;
			} else if (simOperator.startsWith("46003")) {
				// 中国电信
				netType = NETTYPE_TELECOM;
			}
		}
		return netType;
	}

	/**
	 * 获取程序的版本
	 *
	 * @param context
	 *            Context
	 * @return 程序自身的版本
	 */
	public static String getVersionName(Context context) {
		String versionName = null;
		String packageName = context.getPackageName();
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionName;
	}

	/**
	 * 获取程序的版本versionCode
	 *
	 * @param context
	 *            Context
	 * @return 程序自身的版本versionCode
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		String packageName = context.getPackageName();
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			versionCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionCode;
	}

	public static boolean hasMarket(Context context) {
		String uriString = "market://search?q=" + context.getPackageName();
		Uri uri = Uri.parse(uriString);
		Intent mainIntent = new Intent(Intent.ACTION_VIEW, uri);
		mainIntent.setPackage("com.android.vending");
		mainIntent.addCategory(Intent.CATEGORY_DEFAULT);

		List<ResolveInfo> mApps = context.getPackageManager().queryIntentActivities(mainIntent, 0);
		if (mApps == null || mApps.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static final int ORIENTATION_REVERSE_PORTRAIT = 9;
	public static final int ORIENTATION_REVERSE_LANDSCAPE = 8;

	public static int mapConfigurationOriActivityInfoOri(Activity activity, int configOri) {
		final Display d = activity.getWindowManager().getDefaultDisplay();
		int naturalOri = Configuration.ORIENTATION_LANDSCAPE;
		switch (d.getRotation()) {
			case Surface.ROTATION_0 :
			case Surface.ROTATION_180 :
				// We are currently in the same basic orientation as the natural
				// orientation
				naturalOri = configOri;
				break;
			case Surface.ROTATION_90 :
			case Surface.ROTATION_270 :
				// We are currently in the other basic orientation to the
				// natural
				// orientation
				naturalOri = (configOri == Configuration.ORIENTATION_LANDSCAPE)
						? Configuration.ORIENTATION_PORTRAIT
						: Configuration.ORIENTATION_LANDSCAPE;
				break;
		}

		int[] oriMap = { ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ORIENTATION_REVERSE_PORTRAIT,
				ORIENTATION_REVERSE_LANDSCAPE };
		// Since the map starts at portrait, we need to offset if this device's
		// natural orientation
		// is landscape.
		int indexOffset = 0;
		if (naturalOri == Configuration.ORIENTATION_LANDSCAPE) {
			indexOffset = 1;
		}
		return oriMap[(d.getRotation() + indexOffset) % 4];
	}

	/**
	 * <br>功能简述:	判断当前机器是否为M9
	 * <br>功能详细描述:
	 * <br>注意:
	 * @return
	 */
	public static boolean isM9() {
		return isPhone(M9BOARD);
	}

	private static boolean isPhone(String[] boards) {
		final String board = Build.BOARD;
		if (board == null) {
			return false;
		}
		final int size = boards.length;
		for (int i = 0; i < size; i++) {
			if (board.equals(boards[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取SIM卡所在的国家
	 * 
	 * @author xiedezhi
	 * @param context
	 * @return 当前手机sim卡所在的国家，如果没有sim卡，取本地语言代表的国家
	 */
	public static String getSimCountry(Context context) {
		String simCountryIso = "";
		try {
			// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			// SIM卡状态
			simCountryIso = manager.getSimCountryIso();

		} catch (Exception e) {
			// TODO: handle exception
		}
		if (simCountryIso == null || simCountryIso.trim().equals("")) {
			return getCountry(context);
		} else if (simCountryIso.contains(",")) {
			String[] simCountryIsoArrary = simCountryIso.split(",");
			if (simCountryIsoArrary != null && simCountryIsoArrary.length > 1) {
				if (simCountryIsoArrary[0] != null && !simCountryIsoArrary[0].trim().equals("")) {
					return simCountryIsoArrary[0];
				} else if (simCountryIsoArrary[1] != null
						&& !simCountryIsoArrary[1].trim().equals("")) {
					return simCountryIsoArrary[1];
				} else {
					return getCountry(context);
				}
			}
		}
		return simCountryIso;
	}

	/**
	 * 获取当前的国家
	 * 
	 * @author zhoujun
	 * @param context
	 * @return
	 */
	public static String getCountry(Context context) {
		return context.getResources().getConfiguration().locale.getCountry();
	}

	/**
	* 获取机器唯一标识
	* @param context
	* @return
	*/
	public static String getLocaldeviceId(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		if (deviceId == null || deviceId.trim().length() == 0) {
			deviceId = getLocalMacAddress(context);
		}
		return deviceId ;
	}

	/**
	 * 获取mac地址
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wifi == null) {
			return String.valueOf(System.currentTimeMillis());
		}
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
}
