package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.mvp.imple.SearchResActivityImple;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.OkHttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class SearchResActivityPresenter implements Config {

    private static final String TAG = "SearchResActivityPresen";

    private Activity               mActivity;
    private SearchResActivityImple mImple;

    public SearchResActivityPresenter(Activity activity, SearchResActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }

    public void getSearch(String key, int pageIndex) {
        FormBody formBody = new FormBody.Builder()
                .add("k", key)
                .build();

        Request request = new Request.Builder()
                .url(URL_SEARCH + pageIndex + "/json")
                .post(formBody)
                .build();

        OkHttpUtils.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
                mActivity.runOnUiThread(() -> mImple.loadSearch(finalString));
            }
        });

    }

}