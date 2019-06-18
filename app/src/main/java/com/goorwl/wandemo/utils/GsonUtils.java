package com.goorwl.wandemo.utils;

import com.google.gson.Gson;

public class GsonUtils {
    private static Gson sSingleTest;

    private GsonUtils() {
    }

    public static Gson getInstance() {
        if (sSingleTest == null) {
            synchronized (Gson.class) {
                if (sSingleTest == null) {
                    sSingleTest = new Gson();
                }
            }
        }
        return sSingleTest;
    }
}
