package com.jk.mymeet.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jk.framework.adapter.CommonAdapter;
import com.jk.framework.adapter.CommonViewHolder;
import com.jk.framework.base.BaseBackActivity;
import com.jk.framework.bmob.BmobManager;
import com.jk.framework.bmob.IMUser;
import com.jk.framework.cloud.CloudManager;
import com.jk.framework.db.LitePalHelper;
import com.jk.framework.db.NewFriend;
import com.jk.framework.event.EventManager;
import com.jk.framework.utils.CommonUtils;
import com.jk.mymeet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By Admin on 2021/1/3
 * Describe:
 */
public class NewFriendActivity extends BaseBackActivity {

    @BindView(R.id.item_empty_view)
    ViewStub itemEmptyView;
    @BindView(R.id.mNewFriendView)
    RecyclerView mNewFriendView;

    private Disposable disposable;
    private CommonAdapter<NewFriend> mNewFriendAdapter;
    private List<NewFriend> mList = new ArrayList<>();
    private IMUser imUser;
    private List<IMUser> mUserList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mNewFriendView.setLayoutManager(new LinearLayoutManager(this));
        mNewFriendView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mNewFriendAdapter = new CommonAdapter<>(mList, new CommonAdapter.OnBindDataListener<NewFriend>() {
            @Override
            public void onBindViewHolder(NewFriend model, CommonViewHolder viewHolder, int type, int position) {
                BmobManager.getInstance().queryObjectIdUser(model.getUserId(), new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        //填充具体属性
                        if (e == null) {
                            imUser = list.get(0);
                            mUserList.add(imUser);
                            viewHolder.setImageUrl(NewFriendActivity.this, R.id.iv_photo,
                                    imUser.getPhoto());
                            viewHolder.setImageResource(R.id.iv_sex, imUser.isSex() ?
                                    R.drawable.img_boy_icon : R.drawable.img_girl_icon);
                            viewHolder.setText(R.id.tv_nickname, imUser.getNickName());
                            viewHolder.setText(R.id.tv_age, imUser.getAge()
                                    + getString(R.string.text_search_age));
                            viewHolder.setText(R.id.tv_desc, imUser.getDesc());
                            viewHolder.setText(R.id.tv_msg, model.getMsg());

                            if (model.getIsAgree() == 0) {
                                viewHolder.getView(R.id.ll_agree).setVisibility(View.GONE);
                                viewHolder.getView(R.id.tv_result).setVisibility(View.VISIBLE);
                                viewHolder.setText(R.id.tv_result, getString(R.string.text_new_friend_agree));
                            } else if (model.getIsAgree() == 1) {
                                viewHolder.getView(R.id.ll_agree).setVisibility(View.GONE);
                                viewHolder.getView(R.id.tv_result).setVisibility(View.VISIBLE);
                                viewHolder.setText(R.id.tv_result, getString(R.string.text_new_friend_no_agree));
                            }
                        }
                    }
                });

                //同意
                viewHolder.getView(R.id.ll_yes).setOnClickListener(v -> {
                    /**
                     * 1.同意则刷新当前的Item
                     * 2.将好友添加到自己的好友列表
                     * 3.通知对方我已经同意了
                     * 4.对方将我添加到好友列表
                     * 5.刷新好友列表
                     */
                    updateItem(position, 0);
                    //将好友添加到自己的好友列表
                    //构建一个ImUSER
                    IMUser friendUser = new IMUser();
                    friendUser.setObjectId(model.getUserId());
                    BmobManager.getInstance().addFriend(friendUser, new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //保存成功
                                //通知对方
                                CloudManager.getInstance().sendTextMessage("",
                                        CloudManager.TYPE_ARGEED_FRIEND, imUser.getObjectId());
                                //刷新好友列表
                                EventManager.post(EventManager.FLAG_UPDATE_FRIEND_LIST);
                            }
                        }
                    });
                });

                //拒绝
                viewHolder.getView(R.id.ll_no).setOnClickListener(v -> updateItem(position, 1));

            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_new_friend_item;
            }
        });

        mNewFriendView.setAdapter(mNewFriendAdapter);

        queryNewFriend();


    }

    private void queryNewFriend() {

        disposable = Observable.create((ObservableOnSubscribe<List<NewFriend>>) emitter -> {
            emitter.onNext(LitePalHelper.getInstance().queryNewFriend());
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newFriends -> {
                    //更新UI
                    if (CommonUtils.isEmpty(newFriends)) {
                        mList.addAll(newFriends);
                        mNewFriendAdapter.notifyDataSetChanged();
                    } else {
                        itemEmptyView.inflate();
                        mNewFriendView.setVisibility(View.GONE);
                    }
                });

    }

    /**
     * 更新Item
     *
     * @param position
     * @param i
     */
    private void updateItem(int position, int i) {
        NewFriend newFriend = mList.get(position);
        //更新数据库
        LitePalHelper.getInstance().updateNewFriend(newFriend.getUserId(), i);
        //更新本地的数据源
        newFriend.setIsAgree(i);
        mList.set(position, newFriend);
        mNewFriendAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed())
            disposable.dispose();
    }


}
