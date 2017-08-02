package com.wgx.love.viewlibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * It is used as a switchover views
 *
 * Created by wugx on 17-7-12.
 */
public class IndicateX extends View {
    private static final String TAG = "IndicateX";

    /**
     * 文字笔
     */
    private Paint mTextPaint;
    /**
     * 线笔
     */
    private Paint mLinePaint;
    /**
     * 标签数组
     */
    private String[] mTabArray;

    /**
     * 默认选中位置
     */
    private int mSelectNum;

    /**
     * 默认颜色
     */
    private int mDefaultColor;
    /**
     * 选中颜色
     */
    private int mFocusColor;
    /**
     * 标签间距
     */
    private int mOffset;
    /**
     * 标签与下划线间距
     */
    private int mDistance;
    /**
     * 默认文字大小
     */
    private float mTextSize;
    /**
     * 默认指示下划线粗细
     */
    private float mLineSize;
    /**
     * 选中文字大小
     */
    private float mFocusTextSize;
    /**
     * 选中指示下划线粗细
     */
    private float mFocusLineSize;

    /**
     * 是否显示指示下划线
     */
    private boolean showLine;

    /**
     * 监听接口
     */
    private IndicateOnClickListener listener;

    public IndicateX(Context context) {
        this(context, null);
    }

    public IndicateX(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicateX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.IndicateX);
        mDefaultColor = ta.getColor(R.styleable.IndicateX_DefaultColor, 0xFFC0C0C0);
        mFocusColor = ta.getColor(R.styleable.IndicateX_FocusColor, 0xFFA440B1);
        mOffset = ta.getInteger(R.styleable.IndicateX_Offset, 8);
        mDistance = ta.getInt(R.styleable.IndicateX_Distance, 10);
        mFocusTextSize = ta.getDimension(R.styleable.IndicateX_FocusTextSize, 34);
        mTextSize = ta.getDimension(R.styleable.IndicateX_TextSize, 18);
        mFocusLineSize = ta.getDimension(R.styleable.IndicateX_FocusLineSize, 8);
        mLineSize = ta.getDimension(R.styleable.IndicateX_LineSize, 4);
        showLine = ta.getBoolean(R.styleable.IndicateX_ShowLine, true);
        ta.recycle();
        init();
    }

    private void init() {
        mTabArray = null;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mLineSize);
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
        int result = 450;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = 450;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 180;
        if (specMode == MeasureSpec.AT_MOST) {
            result = 180;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mTabArray) {
            return;
        }
        drawText(canvas);

        if (!showLine) {
            mDistance = 0;
            return;
        }
        drawLine(canvas);
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    public void drawText(Canvas canvas) {

        for (int i = 0; i < mTabArray.length; i++) {
            float distanceX = calculateTextX(i);
            float distanceY = calculateTextY();
            if (i == mSelectNum) {
                mTextPaint.setTextSize(mFocusTextSize);
                mTextPaint.setColor(mFocusColor);
            } else {
                mTextPaint.setTextSize(mTextSize);
                mTextPaint.setColor(mDefaultColor);
            }
            canvas.drawText(mTabArray[i], distanceX, distanceY - mDistance / 2, mTextPaint);

        }
    }

    /**
     * 画线
     *
     * @param canvas
     */
    public void drawLine(Canvas canvas) {
        for (int i = 0; i < mTabArray.length; i++) {
            float[] distanceX = calculateLineX(i);
            float distanceY = calculateTextY();
            if (i == mSelectNum) {
                mLinePaint.setTextSize(mFocusLineSize);
                mLinePaint.setColor(mFocusColor);
            } else {
                mLinePaint.setTextSize(mLineSize);
                mLinePaint.setColor(mDefaultColor);
            }
            canvas.drawLine(distanceX[0], distanceY + mDistance / 2, distanceX[1], distanceY + mDistance / 2, mLinePaint);
        }
    }

    /**
     * 计算文字X轴位置
     *
     * @param index 　文字下标
     * @return
     */
    private float calculateTextX(int index) {
        float result = 0.0f;
        for (int i = 0; i <= index; i++) {
            if (mSelectNum == i - 1) {
                mTextPaint.setTextSize(mFocusTextSize);
            } else {
                mTextPaint.setTextSize(mTextSize);
            }
            if (i == 0) {
                result += mOffset;
            } else {
                result += mTextPaint.measureText(mTabArray[i - 1]) + mOffset;
            }
        }
        return result;
    }

    /**
     * 计算下划线X轴位置
     *
     * @param index
     * @return
     */
    private float[] calculateLineX(int index) {
        float[] result = new float[2];
        for (int i = 0; i <= index; i++) {
            if (i == 0) {
                result[0] += mOffset;
                if (mSelectNum == 0) {
                    mTextPaint.setTextSize(mFocusTextSize);
                } else {
                    mTextPaint.setTextSize(mTextSize);
                }
                result[1] += mOffset + mTextPaint.measureText(mTabArray[0]);
            } else {
                if (mSelectNum == i - 1) {
                    mTextPaint.setTextSize(mFocusTextSize);
                } else {
                    mTextPaint.setTextSize(mTextSize);
                }
                result[0] += mTextPaint.measureText(mTabArray[i - 1]) + mOffset;
                if (mSelectNum == i) {
                    mTextPaint.setTextSize(mFocusTextSize);
                } else {
                    mTextPaint.setTextSize(mTextSize);
                }
                result[1] += mOffset + mTextPaint.measureText(mTabArray[i]);
            }
        }
        return result;
    }

    /**
     * 计算文字Y轴位置
     *
     * @return
     */
    private float calculateTextY() {
        Rect rect = new Rect();
        mTextPaint.setTextSize(mFocusTextSize);
        mTextPaint.getTextBounds(mTabArray[0], 0, mTabArray[0].length(), rect);
        FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        return (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int position = -1;
        float touchX = event.getX();
        float touchY = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                position = calculateCurrentPosition(touchX, touchY);
                break;
            default:
                Log.i(TAG, "action:" + action);
                break;
        }
        if (position != -1) {
            if (null != listener) {
                listener.onClick(this, position, mTabArray[position]);
            }
            setSelectNum(position);
        }
        return true;
    }

    /**
     * 计算当前点击的位置
     *
     * @param cx 　点击坐标Ｘ
     * @param cy 　点击坐标Ｙ
     * @return　位置，数组下标
     */
    private int calculateCurrentPosition(float cx, float cy) {
        int position = -1;
        for (int i = 0; i < mTabArray.length; i++) {
            float[] useX = calculateLineX(i);
            float useMinY = calculateTextY() - mDistance / 2;
            float useMaxY = (getMeasuredHeight() - getPaddingBottom() - getPaddingTop()) / 2 + useMinY;
            if (cx <= useX[1] && cx >= useX[0] && cy >= useMinY && cy <= useMaxY) {
                position = i;
            }
        }
        return position;
    }

    /**
     * 添加数据
     *
     * @param array
     */
    public void addTabData(String[] array) {
        if (null == array || array.length < 2 || array.length > 6) {
            throw new RuntimeException("TabArray length must more big than zero and more small than six !");
        }
        mTabArray = array;
        invalidate();
    }

    /**
     * 设置初始位置
     *
     * @param pos
     */
    public void setSelectNum(int pos) {
        if (pos > 0 && pos < mTabArray.length) {
            mSelectNum = pos;
        } else {
            mSelectNum = 0;
        }
        invalidate();
    }

    public void setIndicateOnClickListener(IndicateOnClickListener l) {
        listener = l;
    }

    /**
     * position:当前点击的下标
     * <p>
     * msg: 对应当前下标数组中的常量
     */
    public interface IndicateOnClickListener {
        /**
         * @param view     当前视图
         * @param position 　位置
         * @param msg      　数组下标对应的参数
         */
        void onClick(View view, int position, String msg);
    }

}
