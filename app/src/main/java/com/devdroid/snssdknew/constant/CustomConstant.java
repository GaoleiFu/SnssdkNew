package com.devdroid.snssdknew.constant;

public class CustomConstant {
    private CustomConstant(){}

    public final static int SNSSDK_TYPE_TEXT = 0;
    public final static int SNSSDK_TYPE_IMAGE = 1;
    public final static int SNSSDK_ALL = 0;
    public final static int SNSSDK_COLLECTION = 1;
    
    //数据备份相关变量
    public static final String COMMAND_BACKUP_INTERNAL_STORAGE = "backup_database_internal_storage";//备份到内存
    public static final String COMMAND_RESTORE_INTERNAL_STORAGE = "restroe_database_internal_storage";//读取内存数据
}
