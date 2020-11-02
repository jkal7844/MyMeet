package com.jk.mymeet.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jk.framework.base.BaseBackActivity;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.helper.FileHelper;
import com.jk.framework.manager.DialogManager;
import com.jk.framework.view.DialogView;
import com.jk.framework.view.LodingView;
import com.jk.mymeet.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By Admin on 2020/7/22
 * Describe:
 */
public class FirstUploadActivity extends BaseBackActivity implements View.OnClickListener {

    @BindView(R.id.iv_photo)
    CircleImageView ivPhoto;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.btn_upload)
    Button btnUpload;

    private LodingView mLodingView;
    private DialogView mPhotoSelectView;

    private TextView tv_camera;
    private TextView tv_ablum;
    private TextView tv_cancel;
    private File uploadFile = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_upload);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {

        initPhotoView();

        btnUpload.setEnabled(false);

        etNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    btnUpload.setEnabled(uploadFile != null);
                } else {
                    btnUpload.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initPhotoView() {

        mLodingView = new LodingView(this);
        mLodingView.setLodingText(getString(R.string.text_upload_photo_loding));

        mPhotoSelectView = DialogManager.getInstance()
                .initView(this, R.layout.dialog_select_photo, Gravity.BOTTOM);

        tv_camera = (TextView) mPhotoSelectView.findViewById(R.id.tv_camera);
        tv_camera.setOnClickListener(this);
        tv_ablum = (TextView) mPhotoSelectView.findViewById(R.id.tv_ablum);
        tv_ablum.setOnClickListener(this);
        tv_cancel = (TextView) mPhotoSelectView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);

    }

    @OnClick({R.id.iv_photo, R.id.btn_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo:
                DialogManager.getInstance().show(mPhotoSelectView);
                break;
            case R.id.btn_upload:
                uploadPhoto();
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_camera:
                DialogManager.getInstance().hide(mPhotoSelectView);
                if (!checkPermissions(Manifest.permission.CAMERA)) {
                    requestPermission(new String[]{Manifest.permission.CAMERA});
                } else {
                    //跳转到相机
                    FileHelper.getInstance().toCamera(this);
                }
                break;
            case R.id.tv_ablum:
                DialogManager.getInstance().hide(mPhotoSelectView);
                //跳转到相册
                FileHelper.getInstance().toAlbum(this);
                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mPhotoSelectView);
                break;
        }
    }

    /**
     * 上传头像
     */
    private void uploadPhoto() {
        //如果条件没有满足，是走不到这里的
        String nickName = etNickname.getText().toString().trim();
        mLodingView.show();
        BmobManager.getInstance().uploadFirstPhoto(nickName, new BmobManager.OnUploadPhotoListener() {
            @Override
            public void OnUpdateDone() {
                mLodingView.hide();
                setResult(1);
//                EventManager.post(EventManager.EVENT_REFRE_TOKEN_STATUS);
                finish();
            }

            @Override
            public void OnUpdateFail(BmobException e) {
                mLodingView.hide();
                Toast.makeText(FirstUploadActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FileHelper.CAMEAR_REQUEST_CODE:
                    try {
                        uploadFile = FileHelper.getInstance().getTempFile();
//                        FileHelper.getInstance().startPhotoZoom(this, FileHelper.getInstance().getTempFile());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case FileHelper.ALBUM_REQUEST_CODE:
                    Uri uri = data.getData();
                    if (uri != null) {
                        String path = FileHelper.getInstance().getRealPathFromURI(this, uri);
                        if (!TextUtils.isEmpty(path)) {
                            uploadFile = new File(path);
                            try {
//                                FileHelper.getInstance().startPhotoZoom(this, uploadFile);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case FileHelper.CAMERA_CROP_RESULT:
                    break;

            }

            //设置头像

            if (uploadFile != null) {
                Bitmap mBitmap = BitmapFactory.decodeFile(uploadFile.getPath());
                ivPhoto.setImageBitmap(mBitmap);

                //判断当前的输入框
                String nickName = etNickname.getText().toString().trim();
                btnUpload.setEnabled(!TextUtils.isEmpty(nickName));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
