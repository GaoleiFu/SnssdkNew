package com.devdroid.snssdknew.remote;

import android.content.Context;
import android.text.TextUtils;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.constant.ApiConstant;
import com.devdroid.snssdknew.model.SnssdkText;
import com.devdroid.snssdknew.utils.Machine;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.LinkedList;

public class RemoteSettingManager {
	private LoadListener mLoadListener;
	public RemoteSettingManager(LoadListener loadListener) {
		mLoadListener = loadListener;
	}

	public void connectToServer(Context context, int type) {
		String url = String.format(ApiConstant.GET_SNSSDK_URL_FRESH, Machine.getLocaldeviceId(context));
		if(type == 2) {
			url = String.format(ApiConstant.GET_SNSSDK_IMAGE_URL_FRESH, Machine.getLocaldeviceId(context));
		}
		TaskProcessor taskProcessor = new TaskProcessor(){
			@Override
			public void processResult(JSONObject result, int type) {
				parseDataAndCache(result, type);
			}
		};
		SnssdkTask snssdkTask = new SnssdkTask(taskProcessor);
		snssdkTask.execute(url, type + "");
	}

	private void parseDataAndCache(final JSONObject result, final int type) {
		Runnable okRunnable = new Runnable() {
			@Override
			public void run() {
				LinkedList<SnssdkText> snssdksContent = new LinkedList<>();
				if(result != null) {
					JSONObject jSonData = result.optJSONObject("data");
					if (jSonData != null) {
						JSONArray jsonArrayData = jSonData.optJSONArray("data");
						if (jsonArrayData != null && jsonArrayData.length() > 0) {
							for (int i = 0; i < jsonArrayData.length(); i++) {
								JSONObject jsonGroup = jsonArrayData.optJSONObject(i).optJSONObject("group");
								if (jsonGroup != null) {
									if(type == 0) {
										String content = jsonGroup.optString("content");
										if (!content.isEmpty()) {
											SnssdkText snssdkText = new SnssdkText();
											snssdkText.setSnssdkType(type);
											snssdkText.setIsCollection(0);
											snssdkText.setSnssdkContent(content);
											snssdksContent.add(snssdkText);
										}
									} else if(type == 2){
										int mediaType = jsonGroup.optInt("media_type");
										JSONObject largeCover;
										if(mediaType < 3) {
											largeCover = jsonGroup.optJSONObject("large_image");
										} else if(mediaType == 3){
											largeCover = jsonGroup.optJSONObject("medium_cover");
										} else {
											break;
										}
										JSONArray urlList = largeCover.optJSONArray("url_list");
										String url = urlList.optJSONObject(0).optString("url");
										if (!TextUtils.isEmpty(url)) {
											SnssdkText snssdkText = new SnssdkText();
											snssdkText.setSnssdkType(type);
											snssdkText.setIsCollection(0);
											snssdkText.setSnssdkContent(url);
											snssdksContent.add(snssdkText);
										}
									}
								}
							}
						}
					}
				}
				LauncherModel.getInstance().getSnssdkTextDao().insertSnssdkItem(snssdksContent);
				mLoadListener.loadLoaded(snssdksContent);
			}
		};
		new Thread(okRunnable).start();
	}
}
