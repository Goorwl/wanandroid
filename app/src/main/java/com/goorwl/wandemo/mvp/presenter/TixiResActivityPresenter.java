package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.mvp.imple.TixiResActivityImple;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TixiResActivityPresenter implements Config {
    private static final String TAG = "TixiResActivityPresente";

    private Activity             mActivity;
    private TixiResActivityImple mImple;

    public TixiResActivityPresenter(Activity activity, TixiResActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }

    public void getData(int cid, int pageIndex) {
        Request request = new Request.Builder()
                .url(URL_TIXIRES + pageIndex + "/json?cid=" + cid)
                .build();

        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mActivity.runOnUiThread(() -> mImple.loadData(NET_ERROR_INFO));
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
                LogUtils.e(TAG, "onResponse: " + string);

                String finalString = string;
                mActivity.runOnUiThread(() -> mImple.loadData(finalString));
            }
        });
    }
}