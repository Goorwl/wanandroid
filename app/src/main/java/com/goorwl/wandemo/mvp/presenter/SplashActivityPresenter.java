package com.goorwl.wandemo.mvp.presenter;

import android.app.Activity;

import com.goorwl.wandemo.mvp.imple.SplashActivityImple;

public class SplashActivityPresenter {

    private Activity            mActivity;
    private SplashActivityImple mImple;

    public SplashActivityPresenter(Activity activity, SplashActivityImple imple) {
        mActivity = activity;
        mImple = imple;
    }

    public void getAdInfo(){
        mImple.showAds("https://img.moegirl.org/common/thumb/8/84/Ika_Musume.jpg/250px-Ika_Musume.jpg","https://www.google.com");
    }

    public void getVersion(){
//        mImple.ShowVersion(false,"xxx","修复bug");
    }
}