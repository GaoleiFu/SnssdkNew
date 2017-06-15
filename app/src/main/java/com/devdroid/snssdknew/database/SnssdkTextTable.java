package com.devdroid.snssdknew.database;
/**
 * 应用锁表
 *
 */
public class SnssdkTextTable {
	public static final String ID = "_id";
	public static final String COMPONENTNAME = "snssdk_content";

	//数据库2.0
	public static final String SNSSDK_TYPE = "snssdk_type";
	public static final String SNSSDK_COLLECTION = "snssdk_collection";

	/**
	 * ============== 表名 ==============
	 */
	public static final String TABLE_NAME = "snssdk_text";

	/**
	 * ============== 构造表的语句 ==============
	 */
	public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
			+ ID + " text, "
			+ COMPONENTNAME + " text,"
			+ SNSSDK_TYPE + " INTEGER,"
			+ SNSSDK_COLLECTION + " INTEGER,"
			+ " PRIMARY KEY("+ID+"))";
}
