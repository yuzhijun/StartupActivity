package com.lenovohit.startupactivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 卡片叠加案例
 * Created by yuzhijun on 2017/9/17.
 */
public class CardLayout extends ViewGroup {
    //默认的水平间距
    private static final int DEFAULT_HORIZONTAL_SPACE = 50;
    //默认的垂直间距
    private static final int DEFAULT_VERTICAL_PACE = 50;
    //水平间距
    private int horizontalSpace;
    //垂直间距
    private int verticalSpace;

    public CardLayout(Context context) {
        this(context,null);
    }

    public CardLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CardLayout);
        horizontalSpace = a.getDimensionPixelOffset(R.styleable.CardLayout_horizontal_space,DEFAULT_HORIZONTAL_SPACE);
        verticalSpace = a.getDimensionPixelOffset(R.styleable.CardLayout_vertical_space,DEFAULT_VERTICAL_PACE);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;
//        measureChildren(widthMeasureSpec,heightMeasureSpec);
        for (int i = 0; i < getChildCount();i++){
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                CustomLayoutParams lp = (CustomLayoutParams) child.getLayoutParams();

                int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,child.getPaddingLeft() + child.getPaddingRight(),lp.width);
                int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,child.getPaddingTop() + child.getPaddingBottom(),lp.height);

                child.measure(childWidthSpec,childHeightSpec);

                width = Math.max(width,i * horizontalSpace + child.getMeasuredWidth());
                if (lp.verSpacing != -1 && i >= 1){
                    height = Math.max(height,lp.verSpacing + (i-1)*verticalSpace + child.getMeasuredHeight());
                }else{
                    height = Math.max(height,i * verticalSpace + child.getMeasuredHeight());
                }
            }
        }

        setMeasuredDimension(MeasureSpec.EXACTLY == widthMode ? widthSize : width,
                MeasureSpec.EXACTLY == heightMode ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            for (int i = 0;i < getChildCount();i++){
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    CustomLayoutParams lp = (CustomLayoutParams) child.getLayoutParams();
                    int leftSpace = horizontalSpace * i;
                    int topSpace = verticalSpace * i;
                    if (lp.verSpacing != -1 && i >= 1){
                        topSpace = lp.verSpacing + (i-1)*verticalSpace;
                    }

                    child.layout(leftSpace,topSpace,leftSpace + child.getMeasuredWidth(),topSpace + child.getMeasuredHeight());
                }
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new CustomLayoutParams(p.width,p.height);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof CustomLayoutParams;
    }

    public static class CustomLayoutParams extends ViewGroup.LayoutParams{
        private int verSpacing;

        public CustomLayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);

            TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CardLayout_LayoutParams);
             try {
                 verSpacing = a.getDimensionPixelSize(R.styleable.CardLayout_vertical_space,-1);
            } finally {
                a.recycle();
            }
        }

        public CustomLayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
