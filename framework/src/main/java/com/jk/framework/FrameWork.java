package com.jk.framework;

import android.content.Context;

import com.jk.framework.bmob.BmobManager;
import com.jk.framework.utils.SpUtils;

/**
 * Created By Admin on 2020/1/2
 * Describe:
 */
public class FrameWork {

    private volatile static FrameWork mFrameWork;

    private FrameWork() {

    }

    public static FrameWork getFrameWork() {
        if (mFrameWork == null) {
            synchronized (FrameWork.class) {
                if (mFrameWork == null)
                    mFrameWork = new FrameWork();
            }
        }
        return mFrameWork;
    }

    public void initFrameWork(Context mContext) {
        SpUtils.getInstance().initSp(mContext);
        BmobManager.getInstance().initBmob(mContext);
    }
}
