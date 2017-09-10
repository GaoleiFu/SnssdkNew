package com.devdroid.snssdknew.utils;

import java.io.File;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
/**
 * 类名称：DevicesUtils
 */
public class DevicesUtils {
	/**
	 * 用户反馈时收集的设备信息<br>
	 * @param context   上下文
	 * @return     信息串
	 */
	public static String getFeedbackDeviceInfo(Context context,String type) {
		return ("\nFeedback Type:" + type) +
				"\nVersion Code:" + String.valueOf(SnssdkUtil.getVersionCode(context)) +
				"\nVersion Name:" + SnssdkUtil.getVersionName(context) +
				"\nUid=" + SnssdkUtil.getChannel(context) +
				"\nNetwork=" + Machine.getNetWorkType(context) +
				"\nProduct=" + android.os.Build.PRODUCT +
				"\nPhoneModel=" + android.os.Build.MODEL +
				"\nROM=" + android.os.Build.DISPLAY +
				"\nBoard=" + android.os.Build.BOARD +
				"\nDevice=" + android.os.Build.DEVICE +
				"\nDensity=" + String.valueOf(context.getResources().getDisplayMetrics().density) +
				"\nPackageName=" + context.getPackageName() +
				"\nAndroidVersion=" + android.os.Build.VERSION.RELEASE +
				"\nTotalMemSize="
				+ (DevicesUtils.getTotalInternalMemorySize() / 1024 / 1024)
				+ "MB" +
				"\nFreeMemSize="
				+ (DevicesUtils.getAvailableInternalMemorySize() / 1024 / 1024)
				+ "MB" +
				"\nRom App Heap Size="
				+ Integer
				.toString((int) (Runtime.getRuntime().maxMemory() / 1024L / 1024L))
				+ "MB" +
				"\nCountry=" + GoHttpHeadUtil.getLocal(context);
	}
	
	/**
	 * Calculates the total memory of the device. This is based on an inspection
	 * of the filesystem, which in android devices is stored in RAM.
	 * 
	 * @return Total number of bytes.
	 */
	private static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long totalBlocks = stat.getBlockCountLong();
		return totalBlocks * blockSize;
	}
	
	/**
	 * Calculates the free memory of the device. This is based on an inspection
	 * of the filesystem, which in android devices is stored in RAM.
	 * 
	 * @return Number of bytes available.
	 */
	private static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		return availableBlocks * blockSize;
	}

	static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
	}
	
}
