package com.devdroid.snssdknew.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.devdroid.snssdknew.model.SnssdkText;

public class SnssdkDatabaseHelper {

	public static final String TAG = SnssdkDatabaseHelper.class.getSimpleName();

	private BaseDataProvider mHelper = null;

	public SnssdkDatabaseHelper(Context context, BaseDataProvider dataProvider) {
		mHelper = dataProvider;
	}
    /**
     * 查询当前笑话信息
     * @return
     */
    public List<SnssdkText> querySnssdkInfo() {
        List<SnssdkText> list = new ArrayList<>();
        Cursor cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, null, null, SnssdkTextTable.ID + " DESC");
        if (null != cursor) {
            try {
                while (cursor.moveToNext()) {
                    String component = cursor.getString(cursor.getColumnIndex(SnssdkTextTable.COMPONENTNAME));
                    int snssdkType = cursor.getInt(cursor.getColumnIndex(SnssdkTextTable.SNSSDK_TYPE));
                    int snssdkCollection = cursor.getInt(cursor.getColumnIndex(SnssdkTextTable.SNSSDK_COLLECTION));
                    if (null != component) {
                        SnssdkText snssdkText = new SnssdkText();
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
    public void insertSnssdkItem(List<SnssdkText> insertSnssdkList) {
        ArrayList<InsertParams> list = new ArrayList<InsertParams>();
        for (SnssdkText pkgName : insertSnssdkList) {
            if (!checkExist("select * from " + SnssdkTextTable.TABLE_NAME + " where " + SnssdkTextTable.COMPONENTNAME + "='" + pkgName + "'")) {
                ContentValues values = new ContentValues();
                values.put(SnssdkTextTable.COMPONENTNAME,pkgName.getSnssdkContent());
                values.put(SnssdkTextTable.SNSSDK_TYPE,pkgName.getSnssdkType());
                values.put(SnssdkTextTable.SNSSDK_COLLECTION,pkgName.getIsCollection());
                InsertParams insert = new InsertParams(SnssdkTextTable.TABLE_NAME, values);
                list.add(insert);
            }
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }

    public void insertSnssdkItem(SnssdkText pkgName) {
        ArrayList<InsertParams> list = new ArrayList<InsertParams>();
        if (!checkExist("select * from " + SnssdkTextTable.TABLE_NAME + " where " + SnssdkTextTable.COMPONENTNAME + "='" + pkgName + "'")) {
            ContentValues values = new ContentValues();
            values.put(SnssdkTextTable.COMPONENTNAME,pkgName.getSnssdkContent());
            values.put(SnssdkTextTable.SNSSDK_TYPE,pkgName.getSnssdkType());
            values.put(SnssdkTextTable.SNSSDK_COLLECTION,pkgName.getIsCollection());
            InsertParams insert = new InsertParams(SnssdkTextTable.TABLE_NAME, values);
            list.add(insert);
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }

    public void deleteSnssdkItem(SnssdkText pkgName) {
    	ArrayList<DeletePamas> list = new ArrayList<>();
        	DeletePamas delete = new DeletePamas(SnssdkTextTable.TABLE_NAME, SnssdkTextTable.COMPONENTNAME + "=?", new String[]{pkgName.getSnssdkContent()});
            list.add(delete);
        if (!list.isEmpty()) {
        	mHelper.delete(list);
        }
    }
}
