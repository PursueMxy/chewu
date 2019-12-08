package com.weimu.chewu.widget;

import android.content.Context;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;

public class WMProgressBar {
    private static MaterialDialog materialDialog;

    /**
     * 使用默认
     *
     * @param context 上下文
     */
    public static void showProgressDialog(Context context) {
        showProgressDialog(context, "请稍等", "努力加载中...");
    }

    /**
     * 修改内容
     *
     * @param context 上下文
     * @param content 内容
     */
    public static void showProgressDialog(Context context, String content) {
        showProgressDialog(context, "请稍等", content);
    }

    /**
     * 修改标题 内容
     *
     * @param context 上下文
     * @param title   标题
     * @param content 内容
     */
    public static void showProgressDialog(Context context, String title, String content) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(context)
                    .title(title)
                    .content(content)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
        } else if (!materialDialog.isShowing()) {
            materialDialog.show();
        }
    }

    public static void hideProgressDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                    materialDialog = null;
                }
            }
        }, 0);
    }
}