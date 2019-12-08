package com.weimu.chewu.origin.view;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

/**
 * Author:你需要一台永动机
 * Date:2018/3/24 18:42
 * Description:
 */

public abstract class BaseEventActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            EventBus.getDefault().register(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            EventBus.getDefault().register(this);
    
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onLanguageFresh(RefreshPageLanguageEvent event) {
//        if (!refreshCurrentPage()) init(null);
//    }

    //刷新当前界面
    protected boolean refreshCurrentPage() {
        return false;
    }
}
