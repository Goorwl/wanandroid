package com.goorwl.wandemo.utils;

import okhttp3.MediaType;

public interface Config {
    //  api文章介绍 ：https://www.wanandroid.com/blog/show/2
    String CONSTANT_SPLASH        = "CONSTANT_SPLASH";
    String CONSTANT_SPEARTOR      = "*#*";
    String CONSTANT_JUMP_DATA     = "CONSTANT_JUMP_DATA";
    String CONSTANT_JUMP_DATA_STR = "CONSTANT_JUMP_DATA_STR";

    MediaType NET_JSON = MediaType.get("application/json; charset=utf-8");

    // URL
    String URL_BASE          = "https://www.wanandroid.com";
    String URL_BANNER        = URL_BASE + "/banner/json";
    String URL_ARTICLE       = URL_BASE + "/article/list/";
    String URL_HOTWORD       = URL_BASE + "/hotkey/json";
    String URL_SEARCH        = URL_BASE + "/article/query/";
    String URL_TIXI          = URL_BASE + "/tree/json";
    String URL_TIXIRES       = URL_BASE + "/article/list/";
    String URL_WECHAT_TAB    = URL_BASE + "/wxarticle/chapters/json";
    String URL_WECHAT_AUTHOR = URL_BASE + "/wxarticle/list/";
    String URL_XIANGMU_TAB   = URL_BASE + "/project/tree/json";
    String URL_XIANGMU_ITEM  = URL_BASE + "/project/list/";

    String PARAM_URL                     = "param_url";
    String PARAM_MODE                    = "param_mode";
    String PARAM_LOAD_URL_TIME           = "loadUrlTime";
    String PARAM_CLICK_TIME              = "clickTime";
    int    MODE_DEFAULT                  = 0;
    int    MODE_SONIC                    = 1;
    int    MODE_SONIC_WITH_OFFLINE_CACHE = 2;

    String NET_ERROR_INFO = "{\"errorCode\":0,\"errorMsg\":\"请求数据异常xxx\"}";

    String SP_WORD_SEARCH_HISTORY = "SP_WORD_SEARCH_HISTORY";
}