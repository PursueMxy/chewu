package com.weimu.chewu.module.welcome;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.weimu.chewu.R;
import com.weimu.chewu.backend.bean.UserB;
import com.weimu.chewu.module.loginx.LoginActivity;
import com.weimu.chewu.module.main.MainActivity;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.origin.view.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.welcome_iv_logo)
    ImageView iv_logo;
    @BindView(R.id.iv_icon_1)
    ImageView iv_icon1;
    @BindView(R.id.iv_icon_2)
    ImageView iv_icon2;
    @BindView(R.id.iv_icon_4)
    ImageView iv_icon4;
    @BindView(R.id.iv_icon_5)
    ImageView iv_icon5;
    @BindView(R.id.rl_icon_3)
    RelativeLayout rl_icon3;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_notify)
    TextView tv_notify;
    @BindView(R.id.btn_next)
    Button btn_next;
    private int position;

    @Override
    protected int getLayoutResID() {
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_welcome;
    }


    @Override
    protected void beforeViewAttach(Bundle savedInstanceState) {
        super.beforeViewAttach(savedInstanceState);
    }

    @Override
    protected void afterViewAttach(Bundle savedInstanceState) {
        super.afterViewAttach(savedInstanceState);
        addAlphaAnimal();
        checkPermission();
//        runEnterAnimationAlpha(iv_icon5, 2);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkPermission();
//            }
//        }, 10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shanShouAnimal();
            }
        },3000);

    }


    private void checkPermission() {
        if (!getCurrentActivity().isDestroyed()){
            new RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                doNext();
                            } else
                                finish();

                        }
                    });
        }


    }

    @OnClick(R.id.btn_next)
    public void onClick() {
        doNext();
    }

    private void addAlphaAnimal() {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (!getCurrentActivity().isDestroyed()){
            rl_icon3.startAnimation(animation);
            iv_icon4.startAnimation(animation);
            iv_icon5.startAnimation(animation);
            tv_name.startAnimation(animation);
            tv_notify.startAnimation(animation);
        }

    }

    private void shanShouAnimal() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        if (!getCurrentActivity().isDestroyed()){
            iv_icon1.startAnimation(alphaAnimation);
            iv_icon2.startAnimation(alphaAnimation);
        }

    }

    /**
     * 动画效果
     */
    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    private void runEnterAnimationAlpha(View view, int position) {
        if (animationsLocked)
            return;
        if (position > lastAnimatedPosition) {
            view.setTranslationY(-300f);
            view.setAlpha(0f);
            view.animate()
                    .alpha(1.f)
                    .translationY(0)
                    .setStartDelay(delayEnterAnimation ? 1000 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(2000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            animationsLocked = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    }).start();
        }
    }

    private void doNext() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        UserB userInfo = UserCenter.getInstance().getUserShareP();
        if (userInfo == null) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
//            }
//        }, 1000);

    }

    @Override
    public void onBackPressed() {

    }


}
