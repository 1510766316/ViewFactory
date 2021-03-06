package com.wgx.love.viewlibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

/**
 * Created by imotor on 17-12-25.
 */

public class VolumeProgressBar extends View {

    private final static String TAG = "VolumeProgressBar";
    private Context mContext;
    /**
     * 默认画笔
     */
    private Paint bgPaint;
    /**
     * 进度画笔
     */
    private Paint progressPaint;
    /**
     * 默认背景色
     */
    private int bgColor;
    /**
     * 进度背景色
     */
    private int progressColor;
    /**
     * 当前进度
     */
    private int proNum;
    /**
     * 视图宽度
     */
    private int mViewWidth;
    /**
     * 视图高度
     */
    private int mViewHeight;
    /**
     * 可画的个数
     */
    private int drawNum;
    /**
     * 每个进度条的高度
     */
    private int itemHeight;
    /**
     * 每个进度条的宽度
     */
    private int itemWidth;
    /**
     * 每个进度条之间的间距
     */
    private int itemOffset;
    /**
     * 每个进度条的圆角大小
     */
    private int circleSize;
    /**
     * 每个进度条前进半格
     */
    private boolean isHalfProgress;
    /**
     * 触摸标识
     */
    private boolean canTouch;

    /**
     * 竖向显示标识
     */
    private boolean isVertical;


    private VolumeProgressListener mListener;

    public VolumeProgressBar(Context context) {
        this(context, null);
    }

    public VolumeProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        Log.i(TAG, "VolumeProgressBar-- ");
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.VolumeProgressBar);
        bgColor = typedArray.getColor(R.styleable.VolumeProgressBar_bgColor, Color.argb(51, 255, 255, 255));
        progressColor = typedArray.getColor(R.styleable.VolumeProgressBar_progressColor, Color.parseColor("#FF3bbf36"));
        itemOffset = typedArray.getInteger(R.styleable.VolumeProgressBar_itemOffset, 20);
        circleSize = typedArray.getInteger(R.styleable.VolumeProgressBar_circleSize, 4);
        isHalfProgress = typedArray.getBoolean(R.styleable.VolumeProgressBar_halfProgress, true);
        canTouch = typedArray.getBoolean(R.styleable.VolumeProgressBar_canTouch, true);
        isVertical = typedArray.getBoolean(R.styleable.VolumeProgressBar_isVertical, true);
        itemHeight = typedArray.getInteger(R.styleable.VolumeProgressBar_itemHeight, isVertical ? 20 : 100);
        itemWidth = typedArray.getInteger(R.styleable.VolumeProgressBar_itemHeight, isVertical ? 100 : 20);
        typedArray.recycle();
        init();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        Log.i(TAG, "onMeasure ");
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为340，这个看我们自定义View的要求
        int result = isVertical ? 120 : 600;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = isVertical ? 600 : 120;
        if (specMode == MeasureSpec.AT_MOST) {
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged ");
        mViewWidth = w;
        mViewHeight = h;
        if (isVertical) {
            drawNum = mViewHeight / (itemHeight + itemOffset * 2);
        } else {
            drawNum = mViewWidth / (itemWidth + itemOffset * 2);
        }

        Log.i(TAG, "onSizeChanged drawNum=" + drawNum);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"ACTION_DOWN");
                playSoundEffect(SoundEffectConstants.CLICK);
                break;
            case MotionEvent.ACTION_MOVE:
                int itemS = (isVertical ? itemHeight : itemWidth) + itemOffset * 2;
                float moveD = isVertical ? event.getY() : event.getX();
                if (moveD < 0 || moveD > ((isVertical ? mViewHeight : mViewWidth) + itemOffset)) {
                    Log.i(TAG,"ACTION_MOVE 0 moveD="+moveD+"  1="+(moveD < 0)+"  2="+(moveD > ((isVertical ? itemHeight : itemWidth) + itemOffset)));
                    return true;
                }
                int pro = isVertical ? (drawNum - (int) moveD / itemS) : (int) moveD / itemS;
                if (isHalfProgress) {
                    pro = pro * 2;
                }
                if (pro < 0 || pro > (isHalfProgress ? drawNum * 2 : drawNum)) {
                    Log.i(TAG,"ACTION_MOVE 2");
                    return true;
                }
                if (proNum != pro) {
                    Log.i(TAG,"ACTION_MOVE pro="+pro);
                    proNum = pro;
                    invalidate();
                    if (null != mListener) {
                        mListener.onTouchListener(pro);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw ");
        drawBg(canvas);
        drawProgress(canvas);
    }

    private void drawBg(Canvas canvas) {
        RectF rectF;
        if (isVertical){
            for (int i = 0; i < drawNum; i++) {
                rectF = new RectF();
                rectF.left = 0;
                rectF.right = itemWidth;
                rectF.top = itemHeight * i + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                rectF.bottom = itemHeight * (i + 1) + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                canvas.drawRoundRect(rectF, circleSize, circleSize, bgPaint);
            }
        }else {
            for (int i = 0; i < drawNum; i++) {
                rectF = new RectF();
                rectF.left = itemWidth * i + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                rectF.right = itemWidth * (i + 1) + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                rectF.top = 0;
                rectF.bottom = itemHeight;
                canvas.drawRoundRect(rectF, circleSize, circleSize, bgPaint);
            }
        }

    }

    private void drawProgress(Canvas canvas) {
        RectF rectF;
        int tmpPro = proNum;
        if (isHalfProgress) {
            tmpPro = proNum % 2 == 1 ? proNum / 2 + 1 : proNum / 2;
        }
        Log.i(TAG, "proNum=" + proNum + " tmpPro=" + tmpPro);
        if (isVertical){
            for (int i = drawNum; i >= drawNum - tmpPro; i--) {
                rectF = new RectF();
                rectF.left = 0;
                rectF.right = itemWidth;
                if (proNum % 2 == 1 && drawNum - i == tmpPro && isHalfProgress) {
                    rectF.top = itemHeight * i + itemOffset * (i > 0 ? 2 * i + 1 : 1) + itemHeight / 2;
                } else {
                    rectF.top = itemHeight * i + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                }
                rectF.bottom = itemHeight * (i + 1) + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                canvas.drawRoundRect(rectF, circleSize, circleSize, progressPaint);
            }
        }else {
            for (int i = 0; i < tmpPro; i++) {
                rectF = new RectF();
                rectF.left = itemWidth * i + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                if (proNum % 2 == 1 &&   i + 1 == tmpPro && isHalfProgress) {
                    rectF.right = itemWidth * (i + 1) + itemOffset * (i > 0 ? 2 * i + 1 : 1) - itemWidth / 2;
                } else {
                    rectF.right = itemWidth * (i + 1) + itemOffset * (i > 0 ? 2 * i + 1 : 1);
                }
                rectF.top = 0;
                rectF.bottom = itemHeight;
                canvas.drawRoundRect(rectF, circleSize, circleSize, progressPaint);
            }
        }
    }

    /**
     * 获取当前最大进度数
     *
     * @return
     */
    public int getDrawNum() {
        return drawNum;
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getProgressNum() {
        return proNum;
    }

    /**
     * 设置进度
     *
     * @param pro
     */
    public void setProgress(int pro) {
        if (pro < 0 || pro > (isHalfProgress ? drawNum * 2 : drawNum)) {
            Log.e(TAG, "pro can not be high than drawNum or low than zero");
            return;
        }
        proNum = pro;
        invalidate();
    }

    public interface VolumeProgressListener {
        void onTouchListener(int pro);
    }

    public void addVolumeTouchListener(VolumeProgressListener l) {
        mListener = l;
    }
}
