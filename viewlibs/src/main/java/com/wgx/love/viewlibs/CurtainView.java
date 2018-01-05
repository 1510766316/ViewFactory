package com.wgx.love.viewlibs;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by imotor on 18-1-5.
 */

public class CurtainView extends LinearLayout {
    /** Scroller 拖动类 */
    private Scroller mScroller;
    /** 屏幕高度  */
    private int mScreenHeigh = 0;
    /** 点击时候Y的坐标*/
    private int downY = 0;
    /** 拖动时候Y的坐标*/
    private int moveY = 0;
    /** 拖动时候Y的方向距离*/
    private int scrollY = 0;
    /** 松开时候Y的坐标*/
    private int upY = 0;
    /** 是否在移动*/
    private Boolean isMoving = false;
    /** 布局的高度*/
    private int viewHeight = 0;
    /** 是否打开*/
    public boolean isShow = false;
    /** 是否可以拖动*/
    public boolean mEnabled = true;
    /** 点击外面是否关闭该界面*/
    public boolean mOutsideTouchable = true;
    /** 上升动画时间 */
    private int mDuration = 800;
    private final static String TAG = "CurtainView";

    private int mChildWitdh;
    private int mChildHeight;

    public CurtainView(Context context) {
        super(context);
        init(context);
    }
    public CurtainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public CurtainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context) {
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setBackgroundColor(Color.argb(0, 0, 0, 0));
        mScreenHeigh = 600;
        setFocusable(true);
        setClickable(true);
        mScroller = new Scroller(context);
        // 背景设置成透明
        this.setBackgroundColor(Color.argb(20, 0, 0, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();
        int cHeight;
        MarginLayoutParams cParams = null;
        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0; i < cCount; i++)
        {
            View childView = getChildAt(i);
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            height = cHeight+20+height+cParams.topMargin;
        }
        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension(sizeWidth, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int num = getChildCount();
        Log.i(TAG," onLayout num ="+num);
        for (int i=0;i<num;i++){
            View childView = getChildAt(i);
            if (View.GONE == childView.getVisibility()) {
                continue;
            }
            mChildWitdh = childView.getMeasuredWidth();
            mChildHeight = childView.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
            viewHeight = mChildHeight+20+viewHeight+cParams.topMargin;
            int pl,pt,pr,pb;
            pl = cParams.leftMargin;
            pt = cParams.topMargin*(i+1)+mChildHeight*i+20*i;
            pr = pl + mChildWitdh;
            pb = pt + +mChildHeight*(i+1)+20*i;
            childView.layout(pl,pt,pr,pb);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, " onInterceptTouchEvent ");
        if(!mEnabled){
            return false;
        }
       return onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                Log.d(TAG, "downY = " + downY);
                //如果完全显示的时候，让布局得到触摸监听，如果不显示，触摸事件不拦截，向下传递
                if(isShow){
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getY();
                scrollY = moveY - downY;
                //向下滑动
                if (scrollY > 0) {
                    if(isShow){
                        scrollTo(0, -Math.abs(scrollY));
                    }
                }else{
                    if(mScreenHeigh - this.getTop() <= viewHeight && !isShow){
                        scrollTo(0, Math.abs(viewHeight - scrollY));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                upY = (int) event.getY();
                if(isShow){
                    if( this.getScrollY() <= -(viewHeight /2)){
                        startMoveAnim(this.getScrollY(),-(viewHeight - this.getScrollY()), mDuration);
                        isShow = false;
                        Log.d(TAG,"isShow ="+ "false");
                    } else {
                        startMoveAnim(this.getScrollY(), -this.getScrollY(), mDuration);
                        isShow = true;
                        Log.d(TAG,"isShow = "+ "true");
                    }
                }
                Log.d(TAG, "this.getScrollY()="+this.getScrollY());
                changed();
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "ACTION_OUTSIDE");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void changeViewPosition(View child, int newIndex) {
        int currentIndex = indexOfChild(child);
        if (child != null && child.getParent() == this && currentIndex != newIndex) {
            removeView(child);
            addView(child, newIndex);
            invalidate();
        }
    }

    /**
     * 拖动动画
     * @param startY
     * @param dy  移动到某点的Y坐标距离
     * @param duration 时间
     */
    public void startMoveAnim(int startY, int dy, int duration) {
        isMoving = true;
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();//通知UI线程的更新
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 更新界面
            postInvalidate();
            isMoving = true;
        } else {
            isMoving = false;
        }
        super.computeScroll();
    }

    /** 开打界面 */
    public void show(){
        if(!isShow && !isMoving){
            setVisibility(VISIBLE);
            startMoveAnim(-viewHeight,0, mDuration);
            isShow = true;
            Log.d(TAG,"isShow"+"true");
            changed();
        }
    }

    /** 关闭界面 */
    public void dismiss(){
        if(isShow && !isMoving){
            startMoveAnim(0, -viewHeight, mDuration);
            isShow = false;
            Log.d(TAG,"isShow"+ "false");
            changed();
            setVisibility(GONE);
        }
    }

    /** 是否打开 */
    public boolean isShow(){
        return isShow;
    }

    /** 获取是否可以拖动*/
    public boolean isSlidingEnabled() {
        return mEnabled;
    }

    /** 设置是否可以拖动*/
    public void setSlidingEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    /**
     * 设置监听接口，实现遮罩层效果
     */
    public void setOnStatusListener(onStatusListener listener){
        this.statusListener = listener;
    }

    public void setOutsideTouchable(boolean touchable) {
        mOutsideTouchable = touchable;
    }
    /**
     * 显示状态发生改变时候执行回调借口
     */
    public void changed(){
        if(statusListener != null){
            if(isShow){
                statusListener.onShow();
            }else{
                statusListener.onDismiss();
            }
        }
    }

    /** 监听接口*/
    public onStatusListener statusListener;

    /**
     * 监听接口，来在主界面监听界面变化状态
     */
    public interface onStatusListener{
        /**  开打状态  */
         void onShow();
        /**  关闭状态  */
         void onDismiss();
    }

}