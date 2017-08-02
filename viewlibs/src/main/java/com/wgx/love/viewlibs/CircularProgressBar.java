package com.wgx.love.viewlibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 自定义圆形进度条
 * Created by wugx on 16-12-28.
 */

public class CircularProgressBar extends View {
    private static final String TAG = CircularProgressBar.class.getSimpleName();

    private Paint mCircularPaint; // 背景圆画笔
    private Paint mCircularOnePaint; // 第一进度圆画笔
    private Paint mCircularTwoPaint; // 第二进度圆画笔
    private Paint mProgressTextPaint;//当前进度画笔
    private float CircularLineWidth; //圆形进度条宽度
    private int CircularBgColor;//背景圆颜色
    private int FillColorOne;//第一填充圆颜色
    private int FillColorTwo;//第二填充圆颜色
    private boolean ShowProgressText;//是否显示进度文字
    private int ProgressTextColor;//当期进度字体颜色
    private float ProgressTextSize;//当期进度字体大小
    private float CircularRadius; //圆半径

    private float default_CircleWidth = 10f;
    private int default_CircleBgColor = Color.parseColor("#FFB0B0B0");
    private int default_ProgressTextColor = Color.parseColor("#FFC0C0C0");
    private float default_Radius = 60f;
    private float default_TextSize = 24f;
    private int default_ThumbSize = 12;

    private float centreX, centreY;//进度视图当前的中心(x,y)
    private float indexOneProgress = 0f;//第一初始进度
    private float indexTwoProgress = 0f;//第二初始进度

    private Paint mThumbPaint;//当前进度画笔
    private int mStartAngle;//初始角度
    private int mThumbX;//指示器X轴位置
    private int mThumbY;//指示器X轴位置
    private Drawable mThumbImage;//指示器图片
    private int mThumbSize;//指示器大小
    private int mThumbColor;//指示器颜色

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);
        CircularLineWidth = a.getDimension(R.styleable.CircularProgressBar_LineWidth, default_CircleWidth);
        CircularBgColor = a.getColor(R.styleable.CircularProgressBar_BgColor, default_CircleBgColor);
        ShowProgressText = a.getBoolean(R.styleable.CircularProgressBar_ShowProgressText, true);
        ProgressTextColor = a.getColor(R.styleable.CircularProgressBar_TipsTextColor, default_ProgressTextColor);
        ProgressTextSize = a.getDimension(R.styleable.CircularProgressBar_TipsTextSize, default_TextSize);
        CircularRadius = a.getDimension(R.styleable.CircularProgressBar_Radius, default_Radius);
        FillColorOne = a.getColor(R.styleable.CircularProgressBar_FillFirstColor, Color.BLUE);
        FillColorTwo = a.getColor(R.styleable.CircularProgressBar_FillSecondColor, Color.parseColor("#FFD0D0D0"));
        mThumbColor = a.getColor(R.styleable.CircularProgressBar_ThumbColor, Color.GREEN);
        mThumbSize = a.getInteger(R.styleable.CircularProgressBar_ThumbSize, default_ThumbSize);
        mThumbImage = a.getDrawable(R.styleable.CircularProgressBar_ThumbImg);
        mStartAngle = a.getInteger(R.styleable.CircularProgressBar_StartAngle, 90);
        if (mStartAngle < 0)
            throw new RuntimeException("can not set startAngle < 0");
        else if (mStartAngle > 360)
            throw new RuntimeException("can not set startAngle > 360");
        Log.i(TAG, "mStartAngle:" + mStartAngle);
        a.recycle();
        initView();
    }

    private void initView() {
        /**
         * 圆
         */
        mCircularPaint = new Paint();
        mCircularPaint.setAntiAlias(true);
        mCircularPaint.setColor(CircularBgColor);
        mCircularPaint.setStrokeWidth(CircularLineWidth);
        mCircularPaint.setDither(true);
        mCircularPaint.setStyle(Paint.Style.STROKE);

        /**
         * 第一进度
         */
        mCircularOnePaint = new Paint();
        mCircularOnePaint.setAntiAlias(true);
        mCircularOnePaint.setColor(FillColorOne);
        mCircularOnePaint.setStrokeWidth(CircularLineWidth);
        mCircularOnePaint.setStrokeCap(Cap.BUTT);
        mCircularOnePaint.setDither(true);
        mCircularOnePaint.setStyle(Paint.Style.STROKE);

        /**
         * 第二进度
         */
        mCircularTwoPaint = new Paint();
        mCircularTwoPaint.setAntiAlias(true);
        mCircularTwoPaint.setColor(FillColorTwo);
        mCircularTwoPaint.setStrokeWidth(CircularLineWidth);
        mCircularTwoPaint.setStrokeCap(Cap.BUTT);
        mCircularTwoPaint.setDither(true);
        mCircularTwoPaint.setStyle(Paint.Style.STROKE);

        /**
         * 进度显示
         */
        mProgressTextPaint = new Paint();
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setColor(ProgressTextColor);
        mProgressTextPaint.setTextSize(ProgressTextSize);

        /**
         * 小指示器
         */
        mThumbPaint = new Paint();
        mThumbPaint.setColor(Color.GREEN);
        mThumbPaint.setStyle(Paint.Style.STROKE);
        mThumbPaint.setStrokeWidth(CircularLineWidth * 2);
        mThumbPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centreX = getWidth() / 2;
        centreY = getHeight() / 2;
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为200，这个看我们自定义View的要求
        int result = 400;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = 400;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 400;
        if (specMode == MeasureSpec.AT_MOST) {
            result = 400;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBase(canvas);
        drawSecondProgress(canvas);
        drawFirstProgress(canvas);
        drawProgressText(canvas);
        drawThumbImg(canvas);
    }

    /**
     * 画基本图形
     *
     * @param c
     */
    private void drawBase(Canvas c) {
        c.drawCircle(centreX, centreY, CircularRadius, mCircularPaint);
    }

    /**
     * 画第一进度
     *
     * @param c
     */
    private void drawFirstProgress(Canvas c) {
        Log.i(TAG, "drawFirstProgress indexOneProgress:" + indexOneProgress);
        RectF rectF = new RectF(centreX - CircularRadius, centreY - CircularRadius, centreX + CircularRadius, centreY + CircularRadius);
        c.drawArc(rectF, mStartAngle, indexOneProgress * 360 / 100, false, mCircularOnePaint);
    }

    /**
     * 画第二进度
     *
     * @param c
     */
    private void drawSecondProgress(Canvas c) {
        Log.i(TAG, "drawSecondProgress drawSecondProgress:" + indexOneProgress);
        RectF rectF = new RectF(centreX - CircularRadius, centreY - CircularRadius, centreX + CircularRadius, centreY + CircularRadius);
        c.drawArc(rectF, mStartAngle, indexTwoProgress * 360 / 100, false, mCircularTwoPaint);
    }

    /**
     * 当前进度显示
     *
     * @param c
     */
    private void drawProgressText(Canvas c) {
        if (!ShowProgressText)
            return;
        String progress = (int) indexTwoProgress + "%";
        Rect rect = new Rect();
        mProgressTextPaint.getTextBounds(progress, 0, progress.length(), rect);
        float textWidth = rect.width();
        float textHeight = rect.height();
        c.drawText(progress, centreX - textWidth / 2, centreY + textHeight / 2, mProgressTextPaint);
    }

    /**
     * 画小指示器
     *
     * @param c
     */
    private void drawThumbImg(Canvas c) {
        // find thumb position
        mThumbX = (int) (centreX + CircularRadius * Math.cos(-((indexOneProgress * 360 / 100) + mStartAngle) * Math.PI / 180));
        mThumbY = (int) (centreY - CircularRadius * Math.sin(-((indexOneProgress * 360 / 100) + mStartAngle) * Math.PI / 180));

        if (mThumbImage != null) {
            // draw png
            mThumbImage.setBounds(mThumbX - mThumbSize / 2, mThumbY - mThumbSize / 2, mThumbX + mThumbSize / 2, mThumbY + mThumbSize / 2);
            mThumbImage.draw(c);
        } else {
            // draw colored circle
            mThumbPaint.setColor(mThumbColor);
            mThumbPaint.setStyle(Paint.Style.FILL);
            c.drawCircle(mThumbX, mThumbY, mThumbSize, mThumbPaint);
        }
    }

    /**
     * 设置第一进度
     *
     * @param pro 进度
     */
    public void setOneProgress(float pro) {
        if (pro <= 0) {
            indexOneProgress = 0;
            invalidate();
        } else if (pro <= 100) {
            indexOneProgress = pro;
            invalidate();
        } else {
            indexOneProgress = 100;//最大100
            invalidate();
        }
    }

    /**
     * 设置第二进度
     *
     * @param pro 进度
     */
    public void setTwoProgress(float pro) {
        if (pro <= 0) {
            indexTwoProgress = 0;
            invalidate();
        } else if (pro <= 100) {
            indexTwoProgress = pro;
            invalidate();
        } else {
            indexTwoProgress = 100;
            invalidate();
        }
    }

}
