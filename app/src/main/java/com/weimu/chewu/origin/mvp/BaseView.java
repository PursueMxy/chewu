package com.weimu.chewu.origin.mvp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Author:你需要一台永动机
 * Date:2018/3/8 10:28
 * Description:
 */

public interface BaseView {

    void showToast(String message);

    Context getApplicationContext();

    Context getContext();

    AppCompatActivity getCurrentActivity();

    void backPreviewPage();

    void showProgressBar();

    void showProgressBar(String message);

    void hideProgressBar();

}
