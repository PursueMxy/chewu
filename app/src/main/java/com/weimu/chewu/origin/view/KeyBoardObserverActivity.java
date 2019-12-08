package com.weimu.chewu.origin.view;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;


/**
 * 监听软键盘启动关闭的基类
 */
public abstract class KeyBoardObserverActivity extends BaseActivity {

    private boolean isShowInputBoard = false;//是否已经显示软键盘


    @Override
    protected void onResume() {
        super.onResume();
        getContentView().getViewTreeObserver().addOnGlobalLayoutListener(onGlobalFocusChangeListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalFocusChangeListener);
    }

    /**
     * 监听软键盘
     */
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalFocusChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {


        @Override
        public void onGlobalLayout() {
            getContentView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isKeyboardShown(getContentView())) {
                        if (!isShowInputBoard) {
                            isShowInputBoard = true;
                            onKeyBoardShow();
                        }

                    } else {
                        if (isShowInputBoard) {
                            isShowInputBoard = false;
                            onKeyBoardDismiss();
                        }

                    }
                }
            }, 100);
        }
    };


    /**
     * 判断软键盘是否打开
     *
     * @param rootView 最上层布局
     * @return 打开：true，隐藏：false
     */
    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    /**
     * 软键盘显示
     * 备注：可能会被多次粗发
     */
    protected void onKeyBoardShow() {

    }

    /**
     * 软键盘隐藏
     * 备注：可能会被多次粗发
     */
    protected void onKeyBoardDismiss() {

    }
}
