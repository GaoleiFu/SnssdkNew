package com.devdroid.snssdknew.database;

import java.util.List;
import android.content.Context;

/**
 * 关于Snssdk信息的数据管理
 * @author zhanghuijun
 *
 */
public class SnssdkTextDao {

    private SnssdkDatabaseHelper mSnssdkDatabaseHelper = null;
    /**
     * Context
     */
    private Context mContext = null;

    public SnssdkTextDao(Context context, BaseDataProvider dataProvider) {
        mContext = context;
        mSnssdkDatabaseHelper = new SnssdkDatabaseHelper(context, dataProvider);
    }

    public List<String> queryLockerInfo() {
        return mSnssdkDatabaseHelper.querySnssdkInfo();
    }

    public void insertSnssdkItem(List<String> insertList) {
        mSnssdkDatabaseHelper.insertSnssdkItem(insertList);
    }

    public void deleteSnssdkItem(List<String> deleteList) {
        mSnssdkDatabaseHelper.deleteSnssdkItem(deleteList);
    }


    public void deleteSnssdkItem(String packageName) {
        mSnssdkDatabaseHelper.deleteSnssdkItem(packageName);
    }

    public void insertSnssdkItem(String packageName) {
        mSnssdkDatabaseHelper.insertSnssdkItem(packageName);
    }

}
