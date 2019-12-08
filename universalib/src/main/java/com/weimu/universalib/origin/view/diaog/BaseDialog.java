package com.weimu.universalib.origin.view.diaog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.weimu.universalib.origin.view.list.BaseRecyclerViewHolder;

/**
 * Author:你需要一台永动机
 * Date:2018/3/19 10:20
 * Description:
 */

public abstract class BaseDialog extends DialogFragment {

    protected abstract String getTagName();

    protected OnDialogButtonListener onDialogButtonListener;
    protected OnDialogListener onDialogListener;

    //默认自适应 无法控制宽度
    protected int getViewWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    //获取视图布局
    protected abstract int getLayoutRes();

    //获取视图的ViewHolder
    protected abstract BaseRecyclerViewHolder getViewHolder(View itemVie);

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.width = getViewWidth();
            windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //windowParams.dimAmount = 0.0f;//设置Dialog外其他区域的alpha值
            window.setAttributes(windowParams);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        View contentView = LayoutInflater.from(getContext()).inflate(getLayoutRes(), null);
        getViewHolder(contentView);
        builder.setView(contentView);
        return builder.show();
    }


    //使用上下文显示
    public BaseDialog show(Context context) {
        if (context instanceof AppCompatActivity) {
            show((AppCompatActivity) context);
        } else {
            throw new IllegalArgumentException("invalid context");
        }
        return this;
    }


    //fragment
    public BaseDialog show(Fragment fragment) {
        return show(fragment, true);
    }

    public BaseDialog show(Fragment fragment, boolean cancelable) {
        showPro(fragment.getChildFragmentManager(), getTagName());
        setCancelable(cancelable);
        return this;
    }

    //activity
    public BaseDialog show(AppCompatActivity activity) {
        return show(activity, true);
    }


    public BaseDialog show(AppCompatActivity activity, boolean cancelable) {
        showPro(activity.getSupportFragmentManager(), getTagName());
        setCancelable(cancelable);
        return this;
    }


    private void showPro(FragmentManager manager, String tag) {
        try {
            show(manager, tag);
        } catch (IllegalStateException e) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }


    public interface OnDialogButtonListener {
        void onPositive();

        void onNegative();
    }

    public BaseDialog setOnDialogButtonListener(OnDialogButtonListener onDialogButtonListener) {
        this.onDialogButtonListener = onDialogButtonListener;
        return this;
    }

    //执行postive的操作
    protected void actionPositiveClick() {
        if (onDialogButtonListener != null)
            onDialogButtonListener.onPositive();
        dismiss();
    }

    //执行negative的操作
    protected void actionNegativeClick() {
        if (onDialogButtonListener != null)
            onDialogButtonListener.onNegative();
        dismiss();
    }


    public interface OnDialogListener {
        void onCancel();

        void onDismiss();
    }

    public void setOnDialogListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onDialogListener != null) {
            onDialogListener.onCancel();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDialogListener != null) {
            onDialogListener.onDismiss();
        }
    }

}
