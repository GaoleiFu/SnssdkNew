package com.devdroid.snssdknew.database.helper;

import android.content.ContentValues;
import android.database.Cursor;
import com.devdroid.snssdknew.database.BaseDataProvider;
import com.devdroid.snssdknew.database.params.DeletePamas;
import com.devdroid.snssdknew.database.params.InsertParams;
import com.devdroid.snssdknew.database.table.SnssdkImageTable;
import com.devdroid.snssdknew.database.table.SnssdkTextTable;
import com.devdroid.snssdknew.model.SnssdkImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片段子
 * Created by Gaolei on 2017/10/22.
 */

public class ImageDatabaseHelper {
    private BaseDataProvider mHelper = null;
    public ImageDatabaseHelper(BaseDataProvider dataProvider) {
        mHelper = dataProvider;
    }
    /**
     * 查询当前笑话信息
     */
    public List<SnssdkImage> querySnssdkInfo(int type) {
        List<SnssdkImage> list = new ArrayList<>();
        Cursor cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, SnssdkTextTable.SNSSDK_COLLECTION + "=?", new String[]{"" + type}, SnssdkTextTable.ID);
        if (null != cursor) {
            try {
                while (cursor.moveToNext()) {
                    String component = cursor.getString(cursor.getColumnIndex(SnssdkTextTable.COMPONENTNAME));
                    String snssdkUrl = cursor.getString(cursor.getColumnIndex(SnssdkImageTable.SNSSDK_IMAGE_URL));
                    int snssdkWidth = cursor.getInt(cursor.getColumnIndex(SnssdkImageTable.SNSSDK_IMAGE_WIDTH));
                    int snssdkHeight = cursor.getInt(cursor.getColumnIndex(SnssdkImageTable.SNSSDK_IMAGE_HEIGHT));
                    int snssdkCollection = cursor.getInt(cursor.getColumnIndex(SnssdkImageTable.SNSSDK_IMAGE_COLLECTION));
                    if (null != component) {
                        SnssdkImage snssdkText = new SnssdkImage();
                        snssdkText.setSnssdkUrl(snssdkUrl);
                        snssdkText.setWidth(snssdkWidth);
                        snssdkText.setHeight(snssdkHeight);
                        snssdkText.setIsCollection(snssdkCollection);
                        list.add(snssdkText);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return list;
    }
    /**
     * 检查是否存在该内容
     */
    private boolean checkExist(String sql) {
        Cursor cursor = mHelper.rawQuery(sql);
        if (null != cursor) {
            try {
                if (cursor.moveToNext()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return false;
    }
    public void insertSnssdkItem(SnssdkImage snssdk) {
        ArrayList<InsertParams> list = new ArrayList<>();
        if (!checkExist("select * from " + SnssdkImageTable.TABLE_NAME + " where " + SnssdkImageTable.SNSSDK_IMAGE_URL + "='" + snssdk.getSnssdkUrl() + "'")) {
            ContentValues values = new ContentValues();
            values.put(SnssdkImageTable.SNSSDK_IMAGE_URL,snssdk.getSnssdkUrl());
            values.put(SnssdkImageTable.SNSSDK_IMAGE_WIDTH,snssdk.getWidth());
            values.put(SnssdkImageTable.SNSSDK_IMAGE_HEIGHT,snssdk.getHeight());
            values.put(SnssdkImageTable.SNSSDK_IMAGE_COLLECTION,snssdk.getIsCollection());
            InsertParams insert = new InsertParams(SnssdkImageTable.TABLE_NAME, values);
            list.add(insert);
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }
    public void insertSnssdkItem(List<SnssdkImage> insertSnssdkList) {
        ArrayList<InsertParams> list = new ArrayList<>();
        for (SnssdkImage snssdk : insertSnssdkList) {
            if (!checkExist("select * from " + SnssdkImageTable.TABLE_NAME + " where " + SnssdkImageTable.SNSSDK_IMAGE_URL + "='" + snssdk.getSnssdkUrl() + "'")) {
                ContentValues values = new ContentValues();
                values.put(SnssdkImageTable.SNSSDK_IMAGE_URL,snssdk.getSnssdkUrl());
                values.put(SnssdkImageTable.SNSSDK_IMAGE_WIDTH,snssdk.getWidth());
                values.put(SnssdkImageTable.SNSSDK_IMAGE_HEIGHT,snssdk.getHeight());
                values.put(SnssdkImageTable.SNSSDK_IMAGE_COLLECTION,snssdk.getIsCollection());
                InsertParams insert = new InsertParams(SnssdkImageTable.TABLE_NAME, values);
                list.add(insert);
            }
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }

    public void deleteSnssdkItem(SnssdkImage snssdkText) {
        ArrayList<DeletePamas> list = new ArrayList<>();
        DeletePamas delete = new DeletePamas(SnssdkImageTable.TABLE_NAME, SnssdkImageTable.SNSSDK_IMAGE_URL + "=?", new String[]{snssdkText.getSnssdkUrl()});
        list.add(delete);
        if (!list.isEmpty()) {
            mHelper.delete(list);
        }
    }
}
