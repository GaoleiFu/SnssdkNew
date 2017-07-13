package com.devdroid.snssdknew.model;

/**
 * 文本Snssdk实体类
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public class SnssdkText extends BaseSnssdkModel {

	private int id;
	private int snssdkType;   //笑话类型   0:文字，2：图片
	private int isCollection;   //是否收藏   0:未收藏
	private String snssdkContent;   //内容或网址
	public SnssdkText() {
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

	public int getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(int isCollection) {
		this.isCollection = isCollection;
	}

	public String getSnssdkContent() {
		return snssdkContent;
	}

	public void setSnssdkContent(String snssdkContent) {
		this.snssdkContent = snssdkContent;
	}
}
