package com.weimu.universalib.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Author:你需要一台永动机
 * Date:2018/3/15 11:03
 * Description:
 */

public class AnimUtils {

    //渐变
    public static void alphaAnim(View view, long duration) {
        final ValueAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0.1f, 1f);
        anim.setDuration(duration);
        anim.setRepeatCount(0);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public static void scaleAnim(View view) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f);
        scaleXAnimator.setDuration(500);
        scaleXAnimator.setRepeatCount(1);
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleXAnimator.start();
    }

    public static void rotationAnim(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
    }

    public static void translateAnim(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f, 100f);
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
    }

    public static void animSet(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f);
        scaleXAnimator.setDuration(500);
        scaleXAnimator.setRepeatCount(1);
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleXAnimator.start();

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f);
        scaleYAnimator.setDuration(500);
        scaleYAnimator.setRepeatCount(1);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);

        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.start();
    }
}
