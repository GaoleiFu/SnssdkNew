package com.devdroid.snssdknew.database.dao;

import java.util.List;

import com.devdroid.snssdknew.database.BaseDataProvider;
import com.devdroid.snssdknew.database.helper.SnssdkDatabaseHelper;
import com.devdroid.snssdknew.model.SnssdkText;

/**
 * 关于Snssdk信息的数据管理
 * @author zhanghuijun
 *
 */
public class SnssdkTextDao {
    private SnssdkDatabaseHelper mSnssdkDatabaseHelper = null;
    public SnssdkTextDao(BaseDataProvider dataProvider) {
        mSnssdkDatabaseHelper = new SnssdkDatabaseHelper(dataProvider);
    }

    public List<SnssdkText> queryLockerInfo(int collectionStatue) {
        return mSnssdkDatabaseHelper.querySnssdkInfo(collectionStatue);
    }

    public void insertSnssdkItem(List<SnssdkText> insertList) {
        mSnssdkDatabaseHelper.insertSnssdkItem(insertList);
    }

    public void insertSnssdkItem(SnssdkText snssdkText) {
        mSnssdkDatabaseHelper.insertSnssdkItem(snssdkText);
    }
    public void deleteSnssdkItem(SnssdkText snssdkText) {
        mSnssdkDatabaseHelper.deleteSnssdkItem(snssdkText);
    }

    public void updateSnssdkItem(SnssdkText snssdkText) {
        mSnssdkDatabaseHelper.updateSnssdkItem(snssdkText);
    }

}
