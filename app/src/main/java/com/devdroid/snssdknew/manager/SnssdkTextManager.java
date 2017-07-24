package com.devdroid.snssdknew.manager;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.eventbus.OnSnssdkLoadedEvent;
import com.devdroid.snssdknew.model.BaseSnssdkModel;
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
        if(checkPermissions(context)) {
            mRemoteSettingManager.connectToServer(context, type);
        }
    }

    private boolean checkPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if(!(context instanceof Activity)) return false;
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 10010);
                int i = 0;
                do {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED && i >= 25) {
                        Toast.makeText(context, context.getResources().getString(R.string.string_no_phone_state), Toast.LENGTH_SHORT).show();
                        SnssdknewApplication.getGlobalEventBus().post(new OnSnssdkLoadedEvent(0));
                        return false;
                    } else if (checkCallPhonePermission == PackageManager.PERMISSION_GRANTED) {
                        break;
                    }
                } while (i++ < 25);
            }
        }
        return true;
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
