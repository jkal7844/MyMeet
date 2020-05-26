package com.jk.mymeet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jk.framework.constants.Constants;
import com.jk.framework.utils.SpUtils;
import com.jk.mymeet.MainActivity;
import com.jk.mymeet.R;

import cn.bmob.v3.BmobUser;


/**
 * Created By Admin on 2020/1/3
 * Describe:
 */
public class IndexActivity extends AppCompatActivity {

    /**
     * 优化
     * 冷启动经过的步骤：
     * 1.第一次安装，加载应用程序并且启动
     * 2.启动后显示一个空白的窗口 getWindow()
     * 3.启动/创建了我们的应用进程
     *
     * App内部：
     * 1.创建App对象/Application对象
     * 2.启动主线程(Main/UI Thread)
     * 3.创建应用入口/LAUNCHER
     * 4.填充ViewGroup中的View
     * 5.绘制View measure -> layout -> draw
     *
     * 优化手段：
     * 1.视图优化
     *   1.设置主题透明
     *   2.设置启动图片
     * 2.代码优化
     *   1.优化Application
     *   2.布局的优化，不需要繁琐的布局
     *   3.阻塞UI线程的操作
     *   4.加载Bitmap/大图
     *   5.其他的一个占用主线程的操作
     *
     *
     * 检测App Activity的启动时间
     * 1.Shell
     *   ActivityManager -> adb shell am start -S -W com.imooc.meet/com.imooc.meet.ui.IndexActivity
     *   ThisTime: 478ms 最后一个Activity的启动耗时
     *   TotalTime: 478ms 启动一连串Activity的总耗时
     *   WaitTime: 501ms 应用创建的时间 + TotalTime
     *   应用创建时间： WaitTime - TotalTime（501 - 478 = 23ms）
     * 2.Log
     *   Android 4.4 开始，ActivityManager增加了Log TAG = displayed
     */

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0:
                    startMain();
            }
            return false;
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //延时两秒
        mHandler.sendEmptyMessageAtTime(0, 2000);

    }


    /**
     * 进入主页
     */
    private void startMain() {
        //1.判断App是否第一次启动 install - first run
        boolean isFirstApp = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP, true);
        Intent intent = new Intent();
        if (isFirstApp) {
            //跳转到引导页
            intent.setClass(this, GuideActivity.class);
            //非第一次启动
            SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP, false);
        } else {
            //2.如果非第一次启动，判断是否曾经登录过
            String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");
            if (TextUtils.isEmpty(token)) {
                //3.判断Bmob是否登录
                if (BmobUser.isLogin()) {
                    //跳转到主页
                    intent.setClass(this, MainActivity.class);
                } else {
                    //跳转到登录页
                    intent.setClass(this, LoginActivity.class);
                }
            } else {
                //跳转到主页
                intent.setClass(this, MainActivity.class);
            }
        }
        startActivity(intent);
        finish();
    }
}
