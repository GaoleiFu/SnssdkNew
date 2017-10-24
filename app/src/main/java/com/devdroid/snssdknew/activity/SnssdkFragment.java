package com.devdroid.snssdknew.activity;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.adapter.SnssdkImageAdapter;
import com.devdroid.snssdknew.adapter.SnssdkTextAdapter;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.constant.CustomConstant;
import com.devdroid.snssdknew.eventbus.OnSnssdkLoadedEvent;
import com.devdroid.snssdknew.listener.OnRecyclerItemClickListener;
import com.devdroid.snssdknew.model.SnssdkImage;
import com.devdroid.snssdknew.model.SnssdkText;
import com.devdroid.snssdknew.preferences.IPreferencesIds;
import com.devdroid.snssdknew.remote.LoadListener;
import com.devdroid.snssdknew.remote.RemoteSettingManager;
import com.devdroid.snssdknew.utils.DividerItemDecoration;
import java.util.List;

public class SnssdkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnRecyclerItemClickListener, LoadListener, Handler.Callback  {
    private static final String ARG_SNSSDK_TYPE = "snssdkType";
    private static final String ARG_COLLECTION_STATUE = "collectionStatue";
    private int snssdkType;
    private int collectionStatue;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mRootView;
    private RemoteSettingManager mRemoteSettingManager;
    private Handler mHandler;
    private List<SnssdkText> mSnssdkTexts;
    private List<SnssdkImage> mSnssdkImages;
    private SnssdkTextAdapter mSnssdkTextAdapter;
    private SnssdkImageAdapter mSnssdkImageAdapter;

    public SnssdkFragment() {
    }

    public static SnssdkFragment newInstance(int snssdkType, int collectionStatue) {
        SnssdkFragment fragment = new SnssdkFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SNSSDK_TYPE, snssdkType);
        args.putInt(ARG_COLLECTION_STATUE, collectionStatue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            snssdkType = getArguments().getInt(ARG_SNSSDK_TYPE);
            collectionStatue = getArguments().getInt(ARG_COLLECTION_STATUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_snssdk, container, false);
        return mRootView;
    }

    /**
     * 初始化数据
     */
    @Override
    public void onResume() {
        super.onResume();
        mHandler = new Handler(this);
        mRemoteSettingManager = new RemoteSettingManager(this);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = mRootView.findViewById(R.id.recyclerView_snssdk_fragment);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        if(snssdkType == CustomConstant.SNSSDK_TYPE_TEXT){
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(layoutManager);
            mSnssdkTexts = LauncherModel.getInstance().getSnssdkTextDao().queryLockerInfo(collectionStatue);
            mSnssdkTextAdapter = new SnssdkTextAdapter(this.getActivity(), mSnssdkTexts);
            mSnssdkTextAdapter.setItemClickListener(this);
            mRecyclerView.setAdapter(mSnssdkTextAdapter);
            if(mSnssdkTexts.size() <= 0){
                mRemoteSettingManager.connectToServer(getActivity(), snssdkType);
                mSwipeRefreshLayout.setRefreshing(true);
            }
        } else if(snssdkType == CustomConstant.SNSSDK_TYPE_IMAGE){
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mSnssdkImages = LauncherModel.getInstance().getSnssdkImage().queryLockerInfo(collectionStatue);
            mSnssdkImageAdapter = new SnssdkImageAdapter(this.getActivity(), mSnssdkImages);
            mSnssdkImageAdapter.setItemClickListener(this);
            mRecyclerView.setAdapter(mSnssdkImageAdapter);
            if(mSnssdkImages.size() <= 0){
                mRemoteSettingManager.connectToServer(getActivity(), snssdkType);
                mSwipeRefreshLayout.setRefreshing(true);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        if( LauncherModel.getInstance().getSharedPreferencesManager().getBoolean(IPreferencesIds.DEFAULT_SHAREPREFERENCES_OFFLINE_MODE, false)) {
            SnssdknewApplication.getGlobalEventBus().post(new OnSnssdkLoadedEvent(0));
            return;
        }
        mRemoteSettingManager.connectToServer(this.getActivity(), snssdkType);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onItemClick(RecyclerView.Adapter parent, View v, int position, int showType) {
//        if(showType == 2) {
//            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//            mRecyclerView.setLayoutManager(gridLayoutManager);
//        } else {
//            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//            mRecyclerView.setLayoutManager(gridLayoutManager);
//        }
//        mRecyclerView.scrollToPosition(position);
    }

    @Override
    public void loadLoaded(int snssdkType, List<SnssdkText> snssdks) {
        mHandler.sendEmptyMessage(snssdkType);
    }

    @Override
    public boolean handleMessage(Message msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(snssdkType == CustomConstant.SNSSDK_TYPE_IMAGE){
            mSnssdkImages.clear();
            mSnssdkImages.addAll(LauncherModel.getInstance().getSnssdkImage().queryLockerInfo(collectionStatue));
            mSnssdkImageAdapter.notifyDataSetChanged();
        } else if(snssdkType == CustomConstant.SNSSDK_TYPE_TEXT){
            mSnssdkTexts.clear();
            mSnssdkTexts.addAll(LauncherModel.getInstance().getSnssdkTextDao().queryLockerInfo(collectionStatue));
            mSnssdkTextAdapter.notifyDataSetChanged();
        }
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
