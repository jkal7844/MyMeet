package com.jk.framework.bmob;

import android.content.Context;


import com.jk.framework.constants.Constants;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created By Admin on 2020/1/7
 * Describe: bmob 管理类
 */
public class BmobManager {

    private volatile static BmobManager mInstance = null;

    private BmobManager() {

    }

    public static BmobManager getInstance() {
        if (mInstance == null) {
            synchronized (BmobManager.class) {
                if (mInstance == null)
                    mInstance = new BmobManager();
            }
        }
        return mInstance;
    }

    /**
     * 初始化 bmob
     *
     * @param mContext
     */
    public void initBmob(Context mContext) {
        Bmob.initialize(mContext, Constants.BMOB_SDK_ID);
    }

    public void requestSMS(String phone, QueryListener<Integer> listener) {
        BmobSMS.requestSMSCode(phone, "", listener);
    }

    public void signOrLoginByMobilePhone(String phone, String code, LogInListener<IMUser> listener) {
        BmobUser.signOrLoginByMobilePhone(phone, code, listener);
    }

    public IMUser getUser() {
        return BmobUser.getCurrentUser(IMUser.class);
    }
}
