package com.goorwl.wandemo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String stampToString(long stamp) {
        String format = new SimpleDateFormat("HH:mm:ss").format(new Date(stamp));
        return format;
    }
}
