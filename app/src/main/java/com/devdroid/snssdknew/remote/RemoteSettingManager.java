package com.devdroid.snssdknew.remote;

import android.content.Context;
import android.text.TextUtils;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.constant.ApiConstant;
import com.devdroid.snssdknew.constant.CustomConstant;
import com.devdroid.snssdknew.model.SnssdkImage;
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
		if(type == CustomConstant.SNSSDK_TYPE_IMAGE) {
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
				if (result != null) {
					JSONObject jSonData = result.optJSONObject("data");
					if (jSonData != null) {
						JSONArray jsonArrayData = jSonData.optJSONArray("data");
						if (jsonArrayData != null && jsonArrayData.length() > 0) {
							LinkedList<SnssdkText> snssdksContent = new LinkedList<>();
							LinkedList<SnssdkImage> snssdksImage = new LinkedList<>();
							if (type == CustomConstant.SNSSDK_TYPE_TEXT) {
								for (int i = 0; i < jsonArrayData.length(); i++) {
									JSONObject jsonGroup = jsonArrayData.optJSONObject(i).optJSONObject("group");
									if (jsonGroup != null) {
										String content = jsonGroup.optString("content");
										if (!content.isEmpty()) {
											SnssdkText snssdkText = new SnssdkText();
											snssdkText.setSnssdkType(type);
											snssdkText.setIsCollection(0);
											snssdkText.setSnssdkContent(content);
											snssdksContent.add(snssdkText);
										}
									}
									LauncherModel.getInstance().getSnssdkTextDao().insertSnssdkItem(snssdksContent);
								}
							} else if (type == CustomConstant.SNSSDK_TYPE_IMAGE) {
								for (int i = 0; i < jsonArrayData.length(); i++) {
									JSONObject jsonGroup = jsonArrayData.optJSONObject(i).optJSONObject("group");
									if (jsonGroup != null) {
										int mediaType = jsonGroup.optInt("media_type");
										JSONObject largeCover;
										if (mediaType < 3) {
											largeCover = jsonGroup.optJSONObject("large_image");
										} else if (mediaType == 3) {
											largeCover = jsonGroup.optJSONObject("medium_cover");
										} else {
											break;
										}
										int height = jsonGroup.optInt("video_height");
										int width = jsonGroup.optInt("video_width");
										JSONArray urlList = largeCover.optJSONArray("url_list");
										String url = urlList.optJSONObject(0).optString("url");
										if (!TextUtils.isEmpty(url)) {
											SnssdkImage snssdkText = new SnssdkImage();
											snssdkText.setHeight(height);
											snssdkText.setWidth(width);
											snssdkText.setIsCollection(0);
											snssdkText.setSnssdkUrl(url);
											snssdksImage.add(snssdkText);
										}
									}
									LauncherModel.getInstance().getSnssdkImage().insertSnssdkItem(snssdksImage);
								}
							}
						}
					}
				mLoadListener.loadLoaded(type, null);
				}
			}
		};
		new Thread(okRunnable).start();
	}
}
