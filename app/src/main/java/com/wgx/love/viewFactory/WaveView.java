package com.wgx.love.viewFactory;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class WaveView extends View {
	private int width = 0;
	private int height = 0;
	private int baseLine = 0;
	private Paint mPaint;
	private int waveHeight = 60;// 波浪的最高度
	private int waveWidth;// 波长
	private float offset = 0f;// 偏移量

	public WaveView(Context context) {
		this(context,null);
	}

	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	/**
	 * 不断的更新偏移量，并且循环。
	 */
	private ValueAnimator mAnimator = null;

	private void updateXControl() {
		if (getVisibility() == View.VISIBLE) {
			if (mAnimator == null) {
				mAnimator = ValueAnimator.ofFloat(0, waveWidth);
			}
			// 设置一个波长的偏移
			mAnimator.setInterpolator(new LinearInterpolator());
			mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					float animatorValue = (float) animation.getAnimatedValue();
					offset = animatorValue;// 不断的设置偏移量，并重画
					postInvalidate();
				}
			});
			mAnimator.setDuration(3000);
			mAnimator.setRepeatCount(ValueAnimator.INFINITE);
			mAnimator.start();
		} else {
			if (mAnimator != null) {
				mAnimator.end();
				mAnimator.cancel();
				mAnimator = null;
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(getPath(1), mPaint);
		canvas.drawPath(getPath(2), mPaint);
		canvas.drawPath(getPath(3), mPaint);
	}

	// 初始化paint，没什么可说的。
	private void initView() {
		mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getMeasuredWidth();// 获取屏幕宽度
		height = getMeasuredHeight();// 获取屏幕高度
		waveWidth = width;
		baseLine = height / 2;
		updateXControl();
	}

	/**
	 * 核心代码，计算path
	 * 
	 * @return
	 */
	private Path getPath(int style) {
		int itemWidth = waveWidth / 2;// 半个波长
		Path mPath = new Path();
		if (style == 1) {
			mPaint.setColor(0xffb1e8fd);
			mPath.moveTo(-itemWidth * 3 + 5, baseLine + 5);// 起始坐标
			for (int i = -3; i < 2; i++) {
				int startX = i * itemWidth;
				mPath.quadTo(startX + itemWidth / 2 + offset + waveWidth / 2 / 3, // 控制点的X,（起始点X
																					// +
																					// itemWidth/2
																					// +
																					// offset)
						getWaveHeigh(i), // 控制点的Y
						startX + itemWidth + offset + waveWidth / 2 / 3, // 结束点的X
						baseLine - i * 2// 结束点的Y
				);// 只需要处理完半个波长，剩下的有for循环自已就添加了。
			}
		} else if (2 == style) {
			mPaint.setColor(0xff9ed4ec);
			mPath.moveTo(-itemWidth * 3, baseLine);// 起始坐标
			for (int i = -3; i < 2; i++) {
				int startX = i * itemWidth;
				mPath.quadTo(startX + itemWidth / 2 + offset, // 控制点的X,（起始点X +
																// itemWidth/2 +
																// offset)
						getWaveHeigh(i), // 控制点的Y
						startX + itemWidth + offset, // 结束点的X
						baseLine + i * 2// 结束点的Y
				);// 只需要处理完半个波长，剩下的有for循环自已就添加了。
			}
		} else if (3 == style) {
			mPaint.setColor(0x214cfffd);
			mPath.moveTo(-itemWidth * 3 - 5, baseLine - 5);// 起始坐标
			for (int i = -3; i < 2; i++) {
				int startX = i * itemWidth;
				mPath.quadTo(startX + itemWidth / 2 + offset - waveWidth / 2 / 3, // 控制点的X,（起始点X
																					// +
																					// itemWidth/2
																					// +
																					// offset)
						getWaveHeigh(i), // 控制点的Y
						startX + itemWidth + offset - waveWidth / 2 / 3, // 结束点的X
						baseLine + i * 2// 结束点的Y
				);// 只需要处理完半个波长，剩下的有for循环自已就添加了。
			}
		}
		// 核心的代码就是这里
		// 下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色，大家可以把这3句话注释了看看效果。
		/*
		 * mPath.lineTo(width, height); mPath.lineTo(0, 2*height);
		 * mPath.close();
		 */
		return mPath;
	}

	// 奇数峰值是正的，偶数峰值是负数
	private int getWaveHeigh(int num) {
		if (num % 2 == 0) {
			return baseLine + waveHeight;
		}
		return baseLine - waveHeight;
	}
	
}
