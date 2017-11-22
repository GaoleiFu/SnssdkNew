package com.devdroid.snssdknew.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.constant.CustomConstant;
import com.devdroid.snssdknew.eventbus.OnUpdateProgressBackup;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseBackupTask extends AsyncTask<String, String, Integer> {
    private Context mContext;
    public DatabaseBackupTask(Context mContext) {
        this.mContext = mContext;
    }
    protected Integer doInBackground(String... params) {
        String command = params[0];
        if (command.equals(CustomConstant.COMMAND_BACKUP_INTERNAL_STORAGE)) {  //备份到内存
            try {
                File exportDirInternalStorage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
                if (!exportDirInternalStorage.exists()) {
                    exportDirInternalStorage.mkdirs();
                }
                File dbFile = new File("/data/data/com.devdroid.snssdknew/databases/", "boost.db");
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String fileName = mContext.getString(R.string.app_name) + "_" + sdf.format(date) + ".back";
                File backupdb = new File(exportDirInternalStorage, fileName);
                if(!backupdb.exists()) backupdb.createNewFile();
                if(dbFile.exists()) {
                    fileCopy(dbFile, backupdb);
                }
                SnssdknewApplication.getGlobalEventBus().post(new OnUpdateProgressBackup(100, 1));
            } catch (Exception e) {
                e.printStackTrace();
                SnssdknewApplication.getGlobalEventBus().post(new OnUpdateProgressBackup(-1, 1));
            }
        } else if (command.equals(CustomConstant.COMMAND_RESTORE_INTERNAL_STORAGE)) { //读取内存数据
            String restoreDataName = params[1];
            try {
                File backupdb = new File(restoreDataName);
                if(backupdb.exists()) {
                    @SuppressLint("SdCardPath") File dbBackFile = new File("/data/data/com.devdroid.snssdknew/databases/", "boost.db");
                    if(!dbBackFile.exists()) {
                        try {
                            dbBackFile.getParentFile().mkdirs();
                            dbBackFile.createNewFile();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    fileCopy(backupdb, dbBackFile);
                }
                SnssdknewApplication.getGlobalEventBus().post(new OnUpdateProgressBackup(100, 0));
            } catch (Exception e) {
                e.printStackTrace();
                SnssdknewApplication.getGlobalEventBus().post(new OnUpdateProgressBackup(-1, 0));
            }
        }
        return null;
    }
    private void fileCopy(File dbFile, File backup) throws IOException {
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) inChannel.close();
            outChannel.close();
        }
    }
}