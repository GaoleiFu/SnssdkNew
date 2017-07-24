package com.devdroid.snssdknew.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;

import com.devdroid.snssdknew.model.BaseSnssdkModel;
import com.devdroid.snssdknew.model.SnssdkText;

class SnssdkDatabaseHelper {
	private BaseDataProvider mHelper = null;
	SnssdkDatabaseHelper(BaseDataProvider dataProvider) {
		mHelper = dataProvider;
	}
    /**
     * 查询当前笑话信息
     */
    List<SnssdkText> querySnssdkInfo(int type) {
        List<SnssdkText> list = new ArrayList<>();
        Cursor cursor;
        if(type == 3) {
            cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, SnssdkTextTable.SNSSDK_COLLECTION + "=? and " + SnssdkTextTable.SNSSDK_TYPE + "=?", new String[]{"1", "2"}, SnssdkTextTable.ID + " DESC");
        } else if(type == 2) {
            cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, SnssdkTextTable.SNSSDK_COLLECTION + "=? and " + SnssdkTextTable.SNSSDK_TYPE + "=?", new String[]{"0", "2"}, SnssdkTextTable.ID + " DESC");
        } else if(type == 1) {
            cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, SnssdkTextTable.SNSSDK_COLLECTION + "=? and " + SnssdkTextTable.SNSSDK_TYPE + "=?", new String[]{"" + type, "0"}, SnssdkTextTable.ID + " DESC");
        }  else {
            cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, SnssdkTextTable.SNSSDK_COLLECTION + "=? and " + SnssdkTextTable.SNSSDK_TYPE + "=?", new String[]{"0", "0"}, SnssdkTextTable.ID + " DESC");
        }
        if (null != cursor) {
            try {
                while (cursor.moveToNext()) {
                    String component = cursor.getString(cursor.getColumnIndex(SnssdkTextTable.COMPONENTNAME));
                    int id = cursor.getInt(cursor.getColumnIndex(SnssdkTextTable.ID));
                    int snssdkType = cursor.getInt(cursor.getColumnIndex(SnssdkTextTable.SNSSDK_TYPE));
                    int snssdkCollection = cursor.getInt(cursor.getColumnIndex(SnssdkTextTable.SNSSDK_COLLECTION));
                    if (null != component) {
                        SnssdkText snssdkText = new SnssdkText();
                        snssdkText.setId(id);
                        snssdkText.setSnssdkContent(component);
                        snssdkText.setIsCollection(snssdkCollection);
                        snssdkText.setSnssdkType(snssdkType);
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
    void insertSnssdkItem(List<SnssdkText> insertSnssdkList) {
        ArrayList<InsertParams> list = new ArrayList<>();
        for (SnssdkText snssdkText : insertSnssdkList) {
            if (!checkExist("select * from " + SnssdkTextTable.TABLE_NAME + " where " + SnssdkTextTable.COMPONENTNAME + "='" + snssdkText + "'")) {
                ContentValues values = new ContentValues();
                values.put(SnssdkTextTable.COMPONENTNAME,snssdkText.getSnssdkContent());
                values.put(SnssdkTextTable.SNSSDK_TYPE,snssdkText.getSnssdkType());
                values.put(SnssdkTextTable.SNSSDK_COLLECTION,snssdkText.getIsCollection());
                InsertParams insert = new InsertParams(SnssdkTextTable.TABLE_NAME, values);
                list.add(insert);
            }
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }

    void deleteSnssdkItem(SnssdkText snssdkText) {
    	ArrayList<DeletePamas> list = new ArrayList<>();
        	DeletePamas delete = new DeletePamas(SnssdkTextTable.TABLE_NAME, SnssdkTextTable.COMPONENTNAME + "=?", new String[]{snssdkText.getSnssdkContent()});
            list.add(delete);
        if (!list.isEmpty()) {
        	mHelper.delete(list);
        }
    }

    void updateSnssdkItem(SnssdkText snssdkText) {
        ArrayList<UpdatePamas> list = new ArrayList<>();
        ContentValues values = new ContentValues();
        values.put(SnssdkTextTable.SNSSDK_COLLECTION,snssdkText.getIsCollection());
        UpdatePamas update = new UpdatePamas(SnssdkTextTable.TABLE_NAME, values, SnssdkTextTable.ID + "=?", new String[]{snssdkText.getId() + ""});
        list.add(update);
        if (!list.isEmpty()) {
            mHelper.update(list);
        }
    }
}
