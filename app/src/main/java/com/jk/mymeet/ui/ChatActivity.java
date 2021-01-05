package com.jk.mymeet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.jk.framework.adapter.CommonAdapter;
import com.jk.framework.base.BaseBackActivity;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.cloud.CloudManager;
import com.jk.framework.constants.Constants;
import com.jk.mymeet.R;
import com.jk.mymeet.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By Admin on 2021/1/5
 * Describe:
 */
public class ChatActivity extends BaseBackActivity {

    @BindView(R.id.mChatView)
    RecyclerView mChatView;
    @BindView(R.id.et_input_msg)
    EditText etInputMsg;
    @BindView(R.id.btn_send_msg)
    Button btnSendMsg;
    @BindView(R.id.ll_voice)
    LinearLayout llVoice;
    @BindView(R.id.ll_camera)
    LinearLayout llCamera;
    @BindView(R.id.ll_pic)
    LinearLayout llPic;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.ll_chat_bg)
    LinearLayout llChatBg;
    private String yourUserId;
    private String yourUserName;
    private String yourUserPhoto;
    private String meUserPhoto;
    private CommonAdapter<ChatModel> mChatAdapter;
    private List<ChatModel> mList = new ArrayList<>();

    /**
     * 跳转
     *
     * @param mContext
     * @param userId
     * @param userName
     * @param userPhoto
     */
    public static void startActivity(Context mContext, String userId,
                                     String userName, String userPhoto) {
        if (!CloudManager.getInstance().isConnect()) {
            Toast.makeText(mContext, mContext.getString(R.string.text_server_status), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, userId);
        intent.putExtra(Constants.INTENT_USER_NAME, userName);
        intent.putExtra(Constants.INTENT_USER_PHOTO, userPhoto);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        loadMeInfo();


    }

    @OnClick({R.id.btn_send_msg, R.id.ll_voice, R.id.ll_camera, R.id.ll_pic, R.id.ll_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_msg:
                break;
            case R.id.ll_voice:
                break;
            case R.id.ll_camera:
                break;
            case R.id.ll_pic:
                break;
            case R.id.ll_location:
                break;
        }
    }

    /**
     * 加载自我信息
     */
    private void loadMeInfo() {
        Intent intent = getIntent();
        yourUserId = intent.getStringExtra(Constants.INTENT_USER_ID);
        yourUserName = intent.getStringExtra(Constants.INTENT_USER_NAME);
        yourUserPhoto = intent.getStringExtra(Constants.INTENT_USER_PHOTO);

        meUserPhoto = BmobManager.getInstance().getUser().getPhoto();

        LogUtils.i("yourUserPhoto:" + yourUserPhoto);
        LogUtils.i("meUserPhoto:" + meUserPhoto);

        //设置标题
        if (!TextUtils.isEmpty(yourUserName)) {
            getSupportActionBar().setTitle(yourUserName);
        }
    }
}
