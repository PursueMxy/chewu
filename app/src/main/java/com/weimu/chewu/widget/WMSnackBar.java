package com.weimu.chewu.widget;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weimu.chewu.R;


/**
 * Created by 艹羊 on 2017/2/23.
 */
public class WMSnackBar {

    public static void show(final View view, final CharSequence text) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
            }
        }, 300);
        count();
    }

    public static void showLong(final View view, final CharSequence text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
        count();
    }

    public static void showByDelay(final View view, final CharSequence text, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
            }
        }, delay);
        count();
    }


    public static void showLoneByDelay(final View view, final CharSequence text, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
            }
        }, delay);
        count();
    }


    private void customStyleSnackbar(Snackbar snackbar) {
        LinearLayout snackbarView = (LinearLayout) snackbar.getView();//获取SnackBar布局View实例
        TextView textView = ((TextView) snackbarView.findViewById(R.id.snackbar_text));//获取文本View实例
        Button button = (Button) snackbarView.findViewById(R.id.snackbar_action);//获取按钮View实例

        snackbarView.setBackgroundColor(Color.parseColor("#eceff1"));//更改背景颜色
        textView.setTextColor(Color.parseColor("#448AFF"));//更改文本颜色
    }


    //优化
    private static int messageShowCount = 0;
    private static int gcCount = 5;

    private static void count() {
        messageShowCount++;
        if (messageShowCount >= gcCount) {
            System.gc();
            messageShowCount = 0;
        }
    }
}
