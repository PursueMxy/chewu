package com.weimu.universalib.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.weimu.universalib.OriginAppData;
import com.weimu.universalib.R;


/**
 * Spannable工具类，用于设置文字的前景色、背景色、Typeface、粗体、斜体、字号、超链接、删除线、下划线、上下标等
 */
public class SpannableUtils {


    /**
     * 加粗
     */
    public static void setBoldSpan(TextView textView, String title, int start, int end) {
        if (textView == null || TextUtils.isEmpty(title))
            return;
        SpannableString str = new SpannableString(title);
        StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
        str.setSpan(styleSpan_B, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
        textView.setText(str);
    }

    /**
     * 斜体
     */
    public static void setItalicSpan(TextView textView, String title, int start, int end) {
        if (textView == null || TextUtils.isEmpty(title))
            return;
        SpannableString str = new SpannableString(title);
        StyleSpan styleSpan_B = new StyleSpan(Typeface.ITALIC);
        str.setSpan(styleSpan_B, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//斜体
        textView.setText(str);
    }


    /**
     * 染色-Html
     */
    public static void colorKeyByHtml(String title, String keyWord, TextView textView, int color) {
        String str = "我已阅读并同意 <font color=" + ContextCompat.getColor(textView.getContext(), color) + ">《法律声明以及隐私政策》</font>";
        textView.setText(Html.fromHtml(str));
    }

    public static class NoLineSpan extends ClickableSpan {
        Context context;

        public NoLineSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            ds.setUnderlineText(false);
        }
    }

    /**
     * 普通染色
     */
    public static void colorNormal(TextView textView, int index, int end, @ColorRes int color) {
        if (textView == null)
            return;
        String origin = textView.getText().toString().trim();
        SpannableString str = new SpannableString(origin);
        str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(textView.getContext(), color)), index, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//染色
        textView.setText(str);
    }

    /**
     * @param title
     * @param keyWord
     * @param color
     * @return SpannableStringBuilder
     * @description 根据关键词染色，懒得再数了
     * @remark 如果要求性能的话，还是继续数数字吧
     */
    public static SpannableStringBuilder colorByKeyWordWithReturn(String title, String keyWord, @ColorRes int color) {
        SpannableStringBuilder str = new SpannableStringBuilder(title);
        int start = 0;
        start = title.indexOf(keyWord);
        for (; ; ) {
            start = title.indexOf(keyWord, start);
            if (start >= title.length()) {
                break;
            }
            if (start < 0) {
                break;
            }

            str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(OriginAppData.getContext(), color)), start, start + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//染色
            start += keyWord.length();
        }
        return str;
    }

    /**
     * 设置点击Span 1
     */
    public static void setClickSpan(TextView textView, int start, int end, boolean isUnderLine, @ColorRes int color, View.OnClickListener clickListener) {
        SpannableString span = new SpannableString(textView.getText());
        if (color != -1)
            span.setSpan(new ForegroundColorSpan(ContextCompat.getColor(textView.getContext(), color)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//染色
        span.setSpan(new clickSpan(isUnderLine, clickListener), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//点击事件
        textView.setText(span);
        textView.setMovementMethod(LinkMovementMethod.getInstance());//必须设置才能点击
    }

    /**
     * 设置点击Span 2
     */
    public static void setClickSpan(TextView textView, int start, int end, View.OnClickListener clickListener) {
        setClickSpan(textView, start, end, true, -1, clickListener);
    }

    //clickSpan
    private static class clickSpan extends ClickableSpan {
        private boolean isUnderLine = false;
        private View.OnClickListener clickListener;

        public clickSpan(View.OnClickListener clickListener) {
            this.clickListener = clickListener;
        }

        public clickSpan(boolean isUnderLine, View.OnClickListener clickListener) {
            this.isUnderLine = isUnderLine;
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View widget) {
            //click
            if (clickListener != null) clickListener.onClick(widget);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderLine);
        }
    }

}
