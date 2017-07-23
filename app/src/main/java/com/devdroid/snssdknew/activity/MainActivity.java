package com.devdroid.snssdknew.activity;

import android.os.Bundle;
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
import android.view.View;
import android.widget.CompoundButton;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.adapter.SnssdkTextAdapter;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.base.BaseActivity;
import com.devdroid.snssdknew.eventbus.OnSnssdkLoadedEvent;
import com.devdroid.snssdknew.listener.NavigationItemSelectedListener;
import com.devdroid.snssdknew.listener.OnRecyclerItemClickListener;
import com.devdroid.snssdknew.manager.SnssdkTextManager;
import com.devdroid.snssdknew.model.BaseSnssdkModel;
import com.devdroid.snssdknew.preferences.IPreferencesIds;
import com.devdroid.snssdknew.utils.DividerItemDecoration;
import com.devdroid.snssdknew.utils.SimpleItemTouchHelperCallback;
import com.devdroid.snssdknew.utils.log.Logger;

import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
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
    private List<BaseSnssdkModel> mSnssdkTexts;
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
        mRecyclerView.setAdapter(mSnssdkAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mSnssdkAdapter);
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
            mToolbar.setTitle(getString(R.string.nav_string_collection));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
        } else if(type == 2){
            mToolbar.setTitle(getString(R.string.nav_string_image));
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
        this.mType = type;
        mSnssdkTexts = SnssdkTextManager.getInstance().getmSnssdks(mType);
        mSnssdkAdapter.notifyDataSetChanged();
    }
}
