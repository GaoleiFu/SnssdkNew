package com.devdroid.snssdknew.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import java.util.Locale;

/**
 * 主要是go http协议头需要的一些参数值获取方法<br>
 * 
 * @author yuanweinan
 *
 */
public class GoHttpHeadUtil {

    public static String getVirtualIMEI(Context context) {
        return DevicesUtils.getIMEI(context);
    }

    /**
     * 从res/raw/uid.txt文件中获取渠道id
     *
     * @param context
     * @return
     */
    public static String getUid(Context context) {
        return SnssdkUtil.getChannel(context);
    }

    /**
     * <br>
     * 功能简述:获取Android ID的方法 <br>
     * 功能详细描述: <br>
     * 注意:
     *
     * @return
     */
    public static String getAndroidId(Context context) {
        String androidId = null;
        if (context != null) {
            androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }

    /**
     * 获取客户端版本号
     *
     * @param context
     */
    public static int getVersionCode(Context context) {
        return SnssdkUtil.getVersionCode(context);
    }


    /**
     * 获取SIM卡所在的国家
     *
     * @author xiedezhi
     * @param context
     * @return 当前手机sim卡所在的国家，如果没有sim卡，取本地语言代表的国家
     */
    public static String getLocal(Context context) {
        String local = null;
        try {
            TelephonyManager telManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (telManager != null) {
                local = telManager.getSimCountryIso();
            }
        } catch (Throwable e) {
        }
        if (TextUtils.isEmpty(local)) {
            local = Locale.getDefault().getCountry().toUpperCase(Locale.US);
        }
        if (TextUtils.isEmpty(local)) {
            local = "US";
        }
        return local;
    }

    /**
     * 获取用户运营商代码
     *
     * @return
     */
    public static String getSimOperator(Context context) {
        String simOperator = "000";
        try {
            if (context != null) {
                // 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
                TelephonyManager manager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                simOperator = manager.getSimOperator();
            }
        } catch (Throwable e) {
        }
        // ====MODIFY====Zhu Qiyong====2014.09.23====
        // 当客户端运营商编码不存在时，simOperator可能被设置为空，服务器端不会受理
        // 故如果simOperator字符串为空，则置为“000”
        return TextUtils.isEmpty(simOperator) ? "000" : simOperator;
        // ====END MODIFY====
    }

    /**
     * 获取设备DIP
     *
     * @param context
     * @return
     */
    public static int getDeviceDIP(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wMgr = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wMgr.getDefaultDisplay().getMetrics(dm);
        return dm != null ? dm.densityDpi : 0;
    }

    /**
     * 获取客户端版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return SnssdkUtil.getVersionName(context);
    }

    /**
     * 获取语言和国家地区的方法<br>
     * 格式: SIM卡方式：cn 系统语言方式：zh-CN<br>
     *
     * @return
     */
    public static String getLanguage(Context context) {
        return Locale.getDefault().getLanguage().toLowerCase(Locale.US);
    }

    /**
     * 通过应用程序包名判断程序是否安装的方法
     *
     * @param packageName
     *            应用程序包名
     * @return 程序已安装返回TRUE，否则返回FALSE
     */
    public static boolean isApplicationExsit(Context context, String packageName) {
        boolean result = false;
        if (context != null && packageName != null) {
            try {
                // context.createPackageContext(packageName,
                // Context.CONTEXT_IGNORE_SECURITY);
                context.getPackageManager().getPackageInfo(packageName,
                        PackageManager.GET_SHARED_LIBRARY_FILES);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 是否存在google电子市场
     *
     * @param context
     * @return
     */
    public static boolean isExistGooglePlay(Context context) {
        String googleMarketPkgName = "com.android.vending";
        return isApplicationExsit(context, googleMarketPkgName);
    }

    /**
     * 获取当前网络类型，wifi，GPRS，3G，4G
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        // build Network conditions
        String ret = "";
        try {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            if (networkinfo != null
                    && networkinfo.getType() == ConnectivityManager.TYPE_WIFI) {
                ret = "WIFI";
            } else if (networkinfo != null
                    && networkinfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                int subtype = networkinfo.getSubtype();
                switch (subtype) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        // 2G
                        ret = "2G" /*
                                 * + "(typeid = " + networkinfo.getType() +
                                 * "  typename = " + networkinfo.getTypeName() +
                                 * "  subtypeid = " + networkinfo.getSubtype() +
                                 * "  subtypename = " +
                                 * networkinfo.getSubtypeName() + ")"
                                 */;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        // 3G,4G
                        ret = "3G/4G" /*
                                 * + "(typeid = " + networkinfo.getType() +
                                 * "  typename = " + networkinfo.getTypeName() +
                                 * "  subtypeid = " + networkinfo.getSubtype() +
                                 * "  subtypename = " +
                                 * networkinfo.getSubtypeName() + ")"
                                 */;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        // unknow
                        ret = "UNKNOW" /*
                                     * + "(typeid = " + networkinfo.getType() +
                                     * "  typename = " +
                                     * networkinfo.getTypeName() +
                                     * "  subtypeid = " +
                                     * networkinfo.getSubtype() +
                                     * "  subtypename = " +
                                     * networkinfo.getSubtypeName() + ")"
                                     */;
                        break;
                }
            } else {
                ret = "UNKNOW" /*
                                 * + "(typeid = " + networkinfo.getType() +
                                 * "  typename = " + networkinfo.getTypeName() +
                                 * ")"
                                 */;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
