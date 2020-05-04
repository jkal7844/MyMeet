package com.jk.mymeet.base;

import android.app.Application;

import com.jk.framework.FrameWork;

/**
 * Created By Admin on 2020/1/7
 * Describe:
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FrameWork.getFrameWork().initFrameWork(this);
    }
}
