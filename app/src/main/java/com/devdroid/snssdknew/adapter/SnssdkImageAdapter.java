package com.devdroid.snssdknew.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.listener.OnRecyclerItemClickListener;
import com.devdroid.snssdknew.model.SnssdkImage;
import java.util.List;


/**
 * 图片适配器
 * Created by Gaolei on 2017/10/23.
 */

public class SnssdkImageAdapter  extends RecyclerView.Adapter<SnssdkImageAdapter.ViewHolder> {
    private List<SnssdkImage> snssdks;
    private final int mScreenWidth;
    private OnRecyclerItemClickListener listener;
    private int mShowColumn = 2;

    public SnssdkImageAdapter(Context context, List<SnssdkImage> snssdks){
        this.snssdks = snssdks;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display d = wm.getDefaultDisplay();
        mScreenWidth = d.getWidth();
    }

    public void setItemClickListener(OnRecyclerItemClickListener itemClickListener){
        this.listener = itemClickListener;
    }

    @Override
    public SnssdkImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_snssdk_image, null);
        return new SnssdkImageAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SnssdkImageAdapter.ViewHolder holder, final int position) {
        if(snssdks != null && snssdks.size() > position) {
            SnssdkImage snssdk = snssdks.get(position);
            final SnssdkImageAdapter.ViewHolder viewHolderImage = holder;
            String url = snssdk.getSnssdkUrl();
            if(url.endsWith("gif")) {
                Glide.with(SnssdknewApplication.getAppContext()).load(snssdk.getSnssdkUrl())
                        .asGif().crossFade().into(viewHolderImage.mImageView);
            } else {
                Glide.with(SnssdknewApplication.getAppContext()).load(snssdk.getSnssdkUrl()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();
                        int height = mScreenWidth / mShowColumn * imageHeight / imageWidth;
                        ViewGroup.LayoutParams para = viewHolderImage.mImageView.getLayoutParams();
                        para.height = height;
                        para.width = mScreenWidth / mShowColumn;
                        viewHolderImage.mImageView.setImageBitmap(resource);
                    }
                });
            }
            if(listener != null){
                viewHolderImage.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(SnssdkImageAdapter.this, v, position, mShowColumn);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(snssdks == null) return 0;
        return snssdks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_item_snssdk_image_image);
        }
    }

}
