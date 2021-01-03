package com.jk.mymeet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jk.framework.base.BaseFragment;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.bmob.IMUser;
import com.jk.framework.helper.GlideHelper;
import com.jk.mymeet.R;
import com.jk.mymeet.ui.NewFriendActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created By Admin on 2020/5/4
 * Describe:
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.iv_me_photo)
    CircleImageView ivMePhoto;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_server_status)
    TextView tvServerStatus;
    @BindView(R.id.ll_me_info)
    LinearLayout llMeInfo;
    @BindView(R.id.ll_new_friend)
    LinearLayout llNewFriend;
    @BindView(R.id.ll_private_set)
    LinearLayout llPrivateSet;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        loadMeInfo();
    }

    @OnClick({R.id.ll_me_info, R.id.ll_new_friend, R.id.ll_private_set, R.id.ll_share, R.id.ll_notice, R.id.ll_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_me_info:
                break;
            case R.id.ll_new_friend:
                startActivity(new Intent(getActivity(), NewFriendActivity.class));
                break;
            case R.id.ll_private_set:
                break;
            case R.id.ll_share:
                break;
            case R.id.ll_notice:
                break;
            case R.id.ll_setting:
                break;
        }
    }

    /**
     * 加载我的个人信息
     */
    private void loadMeInfo() {
        IMUser imUser = BmobManager.getInstance().getUser();
        GlideHelper.loadSmollUrl(getActivity(), imUser.getPhoto(), 100, 100, ivMePhoto);
        tvNickname.setText(imUser.getNickName());
    }
}
