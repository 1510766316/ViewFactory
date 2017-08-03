package com.wgx.love.viewlibs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wugx on 17-8-3.
 */

public class ElegantLines extends View {
    private final static String TAG = "ElegantLines";

    private Path mPath;
    private Paint mPaint;

    private int mCenterX, mCenterY;
    private int mLineLenght;
    private int mLineCount;
    public ElegantLines(Context context) {
        this(context, null);
    }


    public ElegantLines(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElegantLines(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLineLenght = 200;
        mPath = new Path();
        mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(2);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.RED);
            mPaint.setAlpha(100 -  30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.moveTo(20,20);
        for (int i = 0; i < mLineCount ; i++) {
            mPath.quadTo(75,32,170,20);
        }

        canvas.drawPath(mPath,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为450，这个看我们自定义View的要求
        int result = 300;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = 300;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 120;
        if (specMode == MeasureSpec.AT_MOST) {
            result = 120;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 需要计算出屏幕能容纳多少个波形
        mCenterY = h / 2;
        mLineCount = (int) Math.round(w / mLineLenght + 1.5); // 计算波形的个数
    }
}
