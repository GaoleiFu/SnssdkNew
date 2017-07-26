package com.devdroid.snssdknew.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.Toast;
import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 应用启动引导页
 * User:Gaolei  gurecn@gmail.com
 * Date:2017/6/13
 * I'm glad to share my knowledge with you all.
 */
public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		initData();
	}

	private void initData(){
		if (Build.VERSION.SDK_INT >= 23) {   //申请通用权限
			String[] permissions = requestPermissions();
			if (permissions != null) {
				requestPermissions(requestPermissions(), 1003);
				return;
			}
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
			}
		}, 1000);
	}
	/**
	 * 请求需要的权限
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private String[] requestPermissions() {
		String[] permissions = null;
		List<String> permissionsList = new ArrayList<>();
		addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
		addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE);
		if (permissionsList.size() > 0) {
			permissions = new String[permissionsList.size()];
			for(int i = 0 ; i < permissionsList.size() ; i++){
				permissions[i] = permissionsList.get(i);
			}
		}
		return permissions;
	}
	private void addPermission(List<String> permissionsList, String permission) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				permissionsList.add(permission);
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == 1003) {
			for (int i = 0; i < grantResults.length;i++) {
				int grant = grantResults[i];
				if (grant != PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "请赋予相应权限", Toast.LENGTH_SHORT).show();
				} else if(i == grantResults.length -1){
					initData();
				}
			}
		}
	}
}
