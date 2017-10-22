package com.devdroid.snssdknew.activity;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.adapter.SnssdkTextAdapter;
import com.devdroid.snssdknew.listener.OnRecyclerItemClickListener;
import com.devdroid.snssdknew.manager.SnssdkTextManager;
import com.devdroid.snssdknew.model.SnssdkText;
import com.devdroid.snssdknew.utils.DividerItemDecoration;
import com.devdroid.snssdknew.utils.log.Logger;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SnssdkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SnssdkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SnssdkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnRecyclerItemClickListener {
    private static final String ARG_SNSSDK_TYPE = "snssdkType";
    private static final String ARG_COLLECTION_STATUE = "collectionStatue";
    private int snssdkType;
    private int collectionStatue;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mRootView;

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
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = mRootView.findViewById(R.id.recyclerView_snssdk_fragment);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        List<SnssdkText> mSnssdkTexts = SnssdkTextManager.getInstance().getmSnssdks(snssdkType);
        Logger.d("11111111111111111", "mSnssdkTexts:" + mSnssdkTexts.size());
        SnssdkTextAdapter mSnssdkAdapter = new SnssdkTextAdapter(this.getActivity(), mSnssdkTexts);
        mSnssdkAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mSnssdkAdapter);
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
//        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        if(snssdkType != 1) {
            mSwipeRefreshLayout.setRefreshing(true);
            SnssdkTextManager.getInstance().freshMore(this.getActivity(), snssdkType);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
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
//        mSnssdkAdapter.setShowColumnChanged();
//        mSnssdkAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
