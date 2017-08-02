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

/**
 * Created by wugx on 17-8-1.
 */

public class WaveX extends View{

    private final static String TAG = "WaveX";

    private int mWaveLength;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mCenterY;
    private  int mWaveCount;
    private int offset;

    private Path mPath;
    private Paint mPaint;

    private ValueAnimator mValueAnimation;
    public WaveX(Context context) {
        this(context,null);
    }

    public WaveX(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveX(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(Color.LTGRAY);
        mWaveLength = 800;
        if (null == mValueAnimation){
            mValueAnimation = ValueAnimator.ofInt(0, mWaveLength);
        }
        mValueAnimation.setDuration(1400);
        mValueAnimation.setInterpolator(new LinearInterpolator());
        mValueAnimation.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
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
        mPath.reset();
        mPath.moveTo(-mWaveLength,mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + offset,mCenterY + 60,-mWaveLength / 2 + i * mWaveLength + offset,mCenterY);
            mPath.quadTo(-mWaveLength / 4 + i * mWaveLength + offset,mCenterY - 60,i * mWaveLength + offset,mCenterY);
        }
        mPath.lineTo(mScreenWidth,mScreenHeight);
        mPath.lineTo(0,mScreenHeight);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }

    public void startAnimation(){
        if (null != mValueAnimation && mValueAnimation.isStarted()){
            mValueAnimation.end();
        }else if(null != mValueAnimation && !mValueAnimation.isStarted()){
            mValueAnimation.start();
        }
    }

    public boolean isPlaying(){
        if (null == mValueAnimation)
            return false;
        return mValueAnimation.isStarted();
    }

    public void stopPlay(){
        if (null != mValueAnimation && mValueAnimation.isStarted()){
            mValueAnimation.end();
        }
    }
}
