package com.weimu.chewu.origin.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.weimu.chewu.AppData;
import com.weimu.chewu.origin.mvp.BaseView;
import com.weimu.chewu.widget.WMProgressBar;
import com.weimu.chewu.widget.WMToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author:你需要一台永动机
 * Date:2018/3/8 11:15
 * Description:
 */

public abstract class BaseFragment extends Fragment implements BaseView {


    private AppCompatActivity mActivity;
    private ViewGroup mContentView;
    private Unbinder mUnbinder;
    private boolean isInit = false;


    @LayoutRes
    protected abstract int getLayoutResID();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViewAttach(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = (ViewGroup) LayoutInflater.from(mActivity).inflate(getLayoutResID(), container, false);
        mUnbinder = ButterKnife.bind(this, mContentView);
        return mContentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        afterViewAttach(savedInstanceState);
        isInit = true;
    }

    protected void beforeViewAttach(Bundle savedInstanceState) {

    }

    protected void afterViewAttach(Bundle savedInstanceState) {
    }


    @Override
    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }


    @Override
    public void showToast(String message) {
        WMToast.show(message);
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public Context getApplicationContext() {
        return AppData.getContext();
    }

    @Override
    public AppCompatActivity getCurrentActivity() {
        return mActivity;
    }

    @Override
    public void backPreviewPage() {

    }

    @Override
    public void showProgressBar() {
        WMProgressBar.showProgressDialog(getActivity());
    }

    @Override
    public void showProgressBar(String message) {
        WMProgressBar.showProgressDialog(getActivity(), message);
    }

    @Override
    public void hideProgressBar() {
        WMProgressBar.hideProgressDialog();
    }


    public ViewGroup getContentView() {
        return mContentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUnbinder.unbind();
        this.mActivity = null;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isInit) return;
        if (hidden) {
            onHide();
        } else {
            onShow();
        }

    }

    //fragment显示
    public void onShow() {

    }

    //fragment隐藏
    public void onHide() {
    }

}
