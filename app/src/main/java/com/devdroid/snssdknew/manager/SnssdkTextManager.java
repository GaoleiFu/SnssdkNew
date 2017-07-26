package com.devdroid.snssdknew.manager;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.eventbus.OnSnssdkLoadedEvent;
import com.devdroid.snssdknew.model.SnssdkText;
import com.devdroid.snssdknew.preferences.IPreferencesIds;
import com.devdroid.snssdknew.remote.LoadListener;
import com.devdroid.snssdknew.remote.RemoteSettingManager;
/**
 * 笑话业务管理类<br>
 */
public class SnssdkTextManager implements LoadListener {

    private static SnssdkTextManager sInstance;
    private List<SnssdkText> mSnssdks;
    private RemoteSettingManager mRemoteSettingManager;
    private int mType;

    private SnssdkTextManager() {
        mSnssdks = new ArrayList<>();
        init();
    }

    private void init() {
        mRemoteSettingManager = new RemoteSettingManager(this);
        mSnssdks = loadMore(0);
    }
    /**
     * 初始化单例,在程序启动时调用<br>
     */
    public static void initSingleton() {
        sInstance = new SnssdkTextManager();
    }

    /**
     * 获取单例.<br>
     */
    public static SnssdkTextManager getInstance() {
        return sInstance;
    }

    /**
     * 下拉刷新
     */
    public void freshMore(Context context, int type) {
        if( LauncherModel.getInstance().getSharedPreferencesManager().getBoolean(IPreferencesIds.DEFAULT_SHAREPREFERENCES_OFFLINE_MODE, false)) {
            SnssdknewApplication.getGlobalEventBus().post(new OnSnssdkLoadedEvent(0));
            return;
        }
        mRemoteSettingManager.connectToServer(context, type);
    }


    /**
     * 上拉数据库加载
     */
    private List<SnssdkText> loadMore(int type) {
        mType = type;
        return LauncherModel.getInstance().getSnssdkTextDao().queryLockerInfo(type);
    }

    /**
     * 数据联网加载成功
     */
    @Override
    public void loadLoaded(List<SnssdkText> snssdks) {
        mSnssdks.clear();
        mSnssdks.addAll(loadMore(mType));
        SnssdknewApplication.getGlobalEventBus().post(new OnSnssdkLoadedEvent(0));
    }

    public List<SnssdkText> getmSnssdks(int type) {
        mSnssdks.clear();
        mSnssdks.addAll(loadMore(type));
        return mSnssdks;
    }
}
