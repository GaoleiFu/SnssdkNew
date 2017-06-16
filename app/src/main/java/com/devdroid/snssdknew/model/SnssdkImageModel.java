package com.devdroid.snssdknew.model;

/**
 * User:Gaolei  gurecn@gmail.com
 * Date:2017/6/16
 * I'm glad to share my knowledge with you all.
 */
public class SnssdkImageModel extends BaseSnssdkModel {
	private int id;
	private int snssdkType;
	private String snssdkContent;
	private String imageUrl;
	public SnssdkImageModel() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSnssdkType() {
		return snssdkType;
	}

	public void setSnssdkType(int snssdkType) {
		this.snssdkType = snssdkType;
	}

	public String getSnssdkContent() {
		return snssdkContent;
	}

	public void setSnssdkContent(String snssdkContent) {
		this.snssdkContent = snssdkContent;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
