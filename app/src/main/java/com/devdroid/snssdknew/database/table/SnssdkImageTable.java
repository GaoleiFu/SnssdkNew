package com.devdroid.snssdknew.database.table;

/**
 * 图片数据库
 * Created by Gaolei on 2017/10/22.
 */

public class SnssdkImageTable {

    public static final String ID = "id";
    public static final String SNSSDK_IMAGE_URL = "snssdk_image_url";
    public static final String SNSSDK_IMAGE_WIDTH = "snssdk_image_width";
    public static final String SNSSDK_IMAGE_HEIGHT = "snssdk_image_height";
    public static final String SNSSDK_IMAGE_COLLECTION = "snssdk_collection";

    /**
     * ============== 表名 ==============
     */
    public static final String TABLE_NAME = "snssdk_image";

    /**
     * ============== 构造表的语句 ==============
     */
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + ID + " integer primary key autoincrement, "
            + SNSSDK_IMAGE_URL + " text,"
            + SNSSDK_IMAGE_WIDTH + " INTEGER,"
            + SNSSDK_IMAGE_HEIGHT + " INTEGER,"
            + SNSSDK_IMAGE_COLLECTION + " INTEGER)";
}
