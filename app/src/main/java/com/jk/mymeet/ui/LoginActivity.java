package com.jk.mymeet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jk.framework.base.BaseUIActivity;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.bmob.IMUser;
import com.jk.framework.constants.Constants;
import com.jk.framework.manager.DialogManager;
import com.jk.framework.view.DialogView;
import com.jk.framework.view.LodingView;
import com.jk.framework.view.TouchPictureView;
import com.jk.mymeet.MainActivity;
import com.jk.mymeet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created By Admin on 2020/1/5
 * Describe:
 */
public class LoginActivity extends BaseUIActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_test_login)
    TextView tvTestLogin;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;

    private static final int H_TIME = 1001;
    private static int TIME = 60;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case H_TIME:
                    TIME--;
                    btnSendCode.setText(TIME + "s");
                    if (TIME > 0)
                        mHandler.sendEmptyMessageDelayed(H_TIME, 1000);
                    else {
                        btnSendCode.setEnabled(true);
                        btnSendCode.setText(getString(R.string.text_login_send));
                    }
                    break;
            }
            return false;
        }
    });
    private DialogView dialogView;
    private TouchPictureView mPictureV;
    private LodingView mLodingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        etPhone.setText(SPUtils.getInstance().getString(Constants.SP_PHONE));

        initDialogView();
    }

    private void initDialogView() {
        mLodingView = new LodingView(this);

        dialogView = DialogManager.getInstance().initView(this, R.layout.dialog_code_view);
        mPictureV = dialogView.findViewById(R.id.mPictureV);
        mPictureV.setViewResultListener(() -> {
            DialogManager.getInstance().hide(dialogView);
            sendSMS();
        });
    }


    @OnClick({R.id.btn_send_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                DialogManager.getInstance().show(dialogView);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(getString(R.string.text_login_phone_null));
            return;
        }

        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort(getString(R.string.text_login_code_null));
            return;
        }

        mLodingView.show(getString(R.string.text_login_now_login_text));

        BmobManager.getInstance().signOrLoginByMobilePhone(phone, code, new LogInListener<IMUser>() {
            @Override
            public void done(IMUser imUser, BmobException e) {
                mLodingView.hide();
                if (e == null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    SPUtils.getInstance().put(Constants.SP_PHONE, phone);
                    finish();
                } else LogUtils.e(e.toString());
            }
        });
    }

    /**
     * 发送验证码
     */
    private void sendSMS() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(getString(R.string.text_login_phone_null));
            return;
        }
        BmobManager.getInstance().requestSMS(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    btnSendCode.setEnabled(false);
                    mHandler.sendEmptyMessage(H_TIME);
                    ToastUtils.showShort(getString(R.string.text_user_resuest_succeed));
                } else {
                    ToastUtils.showShort(getString(R.string.text_user_resuest_fail));
                    LogUtils.e(e.toString());
                }
            }
        });
    }
}
