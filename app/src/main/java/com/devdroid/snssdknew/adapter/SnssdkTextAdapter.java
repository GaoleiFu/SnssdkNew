package com.devdroid.snssdknew.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.listener.OnDismissAndShareListener;
import com.devdroid.snssdknew.model.BaseSnssdkModel;
import com.devdroid.snssdknew.model.SnssdkText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 主界面文本Snssdk的适配器
 * User:Gaolei  gurecn@gmail.com
 * Date:2016/10/10
 * I'm glad to share my knowledge with you all.
 */
public class SnssdkTextAdapter extends RecyclerView.Adapter<SnssdkTextAdapter.ViewHolder> implements OnDismissAndShareListener {

    private Context mContext;
    private List<BaseSnssdkModel> snssdks;
    private final int mScreenWidth;

    public SnssdkTextAdapter(Context context, List<BaseSnssdkModel> snssdks){
        this.mContext = context;
        this.snssdks = snssdks;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        mScreenWidth = d.getWidth();
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(snssdks != null && snssdks.size() > position) {
            SnssdkText snssdk = (SnssdkText)snssdks.get(position);
            if(snssdk.getSnssdkType() == 0) {
                holder.mTextValue.setText((snssdk).getSnssdkContent());
                holder.mTextValue.requestFocus();
            } else if(snssdk.getSnssdkType() == 2) {
                final ViewHolderImage viewHolderImage = (ViewHolderImage)holder;
                String url = snssdk.getSnssdkContent();
                if(url.endsWith("gif")) {
                    Glide.with(mContext).load(snssdk.getSnssdkContent()).asGif().placeholder(R.mipmap.ic_launcher).crossFade().into(viewHolderImage.mImageView);
                } else {
                    Glide.with(mContext).load(snssdk.getSnssdkContent()).asBitmap().placeholder(R.mipmap.ic_launcher).centerCrop().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int imageWidth = resource.getWidth();
                            int imageHeight = resource.getHeight();
                            int height = mScreenWidth / 2 * imageHeight / imageWidth;
                            ViewGroup.LayoutParams para = viewHolderImage.mImageView.getLayoutParams();
                            para.height = height;
                            para.width = mScreenWidth / 2;
                            viewHolderImage.mImageView.setImageBitmap(resource);
                        }
                    });
                }
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
            if(snssdkText.getSnssdkType() == 2) {  //分享图片
                shareImage(snssdkText.getSnssdkContent());
                notifyItemChanged(position);
            } else {
                shareText(snssdkText.getSnssdkContent());
                notifyItemChanged(position);
            }
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

    private class ViewHolderImage extends ViewHolder{
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

    private void shareImage(final String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                try {

                    Bitmap bitmap = Glide.with(mContext)
                            .load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    String[] filePaths = url.split("/");
                    String fileName = filePaths[filePaths.length - 1];
                    File cacheFile =  saveImageFile(bitmap, fileName);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, cacheFile);
                    intent.putExtra("Kdescription", url);
                    mContext.startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public File saveImageFile(Bitmap bmp, String fileName) {
        // 首先保存图片
        String file = Environment.getExternalStorageDirectory().getAbsolutePath();//注意小米手机必须这样获得public绝对路径
        File appDir = new File(file ,fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(appDir);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        return appDir;
    }

}
