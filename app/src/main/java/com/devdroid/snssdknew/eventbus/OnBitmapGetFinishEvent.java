package com.devdroid.snssdknew.eventbus;

import android.graphics.Bitmap;

/**
 * User:Gaolei  gurecn@gmail.com
 * Date:2017/7/26
 * I'm glad to share my knowledge with you all.
 */
public class OnBitmapGetFinishEvent {
	private Bitmap bitmap;
	private String fileName;

	public OnBitmapGetFinishEvent(Bitmap bitmap, String fileName) {
		this.bitmap = bitmap;
		this.fileName = fileName;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public String getFileName() {
		return fileName;
	}
}
