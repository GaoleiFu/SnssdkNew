package com.devdroid.snssdknew.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devdroid.snssdknew.application.LauncherModel;
import com.devdroid.snssdknew.application.SnssdknewApplication;
import com.devdroid.snssdknew.model.SnssdkImage;
import java.util.List;
import uk.co.senab.photoview.PhotoView;


public class ImagePagerAdapter extends PagerAdapter {
    private final List<SnssdkImage> mSnssdkImages;

    public ImagePagerAdapter(int collectionStatue) {
        mSnssdkImages = LauncherModel.getInstance().getSnssdkImage().queryLockerInfo(collectionStatue);
    }

    @Override
    public int getCount() {
        return mSnssdkImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        Glide.with(SnssdknewApplication.getAppContext()).load(mSnssdkImages.get(position).getSnssdkUrl()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
