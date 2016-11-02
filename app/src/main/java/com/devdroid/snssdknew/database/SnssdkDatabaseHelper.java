package com.devdroid.snssdknew.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SnssdkDatabaseHelper {

	public static final String TAG = SnssdkDatabaseHelper.class.getSimpleName();

	private BaseDataProvider mHelper = null;

	public SnssdkDatabaseHelper(Context context, BaseDataProvider dataProvider) {
		mHelper = dataProvider;
	}
    /**
     * 查询当前加锁应用信息
     * @return
     */
    public List<String> querySnssdkInfo() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, null, null, SnssdkTextTable.ID + " DESC");
        if (null != cursor) {
            try {
                while (cursor.moveToNext()) {
                    String component = cursor.getString(cursor.getColumnIndex(SnssdkTextTable.COMPONENTNAME));
                    if (null != component) {
                        list.add(component);
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
    public void insertSnssdkItem(List<String> insertSnssdkList) {
        ArrayList<InsertParams> list = new ArrayList<InsertParams>();
        for (String pkgName : insertSnssdkList) {
            if (!checkExist("select * from " + SnssdkTextTable.TABLE_NAME + " where " + SnssdkTextTable.COMPONENTNAME + "='" + pkgName + "'")) {
                ContentValues values = new ContentValues();
                values.put(SnssdkTextTable.COMPONENTNAME,pkgName);
                InsertParams insert = new InsertParams(SnssdkTextTable.TABLE_NAME, values);
                list.add(insert);
            }
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }

    public void deleteSnssdkItem(List<String> deleteSnssdkList) {
        ArrayList<DeletePamas> list = new ArrayList<DeletePamas>();
        for (String pkgName : deleteSnssdkList) {
            DeletePamas delete = new DeletePamas(SnssdkTextTable.TABLE_NAME, SnssdkTextTable.COMPONENTNAME + "=?", new String[]{pkgName});
            list.add(delete);
        }
        if (!list.isEmpty()) {
            mHelper.delete(list);
        }
    }

    public void insertSnssdkItem(String pkgName) {
        ArrayList<InsertParams> list = new ArrayList<InsertParams>();
        if (!checkExist("select * from " + SnssdkTextTable.TABLE_NAME + " where " + SnssdkTextTable.COMPONENTNAME + "='" + pkgName + "'")) {
            ContentValues values = new ContentValues();
            values.put(SnssdkTextTable.COMPONENTNAME,pkgName);
            InsertParams insert = new InsertParams(SnssdkTextTable.TABLE_NAME, values);
            list.add(insert);
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }

    public void deleteSnssdkItem(String pkgName) {
    	ArrayList<DeletePamas> list = new ArrayList<DeletePamas>();
        	DeletePamas delete = new DeletePamas(SnssdkTextTable.TABLE_NAME, SnssdkTextTable.COMPONENTNAME + "=?", new String[]{pkgName});
            list.add(delete);
        if (!list.isEmpty()) {
        	mHelper.delete(list);
        }
    }
}
