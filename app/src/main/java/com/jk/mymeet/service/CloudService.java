package com.jk.mymeet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.SurfaceView;

import com.blankj.utilcode.util.LogUtils;
import com.jk.framework.cloud.CloudManager;
import com.jk.framework.constants.Constants;
import com.jk.framework.utils.SpUtils;

import java.util.HashMap;

import io.rong.calllib.IRongCallListener;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;

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
             *                    如果对端调用{@link RongCallClient#startCall(int, boolean, Conversation.ConversationType, String, List, List, RongCallCommon.CallMediaType, String, StartCameraCallback)} 或
             *                    {@link RongCallClient#acceptCall(String, int, boolean, StartCameraCallback)}开始的音视频通话，则可以使用如下设置改变对端视频流的镜像显示：<br />
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
}
