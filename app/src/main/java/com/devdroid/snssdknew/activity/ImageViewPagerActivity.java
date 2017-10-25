package com.devdroid.snssdknew.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.adapter.ImagePagerAdapter;
import com.devdroid.snssdknew.base.BaseActivity;

public class ImageViewPagerActivity extends BaseActivity {
    private ViewPager mVpImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_pager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(67108864);
        }
        mVpImage = (ViewPager)findViewById(R.id.vp_image_view_pager);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int collectionStatue= intent.getIntExtra("collectionStatue", 0);
        int position= intent.getIntExtra("position", 0);
        mVpImage.setAdapter(new ImagePagerAdapter(collectionStatue));
        mVpImage.setCurrentItem(position);
    }
}
