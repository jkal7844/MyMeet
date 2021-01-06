package com.jk.mymeet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.SurfaceView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.cloud.CloudManager;
import com.jk.framework.constants.Constants;
import com.jk.framework.db.LitePalHelper;
import com.jk.framework.event.EventManager;
import com.jk.framework.event.MessageEvent;
import com.jk.framework.gson.TextBean;

import java.util.HashMap;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.rong.calllib.IRongCallListener;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;

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
        String token = SPUtils.getInstance().getString(Constants.SP_TOKEN);
        //连接服务
        CloudManager.getInstance().connect(token);


        //接收消息
        CloudManager.getInstance().OnReceiveMessageListener((message, i) -> {
            parsingImMessage(message);
            return false;
        });

        //监听通话
        CloudManager.getInstance().setReceivedCallListener(new IRongReceivedCallListener() {
            @Override
            public void onReceivedCall(RongCallSession rongCallSession) {
                LogUtils.i("rongCallSession");

            }

            /**
             * targetSDKVersion 大于等于 23 时检查权限的回调。当 targetSDKVersion 小于 23 的时候不需要实现。
             * 在这个回调里用户需要使用Android6.0新增的动态权限分配接口requestCallPermissions通知用户授权，
             * 然后在onRequestPermissionResult回调里根据用户授权或者不授权分别回调
             * RongCallClient.getInstance().onPermissionGranted()和
             * RongCallClient.getInstance().onPermissionDenied()来通知CallLib。
             * @param rongCallSession 通话实体
             */
            @Override
            public void onCheckPermission(RongCallSession rongCallSession) {
                LogUtils.i("onCheckPermission:" + rongCallSession.toString());
            }
        });


        //监听通话状态
        CloudManager.getInstance().setVoIPCallListener(new IRongCallListener() {
            /**
             * 电话已拨出。
             * 主叫端拨出电话后，通过回调 onCallOutgoing 通知当前 call 的详细信息。
             *
             * @param rongCallSession 通话实体。
             * @param surfaceView  本地 camera 信息。
             */
            @Override
            public void onCallOutgoing(RongCallSession rongCallSession, SurfaceView surfaceView) {
            }


            /**
             * 已建立通话。
             * 通话接通时，通过回调 onCallConnected 通知当前 call 的详细信息。
             *
             * @param rongCallSession 通话实体。
             * @param surfaceView  本地 camera 信息。
             */
            @Override
            public void onCallConnected(RongCallSession rongCallSession, SurfaceView surfaceView) {

            }

            /**
             * 通话结束。
             * 通话中，对方挂断，己方挂断，或者通话过程网络异常造成的通话中断，都会回调 onCallDisconnected。
             *
             * @param rongCallSession 通话实体。
             * @param callDisconnectedReason      通话中断原因。
             */
            @Override
            public void onCallDisconnected(RongCallSession rongCallSession, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {

            }

            @Override
            public void onRemoteUserRinging(String s) {

            }

            /**
             * 被叫端加入通话。
             * 主叫端拨出电话，被叫端收到请求后，加入通话，回调 onRemoteUserJoined。
             *
             * @param userId      加入用户的 id。<br />
             * @param mediaType   加入用户的媒体类型，audio or video。<br />
             * @param userType    加入用户的类型，1:正常用户,2:观察者。<br />
             * @param remoteVideo 加入用户者的 camera 信息。如果 userType为2，remoteVideo对象为空；<br />
             *                    <pre class="prettyprint">
             *                                            public void onRemoteUserJoined(String userId, RongCallCommon.CallMediaType mediaType, int userType, SurfaceView remoteVideo) {
             *                                                 if (null != remoteVideo) {
             *                                                     ((RongRTCVideoView) remoteVideo).setMirror( boolean);//观看对方视频流是否镜像处理
             *                                                 }
             *                                            }
             *                                            </pre>
             */
            @Override
            public void onRemoteUserJoined(String userId, RongCallCommon.CallMediaType mediaType, int userType, SurfaceView remoteVideo) {

            }

            @Override
            public void onRemoteUserInvited(String s, RongCallCommon.CallMediaType callMediaType) {

            }

            /**
             * 通话中的远端参与者离开。
             * 回调 onRemoteUserLeft 通知状态更新。
             *
             * @param s 远端参与者的 id。
             * @param callDisconnectedReason 远端参与者离开原因。
             */
            @Override
            public void onRemoteUserLeft(String s, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {

            }

            @Override
            public void onMediaTypeChanged(String s, RongCallCommon.CallMediaType callMediaType, SurfaceView surfaceView) {

            }

            @Override
            public void onError(RongCallCommon.CallErrorCode callErrorCode) {

            }

            @Override
            public void onRemoteCameraDisabled(String s, boolean b) {

            }

            @Override
            public void onRemoteMicrophoneDisabled(String s, boolean b) {

            }

            @Override
            public void onNetworkReceiveLost(String s, int i) {

            }

            @Override
            public void onNetworkSendLost(int i, int i1) {

            }

            @Override
            public void onFirstRemoteVideoFrame(String s, int i, int i1) {

            }

            @Override
            public void onAudioLevelSend(String s) {

            }

            @Override
            public void onAudioLevelReceive(HashMap<String, String> hashMap) {

            }

            @Override
            public void onRemoteUserPublishVideoStream(String s, String s1, String s2, SurfaceView surfaceView) {

            }

            @Override
            public void onRemoteUserUnpublishVideoStream(String s, String s1, String s2) {

            }
        });

    }

    /**
     * 解析消息体
     *
     * @param message
     */
    private void parsingImMessage(Message message) {
        LogUtils.i("message:" + message);
        String objectName = message.getObjectName();
        //文本消息
        if (objectName.equals(CloudManager.MSG_TEXT_NAME)) {
            //获取消息主体
            TextMessage textMessage = (TextMessage) message.getContent();
            String content = textMessage.getContent();
            LogUtils.i("content:" + content);
            TextBean textBean = null;
            try {
                textBean = new Gson().fromJson(content, TextBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //普通消息
            if (textBean.getType().equals(CloudManager.TYPE_TEXT)) {
                MessageEvent event = new MessageEvent(EventManager.FLAG_SEND_TEXT);
                event.setText(textBean.getMsg());
                event.setUserId(message.getSenderUserId());
                EventManager.post(event);
            }
            //添加好友消息
            else if (textBean.getType().equals(CloudManager.TYPE_ADD_FRIEND)) {
                //存入数据库 Bmob RongCloud 都没有提供存储方法
                //使用另外的方法来实现 存入本地数据库
                LogUtils.i("添加好友消息");
                LitePalHelper.getInstance().saveNewFriend(textBean.getMsg(), message.getSenderUserId());
//                saveNewFriend(textBean.getMsg(), message.getSenderUserId());
            } else if (textBean.getType().equals(CloudManager.TYPE_ARGEED_FRIEND)) {
                //1.添加到好友列表
                BmobManager.getInstance().addFriend(message.getSenderUserId(), new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
//                            pushSystem(message.getSenderUserId(), 0, 1, 0, "");
                            //2.刷新好友列表
                            EventManager.post(EventManager.FLAG_UPDATE_FRIEND_LIST);
                        }
                    }
                });
            }
        }
        //图片消息
        else if (objectName.equals(CloudManager.MSG_IMAGE_NAME)) {
            try {
                ImageMessage imageMessage = (ImageMessage) message.getContent();
                String url = imageMessage.getRemoteUri().toString();
                if (!TextUtils.isEmpty(url)) {
                    LogUtils.i("url:" + url);
                    MessageEvent event = new MessageEvent(EventManager.FLAG_SEND_IMAGE);
                    event.setImgUrl(url);
                    event.setUserId(message.getSenderUserId());
                    EventManager.post(event);
                }
            } catch (Exception e) {
                LogUtils.e("e." + e.toString());
                e.printStackTrace();
            }
        }
    }
}
