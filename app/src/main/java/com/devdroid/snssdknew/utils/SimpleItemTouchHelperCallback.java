package com.devdroid.snssdknew.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.devdroid.snssdknew.listener.OnDismissAndShareListener;

/**
 * Created with IntelliJ IDEA.
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/11
 * I'm glad to share my knowledge with you all.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private OnDismissAndShareListener mAdapterListener;

    public SimpleItemTouchHelperCallback(OnDismissAndShareListener listener){
        mAdapterListener = listener;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //如果是ListView样式的RecyclerView
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            //设置拖拽方向为上下
            final int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
            //设置侧滑方向为从左到右和从右到左都可以
            final int swipeFlags = ItemTouchHelper.START|ItemTouchHelper.END;
            //将方向参数设置进去
            return makeMovementFlags(dragFlags,swipeFlags);
        }
        return 0;
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == 16) {
            mAdapterListener.onItemDismiss(viewHolder.getAdapterPosition());
        } else if (direction == 32) {
            mAdapterListener.onItemShare(viewHolder.getAdapterPosition(),viewHolder.itemView);
        }
    }
}
