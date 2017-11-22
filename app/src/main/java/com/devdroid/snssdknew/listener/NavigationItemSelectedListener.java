package com.devdroid.snssdknew.listener;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.activity.AboutActivity;
import com.devdroid.snssdknew.activity.FeedbackActivity;
import com.devdroid.snssdknew.activity.MainActivity;
import com.devdroid.snssdknew.constant.CustomConstant;
import com.devdroid.snssdknew.database.DatabaseBackupTask;
import com.devdroid.snssdknew.utils.DevicesUtils;

/**
 * 侧滑菜单事件监听
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public class NavigationItemSelectedListener  implements NavigationView.OnNavigationItemSelectedListener {
    private MainActivity mAppCompatActivity;
    private DrawerLayout mDrawerLayout;
    public NavigationItemSelectedListener(MainActivity appCompatActivity, DrawerLayout drawer) {
        this.mAppCompatActivity = appCompatActivity;
        this.mDrawerLayout = drawer;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_text :
                mAppCompatActivity.setSnssdkType(0);
                break;
            case R.id.nav_gallery :
                mAppCompatActivity.setSnssdkType(2);
                break;
            case R.id.nav_collection_text :
                mAppCompatActivity.setSnssdkType(1);
                break;
            case R.id.nav_collection_image :
                mAppCompatActivity.setSnssdkType(3);
                break;
            case R.id.nav_data_export :
                new DatabaseBackupTask(mAppCompatActivity).execute(CustomConstant.COMMAND_BACKUP_INTERNAL_STORAGE);
                Toast.makeText(mAppCompatActivity, mAppCompatActivity.getString(R.string.ime_setting_backuping), Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_data_import :
                showFileChooser();
                break;
            case R.id.nav_share :
                shareText();
                break;
            case R.id.nav_send :
                mAppCompatActivity.startActivity(new Intent(mAppCompatActivity, FeedbackActivity.class));
                mAppCompatActivity.overridePendingTransition(R.anim.activity_in_from_right,R.anim.activity_out_from_left);
                break;
            case R.id.nav_about :
                mAppCompatActivity.startActivity(new Intent(mAppCompatActivity, AboutActivity.class));
                mAppCompatActivity.overridePendingTransition(R.anim.activity_in_from_right,R.anim.activity_out_from_left);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    
    /**
     * 打开文件选择器
     */
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            mAppCompatActivity.startActivityForResult(Intent.createChooser(intent, "Select a File to Import"), 1111);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 分享应用
     */
    public void shareText() {
        Intent shareIntent = new Intent();
        String shareAppText = mAppCompatActivity.getResources().getString(R.string.share_app_text);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TITLE,shareAppText);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareAppText);
        shareIntent.setType("text/plain");
        mAppCompatActivity.startActivity(Intent.createChooser(shareIntent, "分享到"));
        mAppCompatActivity.overridePendingTransition(R.anim.activity_in_from_right,R.anim.activity_out_from_left);
    }
}
