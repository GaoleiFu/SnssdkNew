package com.devdroid.snssdknew.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.listener.OnDismissAndShareListener;
import com.devdroid.snssdknew.model.SnssdkText;
import java.util.List;

/**
 * 主界面文本Snssdk的适配器
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public class SnssdkTextAdapter extends RecyclerView.Adapter<SnssdkTextAdapter.ViewHolder> implements OnDismissAndShareListener {

    private Context mContext;
    private List<SnssdkText> snssdks;
    public SnssdkTextAdapter(Context context, List<SnssdkText> snssdks){
        this.mContext = context;
        this.snssdks = snssdks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_snssdk_text, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(snssdks != null && snssdks.size() > position) {
            holder.mTextValue.setText(snssdks.get(position).getSnssdkContent());
            holder.mTextValue.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        if(snssdks == null) return 0;
        return snssdks.size();
    }

    @Override
    public void onItemDismiss(int position) {
        LauncherModel.getInstance().getSnssdkTextDao().deleteSnssdkItem(snssdks.get(position));
        snssdks.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemShare(int position,View currentView) {
        SnssdkText snssdkText = snssdks.get(position);
        if(snssdkText.getIsCollection() == 1) {
            shareText(snssdkText.getSnssdkContent());
            notifyItemChanged(position);
        } else {
            snssdkText.setIsCollection(1);
            LauncherModel.getInstance().getSnssdkTextDao().updateSnssdkItem(snssdkText);
            snssdks.remove(position);
            notifyItemRemoved(position);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextValue;
        ViewHolder(View itemView) {
            super(itemView);
            mTextValue = (TextView)itemView.findViewById(R.id.item_snssdk_text_value);
        }
    }

    private void shareText(String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TITLE,text);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
