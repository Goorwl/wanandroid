package com.goorwl.wandemo.globl;

import android.app.Application;

import com.goorwl.utils.SPUtils;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtils.init(this);
    }
}
