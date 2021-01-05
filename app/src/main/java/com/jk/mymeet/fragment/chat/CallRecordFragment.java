package com.jk.mymeet.fragment.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jk.framework.base.BaseFragment;
import com.jk.mymeet.R;

/**
 * Created By Admin on 2021/1/4
 * Describe:
 */
public class CallRecordFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_record, null);
//        initView(view);
        return view;
    }
}
