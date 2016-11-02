package com.devdroid.snssdknew.remote;

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
import com.devdroid.snssdknew.constant.ApiConstant;
import com.devdroid.snssdknew.eventbus.OnSnssdkLoadedEvent;
import com.devdroid.snssdknew.manager.SharedPreferencesManager;
import com.devdroid.snssdknew.preferences.IPreferencesIds;
import com.devdroid.snssdknew.utils.Machine;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;

/**
 * Created by wangying on 16/3/30.
 */
public class RemoteSettingManager {

	private static final String LOG_TAG = "ScanModeManager";
	LoadListener mLoadListener;

	public RemoteSettingManager(LoadListener loadListener) {
		mLoadListener = loadListener;
	}

	public void connectToServer(Context context) {
		String url = String.format(ApiConstant.GET_SNSSDK_URL_FRESH, Machine.getLocaldeviceId(context));
		TaskProcessor taskProcessor = new TaskProcessor(){
			@Override
			public void processResult(JSONObject result) {
				parseDataAndCache(result);
			}
		};
		SnssdkTask snssdkTask = new SnssdkTask(taskProcessor);
		snssdkTask.execute(url);
	}

	/**
	 * @param result
	 */
	private void parseDataAndCache(final JSONObject result) {
		Runnable okRunnable = new Runnable() {
			@Override
			public void run() {
				LinkedList<String> snssdksContent = new LinkedList<>();
				if(result != null) {
					JSONObject jSonData = result.optJSONObject("data");
					if (jSonData != null) {
						JSONArray jsonArrayData = jSonData.optJSONArray("data");
						if (jsonArrayData != null && jsonArrayData.length() > 0) {
							for (int i = 0; i < jsonArrayData.length(); i++) {
								JSONObject jsonGroup = jsonArrayData.optJSONObject(i).optJSONObject("group");
								if (jsonGroup != null) {
									String content = jsonGroup.optString("content");
									if (!content.isEmpty()) {
										snssdksContent.add(content);
									}
								}
							}
						}
					}
				}
				mLoadListener.loadLoaded(snssdksContent);
				LauncherModel.getInstance().getSnssdkTextDao().insertSnssdkItem(snssdksContent);
			}
		};
		new Thread(okRunnable).start();
	}
}
