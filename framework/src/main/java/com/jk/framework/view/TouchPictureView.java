package com.jk.framework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.jk.framework.R;

/**
 * Created By Admin on 2020/1/5
 * Describe: 图片验证
 */
public class TouchPictureView extends View {

    //背景
    private Bitmap bgBitmap;
    private Paint bgPaint;

    //空白块
    private Bitmap mNullBitmap;
    private Paint mNullPaint;

    //移动方块
    private Bitmap mMoveBitmap;
    private Paint mMovePaint;

    //view 宽高
    private int mWidth;
    private int mHeight;

    //空白块宽高
    private int CODE_SIZE = 200;

    //空白块坐标
    private int LINE_W, LINE_H = 0;

    //移动块横坐标
    private int MOVE_X = 200;

    //误差值
    private int errorValue = 20;

    private boolean isMove;

    private onViewResultListener viewResultListener;

    public void setViewResultListener(onViewResultListener onViewResultListener) {
        this.viewResultListener = onViewResultListener;
    }


    public TouchPictureView(Context context) {
        super(context);
        init();
    }


    public TouchPictureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchPictureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        bgPaint = new Paint();
        mMovePaint = new Paint();
        mNullPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawNullCard(canvas);
        drawMoveCard(canvas);

    }

    /**
     * 绘制移动方块
     *
     * @param canvas
     */
    private void drawMoveCard(Canvas canvas) {
        //截取空白块位置的坐标的BitMap图像
        mMoveBitmap = Bitmap.createBitmap(bgBitmap, LINE_W, LINE_H, CODE_SIZE, CODE_SIZE);

        canvas.drawBitmap(mMoveBitmap, MOVE_X, LINE_H, mMovePaint);
    }

    /**
     * 绘制空白块
     *
     * @param canvas
     */
    private void drawNullCard(Canvas canvas) {
        mNullBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_null_card);
        //bitmap的宽
        CODE_SIZE = mNullBitmap.getWidth();

        LINE_W = mWidth / 3 * 2;
        LINE_H = mHeight / 2 - (CODE_SIZE / 2);
        canvas.drawBitmap(mNullBitmap, LINE_W, LINE_H, mNullPaint);
    }

    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawBg(Canvas canvas) {
        //获取图片
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_bg);

        //创建背景bitmap
        bgBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bgBitmap);
        mCanvas.drawBitmap(mBitmap, null, new Rect(0, 0, mWidth, mHeight), bgPaint);

        canvas.drawBitmap(bgBitmap, null, new Rect(0, 0, mWidth, mHeight), bgPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //判断点击的坐标是否方块内部，如果是允许拖动
                float x = event.getX();
                float y = event.getY();
                if (x < MOVE_X + CODE_SIZE && x > MOVE_X && y < LINE_H + CODE_SIZE && y > LINE_H)
                    isMove = true;
                else isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove) {
                    //防止越界
                    if (event.getX() > 0 && event.getX() < mWidth - CODE_SIZE) {
                        MOVE_X = (int) event.getX();
                        invalidate();

                        if (MOVE_X > (LINE_W - errorValue) && MOVE_X < (LINE_W + errorValue)) {
                            //验证通过
                            if (viewResultListener != null)
                                viewResultListener.Success();
//                            MOVE_X = 200;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起之后验证
                if (MOVE_X <= (LINE_W - errorValue) || MOVE_X >= (LINE_W + errorValue)) {
                    MOVE_X = 200;
                    invalidate();
                }
                break;
        }
        return true;
    }

    public interface onViewResultListener {
        void Success();
    }
}
