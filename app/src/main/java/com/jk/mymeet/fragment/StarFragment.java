package com.jk.mymeet.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.jk.framework.adapter.CloudTagAdapter;
import com.jk.framework.base.BaseFragment;
import com.jk.mymeet.R;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created By Admin on 2020/5/4
 * Describe:
 */
public class StarFragment extends BaseFragment {

    @BindView(R.id.tv_star_title)
    TextView tvStarTitle;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_connect_status)
    TextView tvConnectStatus;
    @BindView(R.id.mCloudView)
    TagCloudView mCloudView;
    @BindView(R.id.tv_random)
    TextView tvRandom;
    @BindView(R.id.ll_random)
    LinearLayout llRandom;
    @BindView(R.id.tv_soul)
    TextView tvSoul;
    @BindView(R.id.ll_soul)
    LinearLayout llSoul;
    @BindView(R.id.tv_fate)
    TextView tvFate;
    @BindView(R.id.ll_fate)
    LinearLayout llFate;
    @BindView(R.id.tv_love)
    TextView tvLove;
    @BindView(R.id.ll_love)
    LinearLayout llLove;

    private List<String> mList = new ArrayList<>();
    private CloudTagAdapter cloudTagAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_star, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        for (int i = 0; i < 100; i++) {
            mList.add("star" + i);
        }



        cloudTagAdapter = new CloudTagAdapter(getActivity(), mList);
        mCloudView.setAdapter(cloudTagAdapter);

        mCloudView.setOnTagClickListener((parent, view, position) -> {
            ToastUtils.showShort("position==" + position);
        });
    }

    @OnClick({R.id.iv_camera, R.id.iv_add, R.id.ll_random, R.id.ll_soul, R.id.ll_fate, R.id.ll_love})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera:
                break;
            case R.id.iv_add:
                break;
            case R.id.ll_random:
                break;
            case R.id.ll_soul:
                break;
            case R.id.ll_fate:
                break;
            case R.id.ll_love:
                break;
        }
    }
}
