package com.devdroid.snssdknew.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devdroid.snssdknew.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {  //点击返回键关闭界面
        this.finish();
        overridePendingTransition(R.anim.activity_in_from_left, R.anim.activity_out_from_right);
    }
}
