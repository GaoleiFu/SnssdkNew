package com.devdroid.snssdknew.database.dao;

import com.devdroid.snssdknew.database.BaseDataProvider;
import com.devdroid.snssdknew.database.helper.ImageDatabaseHelper;
import com.devdroid.snssdknew.model.SnssdkImage;
import java.util.List;


public class SnssdkImageDao {

    private ImageDatabaseHelper mSnssdkDatabaseHelper = null;
    public SnssdkImageDao(BaseDataProvider dataProvider) {
        mSnssdkDatabaseHelper = new ImageDatabaseHelper(dataProvider);
    }

    public List<SnssdkImage> queryLockerInfo(int type) {
        return mSnssdkDatabaseHelper.querySnssdkInfo(type);
    }

    public void insertSnssdkItem(List<SnssdkImage> insertList) {
        mSnssdkDatabaseHelper.insertSnssdkItem(insertList);
    }

    public void insertSnssdkItem(SnssdkImage snssdkText) {
        mSnssdkDatabaseHelper.insertSnssdkItem(snssdkText);
    }
    public void deleteSnssdkItem(SnssdkImage snssdkText) {
        mSnssdkDatabaseHelper.deleteSnssdkItem(snssdkText);
    }
}
