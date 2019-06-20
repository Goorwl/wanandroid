package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.utils.LogUtils;
import com.goorwl.utils.SPUtils;
import com.goorwl.wandemo.mvp.imple.SearchActivityImple;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivityPresenter implements Config {

    private Activity            mActivity;
    private SearchActivityImple mImple;

    private static final String TAG = "SearchActivityPresenter";

    public SearchActivityPresenter(Activity activity, SearchActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }

    public void getHotword() {
        Request request = new Request.Builder()
                .url(URL_HOTWORD)
                .build();
        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {

            private String mString;

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "onFailure: " + e.getMessage());
                mActivity.runOnUiThread(() -> {
                    mImple.loadHotWord(NET_ERROR_INFO);
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    mString = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    mString = NET_ERROR_INFO;
                }
                mActivity.runOnUiThread(() -> {
                    mImple.loadHotWord(mString);
                });
            }
        });
    }

    public void getHistory(){
        String string = SPUtils.getString(SP_WORD_SEARCH_HISTORY, "");
        mImple.loadHistory(string);
    }
}