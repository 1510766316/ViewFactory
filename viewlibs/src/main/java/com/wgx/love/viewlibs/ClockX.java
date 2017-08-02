package com.wgx.love.viewlibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * Created by wugx on 17-8-2.
 */

public class ClockX extends View {
    private final static String TAG = ClockX.class.getSimpleName();

    private Paint mCirclePaint;//表框画笔
    private Paint mNumPaint;//表盘数字画笔
    private Paint mHeartPaint;//表心画笔
    private Paint mHPaint;//时针画笔
    private Paint mMPaint;//分针画笔
    private Paint mSPaint;//秒针画笔
    private Drawable mClockDrawable;//表框背景图

    private int mViewWidth;//视图宽度
    private int mViewHeight;//视图高度
    private int offest;//偏移量
    private int mHWidth;//时针宽度
    private int mMWidth;//分针宽度
    private int mSWidth;//秒针宽度
    private float mDensityHour;//时针相对表框半径比例
    private float mDensityMinute;//分针相对表框半径比例
    private float mDensitySecond;//秒针相对表框半径比例
    private int mCircleColor;//表框颜色
    private int mHeartColor;//表心颜色
    private int mNumColor;//表盘数字颜色
    private int mHColor;//时针颜色
    private int mMColor;//分针颜色
    private int mSColor;//秒针颜色
    private int mCurrentH;//当前时
    private int mCurrentM;//当前分
    private int mCurrentS;//当前秒
    private int mMillSecond;//当前毫秒

    private float mClockRadius;//表框半径
    private float mHeartRadius;//表心半径
    private int mCircleWidth;//表框画笔宽度
    private int mNumWidth;//表框数字画笔宽度
    private int mHeartWidth;//表心画笔宽度

    private int DEFAULT_CIRCLE_WIDTH = 2;//默认表框画笔宽度
    private int DEFAULT_NUM_WIDTH = 4;//默认数字画笔宽度
    private int DEFAULT_HEART_WIDTH = 4;//默认数字画笔宽度
    private int DEFAULT_H_WIDTH = 3;//默认时针画笔宽度
    private int DEFAULT_M_WIDTH = 3;//默认分针画笔宽度
    private int DEFAULT_S_WIDTH = 3;//默认秒针画笔宽度
    private int DEFAULT_OFFEST = 6;//默认偏移量
    private int DEFAULT_HEART_RADIUS = 4;//默认表心半径
    private float DEFAULT_DENSITY_HOUR = 0.56f;//默认时针相对表框半径比例
    private float DEFAULT_DENSITY_MINUTE = 0.70f;//默认分针相对表框半径比例
    private float DEFAULT_DENSITY_SECOND = 0.85f;//默认秒针相对表框半径比例


    private int DEFAULT_H_COLOR = Color.parseColor("#76FF03");//默认时针颜色
    private int DEFAULT_M_COLOR = Color.parseColor("#00B0FF");//默认分针颜色
    private int DEFAULT_S_COLOR = Color.parseColor("#303F9F");//默认秒针颜色
    private int DEFAULT_CIRCLE_COLOR = Color.parseColor("#795548");//默认表框颜色
    private int DEFAULT_HEART_COLOR = Color.parseColor("#795548");//默认表心颜色
    private int DEFAULT_Num_COLOR = Color.parseColor("#004D40");//默认表盘数字颜色


    private int mCentreX;//视图X
    private int mCentreY;//视图Y

    private Thread refreshThread;//刷新时间线程
    private int delayTime = 1000;//秒针刷新的时间
    private Calendar mCalendar;//获取时间
    private boolean refreshTime = false;//刷新标识

    public ClockX(Context context) {
        this(context, null);
    }

    public ClockX(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockX(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClockX);
        offest = ta.getInteger(R.styleable.ClockX_offset, DEFAULT_OFFEST);
        mHWidth = ta.getInteger(R.styleable.ClockX_hWidth, DEFAULT_H_WIDTH);
        mMWidth = ta.getInteger(R.styleable.ClockX_hWidth, DEFAULT_M_WIDTH);
        mSWidth = ta.getInteger(R.styleable.ClockX_hWidth, DEFAULT_S_WIDTH);
        mHColor = ta.getColor(R.styleable.ClockX_hColor, DEFAULT_H_COLOR);
        mMColor = ta.getColor(R.styleable.ClockX_mColor, DEFAULT_M_COLOR);
        mSColor = ta.getColor(R.styleable.ClockX_sColor, DEFAULT_S_COLOR);
        mCircleColor = ta.getColor(R.styleable.ClockX_circleColor, DEFAULT_CIRCLE_COLOR);
        mHeartColor = ta.getColor(R.styleable.ClockX_heartColor, DEFAULT_HEART_COLOR);
        mNumColor = ta.getColor(R.styleable.ClockX_numColor, DEFAULT_Num_COLOR);
        mHeartRadius = ta.getInteger(R.styleable.ClockX_heartColor, DEFAULT_HEART_RADIUS);
        mCircleWidth = ta.getInteger(R.styleable.ClockX_circleWidth, DEFAULT_CIRCLE_WIDTH);
        mNumWidth = ta.getInteger(R.styleable.ClockX_numWidth, DEFAULT_NUM_WIDTH);
        mHeartWidth = ta.getInteger(R.styleable.ClockX_heartWidth, DEFAULT_HEART_WIDTH);
        mDensityHour = ta.getFloat(R.styleable.ClockX_densityHour, DEFAULT_DENSITY_HOUR);
        mDensityMinute = ta.getFloat(R.styleable.ClockX_densityMinute, DEFAULT_DENSITY_MINUTE);
        mDensitySecond = ta.getFloat(R.styleable.ClockX_densitySecond, DEFAULT_DENSITY_SECOND);
        ta.recycle();
        initView();
    }

    private void initView() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(mCircleWidth);

        mHeartPaint = new Paint();
        mHeartPaint.setColor(mHeartColor);
        mHeartPaint.setStyle(Paint.Style.FILL);
        mHeartPaint.setAntiAlias(true);
        mHeartPaint.setStrokeWidth(mHeartWidth);

        mNumPaint = new Paint();
        mNumPaint.setColor(mNumColor);
        mNumPaint.setStyle(Paint.Style.FILL);
        mNumPaint.setAntiAlias(true);
        mNumPaint.setTextSize(12);
        mNumPaint.setStrokeWidth(mNumWidth);

        mHPaint = new Paint();
        mHPaint.setColor(mHColor);
        mHPaint.setAntiAlias(true);
        mHPaint.setStrokeWidth(mHWidth);

        mMPaint = new Paint();
        mMPaint.setColor(mMColor);
        mMPaint.setAntiAlias(true);
        mMPaint.setStrokeWidth(mMWidth);

        mSPaint = new Paint();
        mSPaint.setColor(mSColor);
        mSPaint.setAntiAlias(true);
        mSPaint.setStrokeWidth(mSWidth);

        refreshTime = true;

        mClockDrawable = getBackground();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCalendar = Calendar.getInstance();
        mCurrentH = mCalendar.get(Calendar.HOUR);
        mCurrentM = mCalendar.get(Calendar.MINUTE);
        mCurrentS = mCalendar.get(Calendar.SECOND);
        mMillSecond = mCalendar.get(Calendar.MILLISECOND);
        Log.i(TAG, "----> mCurrentH:" + mCurrentH + " mCurrentM:" + mCurrentM + "  mCurrentS:" + mCurrentS);
        if (null == mClockDrawable || mClockDrawable instanceof ColorDrawable) {
            drawFrame(canvas);
            drawNum(canvas);
        }
        drawMinute(canvas);
        drawHour(canvas);
        drawSecond(canvas);
    }

    /**
     * 绘制表框,刻度,数字,表心
     *
     * @param canvas
     */
    private void drawFrame(Canvas canvas) {
        canvas.drawCircle(mCentreX, mCentreY, mClockRadius, mCirclePaint);
        for (int i = 0; i < 60; i++) {
            //在旋转之前保存画布状态
            canvas.save();
            if (i % 5 == 0) {
                canvas.rotate(i * 6, mCentreX, mCentreY);
                //1.2表示起点坐标，3.4表示终点坐标，5.画笔
                canvas.drawLine(mCentreX, mCentreY - mClockRadius, mCentreX, mCentreY - mClockRadius + 2 * offest, mCirclePaint);
            } else {
                canvas.rotate(i * 6, mCentreX, mCentreY);
                canvas.drawLine(mCentreX, mCentreY - mClockRadius, mCentreX, mCentreY - mClockRadius + offest, mCirclePaint);
            }
            //恢复画布状态
            canvas.restore();
        }
        canvas.drawCircle(mCentreX, mCentreY, mHeartRadius, mHeartPaint);
    }

    /**
     * 绘制数字
     *
     * @param canvas
     */
    private void drawNum(Canvas canvas) {
        String targetText[] = getContext().getResources().getStringArray(R.array.clockX);
        //绘制时间文字
        float startX = mViewWidth / 2 - mNumPaint.measureText(targetText[1]) / 2;
        float startY = mViewHeight / 2 - mViewWidth / 2 + offest * 5;
        float textR = (float) Math.sqrt(Math.pow(mViewWidth / 2 - startX, 2) + Math.pow(mViewHeight / 2 - startY, 2));

        for (int i = 0; i < 12; i++) {
            float x = (float) (startX + Math.sin(Math.PI / 6 * i) * textR);
            float y = (float) (startY + textR - Math.cos(Math.PI / 6 * i) * textR);
            if (i != 11 && i != 10 && i != 0) {
                y = y + mNumPaint.measureText(targetText[i]) / 2;
            } else {
                x = x - mNumPaint.measureText(targetText[i]) / 4;
                y = y + mNumPaint.measureText(targetText[i]) / 4;
            }
            canvas.drawText(targetText[i], x, y, mNumPaint);
        }
    }

    /**
     * 绘制时针
     *
     * @param canvas
     */
    private void drawHour(Canvas canvas) {
        float degreeHour = (float) mCurrentH * 360 / 12;
        float degreeMinute = (float) mCurrentM / 60 * 360 / 12;
        float degree = degreeHour + degreeMinute;
        canvas.rotate(degree, mViewWidth / 2, mViewHeight / 2);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, mViewHeight / 2 - (mViewWidth / 2 - offest * 2) * mDensityHour, mHPaint);
        canvas.rotate(-degree, mViewWidth / 2, mViewHeight / 2);

        //绘制尾部
        canvas.rotate(180 + degree, mViewWidth / 2, mViewHeight / 2);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, mViewHeight / 2 - offest * 1f, mHPaint);
        canvas.rotate(180 - degree, mViewWidth / 2, mViewHeight / 2);
    }

    /**
     * 绘制分针
     *
     * @param canvas
     */
    private void drawMinute(Canvas canvas) {
        float degree = (float) (mCurrentM * 360 / 60);
        canvas.rotate(degree, mViewWidth / 2, mViewHeight / 2);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, mViewHeight / 2 - (mViewWidth / 2 - offest * 2) * mDensityMinute, mMPaint);
        canvas.rotate(-degree, mViewWidth / 2, mViewHeight / 2);
        //绘制尾部
        canvas.rotate(180 + degree, mViewWidth / 2, mViewHeight / 2);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, mViewHeight / 2 - offest * 1.5f, mMPaint);
        canvas.rotate(180 - degree, mViewWidth / 2, mViewHeight / 2);
    }

    /**
     * 绘制秒针
     *
     * @param canvas
     */
    private void drawSecond(Canvas canvas) {
        float degree = delayTime > 1000 ? (float) (mCurrentS * 360 / 60) : (float) (mCurrentS * 360 / 60 + mMillSecond / 1000 * 360 / 60);
        canvas.rotate(degree, mViewWidth / 2, mViewHeight / 2);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, mViewHeight / 2 - (mViewWidth / 2 - offest * 2) * mDensitySecond, mSPaint);
        canvas.rotate(-degree, mViewWidth / 2, mViewHeight / 2);

        //绘制尾部
        canvas.rotate(180 + degree, mViewWidth / 2, mViewHeight / 2);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, mViewHeight / 2 - offest * 2f, mSPaint);
        canvas.rotate(180 - degree, mViewWidth / 2, mViewHeight / 2);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        width = height = Math.min(width, height);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为200，这个看我们自定义View的要求
        int result = 350;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = 350;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 350;
        if (specMode == MeasureSpec.AT_MOST) {
            result = 350;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mViewWidth = w;
        mViewHeight = h;
        mCentreX = mViewWidth / 2;
        mCentreY = mViewHeight / 2;
        mClockRadius = mViewWidth / 2 - offest;
        Log.i(TAG, "mViewWidth:" + mViewWidth + " -- mViewHeight:" + mViewHeight + " -- mClockRadius:" + mClockRadius);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //在添加到Activity的时候启动线程
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (refreshTime) {
                    //设置更新界面的刷新时间
                    SystemClock.sleep((long) delayTime);
                    postInvalidate();
                }
            }
        });
        refreshThread.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //停止刷新线程
        refreshTime = false;
    }
}
