package com.wgx.love.viewlibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wugx on 17-12-15.
 */

public class DrawCircle extends View {
    private final static String TAG = "DrawCircle";
    private Context mContext;
    /**
     *外圈半径
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
     *外圈背景色
     */
    private int outBg;
    /**
     *内圈背景色
     */
    private int inBg;
    /**
     *外圈长线条长度
     */
    private int outLongLine;
    /**
     *外圈短线条长度
     */
    private int outShortLine;
    /**
     *外圈长线条颜色
     */
    private int outLongLineColor;
    /**
     *外圈短线条颜色
     */
    private int outShortLineColor;
    /**
     * 线条宽度
     */
    private int lineWidth;
    /**
     *指示器图片
     */
    private Drawable indicateView;

    /**
     * 3D显示滑动动画
     */
    private boolean threeD;

    /**
     * 视图宽度
     */
    private int mViewWidth;
    /**
     * 视图高度
     */
    private int mViewHeight;
    /**
     *视图中心X坐标
     */
    private int mCentreX;
    /**
     *视图中心Y坐标
     */
    private int mCentreY;
    /**
     * 滑动到Ｘ坐标
     */
    private int mMoveX;
    /**
     * 滑动到Ｙ坐标
     */
    private int mMoveY;
    /**
     * 线条画笔
     */
    private Paint mLinePaint;
    /**
     * 背景画笔
     */
    private Paint mBgPaint;

    private Matrix mCameraMatrix;

    private Camera mCamera;
    public DrawCircle(Context context) {
        this(context,null);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.DrawCircle);
        outRadius = typedArray.getDimension(R.styleable.DrawCircle_outRadius,60f);
        inRadius = typedArray.getDimension(R.styleable.DrawCircle_inRadius,110f);
        lineOffset = typedArray.getInteger(R.styleable.DrawCircle_lineOffset,10);
        outBg = typedArray.getColor(R.styleable.DrawCircle_outBg, Color.parseColor("#00000000"));
        inBg = typedArray.getColor(R.styleable.DrawCircle_inBg, Color.parseColor("#FF6458d4"));
        outLongLine = typedArray.getInteger(R.styleable.DrawCircle_outLongLine,24);
        outShortLine = typedArray.getInteger(R.styleable.DrawCircle_outShortLine,14);
        outLongLineColor = typedArray.getColor(R.styleable.DrawCircle_outLongLineColor,Color.parseColor("#FF43abe9"));
        outShortLineColor = typedArray.getColor(R.styleable.DrawCircle_outShortLineColor,Color.parseColor("#FFFFFFFF"));
        lineWidth = typedArray.getInteger(R.styleable.DrawCircle_lineWidth,2);
        indicateView = typedArray.getDrawable(R.styleable.DrawCircle_indicateView);
        threeD = typedArray.getBoolean(R.styleable.DrawCircle_threeD,true);
        initView();
    }

    private void initView(){
        mCameraMatrix= new Matrix();
        mCamera = new Camera();
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
        rotateView(canvas);
        drawOutView(canvas);
    }

    /**
     * 画内圈
     */
    private void drawInView(Canvas canvas){
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(inBg);
        canvas.drawCircle(mCentreX,mCentreY,inRadius,mBgPaint);
    }

    /**
     * 画外圈
     */
    private void drawOutView(Canvas canvas){
        //先画外圈背景图
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(outBg);
        canvas.drawCircle(mCentreX,mCentreY,outRadius+inRadius,mBgPaint);
        //画刻度
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(lineWidth);
        for (int i = 0; i < 60; i++) {
            //在旋转之前保存画布状态
            canvas.save();
            if (i % 5 == 0) {
                mLinePaint.setColor(outLongLineColor);
                canvas.rotate(i * 6, mCentreX, mCentreY);
                canvas.drawLine(mCentreX, mCentreY - inRadius - lineOffset, mCentreX, mCentreY - inRadius - outLongLine - lineOffset , mLinePaint);
            } else {
                mLinePaint.setColor(outShortLineColor);
                canvas.rotate(i * 6, mCentreX, mCentreY);
                canvas.drawLine(mCentreX, mCentreY - inRadius - lineOffset, mCentreX, mCentreY - inRadius - outShortLine - lineOffset, mLinePaint);
            }
            canvas.restore();
        }
    }

    private void rotateView(Canvas canvas){
        mCameraMatrix.reset();
        mCamera.save();
        mCamera.rotateX(30);
        mCamera.rotateY(40);
        mCamera.getMatrix(mCameraMatrix);
        mCamera.restore();

        int matrixCenterX = mCentreX;
        int matrixCenterY = mCentreY;

        mCameraMatrix.preTranslate(-matrixCenterX, -matrixCenterY);
        mCameraMatrix.postTranslate(matrixCenterX, matrixCenterY);
        // 对matrix的变换应用到canvas上的所有对象
        canvas.concat(mCameraMatrix);
    }
}
