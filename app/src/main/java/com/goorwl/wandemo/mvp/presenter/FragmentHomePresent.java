package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.mvp.fragment.FragmentHome;
import com.goorwl.wandemo.mvp.imple.FragmentHomeImple;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentHomePresent implements Config {
    private static final String            TAG = "FragmentHomePresent";
    private              Activity      mActivity;
    private              FragmentHomeImple mFragmentHomeImple;

    public FragmentHomePresent(Activity activity, FragmentHomeImple fragmentHomeImple) {
        mActivity = activity;
        mFragmentHomeImple = fragmentHomeImple;
    }

    // 获取banner数据
    public void getBannerInfo() {
        Request request = new Request.Builder()
                .url(URL_BANNER)
                .build();

        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(() -> mFragmentHomeImple.loadBanner(NET_ERROR_INFO));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                mActivity.runOnUiThread(() -> {
                    LogUtils.e(TAG, "onResponse: getBannerInfo= " + string);
                    mFragmentHomeImple.loadBanner(string);
                });
            }
        });

    }

    // 获取首页文章
    public void getArticle(int pageIndex) {
        Request request = new Request.Builder()
                .url(URL_ARTICLE + pageIndex + "/json")
                .build();

        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(() -> mFragmentHomeImple.loadAitcle(NET_ERROR_INFO));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                mActivity.runOnUiThread(() -> {
                    LogUtils.e(TAG, "onResponse: getArticle=" + string);
                    mFragmentHomeImple.loadAitcle(string);
                });
            }
        });
    }
}
