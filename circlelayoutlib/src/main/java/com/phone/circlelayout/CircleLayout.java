package com.phone.circlelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Phone on 2015/5/24.
 */

public class CircleLayout extends ViewGroup {

	private static final int DEFAULT_START_ANGLE = 0;
	private static final int DEFAULT_SWEEP_ANGLE = 360;
	private static final int DEFAULT_RADIUS = 300;

	private int mStartAngle;//开始放置子控件的角度
	private int mSweepAngle;//借宿放置子控件的角度
	private int mRadius;//放置子控件圆形的半径

	public CircleLayout(Context context) {
		this(context, null);
	}

	public CircleLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleLayout);
		mStartAngle = a.getInt(R.styleable.CircleLayout_startAngle, DEFAULT_START_ANGLE);
		mSweepAngle = a.getInt(R.styleable.CircleLayout_sweepAngle, DEFAULT_SWEEP_ANGLE);
		mRadius = dp2px((int) a.getDimension(R.styleable.CircleLayout_radius, DEFAULT_RADIUS));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int count = getChildCount();

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		//测量子控件并找出子控件的最大宽高值
		int maxChildWidth = 0;
		int maxChildHeight = 0;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				measureChild(child, widthMeasureSpec, heightMeasureSpec);
				maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
				maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
			}
		}
		//直径+最大子控件的宽/高
		int maxWidth = maxChildWidth + mRadius * 2;
		int maxHeight = maxChildHeight + mRadius * 2;
		//加上padding值
		maxWidth += getPaddingLeft() + getPaddingRight();
		maxHeight += getPaddingTop() + getPaddingBottom();

		//与背景图片的宽高比较，取大值
		maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
		maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());

		//设置测量尺寸，使生效--此时FrameLayout的尺寸大小确定
		widthSize = widthMode == MeasureSpec.EXACTLY ? widthSize : maxWidth;
		heightSize = heightMode == MeasureSpec.EXACTLY ? heightSize : maxHeight;
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int width = right - left;
		int height = bottom - top;
		int count = getChildCount();
		//计算非Gone子控件的数量、最大宽高值
		int noGoneCount = 0;
		int maxChildWidth = 0;
		int maxChildHeight = 0;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				noGoneCount++;
				maxChildWidth = Math.max(child.getMeasuredWidth(), maxChildWidth);
				maxChildHeight = Math.max(child.getMeasuredHeight(), maxChildHeight);
			}
		}
		//计算每个子控件与圆心的夹角（弧度）
		int deltaAngle = mSweepAngle / noGoneCount;
		//布置子控件
		for (int i = 0, j = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			double curAngle = (mStartAngle + deltaAngle * j) * Math.PI / 180;
			int x = (int) (width / 2 + mRadius * Math.cos(curAngle));
			int y = (int) (height / 2 + mRadius * Math.sin(curAngle));
			child.layout(x - child.getMeasuredWidth() / 2, y - child.getMeasuredHeight() / 2,
					x + child.getMeasuredWidth() / 2, y + child.getMeasuredHeight() / 2);
			j++;
		}
	}

	private int dp2px(float dp) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
