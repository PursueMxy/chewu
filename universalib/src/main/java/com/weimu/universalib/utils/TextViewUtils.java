package com.weimu.universalib.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.TextView;

import com.weimu.universalib.OriginAppData;


public class TextViewUtils {
    //获取编辑框的内容
    public static String getContent(TextView textView) {
        if (textView != null) {
            return textView.getText().toString().trim();
        }
        return null;
    }


    /**
     * 顶部画drawable
     */
    public static void setTopDrawable(Context context, TextView mTextView, int recourse) {
        Drawable drawable = context.getResources().getDrawable(recourse);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mTextView.setCompoundDrawables(null, drawable, null, null);//画在左边边
    }

    /**
     * 左边画drawable
     */
    public static void setLeftDrawable(Context context, TextView mTextView, int recourse) {
        Drawable drawable = context.getResources().getDrawable(recourse);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mTextView.setCompoundDrawables(drawable, null, null, null);//画在左边边
    }


    /**
     * 右边边画drawable
     */
    public static void setRightDrawable(Context context, TextView mTextView, int recourse) {
        if (recourse == -1) return;
        Drawable drawable = context.getResources().getDrawable(recourse);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mTextView.setCompoundDrawables(null, null, drawable, null);//画在左边边
    }


    public static void setLeftRightDrawable(Context context, TextView textView, @DrawableRes int leftRes, @DrawableRes int rightRes) {
        if (leftRes == -1 || rightRes == -1) return;
        Drawable leftDrawable = context.getResources().getDrawable(leftRes);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight()); //设置
        Drawable rightDrawable = context.getResources().getDrawable(rightRes);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight()); //设置

        textView.setCompoundDrawables(leftDrawable, null, rightDrawable, null);//画在左边边
    }


    /**
     * 清除原有的drawable
     *
     * @param mTextView
     */
    public static void clearDrawable(TextView mTextView) {
        mTextView.setCompoundDrawables(null, null, null, null);//画在左边边
    }

    //显示阴影
    public static void showAllShadow(TextView... texteViews) {
        for (TextView mTextView : texteViews) {
            mTextView.setShadowLayer(20, 0, 10, android.R.color.black);
        }
    }

    /**
     * 改变颜色
     */
    public static void changeColor(int color, TextView... textViews) {
        for (TextView mTextView : textViews) {
            mTextView.setTextColor(color);
        }
    }

    /**
     * 修改字体样式
     */
    public static void changeFonts(TextView... textViews) {
        Typeface tf = Typeface.createFromAsset(OriginAppData.getContext().getAssets(), "font/HelveticaNeue-Light.ttf");
        for (TextView tv : textViews) {
            tv.setTypeface(tf);
        }
    }

    public static void setDeleteLine(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }


}
