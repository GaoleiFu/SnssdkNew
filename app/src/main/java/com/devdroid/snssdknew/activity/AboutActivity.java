package com.devdroid.snssdknew.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.base.BaseActivity;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TextView tvVersion = (TextView) findViewById(R.id.tv_app_version);
        LinearLayout llAppRight = (LinearLayout)findViewById(R.id.ll_app_right);
        llAppRight.setOnClickListener(this);
        PackageManager pm=getPackageManager();
        PackageInfo info;
        try {
            info = pm.getPackageInfo(getPackageName(), 0);
            tvVersion.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_from_right);
                break;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_app_right :
                String url = "http://www.devdroid.cn/sample-page/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent .setData(Uri.parse(url));
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_in_from_right,R.anim.activity_out_from_left);
                break;
        }
    }
}
