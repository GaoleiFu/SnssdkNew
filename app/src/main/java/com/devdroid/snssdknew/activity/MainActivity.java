package com.devdroid.snssdknew.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.adapter.SnssdkTextAdapter;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.base.BaseActivity;
import com.devdroid.snssdknew.eventbus.OnSnssdkLoadedEvent;
import com.devdroid.snssdknew.listener.NavigationItemSelectedListener;
import com.devdroid.snssdknew.listener.OnDismissAndShareListener;
import com.devdroid.snssdknew.listener.OnRecyclerItemClickListener;
import com.devdroid.snssdknew.manager.SnssdkTextManager;
import com.devdroid.snssdknew.model.SnssdkText;
import com.devdroid.snssdknew.preferences.IPreferencesIds;
import com.devdroid.snssdknew.utils.DividerItemDecoration;
import com.devdroid.snssdknew.utils.SimpleItemTouchHelperCallback;
import com.devdroid.snssdknew.utils.log.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener , OnRecyclerItemClickListener, OnDismissAndShareListener {
    private SnssdkTextAdapter mSnssdkAdapter;
    /**
     * 事件监听
     */
    private final Object mEventSubscriber = new Object() {
        @SuppressWarnings("unused")
        public void onEventMainThread(OnSnssdkLoadedEvent event) {
            mSnssdkAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwitchCompat mSwNetSetting;
    private int mType = 0;
    private List<SnssdkText> mSnssdkTexts;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

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
        } else if(item.getItemId() == R.id.action_resave){

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
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigation_view);
        mSwNetSetting = (SwitchCompat)navigationView.getHeaderView(0).findViewById(R.id.switch_nav_header_net);
        NavigationItemSelectedListener navigationItemSelectedListener = new NavigationItemSelectedListener(this, drawer);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mSnssdkTexts = SnssdkTextManager.getInstance().getmSnssdks(mType);
        mSnssdkAdapter = new SnssdkTextAdapter(this, mSnssdkTexts);
        mSnssdkAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mSnssdkAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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

    @Override
    public void onRefresh() {
        if(mType != 1) {
            mSwipeRefreshLayout.setRefreshing(true);
            SnssdkTextManager.getInstance().freshMore(this, mType);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void setSnssdkType(int type){
        mType = type;
        if(type == 0){
            mToolbar.setTitle(getString(R.string.nav_string_text));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
        } else if(type == 1){
            mToolbar.setTitle(getString(R.string.nav_string_collection_text));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
        } else if(type == 2){
            mToolbar.setTitle(getString(R.string.nav_string_image));
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if(type == 3){
            mToolbar.setTitle(getString(R.string.nav_string_collection_image));
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
        this.mType = type;
        mSnssdkTexts = SnssdkTextManager.getInstance().getmSnssdks(mType);
        mSnssdkAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(RecyclerView.Adapter parent, View v, int position, int showType) {
        if(showType == 2) {
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
        mRecyclerView.scrollToPosition(position);
        mSnssdkAdapter.setShowColumnChanged();
        mSnssdkAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        SnssdkText snssdkText = mSnssdkTexts.get(position);
        LauncherModel.getInstance().getSnssdkTextDao().deleteSnssdkItem(snssdkText);
        mSnssdkTexts.remove(position);
        mSnssdkAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemShare(int position, View currentView) {
        SnssdkText snssdkText = mSnssdkTexts.get(position);
        if(snssdkText.getIsCollection() == 1) {
            if(snssdkText.getSnssdkType() == 2) {  //分享图片
                shareImage(snssdkText.getSnssdkContent());
                mSnssdkAdapter.notifyItemChanged(position);
            } else {
                shareText(snssdkText.getSnssdkContent());
                mSnssdkAdapter.notifyItemChanged(position);
            }
        } else {
            snssdkText.setIsCollection(1);
            LauncherModel.getInstance().getSnssdkTextDao().updateSnssdkItem(snssdkText);
            mSnssdkTexts.remove(position);
            mSnssdkAdapter.notifyItemRemoved(position);
        }
    }

    private void shareText(String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TITLE,text);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        this.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    private void shareImage(final String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                try {
                    Bitmap bitmap = Glide.with(MainActivity.this).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    String[] filePaths = url.split("/");
                    String fileName = filePaths[filePaths.length - 1];
                    File cacheFile =  saveImageFile(bitmap, fileName);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cacheFile));
                    intent.setType("image/*");
                    intent.putExtra("sms_body", url);
                    MainActivity.this.startActivity(intent);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private File saveImageFile(Bitmap bmp, String fileName) {
        // 首先保存图片
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "snssdk"  + File.separator + "share";//注意小米手机必须这样获得public绝对路径
        File file = new File(filePath ,fileName);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
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
