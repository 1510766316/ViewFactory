package com.wgx.love.viewlibs;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

/**
 * Created by wugx on 17-8-1.
 */

public class WaveX extends View {

    private final static String TAG = "WaveX";

    private int mWaveLength;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mCenterY;
    private int mWaveCount;
    private int offset;

    private Path mPath;
    private Paint mPaint;

    private ValueAnimator mValueAnimation;

    private boolean status = true;
    Random random = new Random();

    public WaveX(Context context) {
        this(context, null);
    }

    public WaveX(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveX(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.YELLOW);
        mWaveLength = 400;
        if (null == mValueAnimation) {
            mValueAnimation = ValueAnimator.ofInt(0, 400);
        }
        mValueAnimation.setDuration(1200);
        mValueAnimation.setInterpolator(new LinearInterpolator());
        mValueAnimation.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate: offset="+offset);
                offset += 5;
                if (offset > mWaveLength) {
                    offset = offset - mWaveLength;
                }

                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 需要计算出屏幕能容纳多少个波形
        mPath = new Path();
        mScreenHeight = h;
        mScreenWidth = w;
        mCenterY = h / 2;
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5); // 计算波形的个数

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: offset="+offset);
        Log.i(TAG, "onDraw: mCenterY="+mCenterY);
        mPath.moveTo(-mWaveLength, mCenterY);
        for (int j = 0; j < mWaveCount; j++) {
            mPath.quadTo(-mWaveLength * 3 / 4 + j * mWaveLength + offset , mCenterY , -mWaveLength / 2 + j * mWaveLength + offset, mCenterY);
            mPath.quadTo(-mWaveLength / 4 + j * mWaveLength + offset, mCenterY, j * mWaveLength + offset, mCenterY);
        }
        canvas.drawPath(mPath, mPaint);
    }

    public void startAnimation() {
        if (null != mValueAnimation && mValueAnimation.isStarted()) {
            mValueAnimation.end();
        } else if (null != mValueAnimation && !mValueAnimation.isStarted()) {
            mValueAnimation.start();
        }
    }

    public boolean isPlaying() {
        if (null == mValueAnimation)
            return false;
        return mValueAnimation.isStarted();
    }

    public void stopPlay() {
        if (null != mValueAnimation && mValueAnimation.isStarted()) {
            mValueAnimation.end();
        }
    }
}
