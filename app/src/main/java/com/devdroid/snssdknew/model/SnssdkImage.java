package com.devdroid.snssdknew.model;

/**
 * 图片段子
 * Created by Gaolei on 2017/10/22.
 */

public class SnssdkImage extends BaseSnssdkModel {
    private int id;
    private int isCollection;   //是否收藏   0:未收藏
    private String snssdkUrl;   //内容或网址
    private int width;
    private int height;
    public SnssdkImage() {
    }

    public SnssdkImage(String snssdkUrl, int width, int height, int isCollection) {
        this.isCollection = isCollection;
        this.snssdkUrl = snssdkUrl;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }

    public String getSnssdkUrl() {
        return snssdkUrl;
    }

    public void setSnssdkUrl(String snssdkUrl) {
        this.snssdkUrl = snssdkUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
