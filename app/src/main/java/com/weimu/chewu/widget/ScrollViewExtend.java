package com.weimu.chewu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 能够兼容ViewPager的ScrollView
 *
 * @Description: 解决了ViewPager在ScrollView中的滑动反弹问题
 */
public class ScrollViewExtend extends ScrollView {


    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;


    private int downX;
    private int downY;
    private int mTouchSlop;


    public ScrollViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                //判断recyclerView的
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
                int moveY = (int) ev.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        //Logger.e("scrollX " + scrollX + " scrollY " + scrollY + " clampedX " + clampedX + " clampedY " + clampedY);

        if (onScrollToBottom != null) {
            onScrollToBottom.onScroll(scrollX, scrollY, clampedX, clampedY);
        }
        if (scrollY != 0 && null != onScrollToBottom) {
            onScrollToBottom.onScrollToBottom(clampedY);
        }
    }

    public void setCustomOnScrollLintener(OnScrollListener listener) {
        onScrollToBottom = listener;
    }


    private OnScrollListener onScrollToBottom;

    public interface OnScrollListener {
        void onScroll(int scrollX, int scrollY, boolean clampedX, boolean clampedY);

        void onScrollToBottom(boolean isBottom);
    }
}
