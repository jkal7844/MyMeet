package com.jk.framework.utils;

import android.text.TextUtils;

import com.jk.framework.BuildConfig;

/**
 * Created By Admin on 2020/1/2
 * Describe:
 * Log不光作为日志的打印，还可以记录日志 ——> File
 */
public class LogUtils {

    public static void i(String text) {
        if (TextUtils.isEmpty(text))
            return;
        if (BuildConfig.LOG_DEBUG)
            com.blankj.utilcode.util.LogUtils.i(text);
    }

    public static void e(String text) {
        if (TextUtils.isEmpty(text))
            return;
        if (BuildConfig.LOG_DEBUG)
            com.blankj.utilcode.util.LogUtils.e(text);
    }
}
