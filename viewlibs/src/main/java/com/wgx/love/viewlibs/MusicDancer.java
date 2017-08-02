package com.wgx.love.viewlibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created by wugx on 17-4-28.
 */

public class MusicDancer extends View {

    private final static String TAG = MusicDancer.class.getSimpleName();
    private int mViewWidth; //视图宽度
    private int mViewHeight; //视图高度

    private boolean autoDance = false;//自动跳舞标识
    private int DEFAULT_DANCER_TIME = 1000; //默认舞者刷新时间
    private int delayTime = DEFAULT_DANCER_TIME;
    private Paint mPaint;
    private int DEFAULT_DANCER_OFFSET = 2; //默认舞者线条偏移量
    private int DEFAULT_DANCER_NUM = 6; //默认舞者线条
    private int DEFAULT_DANCER_COLOR = Color.parseColor("#03A9F4");//默认舞者线条颜色
    private int DEFAULT_DANCER_WIDTH = 6; //默认舞者线条宽度
    private int danceOffset = DEFAULT_DANCER_OFFSET;
    private int dancerNum = DEFAULT_DANCER_NUM;
    private int dancerColor = DEFAULT_DANCER_COLOR;
    private int dancerWidth = DEFAULT_DANCER_WIDTH;

    public MusicDancer(Context context) {
        this(context, null);
    }

    public MusicDancer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicDancer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicDancer);
        autoDance = ta.getBoolean(R.styleable.MusicDancer_danceAuto, true);
        delayTime = ta.getInteger(R.styleable.MusicDancer_danceRefreshTime, DEFAULT_DANCER_TIME);
        danceOffset = ta.getInteger(R.styleable.MusicDancer_danceOffset, DEFAULT_DANCER_OFFSET);
        dancerNum = ta.getInteger(R.styleable.MusicDancer_danceNum, DEFAULT_DANCER_NUM);
        dancerColor = ta.getColor(R.styleable.MusicDancer_danceColor, DEFAULT_DANCER_COLOR);
        dancerWidth = ta.getInteger(R.styleable.MusicDancer_danceWidth, DEFAULT_DANCER_WIDTH);
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(dancerColor);
        mPaint.setStrokeWidth(dancerWidth);
    }

    /**
     * 开始动画
     */
    public void startAutoDance() {
        autoDance = true;
        invalidate();
    }

    public void stopDance() {
        autoDance = false;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Random random = new Random();
        for (int i = 0; i < dancerNum; i++) {
            canvas.drawRect(dancerWidth + danceOffset * 2 + (i - 1) * (dancerWidth + danceOffset), random.nextInt(mViewHeight), (dancerWidth + danceOffset) * (i + 1), mViewHeight, mPaint);
        }
        canvas.restore();
        if (autoDance)
            postInvalidateDelayed(delayTime);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为200，这个看我们自定义View的要求
        int result = 100;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = 100;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 60;
        if (specMode == MeasureSpec.AT_MOST) {
            result = 60;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        if (mViewWidth < dancerWidth * dancerNum + (dancerNum - 1) * danceOffset)
            throw new RuntimeException("mViewWidth < dancerWidth and dancerNum ");
        Log.i(TAG, "mViewWidth:" + mViewWidth + "  mViewHeight:" + mViewHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        autoDance = true;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        autoDance = false;
    }
}
