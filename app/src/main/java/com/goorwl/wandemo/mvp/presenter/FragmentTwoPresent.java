package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.mvp.imple.FragmentTwoImple;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentTwoPresent implements Config {
    private static final String TAG = "FragmentTwoPresent";

    private Activity         mActivity;
    private FragmentTwoImple mImple;

    public FragmentTwoPresent(Activity activity, FragmentTwoImple imple) {
        mActivity = activity;
        mImple = imple;
    }

    public void getData() {
        Request request = new Request.Builder()
                .url(URL_TIXI)
                .build();

        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "onFailure: " + e.getMessage());
                mActivity.runOnUiThread(() -> mImple.loadData(NET_ERROR_INFO));
            }

            @Override
            public void onResponse(Call call, Response response) {
                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String finalString = string;
                mActivity.runOnUiThread(() -> mImple.loadData(finalString));
            }
        });
    }
}
