package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.wandemo.mvp.imple.MainActivityImple;

public class MainActivityPresenter {

    private Activity          mActivity;
    private MainActivityImple mImple;

    public MainActivityPresenter(Activity activity, MainActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }
}