package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.wandemo.mvp.imple.SearchResActivityImple;

public class SearchResActivityPresenter {

    private Activity               mActivity;
    private SearchResActivityImple mImple;

    public SearchResActivityPresenter(Activity activity, SearchResActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }
}