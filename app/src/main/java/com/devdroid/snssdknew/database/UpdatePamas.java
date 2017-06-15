package com.devdroid.snssdknew.database;

import android.content.ContentValues;

/**
 * SQL更新参数
 * 
 * 类名称：UpdatePamas
 * 类描述：
 * 创建人：makai
 * 修改人：makai
 * 修改时间：2015年1月6日 下午3:22:59
 * 修改备注：
 * @version 1.0.0
 *
 */
public class UpdatePamas extends InsertParams {
	
	private String mSelection;
	private String[] mWhereArgs;
	
	public UpdatePamas(String mTableName, ContentValues mContentValues, String mSelection, String[] whereArgs) {
		super(mTableName, mContentValues);
		this.mSelection = mSelection;
		this.mWhereArgs = whereArgs;
	}
	
	public UpdatePamas(String mTableName, ContentValues mContentValues, String mSelection) {
		this(mTableName, mContentValues, mSelection, null);
	}
	
	public String getSelection() {
		return mSelection;
	}
	
	public String[] getWhereArgs() {
		return mWhereArgs;
	}
	
	@Override
	public String toString() {
		return super.toString() + " , mSelection : " + mSelection;
	}
}
