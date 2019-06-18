package com.goorwl.wandemo.utils;

import okhttp3.MediaType;

public interface Config {
    //  api文章介绍 ：https://www.wanandroid.com/blog/show/2
    String CONSTANT_SPLASH = "CONSTANT_SPLASH";

    MediaType NET_JSON = MediaType.get("application/json; charset=utf-8");

    // URL
    String URL_BASE   = "https://www.wanandroid.com";
    String URL_BANNER = URL_BASE + "/banner/json";
    String URL_ARTICLE =URL_BASE + "/article/list/";
}
