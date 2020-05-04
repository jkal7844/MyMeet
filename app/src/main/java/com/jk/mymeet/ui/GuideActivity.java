package com.jk.mymeet.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.jk.framework.base.BasePageAdapter;
import com.jk.framework.base.BaseUIActivity;
import com.jk.framework.manager.MediaPlayerManager;
import com.jk.framework.utils.AnimUtils;
import com.jk.mymeet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By Admin on 2020/1/5
 * Describe:
 */
public class GuideActivity extends BaseUIActivity {


    private View view1;
    private View view2;
    private View view3;
    private List<View> mPageList = new ArrayList<>();
    private BasePageAdapter mPageAdapter;

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.iv_music_switch)
    ImageView ivMusicSwitch;
    @BindView(R.id.tv_guide_skip)
    TextView tvGuideSkip;
    @BindView(R.id.iv_guide_point_1)
    ImageView ivGuidePoint1;
    @BindView(R.id.iv_guide_point_2)
    ImageView ivGuidePoint2;
    @BindView(R.id.iv_guide_point_3)
    ImageView ivGuidePoint3;

    private ImageView ivGuideStar;
    private TextView tvGuideStar;
    private ImageView ivGuideNight;
    private ImageView ivGuideSmile;
    private MediaPlayerManager mGuideMusic;
    private ObjectAnimator mAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        initView();
        startMusic();
    }


    private void initView() {
        view1 = View.inflate(this, R.layout.layout_pager_guide_1, null);
        view2 = View.inflate(this, R.layout.layout_pager_guide_2, null);
        view3 = View.inflate(this, R.layout.layout_pager_guide_3, null);

        mPageList.add(view1);
        mPageList.add(view2);
        mPageList.add(view3);

        //预加载
        mViewPager.setOffscreenPageLimit(mPageList.size());
        mPageAdapter = new BasePageAdapter(mPageList);
        mViewPager.setAdapter(mPageAdapter);

        ivGuideStar = view1.findViewById(R.id.iv_guide_star);
        ivGuideNight = view2.findViewById(R.id.iv_guide_night);
        ivGuideSmile = view3.findViewById(R.id.iv_guide_smile);

        //播放帧动画
        AnimationDrawable animStar = (AnimationDrawable) ivGuideStar.getBackground();
        animStar.start();
        AnimationDrawable animNight = (AnimationDrawable) ivGuideNight.getBackground();
        animNight.start();
        AnimationDrawable animSmile = (AnimationDrawable) ivGuideSmile.getBackground();
        animSmile.start();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ivGuidePoint1.setImageResource(R.drawable.img_guide_point_p);
                        ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                        break;
                    case 1:
                        ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint2.setImageResource(R.drawable.img_guide_point_p);
                        ivGuidePoint3.setImageResource(R.drawable.img_guide_point);
                        break;
                    case 2:
                        ivGuidePoint1.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint2.setImageResource(R.drawable.img_guide_point);
                        ivGuidePoint3.setImageResource(R.drawable.img_guide_point_p);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void startMusic() {
        mGuideMusic = new MediaPlayerManager();
//        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.guide);
//        mGuideMusic.startPlay(file);

        mAnim = AnimUtils.rotation(ivMusicSwitch);
        mAnim.start();
    }

    @OnClick({R.id.iv_music_switch, R.id.tv_guide_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_music_switch:
                if (mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PAUSE) {
                    mAnim.start();
                    mGuideMusic.continuePlay();
                    ivMusicSwitch.setImageResource(R.drawable.img_guide_music);
                } else if (mGuideMusic.MEDIA_STATUS == MediaPlayerManager.MEDIA_STATUS_PLAY) {
                    mAnim.pause();
                    mGuideMusic.pausePlay();
                    ivMusicSwitch.setImageResource(R.drawable.img_guide_music_off);
                }
                break;
            case R.id.tv_guide_skip:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGuideMusic.stopPlay();
    }
}
