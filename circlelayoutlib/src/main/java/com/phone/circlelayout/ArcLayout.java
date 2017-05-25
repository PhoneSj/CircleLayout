package com.phone.circlelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Phone on 2015/5/24.
 */

public class ArcLayout extends ViewGroup {

	private static final int DEFAULT_START_ANGLE = 0;
	private static final int DEFAULT_END_ANGLE = 360;

	private int startAngle;
	private int endAngle;

	public ArcLayout(Context context) {
		this(context, null);
	}

	public ArcLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArcLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcLayout);
		startAngle = a.getInt(R.styleable.ArcLayout_startAngle, DEFAULT_START_ANGLE);
		endAngle = a.getInt(R.styleable.ArcLayout_endAngle, DEFAULT_END_ANGLE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.EXACTLY) {
			Log.i("phoneTest", "=============0=============");
			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST),
						MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.AT_MOST));
			}
		} else {
			Log.i("phoneTest", "=============1=============");
			int maxChildWidth = 0;
			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				child.measure(widthMeasureSpec, heightMeasureSpec);
				maxChildWidth = Math.max(child.getMeasuredWidth(), maxChildWidth);
			}
			widthSize = maxChildWidth * 3;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			Log.i("phoneTest", "=============2=============");
			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST),
						MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.AT_MOST));
			}
		} else {
			Log.i("phoneTest", "=============3=============");
			int maxChildHeight = 0;
			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				child.measure(widthMeasureSpec, heightMeasureSpec);
				maxChildHeight = Math.max(child.getMeasuredHeight(), maxChildHeight);
			}
			heightSize = maxChildHeight * 3;
		}
		Log.i("phoneTest", "widthSize:" + widthSize + ",heightSize:" + heightSize);
		//		setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, widthMode),
		//				MeasureSpec.makeMeasureSpec(heightSize, heightMode));
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
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
		int radius = Math.min((r - l), (b - t)) / 2 - Math.max(maxChildWidth, maxChildHeight) / 2;
		int deltaAngle = endAngle - startAngle;
		int interalAngle = deltaAngle / noGoneCount;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			int x = (int) ((l + r) / 2 + radius * Math.cos((startAngle + interalAngle * i) * Math.PI / 180));
			int y = (int) ((b + t) / 2 + radius * Math.sin((startAngle + interalAngle * i) * Math.PI / 180));
			child.layout(x - child.getMeasuredWidth() / 2, y - child.getMeasuredHeight() / 2,
					x + child.getMeasuredWidth() / 2, y + child.getMeasuredHeight() / 2);
		}

	}
}
