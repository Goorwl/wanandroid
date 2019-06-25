package com.goorwl.wandemo.mvp.presenter;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.FragmentThreeImple;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentThreePresenter implements Config {
    private static final String TAG = "FragmentThreePresenter";

    private BaseActivity       mActivity;
    private FragmentThreeImple mImple;

    public FragmentThreePresenter(BaseActivity activity, FragmentThreeImple imple) {
        mActivity = activity;
        mImple = imple;
    }

    public void getTab() {
        Request request = new Request.Builder()
                .url(URL_XIANGMU_TAB)
                .build();

        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(() -> mImple.loadTab(NET_ERROR_INFO));
            }

            @Override
            public void onResponse(Call call, Response response) {
                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    string = NET_ERROR_INFO;
                    LogUtils.e(TAG, "onResponse: " + e.getMessage());
                }
                String finalString = string;
                mActivity.runOnUiThread(() -> mImple.loadTab(finalString));
            }
        });
    }

    public void getItem(int cid, int pageIndex) {
        Request request = new Request.Builder()
                .url(URL_XIANGMU_ITEM + pageIndex + "/json?cid=" + cid)
                .build();

        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(() -> mImple.loadItem(NET_ERROR_INFO));
            }

            @Override
            public void onResponse(Call call, Response response) {
                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    string = NET_ERROR_INFO;
                    LogUtils.e(TAG, "onResponse: " + e.getMessage());
                }
                String finalString = string;
                mActivity.runOnUiThread(() -> mImple.loadItem(finalString));
            }
        });
    }
}
