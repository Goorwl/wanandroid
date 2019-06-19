package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.wandemo.mvp.imple.SearchActivityImple;

public class SearchActivityPresenter {

    private Activity            mActivity;
    private SearchActivityImple mImple;

    public SearchActivityPresenter(Activity activity, SearchActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }
}