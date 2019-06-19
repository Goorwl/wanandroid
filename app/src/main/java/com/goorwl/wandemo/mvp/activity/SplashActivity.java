package com.goorwl.wandemo.mvp.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.goorwl.utils.LiveEventBus;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.SplashActivityImple;
import com.goorwl.wandemo.mvp.presenter.SplashActivityPresenter;
import com.goorwl.wandemo.tool.HostSonicRuntime;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;

public class SplashActivity extends BaseActivity implements SplashActivityImple {

    private SplashActivityPresenter mPresenter;
    private Context                 mContext;
    private ImageView               mIvAd;
    private static final int PERMISSION_REQUEST_CODE_STORAGE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView() {
        mIvAd = findViewById(R.id.splash_iv);
        mContext = this;
        // GET INSTANCE OF PRESENTER
        mPresenter = new SplashActivityPresenter(this, this);
        mPresenter.getVersion();
        mPresenter.getAdInfo();

        LiveEventBus.get().with(CONSTANT_SPLASH).observe1(this, o -> {
            jumpActivity(MainActivity.class);
            finish();
        });

        if (hasPermission()){
            init();
        }else {
            requestPermission();
        }   
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                init();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 初始化  sonic vas webview
    private void init() {
        // init sonic engine
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new HostSonicRuntime(getApplication()), new SonicConfig.Builder().build());
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    public void showAds(String imgUrl, String url) {
        mIvAd.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(imgUrl).into(mIvAd);
        mIvAd.setOnClickListener(v -> {
            startBroswerActivity(MODE_SONIC, "https://www.baidu.com");
        });
        LiveEventBus.get().with(CONSTANT_SPLASH).postValueDelay(1, 3000);
    }

    @Override
    public void ShowVersion(boolean isForce, String url, String msg) {
        if (isForce) {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setPositiveButton(R.string.now_download, null)
                    .setCancelable(false)
                    .setMessage(msg)
                    .setTitle(R.string.version_title)
                    .create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                Toast.makeText(mContext, "下载地址", Toast.LENGTH_SHORT).show();
            });
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setPositiveButton(R.string.now_download, (a, v) -> {
                        Toast.makeText(mContext, "立即下载地址", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.next_time, (a, v) -> {
                        a.dismiss();
                    })
                    .setCancelable(false)
                    .setMessage(msg)
                    .setTitle(R.string.version_title)
                    .create();
            alertDialog.show();
        }
    }
}
