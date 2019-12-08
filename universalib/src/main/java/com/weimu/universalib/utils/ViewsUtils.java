package com.weimu.universalib.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * 视图工具
 */
public class ViewsUtils {

    /**
     * 设置高度
     */
    public static void setHeight(View v, int height) {
        setViewLayoutParams(v, -1, height);
    }

    /**
     * 设置宽度
     */
    public static void setWidth(View v, int width) {
        setViewLayoutParams(v, width, -1);
    }

    /**
     * 设置宽度和宽度
     */
    private static void setViewLayoutParams(View v, int width, int height) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        if (width >= 0) {
            p.width = width;
        }
        if (height >= 0) {
            p.height = height;
        }
        v.setLayoutParams(p);
    }

    /**
     * 设置外边界
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    //设置View的激活状态
    public static void setViewActivated(boolean isActivate, View... views) {
        for (View view : views) {
            view.setActivated(isActivate);
        }
    }

    //隐藏所有视图
    public static void hideAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    //隐藏所有视图
    public static void inVisibilityAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    //显示所有视图  one by one
    public static void showAllViewWithDelay(View... views) {
        showAllViewOneByOne((long) 300, views);
    }


    //显示所有视图    one by one
    public static void showAllViewOneByOne(Long timeMillis, View... views) {
        if (views.length < 1)
            return;
        views[0].setVisibility(View.VISIBLE);
        for (int i = 1; i < views.length; i++) {
            final View view = views[i];
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                }
            }, timeMillis * i);
        }
    }


    public static void fadeInAllViews(View... views) {
        for (View view : views) {
            AnimUtils.alphaAnim(view, 500);
        }
    }


    //显示所有视图
    public static void showAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    //请求获取焦点
    public static void requestFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    //请求去除焦点
    public static void clearFocus(View view) {
        view.clearFocus();
    }

    /**
     * 请求去除焦点
     */
    public static void clearFocus(View... views) {
        for (View view : views) {
            view.clearFocus();
        }
    }

    public static void setFocusEnable(View... views) {
        for (View view : views) {
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
            view.clearFocus();
        }
    }

    public static void setAlpha(View view, float alpha) {
        view.setAlpha(alpha);
    }


    public static void changeFonts(Context context, ViewGroup root) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/HelveticaNeue-Light.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFonts(context, (ViewGroup) v);
            }
        }

    }

    private static long lastClickTime;

    /**
     * 避免按钮被重复点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 600) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


}
