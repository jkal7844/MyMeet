package com.jk.mymeet.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jk.framework.base.BaseFragment;
import com.jk.mymeet.R;

/**
 * Created By Admin on 2020/5/4
 * Describe:
 */
public class ChatFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        return view;
    }
}
