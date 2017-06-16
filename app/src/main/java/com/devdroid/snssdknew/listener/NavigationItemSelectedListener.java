package com.devdroid.snssdknew.listener;

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
            case R.id.nav_collection :
                mAppCompatActivity.setSnssdkType(1);
                break;
            case R.id.nav_share :
                shareText();
                break;
            case R.id.nav_send :
                mAppCompatActivity.startActivity(new Intent(mAppCompatActivity, FeedbackActivity.class));
                break;
            case R.id.nav_about :
                mAppCompatActivity.startActivity(new Intent(mAppCompatActivity, AboutActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
    }
}
