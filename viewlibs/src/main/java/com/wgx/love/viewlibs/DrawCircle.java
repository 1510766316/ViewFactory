package com.wgx.love.viewlibs;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;

/**
 * Created by wugx on 17-12-15.
 */

public class DrawCircle extends View {
    private final static String TAG = "DrawCircle";
    private Context mContext;
    /**
     * 外圈半径
     */
    private float outRadius;
    /**
     * 内圈半径
     */
    private float inRadius;
    /**
     * 线条偏移量
     */
    private int lineOffset;
    /**
     * 外圈背景色
     */
    private int outBg;
    /**
     * 内圈背景色
     */
    private int inBg;
    /**
     * 外圈长线条长度
     */
    private int outLongLine;
    /**
     * 外圈短线条长度
     */
    private int outShortLine;
    /**
     * 外圈长线条颜色
     */
    private int outLongLineColor;
    /**
     * 外圈短线条颜色
     */
    private int outShortLineColor;
    /**
     * 线条宽度
     */
    private int lineWidth;
    /**
     * 指示器图片
     */
    private Drawable indicateView;
    /**
     * 是否显示指示器
     */
    private boolean showIndicate;
    /**
     * 指示器X轴位置
     */
    private int mThumbX;
    /**
     *指示器X轴位置
     */
    private int mThumbY;
    /**
     * 指示器位图
     */
    private Bitmap indicateBitmap;
    /**
     * 视图宽度
     */
    private int mViewWidth;
    /**
     * 视图高度
     */
    private int mViewHeight;
    /**
     * 视图中心X坐标
     */
    private int mCentreX;
    /**
     * 视图中心Y坐标
     */
    private int mCentreY;
    /**
     * 按的Ｘ坐标
     */
    private float mDownX;
    /**
     * 按的Ｙ坐标
     */
    private float mDownY;
    /**
     * 滑动到Ｘ坐标
     */
    private float mMoveX;
    /**
     * 滑动到Ｙ坐标
     */
    private float mMoveY;

    /**
     * 当前夹角
     */
    private float mCurrentAngle;
    /**
     * 线条画笔
     */
    private Paint mLinePaint;
    /**
     * 背景画笔
     */
    private Paint mBgPaint;
    /**
     * 指示器画笔
     */
    private Paint mThumbPaint;
    /**
     * 正在滑动
     */
    private boolean isMoving = false;
    /**
     * 旋转角度
     */
    private float rotateAngle;
    private float moveAngle;
    /**
     * 最小进度值
     */
    private int minNum = 0;
    /**
     * 最大进度值
     */
    private int maxNum = 360;
    /**
     * 进度值
     */
    private int progress;
    /**
     * 滑动步进值
     */
    private int turnStep = 10;
    /**
     * 回调监听
     */
    private DrawCircleListener mListener;

    public DrawCircle(Context context) {
        this(context, null);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.DrawCircle);
        outRadius = typedArray.getDimension(R.styleable.DrawCircle_outRadius, 60f);
        inRadius = typedArray.getDimension(R.styleable.DrawCircle_inRadius, 110f);
        lineOffset = typedArray.getInteger(R.styleable.DrawCircle_lineOffset, 10);
        outBg = typedArray.getColor(R.styleable.DrawCircle_outBg, Color.parseColor("#00000000"));
        inBg = typedArray.getColor(R.styleable.DrawCircle_inBg, Color.parseColor("#FF6458d4"));
        outLongLine = typedArray.getInteger(R.styleable.DrawCircle_outLongLine, 24);
        outShortLine = typedArray.getInteger(R.styleable.DrawCircle_outShortLine, 14);
        outLongLineColor = typedArray.getColor(R.styleable.DrawCircle_outLongLineColor, Color.parseColor("#FF43abe9"));
        outShortLineColor = typedArray.getColor(R.styleable.DrawCircle_outShortLineColor, Color.parseColor("#FFFFFFFF"));
        lineWidth = typedArray.getInteger(R.styleable.DrawCircle_lineWidth, 2);
        indicateView = typedArray.getDrawable(R.styleable.DrawCircle_indicateView);
        showIndicate = typedArray.getBoolean(R.styleable.DrawCircle_showIndicate,true);
        typedArray.recycle();
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
        //设置一个默认值，就是这个View的默认宽度为340，这个看我们自定义View的要求
        int result = 340;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = 340;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 340;
        if (specMode == MeasureSpec.AT_MOST) {
            result = 340;
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
        mCentreX = mViewWidth / 2;
        mCentreY = mViewHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInView(canvas);
        drawOutView(canvas);
        drawThumbImg(canvas);
    }

    /**
     * 画内圈
     */
    private void drawInView(Canvas canvas) {
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(inBg);
        canvas.drawCircle(mCentreX, mCentreY, inRadius, mBgPaint);
    }

    /**
     * 画外圈
     */
    private void drawOutView(Canvas canvas) {
        //先画外圈背景图
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(outBg);
        canvas.drawCircle(mCentreX, mCentreY, outRadius + inRadius, mBgPaint);
        //画刻度
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(lineWidth);
        for (int i = 0; i < 48; i++) {
            //在旋转之前保存画布状态
            canvas.save();
            if (i % 6 == 0) {
                mLinePaint.setColor(outLongLineColor);
                canvas.rotate(i * 7.5f, mCentreX, mCentreY);
                canvas.drawLine(mCentreX, mCentreY - inRadius - lineOffset, mCentreX, mCentreY - inRadius - outLongLine - lineOffset, mLinePaint);
            } else {
                mLinePaint.setColor(outShortLineColor);
                canvas.rotate(i * 7.5f, mCentreX, mCentreY);
                canvas.drawLine(mCentreX, mCentreY - inRadius - lineOffset, mCentreX, mCentreY - inRadius - outShortLine - lineOffset, mLinePaint);
            }
            canvas.restore();
        }
    }

    /**
     * 画小指示器
     *
     * @param c
     */
    private void drawThumbImg(Canvas c) {
        mThumbPaint = new Paint();
        mThumbPaint.setColor(Color.WHITE);
        mThumbPaint.setStyle(Paint.Style.FILL);
        mThumbPaint.setAntiAlias(true);
        mThumbX = (int) (mCentreX + (inRadius+ outRadius/2 ) * Math.cos(mCurrentAngle));
        Log.i(TAG,"moveX="+mMoveX+"  mThumbX="+mThumbX);

        mThumbY = (int) (mCentreY - (inRadius+ outRadius/2 ) * Math.sin(mCurrentAngle));
        Log.i(TAG,"mMoveY="+mMoveY+"  mThumbY="+mThumbY);
        if (indicateView != null) {
            indicateView.setBounds(mThumbX - (int)outRadius / 2, mThumbY - (int)outRadius / 2, mThumbX + (int)outRadius / 2, mThumbY + (int)outRadius / 2);
            indicateView.draw(c);
        } else {
            // draw colored circle
            c.drawCircle(mThumbX, mThumbY, outRadius/4, mThumbPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMoving = false;
                mDownX = event.getX();
                mDownY = event.getY();
                mCurrentAngle = calcAngle(mDownX,mDownY);
                break;
            case MotionEvent.ACTION_MOVE:
                isMoving = true;
                mMoveX = event.getX();
                mMoveY = event.getY();
                moveAngle = calcAngle(mMoveX,mMoveY);
                float angleIncreased = moveAngle - mCurrentAngle;
                if (angleIncreased < -270){
                    angleIncreased = angleIncreased +360;
                }else if (angleIncreased > 270){
                    angleIncreased = angleIncreased -360;
                }
                if (inCircle()) {
                    calculateProgress(angleIncreased);
                    if (null != mListener){
                        mListener.onMoveDraw(progress);
                    }
                }
                mCurrentAngle = moveAngle;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isMoving) {
                    // 纠正指针位置
                    rotateAngle = (float) ((progress - minNum) * 7.5 / turnStep);
                    invalidate();
                    if (null != mListener){
                        mListener.onUpDraw(progress);
                    }
                }
                isMoving = false;
                break;
        }
        return true;
    }

    /**
     * 判断是否在外圆圈内
     *
     * @return
     */
    private boolean inCircle() {
        return (Math.abs(Math.pow(mDownX - mCentreX, 2) + Math.pow(mDownY - mCentreY, 2) - Math.pow(outRadius + inRadius, 2)) <= Math.pow(inRadius+lineOffset, 2));
    }

    /**
     * 以按钮圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     *
     * @param targetX x坐标
     * @param targetY y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        float x = targetX - mCentreX;
        float y = targetY - mCentreY;
        double radian;

        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }

    /**
     * 计算进度
     * @param angle
     */
    private void calculateProgress(float angle) {
        rotateAngle += angle;
        progress = (int)(rotateAngle/7.5)*turnStep+minNum;
        if (progress < minNum){
            progress = maxNum;
            rotateAngle = (float) ((progress - minNum) * 7.5 / turnStep);
        }else if (progress > maxNum){
            progress = minNum;
            rotateAngle = (float) ((progress - minNum) * 7.5 / turnStep);
        }
    }

    public void addDrawCircleListener(DrawCircleListener l) {
        mListener = l;
    }

    public interface DrawCircleListener {
        /**
         * 滑动时，显示进度值
         * @param tmpPro
         */
        void onMoveDraw(int tmpPro);

        /**
         * 滑动停止时，当前进度值
         * @param por
         */
        void onUpDraw(int por);
    }

    /**
     * 设置必要参数
     * @param min
     * @param max
     * @param Step
     */
    public void setDrawParms(int min, int max,int Step) {
        minNum = min;
        maxNum = max;
        turnStep = Step;
        invalidate();
    }

    /**
     * 设置进度值
     * @param pro
     */
    public void setProgress(int pro) {
        progress = pro;
        rotateAngle = (float) ((progress - minNum) * 7.5 / turnStep);
        invalidate();
    }

    /**
     * 返回进度值
     * @return
     */
    public int getProgress(){
        return progress;
    }

}
