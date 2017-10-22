package com.devdroid.snssdknew.application;

import android.content.Context;

import com.devdroid.snssdknew.database.BaseDataProvider;
import com.devdroid.snssdknew.database.dao.SnssdkImageDao;
import com.devdroid.snssdknew.database.dao.SnssdkTextDao;
import com.devdroid.snssdknew.manager.SharedPreferencesManager;

/**
 * Created with IntelliJ IDEA.
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public class LauncherModel {
    private static LauncherModel sInstance;
    private final SharedPreferencesManager mSharedPreferencesManager;
    private final SnssdkTextDao mSnssdkTextDao;
    private final SnssdkImageDao mSnssdkImageDao;

    private LauncherModel(Context context) {
        Context mContext = context.getApplicationContext();
        mSharedPreferencesManager = new SharedPreferencesManager(mContext);
        BaseDataProvider dataProvider = new BaseDataProvider(mContext);
        mSnssdkTextDao = new SnssdkTextDao(dataProvider);
        mSnssdkImageDao = new SnssdkImageDao(dataProvider);
    }
    /**
     * 初始化单例,在程序启动时调用<br>
     */
    public static void initSingleton(Context context) {
        sInstance = new LauncherModel(context);
    }
    /**
     * 获取实例<br>
     */
    public static LauncherModel getInstance() {
        return sInstance;
    }

    public SharedPreferencesManager getSharedPreferencesManager() {
        return mSharedPreferencesManager;
    }
    public SnssdkTextDao getSnssdkTextDao() {
        return mSnssdkTextDao;
    }
    public SnssdkImageDao getSnssdkImage() {
        return mSnssdkImageDao;
    }
}
