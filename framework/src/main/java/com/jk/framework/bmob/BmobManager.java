package com.jk.framework.bmob;

import android.content.Context;

import com.jk.framework.constants.Constants;
import com.jk.framework.utils.CommonUtils;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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

    public interface OnUploadPhotoListener {

        void OnUpdateDone();

        void OnUpdateFail(BmobException e);
    }

    /**
     * 查询基类
     *
     * @param key
     * @param values
     * @param listener
     */
    private void baseQuery(String key, String values, FindListener<IMUser> listener) {
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.addWhereEqualTo(key, values);
        query.findObjects(listener);
    }

    /**
     * 上传头像
     *
     * @param nickName
     * @param listener
     */
    public void uploadFirstPhoto(final String nickName, final OnUploadPhotoListener listener) {
        /**
         * 1.上传文件拿到地址
         * 2.更新用户信息
         */
        IMUser user = getUser();
        user.setNickName(nickName);
        user.setTokenNickName("asdfghjkl");
        user.setPhoto("http://b-ssl.duitang.com/uploads/item/201804/29/20180429210111_gtsnf.jpg");
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.OnUpdateDone();
                } else {
                    listener.OnUpdateFail(e);
                }
            }
        });
    }


    /**
     * 根据电话号码查询用户
     *
     * @param phone
     */
    public void queryPhoneUser(String phone, FindListener<IMUser> listener) {
        baseQuery("mobilePhoneNumber", phone, listener);
    }

    /**
     * 查询所有的用户
     *
     * @param listener
     */
    public void queryAllUser(FindListener<IMUser> listener) {
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.findObjects(listener);
    }

    /**
     * 根据objectId查询用户
     *
     * @param objectId
     * @param listener
     */
    public void queryObjectIdUser(String objectId, FindListener<IMUser> listener) {
        baseQuery("objectId", objectId, listener);
    }

    /**
     * 查询我的好友
     *
     * @param listener
     */
    public void queryMyFriends(FindListener<Friend> listener) {
        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("user", getUser());
        query.findObjects(listener);
    }

    /**
     * 添加好友
     *
     * @param imUser
     * @param listener
     */
    public void addFriend(IMUser imUser, SaveListener<String> listener) {
        Friend friend = new Friend();
        friend.setUser(getUser());
        friend.setFriendUser(imUser);
        friend.save(listener);
    }

    /**
     * 通过ID添加好友
     *
     * @param id
     * @param listener
     */
    public void addFriend(String id, final SaveListener<String> listener) {
        queryObjectIdUser(id, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        IMUser imUser = list.get(0);
                        addFriend(imUser, listener);
                    }
                }
            }
        });
    }

}
