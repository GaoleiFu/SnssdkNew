package com.devdroid.snssdknew.database;

import java.util.List;
import android.content.Context;

import com.devdroid.snssdknew.model.SnssdkText;

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

    public List<SnssdkText> queryLockerInfo(int type) {
        return mSnssdkDatabaseHelper.querySnssdkInfo(type);
    }

    public void insertSnssdkItem(List<SnssdkText> insertList) {
        mSnssdkDatabaseHelper.insertSnssdkItem(insertList);
    }


    public void deleteSnssdkItem(SnssdkText packageName) {
        mSnssdkDatabaseHelper.deleteSnssdkItem(packageName);
    }

    public void updateSnssdkItem(SnssdkText packageName) {
        mSnssdkDatabaseHelper.updateSnssdkItem(packageName);
    }

}
