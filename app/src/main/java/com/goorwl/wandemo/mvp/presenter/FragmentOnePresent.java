package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.mvp.imple.FragmentOneImple;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentOnePresent implements Config {
    private static final String TAG = "FragmentOnePresent";

    private Activity         mActivity;
    private FragmentOneImple mImple;

    public FragmentOnePresent(Activity activity, FragmentOneImple imple) {
        mActivity = activity;
        mImple = imple;
    }

    public void getTab() {
        Request request = new Request.Builder()
                .url(URL_WECHAT_TAB)
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
                }
                String finalString = string;
                mActivity.runOnUiThread(() -> mImple.loadTab(finalString));
            }
        });
    }

    public void getItem(int id, int pageIndex, String key) {
        String url = URL_WECHAT_AUTHOR + id + "/" + pageIndex + "/json";
        if (!TextUtils.isEmpty(key)) {
            url = URL_WECHAT_AUTHOR + id + "/" + pageIndex + "/json?k=" + key;
        }
        LogUtils.e(TAG, "getItem 2019年6月24日: " + url);
        Request request = new Request.Builder()
                .url(url)
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
                }
                String finalString = string;
                LogUtils.e(TAG, "onResponse: " + string);
                mActivity.runOnUiThread(() -> mImple.loadItem(finalString));
            }
        });
    }


}
