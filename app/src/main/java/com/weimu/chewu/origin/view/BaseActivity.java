package com.weimu.chewu.origin.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;


import com.umeng.analytics.MobclickAgent;
import com.weimu.chewu.AppData;
import com.weimu.chewu.R;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.widget.WMProgressBar;
import com.weimu.chewu.widget.WMToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author:你需要一台永动机
 * Date:2018/3/7 17:24
 * Description:Activity的基类
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private Unbinder mUnbinder;


    @LayoutRes
    protected abstract int getLayoutResID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);


    }

    protected void init(@Nullable Bundle savedInstanceState) {
        //PushAgent.getInstance(this).onAppStart();//统计

        beforeViewAttach(savedInstanceState);

        setContentView(getLayoutResID());

        mUnbinder = ButterKnife.bind(this);


        afterViewAttach(savedInstanceState);
    }

    protected void beforeViewAttach(Bundle savedInstanceState) {

    }

    protected void afterViewAttach(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//请求为竖直屏幕
    }


    public ViewGroup getContentView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    public void finishByDefault() {
        super.finish();
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Context getApplicationContext() {
        return AppData.getContext();
    }

    @Override
    public AppCompatActivity getCurrentActivity() {
        return this;
    }

    @Override
    public void showToast(String message) {
        WMToast.show(message);
    }

    @Override
    public void backPreviewPage() {
        onBackPressed();
    }

    @Override
    public void showProgressBar() {
        WMProgressBar.showProgressDialog(this);
    }

    @Override
    public void showProgressBar(String message) {
        WMProgressBar.showProgressDialog(this, message);
    }

    @Override
    public void hideProgressBar() {
        WMProgressBar.hideProgressDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
