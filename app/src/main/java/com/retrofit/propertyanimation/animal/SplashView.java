package com.retrofit.propertyanimation.animal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.retrofit.propertyanimation.R;


public class SplashView extends View {

    /**
     * 大圆半径
     */
    private float mRotationRadius = 90;
    /**
     * 小圆半径
     */
    private float mCircleRadius = 18;
    /**
     * 小圆圈的颜色列表
     */
    private int[] mCircleColors;
    /**
     * 大圆和小圆旋转的时间,可以控制旋转速度
     */
    private long mRotationDuration = 3200;
    /**
     * 第二部分动画执行的总时间(各占1/3)
     */
    private long mSplashDuration = 3200;
    /**
     * 整体的背景颜色
     */
    private int mSplashBgColor = Color.WHITE;

    //空心圆初始半径
    private float mHoleRadius = 0F;
    /**
     * 当前大圆旋转的角度
     */
    private float mCurrentRotationAngle = 0F;
    /**
     * 当前大圆半径
     */
    private float mCurrentRotationRadius = mRotationRadius;

    /**
     * 绘制圆的画笔
     */
    private Paint mPaint = new Paint();
    /**
     * 绘制背景画笔
     */
    private Paint mPaintBackground = new Paint();

    /**
     * 屏幕正中心坐标
     */
    private float mCenterX;
    private float mCenterY;
    /**
     * 屏幕对角线一半
     */
    private float mDiagonalDist;

    /**
     * 保存当前动画状态
     */
    private SplashState mState = null;

    public SplashView(Context context) {
        super(context);
        init(context);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);

        //设置取消锯齿
        mPaint.setAntiAlias(true);
        mPaintBackground.setAntiAlias(true);
        //设置边框样式
        mPaintBackground.setStyle(Style.STROKE);
        mPaintBackground.setColor(mSplashBgColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2f;
        mCenterY = h / 2f;
        mDiagonalDist = (float) (Math.sqrt(w * w + h * h) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mState == null) {
            mState = new RotationState();
        }
        mState.drawState(canvas);
        super.onDraw(canvas);
    }

    /**
     * 数据加载完之后，加载后面的两个动画
     */
    public void splashAndDisapper() {
        if (mState != null && mState instanceof RotationState) {
            RotationState rs = (RotationState) mState;
            //取消第一个动画
            rs.cancel();
            post(new Runnable() {
                @Override
                public void run() {
                    mState = new MergingState();
                }

            });
        }
    }

    /**
     * 旋转类
     */
    private class RotationState extends SplashState {
        private ValueAnimator mAnimator;

        public RotationState() {
            //小圆半径,需要大圆半径和它旋转的角度
            //估值器--它使用的是弧度0~2PI
            mAnimator = ValueAnimator.ofFloat(0, (float) Math.PI * 2);
            //线性插值器，会平滑地计算；这样在每完成一个周期时，它不会卡顿
            mAnimator.setInterpolator(new LinearInterpolator());
            //设置旋转时间
            mAnimator.setDuration(mRotationDuration);
            mAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //这个mCurrentRotationAngle这个值的变化，就是造成小圆旋转的原因;若
                    //这个值不变了，则小圆们就不会旋转
                    mCurrentRotationAngle = (Float) animation.getAnimatedValue();
                    //提醒view重绘
                    invalidate();
                }
            });

            //设置旋转次数--无穷次数;因为它不知道什么时候进入下一个动画，所以把它设置为重复次数为无穷
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            //实现动画的绘制
            //1、清空背景
            drawBackground(canvas);
            //绘制小圆
            drawCircle(canvas);
        }

        /**
         * 取消该动画
         */
        public void cancel() {
            if (mAnimator!=null) {
                mAnimator.cancel();
            }
        }

    }

    /**
     * 聚合动画
     */
    public class MergingState extends SplashState {
        private ValueAnimator mAnimator;

        public MergingState() {
            //小圆半径,需要大圆半径和它旋转的角度
            //估值器
            mAnimator = ValueAnimator.ofFloat(0, mRotationRadius);
            //插值器-弹射效果的，tension表示弹射的范围长度
            mAnimator.setInterpolator(new OvershootInterpolator(30f));
            //设置动画时间
            mAnimator.setDuration(mSplashDuration / 3);
            mAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationRadius = (Float) animation.getAnimatedValue();
                    //提醒view重绘
                    invalidate();
                }
            });

            //监听动画执行完毕状态
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //进入下一个动画
                    mState = new ExpandingState();
                }
            });
            //反向调用的点向后播放
            mAnimator.reverse();
        }

        @Override
        public void drawState(Canvas canvas) {
            //实现动画的绘制
            //1、清空背景
            drawBackground(canvas);
            //绘制小圆
            drawCircle(canvas);
        }

    }

    /**
     * 扩散动画
     */
    private class ExpandingState extends SplashState {
        private ValueAnimator mAnimator;

        public ExpandingState() {
            //估值器 --空心圆的半径:0到对角线的一半
            mAnimator = ValueAnimator.ofFloat(0, mDiagonalDist);
            //设置动画时间
            mAnimator.setDuration(mSplashDuration / 3);
            mAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //空心圆半径
                    mHoleRadius = (Float) animation.getAnimatedValue();
                    //提醒view重绘
                    invalidate();
                }
            });


            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            //1、清空背景
            //drawBackground(canvas);
            //绘制空心圆效果
            if (mHoleRadius > 0f) {
                //画笔的宽度:对角线的一半 - 空心圆半径
                float storkeWidth = mDiagonalDist - mHoleRadius;
                mPaintBackground.setStrokeWidth(storkeWidth);
                //为了看的清楚动画扩散，storkeWidth设置为10
//                mPaintBackground.setStrokeWidth(10);

                float circleRadius = mHoleRadius + mDiagonalDist / 2f;
                //绘制扩散圆的效果
                canvas.drawCircle(mCenterX, mCenterY, circleRadius, mPaintBackground);
            }
        }

    }

    /**
     * 清空画布
     *
     * @param canvas
     */
    public void drawBackground(Canvas canvas) {
        canvas.drawColor(mSplashBgColor);
    }

    /**
     * 画多个小圆
     *
     * @param canvas
     */
    public void drawCircle(Canvas canvas) {
        //每个小圆的间隔角度
        float rotationAngle = (float) (Math.PI * 2 / mCircleColors.length);

        for (int i = 0; i < mCircleColors.length; i++) {
            //每个小圆i*间隔角度+旋转角度=当前小圆的真实角度
            double angle = mCurrentRotationAngle + i * rotationAngle;

            float cx = (float) (mCenterX + mCurrentRotationRadius * Math.cos(angle));
            float cy = (float) (mCenterY + mCurrentRotationRadius * Math.sin(angle));
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }

}