package com.devdroid.snssdknew.database.helper;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;

import com.devdroid.snssdknew.database.BaseDataProvider;
import com.devdroid.snssdknew.database.params.DeletePamas;
import com.devdroid.snssdknew.database.params.InsertParams;
import com.devdroid.snssdknew.database.params.UpdatePamas;
import com.devdroid.snssdknew.database.table.SnssdkTextTable;
import com.devdroid.snssdknew.model.SnssdkText;

public class SnssdkDatabaseHelper {
	private BaseDataProvider mHelper = null;
	public SnssdkDatabaseHelper(BaseDataProvider dataProvider) {
		mHelper = dataProvider;
	}
    /**
     * 查询当前笑话信息
     */
    public List<SnssdkText> querySnssdkInfo(int collectionStatue) {
        List<SnssdkText> list = new ArrayList<>();
        Cursor cursor = mHelper.query(SnssdkTextTable.TABLE_NAME, null, SnssdkTextTable.SNSSDK_COLLECTION + "=?", new String[]{"" + collectionStatue}, SnssdkTextTable.ID);
        if (null != cursor) {
            try {
                while (cursor.moveToNext()) {
                    String component = cursor.getString(cursor.getColumnIndex(SnssdkTextTable.COMPONENTNAME));
                    int id = cursor.getInt(cursor.getColumnIndex(SnssdkTextTable.ID));
                    int snssdkCollection = cursor.getInt(cursor.getColumnIndex(SnssdkTextTable.SNSSDK_COLLECTION));
                    if (null != component) {
                        SnssdkText snssdkText = new SnssdkText();
                        snssdkText.setId(id);
                        snssdkText.setSnssdkContent(component);
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
    public void insertSnssdkItem(SnssdkText snssdkText) {
        ArrayList<InsertParams> list = new ArrayList<>();
        if (!checkExist("select * from " + SnssdkTextTable.TABLE_NAME + " where " + SnssdkTextTable.COMPONENTNAME + "='" + snssdkText.getSnssdkContent() + "'")) {
            ContentValues values = new ContentValues();
            values.put(SnssdkTextTable.COMPONENTNAME,snssdkText.getSnssdkContent());
            values.put(SnssdkTextTable.SNSSDK_COLLECTION,snssdkText.getIsCollection());
            InsertParams insert = new InsertParams(SnssdkTextTable.TABLE_NAME, values);
            list.add(insert);
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }
    public void insertSnssdkItem(List<SnssdkText> insertSnssdkList) {
        ArrayList<InsertParams> list = new ArrayList<>();
        for (SnssdkText snssdkText : insertSnssdkList) {
            if (!checkExist("select * from " + SnssdkTextTable.TABLE_NAME + " where " + SnssdkTextTable.COMPONENTNAME + "='" + snssdkText.getSnssdkContent() + "'")) {
                ContentValues values = new ContentValues();
                values.put(SnssdkTextTable.COMPONENTNAME,snssdkText.getSnssdkContent());
                values.put(SnssdkTextTable.SNSSDK_COLLECTION,snssdkText.getIsCollection());
                InsertParams insert = new InsertParams(SnssdkTextTable.TABLE_NAME, values);
                list.add(insert);
            }
        }
        if (!list.isEmpty()) {
            mHelper.insert(list);
        }
    }

    public void deleteSnssdkItem(SnssdkText snssdkText) {
    	ArrayList<DeletePamas> list = new ArrayList<>();
        	DeletePamas delete = new DeletePamas(SnssdkTextTable.TABLE_NAME, SnssdkTextTable.COMPONENTNAME + "=?", new String[]{snssdkText.getSnssdkContent()});
            list.add(delete);
        if (!list.isEmpty()) {
        	mHelper.delete(list);
        }
    }

    public void updateSnssdkItem(SnssdkText snssdkText) {
        ArrayList<UpdatePamas> list = new ArrayList<>();
        ContentValues values = new ContentValues();
        values.put(SnssdkTextTable.SNSSDK_COLLECTION,snssdkText.getIsCollection());
        UpdatePamas update = new UpdatePamas(SnssdkTextTable.TABLE_NAME, values, SnssdkTextTable.COMPONENTNAME + "=?", new String[]{snssdkText.getSnssdkContent() + ""});
        list.add(update);
        if (!list.isEmpty()) {
            mHelper.update(list);
        }
    }
}
