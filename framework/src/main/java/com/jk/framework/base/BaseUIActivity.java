package com.jk.framework.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.jk.framework.utils.SystemUI;

/**
 * Created By Admin on 2020/1/2
 * Describe:
 */
public class BaseUIActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemUI.fixSystemUI(this);
    }
}
