package com.devdroid.snssdknew.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.listener.OnDismissAndShareListener;
import com.devdroid.snssdknew.model.BaseSnssdkModel;
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
    private List<BaseSnssdkModel> snssdks;
    public SnssdkTextAdapter(Context context, List<BaseSnssdkModel> snssdks){
        this.mContext = context;
        this.snssdks = snssdks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 2){
            View view = View.inflate(parent.getContext(), R.layout.item_snssdk_image, null);
            return new ViewHolderImage(view);
        } else {
            View view = View.inflate(parent.getContext(), R.layout.item_snssdk_text, null);
            return new ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        SnssdkText snssdk = (SnssdkText)snssdks.get(position);
        return snssdk.getSnssdkType();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(snssdks != null && snssdks.size() > position) {
            SnssdkText snssdk = (SnssdkText)snssdks.get(position);
            if(snssdk.getSnssdkType() == 0) {
                holder.mTextValue.setText((snssdk).getSnssdkContent());
                holder.mTextValue.requestFocus();
            } else  if(snssdk.getSnssdkType() == 1) {
                ViewHolderImage viewHolderImage = (ViewHolderImage)holder;
                viewHolderImage.mTextValue.setText(snssdk.getSnssdkContent());
            }
        }
    }

    @Override
    public int getItemCount() {
        if(snssdks == null) return 0;
        return snssdks.size();
    }

    @Override
    public void onItemDismiss(int position) {
        SnssdkText snssdkText = (SnssdkText)snssdks.get(position);
        LauncherModel.getInstance().getSnssdkTextDao().deleteSnssdkItem(snssdkText);
        snssdks.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemShare(int position,View currentView) {
        SnssdkText snssdkText = (SnssdkText)snssdks.get(position);
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
            mTextValue = (TextView)itemView.findViewById(R.id.tv_item_snssdk_content);
        }
    }

    class ViewHolderImage extends ViewHolder{
        ImageView mImageView;
        ViewHolderImage(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.iv_item_snssdk_image_image);
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
