package com.jk.mymeet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.jk.framework.base.BaseUIActivity;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.bmob.IMUser;
import com.jk.framework.constants.Constants;
import com.jk.framework.gson.TokenBean;
import com.jk.framework.manager.DialogManager;
import com.jk.framework.manager.HttpManager;
import com.jk.framework.view.DialogView;
import com.jk.mymeet.fragment.ChatFragment;
import com.jk.mymeet.fragment.MeFragment;
import com.jk.mymeet.fragment.SquareFragment;
import com.jk.mymeet.fragment.StarFragment;
import com.jk.mymeet.service.CloudService;
import com.jk.mymeet.ui.FirstUploadActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseUIActivity {


    @BindView(R.id.mMainLayout)
    FrameLayout mMainLayout;
    @BindView(R.id.iv_star)
    ImageView ivStar;
    @BindView(R.id.tv_star)
    TextView tvStar;
    @BindView(R.id.ll_star)
    LinearLayout llStar;
    @BindView(R.id.iv_square)
    ImageView ivSquare;
    @BindView(R.id.tv_square)
    TextView tvSquare;
    @BindView(R.id.ll_square)
    LinearLayout llSquare;
    @BindView(R.id.iv_chat)
    ImageView ivChat;
    @BindView(R.id.tv_chat)
    TextView tvChat;
    @BindView(R.id.ll_chat)
    LinearLayout llChat;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.tv_me)
    TextView tvMe;
    @BindView(R.id.ll_me)
    LinearLayout llMe;
    private StarFragment mStarFragment = null;
    private FragmentTransaction mStarTransaction = null;

    //广场
    private SquareFragment mSquareFragment = null;
    private FragmentTransaction mSquareTransaction = null;

    //聊天
    private ChatFragment mChatFragment = null;
    private FragmentTransaction mChatTransaction = null;

    //我的
    private MeFragment mMeFragment = null;
    private FragmentTransaction mMeTransaction = null;
    private DialogView mUploadView;
    private Disposable disposable;

//    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initFragment();

        checkMainTab(0);

        checkToken();

    }


    private void initView() {
        requestPermiss();
    }


    /**
     * 启动云服务去连接融云服务
     */
    private void startCloudService() {
        LogUtils.i("startCloudService");
        startService(new Intent(this, CloudService.class));
        //检查更新
//        new UpdateHelper(this).updateApp(null);
    }

    private void checkToken() {
        String token = SPUtils.getInstance().getString(Constants.SP_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            //todo 连接融云服务
            startCloudService();
        } else {
            IMUser user = BmobManager.getInstance().getUser();
            String tokenNickName = user.getTokenNickName();
            if (!TextUtils.isEmpty(tokenNickName)) {
                //创建token
                createToken();
            } else {
                createUploadDialog();
            }
        }
    }

    /**
     * 创建TOKEN
     */
    private void createToken() {
        LogUtils.i("createToken");
        if (BmobManager.getInstance().getUser() == null) {
            Toast.makeText(this, "登录异常", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 1.去融云后台获取Token
         * 2.连接融云
         */
        final HashMap<String, String> map = new HashMap<>();
        map.put("userId", BmobManager.getInstance().getUser().getObjectId());
        map.put("name", BmobManager.getInstance().getUser().getTokenNickName());
        map.put("portraitUri", TextUtils.isEmpty(BmobManager.getInstance().getUser().getTokenPhoto())
                ? "http://b-ssl.duitang.com/uploads/item/201804/29/20180429210111_gtsnf.jpg" : BmobManager.getInstance().getUser().getTokenPhoto());

        //通过OkHttp请求Token
        //线程调度
//        RxAndroidPlugins

        disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                String json = HttpManager.getInstance().postCloudToken(map);
                observableEmitter.onNext(json);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        parsingCloudToken(s);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });

    }

    /**
     * 解析Token
     *
     * @param s
     */
    private void parsingCloudToken(String s) {
        try {
            LogUtils.i("parsingCloudToken:" + s);

            TokenBean tokenBean = GsonUtils.fromJson(s, TokenBean.class);
            if (tokenBean.getCode() == 200) {
                if (!TextUtils.isEmpty(tokenBean.getToken())) {
                    //保存Token

                    SPUtils.getInstance().put(Constants.SP_TOKEN, tokenBean.getToken());
                    startCloudService();
                }
            } else if (tokenBean.getCode() == 2007) {
                Toast.makeText(this, "注册人数已达上限，请替换成自己的Key", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            LogUtils.i("parsingCloudToken:" + e.toString());
        }
    }


    /**
     * 创建上传提示框
     */
    private void createUploadDialog() {
        mUploadView = DialogManager.getInstance().initView(this, R.layout.dialog_first_upload);
        //外部点击不能消息
        mUploadView.setCancelable(false);
        ImageView iv_go_upload = mUploadView.findViewById(R.id.iv_go_upload);
        iv_go_upload.setOnClickListener(view -> {
            startActivityForResult(new Intent(MainActivity.this, FirstUploadActivity.class), 1);
//            startActivity(new Intent(MainActivity.this, FirstUploadActivity.class));
        });
        DialogManager.getInstance().show(mUploadView);
    }

    private void initFragment() {

        if (mStarFragment == null) {
            mStarFragment = new StarFragment();
            mStarTransaction = getSupportFragmentManager().beginTransaction();
            mStarTransaction.add(R.id.mMainLayout, mStarFragment);
            mStarTransaction.commit();
        }

        if (mChatFragment == null) {
            mChatFragment = new ChatFragment();
            mChatTransaction = getSupportFragmentManager().beginTransaction();
            mChatTransaction.add(R.id.mMainLayout, mChatFragment);
            mChatTransaction.commit();
        }

        if (mSquareFragment == null) {
            mSquareFragment = new SquareFragment();
            mSquareTransaction = getSupportFragmentManager().beginTransaction();
            mSquareTransaction.add(R.id.mMainLayout, mSquareFragment);
            mSquareTransaction.commit();
        }

        if (mMeFragment == null) {
            mMeFragment = new MeFragment();
            mMeTransaction = getSupportFragmentManager().beginTransaction();
            mMeTransaction.add(R.id.mMainLayout, mMeFragment);
            mMeTransaction.commit();
        }
    }

    /**
     * 显示Fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏所有的Fragment
     *
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mStarFragment != null) {
            transaction.hide(mStarFragment);
        }
        if (mSquareFragment != null) {
            transaction.hide(mSquareFragment);
        }
        if (mChatFragment != null) {
            transaction.hide(mChatFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    /**
     * 切换主页选项卡
     *
     * @param index 0：星球
     *              1：广场
     *              2：聊天
     *              3：我的
     */
    private void checkMainTab(int index) {
        switch (index) {
            case 0:
                showFragment(mStarFragment);

                ivStar.setImageResource(R.drawable.img_star_p);
                ivSquare.setImageResource(R.drawable.img_square);
                ivChat.setImageResource(R.drawable.img_chat);
                ivMe.setImageResource(R.drawable.img_me);

                tvStar.setTextColor(getResources().getColor(R.color.colorAccent));
                tvSquare.setTextColor(Color.BLACK);
                tvChat.setTextColor(Color.BLACK);
                tvMe.setTextColor(Color.BLACK);

                break;
            case 1:
                showFragment(mSquareFragment);

                ivStar.setImageResource(R.drawable.img_star);
                ivSquare.setImageResource(R.drawable.img_square_p);
                ivChat.setImageResource(R.drawable.img_chat);
                ivMe.setImageResource(R.drawable.img_me);

                tvStar.setTextColor(Color.BLACK);
                tvSquare.setTextColor(getResources().getColor(R.color.colorAccent));
                tvChat.setTextColor(Color.BLACK);
                tvMe.setTextColor(Color.BLACK);

                break;
            case 2:
                showFragment(mChatFragment);

                ivStar.setImageResource(R.drawable.img_star);
                ivSquare.setImageResource(R.drawable.img_square);
                ivChat.setImageResource(R.drawable.img_chat_p);
                ivMe.setImageResource(R.drawable.img_me);

                tvStar.setTextColor(Color.BLACK);
                tvSquare.setTextColor(Color.BLACK);
                tvChat.setTextColor(getResources().getColor(R.color.colorAccent));
                tvMe.setTextColor(Color.BLACK);

                break;
            case 3:
                showFragment(mMeFragment);

                ivStar.setImageResource(R.drawable.img_star);
                ivSquare.setImageResource(R.drawable.img_square);
                ivChat.setImageResource(R.drawable.img_chat);
                ivMe.setImageResource(R.drawable.img_me_p);

                tvStar.setTextColor(Color.BLACK);
                tvSquare.setTextColor(Color.BLACK);
                tvChat.setTextColor(Color.BLACK);
                tvMe.setTextColor(getResources().getColor(R.color.colorAccent));

                break;
        }
    }


    /**
     * 请求权限
     */
    private void requestPermiss() {
        //危险权限
        request(new OnPermissionsResult() {
            @Override
            public void OnSuccess() {

            }

            @Override
            public void OnFail(List<String> noPermissions) {
                LogUtils.e("noPermissions:" + noPermissions.toString());
            }
        });
    }


    /**
     * 防止重叠
     * 当应用的内存紧张的时候，系统会回收掉Fragment对象
     * 再一次进入的时候会重新创建Fragment
     * 非原来对象，我们无法控制，导致重叠
     *
     * @param fragment
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (mStarFragment == null && fragment instanceof StarFragment) {
            mStarFragment = (StarFragment) fragment;
        }
        if (mSquareFragment == null && fragment instanceof SquareFragment) {
            mSquareFragment = (SquareFragment) fragment;
        }
        if (mChatFragment == null && fragment instanceof ChatFragment) {
            mChatFragment = (ChatFragment) fragment;
        }
        if (mMeFragment == null && fragment instanceof MeFragment) {
            mMeFragment = (MeFragment) fragment;
        }
    }


    @OnClick({R.id.ll_star, R.id.ll_square, R.id.ll_chat, R.id.ll_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_star:
                checkMainTab(0);
                break;
            case R.id.ll_square:
                checkMainTab(1);
                break;
            case R.id.ll_chat:
                checkMainTab(2);
                break;
            case R.id.ll_me:
                checkMainTab(3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    DialogManager.getInstance().hide(mUploadView);
                    createToken();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed())
            disposable.dispose();
    }
}
