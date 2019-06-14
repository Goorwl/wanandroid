package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.wandemo.mvp.imple.WebActivityImple;

public class WebActivityPresenter {

    private Activity         mActivity;
    private WebActivityImple mImple;

    public WebActivityPresenter(Activity activity, WebActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }
}