package com.devdroid.snssdknew.database;

import java.util.List;

import com.devdroid.snssdknew.model.BaseSnssdkModel;
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

    public List<SnssdkText> queryLockerInfo(int type, int page) {
        return mSnssdkDatabaseHelper.querySnssdkInfo(type, page);
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
