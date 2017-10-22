package com.devdroid.snssdknew.database;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.devdroid.snssdknew.database.params.DeletePamas;
import com.devdroid.snssdknew.database.params.InsertParams;
import com.devdroid.snssdknew.database.params.UpdatePamas;

/**
 * 基础数据库操作类(所有需要操作数据库的接口需要实现该类)
 * 类名称：BaseDataProvider
 * 创建人：makai
 * 修改时间：2014年10月14日 下午4:10:36
 * @version 1.0.0
 *
 */
public class BaseDataProvider {
	protected Object mLock;
	protected BaseDatabaseHelper mDBHelper;
	
	public BaseDataProvider(Context context) {
		mDBHelper = new BaseDatabaseHelper(context);
		mLock = new Object();
	}
	
	public Cursor query(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		synchronized (mLock) {
			return mDBHelper.query(table, projection, selection, selectionArgs, sortOrder);
		}
	}
	public Cursor query(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder, String limit) {
		synchronized (mLock) {
			return mDBHelper.query(table, projection, selection, selectionArgs, sortOrder, limit);
		}
	}
	
	public boolean delete(List<DeletePamas> list) {
		synchronized (mLock) {
			boolean isSuccess = false;
			try {
				isSuccess = mDBHelper.delete(list);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			return isSuccess;
		}
	}

	public boolean insert(List<InsertParams> list) {
		synchronized (mLock) {
			boolean isSuccess = false;
			try {
				mDBHelper.insert(list);
				isSuccess = true;
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			return isSuccess;
		}
	}
	
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		synchronized (mLock) {
			return mDBHelper.rawQuery(sql, selectionArgs);
		}
	}
	
	public Cursor rawQuery(String sql) {
		return rawQuery(sql, null);
	}

	/**
	 * 更新
	 * update(这里用一句话描述这个方法的作用)
	 */
	public boolean update(String tableName, ContentValues values, String selection) {
		synchronized (mLock) {
			boolean isSuccess = false;
			try {
				int count = mDBHelper.update(tableName, values, selection, null);
				if (count > 0) {
					isSuccess = true;
				}
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			return isSuccess;
		}
	}

	/**
	 * 更新
	 * update(这里用一句话描述这个方法的作用)
	 */
	public boolean update(String tableName, ContentValues values, String selection, String[] selectionArgs) {
		synchronized (mLock) {
			boolean isSuccess = false;
			try {
				int count = mDBHelper.update(tableName, values, selection, selectionArgs);
				if (count > 0) {
					isSuccess = true;
				}
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			return isSuccess;
		}
	}

	public boolean update(List<UpdatePamas> list) {
		synchronized (mLock) {
			boolean isSuccess = false;
			try {
				isSuccess = mDBHelper.update(list);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			return isSuccess;
		}
	}
}
