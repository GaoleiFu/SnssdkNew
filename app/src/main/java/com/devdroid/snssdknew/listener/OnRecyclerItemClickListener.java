package com.devdroid.snssdknew.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 表情类型RecyclerView点击响应事件
 * Created by Gaolei on 2016/12/17.
 */

public interface OnRecyclerItemClickListener {
    void onItemClick(RecyclerView.Adapter parent, View v, int position);
}
