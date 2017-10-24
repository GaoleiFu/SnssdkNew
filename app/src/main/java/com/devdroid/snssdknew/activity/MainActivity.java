package com.devdroid.snssdknew.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.SwitchCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.base.BaseActivity;
import com.devdroid.snssdknew.constant.CustomConstant;
import com.devdroid.snssdknew.eventbus.OnBitmapGetFinishEvent;
import com.devdroid.snssdknew.listener.NavigationItemSelectedListener;
import com.devdroid.snssdknew.model.SnssdkText;
import com.devdroid.snssdknew.preferences.IPreferencesIds;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity{
    /**
     * 事件监听
     */
    private final Object mEventSubscriber = new Object() {
        @SuppressWarnings("unused")
        public void onEventMainThread(OnBitmapGetFinishEvent event) {
            File cacheFile =  saveImageFile(event.getBitmap(), event.getFileName());
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cacheFile));
            intent.setType("image/*");
            intent.putExtra("sms_body", event.getFileName());
            MainActivity.this.startActivity(intent);
        }
    };
    private SwitchCompat mSwNetSetting;
    private SnssdkText oldSnssdkText;
    private SnssdkFragment currentFragment;
    private SnssdkFragment mFragmentText;
    private SnssdkFragment mFragmentTextCollection;
    private SnssdkFragment mFragmentImage;
    private SnssdkFragment mFragmentImageCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        intData();
        SnssdknewApplication.getGlobalEventBus().register(mEventSubscriber);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==0 || item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else if(item.getItemId() == R.id.action_resave && oldSnssdkText != null){
            LauncherModel.getInstance().getSnssdkTextDao().insertSnssdkItem(oldSnssdkText);
            oldSnssdkText = null;
            item.setVisible(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void intData() {
        if( !LauncherModel.getInstance().getSharedPreferencesManager().getBoolean(IPreferencesIds.DEFAULT_SHAREPREFERENCES_OFFLINE_MODE, false)) {
            mSwNetSetting.setChecked(false);
        } else {
            mSwNetSetting.setChecked(true);
        }
        mSwNetSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    LauncherModel.getInstance().getSharedPreferencesManager().commitBoolean(IPreferencesIds.DEFAULT_SHAREPREFERENCES_OFFLINE_MODE, true);
                } else {
                    LauncherModel.getInstance().getSharedPreferencesManager().commitBoolean(IPreferencesIds.DEFAULT_SHAREPREFERENCES_OFFLINE_MODE, false);
                }
            }
        });

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentText = SnssdkFragment.newInstance(CustomConstant.SNSSDK_TYPE_TEXT, CustomConstant.SNSSDK_ALL);
        mFragmentTextCollection = SnssdkFragment.newInstance(CustomConstant.SNSSDK_TYPE_TEXT, CustomConstant.SNSSDK_COLLECTION);
        mFragmentImage = SnssdkFragment.newInstance(CustomConstant.SNSSDK_TYPE_IMAGE, CustomConstant.SNSSDK_ALL);
        mFragmentImageCollection = SnssdkFragment.newInstance(CustomConstant.SNSSDK_TYPE_IMAGE, CustomConstant.SNSSDK_COLLECTION);
        fragmentTransaction.add(R.id.fragment_container, mFragmentText).commit();
        currentFragment = mFragmentText;
    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        mSwNetSetting = navigationView.getHeaderView(0).findViewById(R.id.switch_nav_header_net);
        NavigationItemSelectedListener navigationItemSelectedListener = new NavigationItemSelectedListener(this, drawer);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SnssdknewApplication.getGlobalEventBus().unregister(mEventSubscriber);
    }

    public void setSnssdkType(int type){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(currentFragment);
        switch (type){
            case 0:
                currentFragment = mFragmentText;
                break;
            case 1:
                currentFragment = mFragmentTextCollection;
                break;
            case 2:
                currentFragment = mFragmentImage;
                break;
            case 3:
                currentFragment = mFragmentImageCollection;
                break;
        }
        if(!currentFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, currentFragment);
        } else {
            fragmentTransaction.show(currentFragment);
        }
        fragmentTransaction.commit();
    }
    
    private File saveImageFile(Bitmap bmp, String fileName) {
        String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "snssdk" + File.separator + "share";
        File filePath = new File(imagePath);
        if (!filePath.isDirectory()) {
            filePath.mkdirs();
        }
        // 首先保存图片
        File file = new File(filePath ,fileName);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
                fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
