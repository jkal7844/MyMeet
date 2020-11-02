package com.jk.mymeet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jk.framework.cloud.CloudManager;
import com.jk.framework.constants.Constants;
import com.jk.framework.utils.SpUtils;

/**
 * Created By Admin on 2020/7/22
 * Describe:
 */
public class CloudService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        linkCloudServer();
    }

    private void linkCloudServer() {
        //获取Token
        String token = SpUtils.getInstance().getString(Constants.SP_TOKEN, "");

        //连接服务
        CloudManager.getInstance().connect(token);
        //接收消息
        CloudManager.getInstance().OnReceiveMessageListener((message, i) -> {
//            parsingImMessage(message);
            return false;
        });
    }
}
