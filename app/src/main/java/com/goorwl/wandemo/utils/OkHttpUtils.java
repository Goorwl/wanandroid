package com.goorwl.wandemo.utils;

import okhttp3.OkHttpClient;

public class OkHttpUtils {
    private static OkHttpClient sSingleTest;

    private OkHttpUtils() {
    }

    public static OkHttpClient getInstance() {
        if (sSingleTest == null) {
            synchronized (OkHttpUtils.class) {
                if (sSingleTest == null) {
                    sSingleTest = new OkHttpClient();
                }
            }
        }
        return sSingleTest;
    }
}
