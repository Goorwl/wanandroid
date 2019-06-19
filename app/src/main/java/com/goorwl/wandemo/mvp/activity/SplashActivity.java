package com.goorwl.wandemo.mvp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.goorwl.utils.LiveEventBus;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.SplashActivityImple;
import com.goorwl.wandemo.mvp.presenter.SplashActivityPresenter;

public class SplashActivity extends BaseActivity implements SplashActivityImple {

    private SplashActivityPresenter mPresenter;
    private Context                 mContext;
    private ImageView               mIvAd;

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
    }

    @Override
    public void showAds(String imgUrl, String url) {
        mIvAd.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(imgUrl).into(mIvAd);
        mIvAd.setOnClickListener(v -> {
            Toast.makeText(mContext, "打开广告链接", Toast.LENGTH_SHORT).show();
            startBroswerActivity(MODE_DEFAULT,"https://www.baidu.com");
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
