package com.weimu.universalib.origin.view.list.decoration;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weimu.universalib.OriginAppData;


/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p>
 * See the license above for details.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };


    private Drawable mDivider;
    private int mSize = 0;
    private int mOrientation;


    private boolean isAddHead = false;
    private boolean isAddFoot = false;


    public DividerItemDecoration(Context context, int orientation, int dividerSize) {
        this(context, orientation, dividerSize, false, false);
    }


    public DividerItemDecoration(Context context, int orientation, int dividerSize, boolean isAddHead, boolean isAddFoot) {
        this.isAddHead = isAddHead;
        this.isAddFoot = isAddFoot;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        mSize = dip2px(dividerSize);
    }

    private static int dip2px(float dpValue) {
        final float scale = OriginAppData.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = isAddHead ? 1 : 0; i < (isAddFoot ? childCount - 1 : childCount); i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mSize);
        } else {
            outRect.set(0, 0, mSize, 0);
        }
    }
}
