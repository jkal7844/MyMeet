package com.jk.mymeet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jk.framework.adapter.CommonAdapter;
import com.jk.framework.adapter.CommonViewHolder;
import com.jk.framework.base.BaseUIActivity;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.bmob.Friend;
import com.jk.framework.bmob.IMUser;
import com.jk.framework.cloud.CloudManager;
import com.jk.framework.constants.Constants;
import com.jk.framework.helper.GlideHelper;
import com.jk.framework.manager.DialogManager;
import com.jk.framework.utils.CommonUtils;
import com.jk.framework.view.DialogView;
import com.jk.mymeet.R;
import com.jk.mymeet.model.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Admin on 2020/11/16
 * Describe:
 */
public class UserInfoActivity extends BaseUIActivity implements View.OnClickListener {

    @BindView(R.id.ll_back)
    RelativeLayout llBack;
    @BindView(R.id.iv_user_photo)
    CircleImageView ivUserPhoto;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.mUserInfoView)
    RecyclerView mUserInfoView;
    @BindView(R.id.btn_add_friend)
    Button btnAddFriend;
    @BindView(R.id.btn_chat)
    Button btnChat;
    @BindView(R.id.btn_audio_chat)
    Button btnAudioChat;
    @BindView(R.id.btn_video_chat)
    Button btnVideoChat;
    @BindView(R.id.ll_is_friend)
    LinearLayout llIsFriend;
    private DialogView mAddFriendDialogView;
    private EditText et_msg;
    private TextView tv_cancel;
    private TextView tv_add_friend;


    /**
     * 跳转
     *
     * @param mContext
     * @param userId
     */
    public static void startActivity(Context mContext, String userId) {
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        intent.putExtra(Constants.INTENT_USER_ID, userId);
        mContext.startActivity(intent);
    }

    //个人信息颜色
    private int[] mColor = {0x881E90FF, 0x8800FF7F, 0x88FFD700, 0x88FF6347, 0x88F08080, 0x8840E0D0};

    //用户ID
    private String userId = "";

    private IMUser imUser;

    private CommonAdapter<UserInfoModel> mUserInfoAdapter;
    private List<UserInfoModel> mUserInfoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        initAddFriendDialog();

        userId = getIntent().getStringExtra(Constants.INTENT_USER_ID);


        //列表
        mUserInfoAdapter = new CommonAdapter<>(mUserInfoList, new CommonAdapter.OnBindDataListener<UserInfoModel>() {
            @Override
            public void onBindViewHolder(UserInfoModel model, CommonViewHolder viewHolder, int type, int position) {
                //viewHolder.setBackgroundColor(R.id.ll_bg, model.getBgColor());
                viewHolder.getView(R.id.ll_bg).setBackgroundColor(model.getBgColor());
                viewHolder.setText(R.id.tv_type, model.getTitle());
                viewHolder.setText(R.id.tv_content, model.getContent());
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_user_info_item;
            }
        });
        mUserInfoView.setLayoutManager(new GridLayoutManager(this, 3));
        mUserInfoView.setAdapter(mUserInfoAdapter);

        queryUserInfo();
    }

    /**
     * 添加好友的提示框
     */
    private void initAddFriendDialog() {
        mAddFriendDialogView = DialogManager.getInstance().initView(this, R.layout.dialog_send_friend);

        et_msg = (EditText) mAddFriendDialogView.findViewById(R.id.et_msg);
        tv_cancel = (TextView) mAddFriendDialogView.findViewById(R.id.tv_cancel);
        tv_add_friend = (TextView) mAddFriendDialogView.findViewById(R.id.tv_add_friend);

        et_msg.setText(getString(R.string.text_me_info_tips) + BmobManager.getInstance().getUser().getNickName());

        tv_cancel.setOnClickListener(this);
        tv_add_friend.setOnClickListener(this);
    }

    /**
     * 查询用户信息
     */
    private void queryUserInfo() {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        //查询用户信息
        BmobManager.getInstance().queryObjectIdUser(userId, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        imUser = list.get(0);
                        updateUserInfo(imUser);
                    }
                }
            }
        });

        //判断好友关系
        BmobManager.getInstance().queryMyFriends(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if (e == null) {
                    if (CommonUtils.isEmpty(list)) {
                        //你有一个好友列表
                        for (int i = 0; i < list.size(); i++) {
                            Friend friend = list.get(i);
                            //判断这个对象中的id是否跟我目前的userId相同
                            if (friend.getFriendUser().getObjectId().equals(userId)) {
                                //你们是好友关系
                                btnAddFriend.setVisibility(View.GONE);
                                llIsFriend.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param imUser
     */
    private void updateUserInfo(IMUser imUser) {
        //设置基本属性
        GlideHelper.loadUrl(UserInfoActivity.this, imUser.getPhoto(),
                ivUserPhoto);
        tvNickname.setText(imUser.getNickName());
        tvDesc.setText(imUser.getDesc());

        //性别 年龄 生日 星座 爱好 单身状态
        addUserInfoModel(mColor[0], getString(R.string.text_me_info_sex), imUser.isSex() ? getString(R.string.text_me_info_boy) : getString(R.string.text_me_info_girl));
        addUserInfoModel(mColor[1], getString(R.string.text_me_info_age), imUser.getAge() + getString(R.string.text_search_age));
        addUserInfoModel(mColor[2], getString(R.string.text_me_info_birthday), imUser.getBirthday());
        addUserInfoModel(mColor[3], getString(R.string.text_me_info_constellation), imUser.getConstellation());
        addUserInfoModel(mColor[4], getString(R.string.text_me_info_hobby), imUser.getHobby());
        addUserInfoModel(mColor[5], getString(R.string.text_me_info_status), imUser.getStatus());
        //刷新数据
        mUserInfoAdapter.notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param color
     * @param title
     * @param content
     */
    private void addUserInfoModel(int color, String title, String content) {
        UserInfoModel model = new UserInfoModel();
        model.setBgColor(color);
        model.setTitle(title);
        model.setContent(content);
        mUserInfoList.add(model);
    }

    @OnClick({R.id.ll_back, R.id.iv_user_photo, R.id.btn_add_friend, R.id.btn_chat, R.id.btn_audio_chat, R.id.btn_video_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_user_photo:
                break;
            case R.id.btn_add_friend:
                DialogManager.getInstance().show(mAddFriendDialogView);
                break;
            case R.id.btn_chat:
                break;
            case R.id.btn_audio_chat:
                break;
            case R.id.btn_video_chat:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_friend:
                String msg = et_msg.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    msg = getString(R.string.text_user_info_add_friend);
                }
                CloudManager.getInstance().sendTextMessage(msg,
                        CloudManager.TYPE_ADD_FRIEND, userId);
                DialogManager.getInstance().hide(mAddFriendDialogView);
                Toast.makeText(this, getString(R.string.text_user_resuest_succeed), Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mAddFriendDialogView);
        }
    }
}
