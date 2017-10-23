package com.devdroid.snssdknew.application;

import android.app.Application;
import android.content.Context;

import com.devdroid.snssdknew.constant.ApiConstant;
import com.devdroid.snssdknew.utils.CrashHandler;

import de.greenrobot.event.EventBus;

/**
 * Created with IntelliJ IDEA.
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public class SnssdknewApplication extends Application {
    private static SnssdknewApplication sInstance;
//    private static RequestQueue sGLOBAL_REQUEST_QUEUE;
    private final static EventBus GLOBAL_EVENT_BUS = EventBus.getDefault();
    public SnssdknewApplication() {
        sInstance = this;
    }
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
//        sGLOBAL_REQUEST_QUEUE = Volley.newRequestQueue(this);
        CrashHandler.getInstance().init(this, ApiConstant.LOG_DIR);
        LauncherModel.initSingleton(this);
    }
    /**
     * 获取一个全局的RequestQueue实例<br>
     *
     * @return
     */
//    public static RequestQueue getRequestQueue() {
//        return sGLOBAL_REQUEST_QUEUE;
//    }

    /**
     * 获取一个全局的EventBus实例<br>
     *
     * @return
     */
    public static EventBus getGlobalEventBus() {
        return GLOBAL_EVENT_BUS;
    }

    /**
     * 使用全局EventBus post一个事件<br>
     *
     * @param event
     */
    public static void postEvent(Object event) {
        GLOBAL_EVENT_BUS.post(event);
    }

    /**
     * 使用全局EventBus post一个Sticky事件<br>
     *
     * @param event
     */
    public static void postStickyEvent(Object event) {
        GLOBAL_EVENT_BUS.postSticky(event);
    }
}
