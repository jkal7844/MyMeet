package com.jk.mymeet;

import android.os.Bundle;

import com.jk.framework.base.BaseUIActivity;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.bmob.IMUser;
import com.jk.framework.utils.LogUtils;

import butterknife.ButterKnife;

public class MainActivity extends BaseUIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        IMUser imUser = BmobManager.getInstance().getUser();
        LogUtils.e(imUser.toString());

    }

}
