package com.jk.mymeet.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.jk.framework.FrameWork;

import org.litepal.LitePal;

/**
 * Created By Admin on 2020/1/7
 * Describe:
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        FrameWork.getFrameWork().initFrameWork(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
