package com.weimu.map.navi;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.orhanobut.logger.Logger;
import com.weimu.map.Const;
import com.weimu.universalib.utils.FileUtils;

/**
 * Author:你需要一台永动机
 * Date:2018/4/7 23:45
 * Description:
 */
public class NaviCenter {
    private static final NaviCenter ourInstance = new NaviCenter();

    public static NaviCenter getInstance() {
        return ourInstance;
    }

    private NaviCenter() {

    }

    public void init(Activity activity) {
        FileUtils.makeDirs(Const.APP_HOME + "/" + Const.APP_NAME + "/");
        BaiduNaviManager.getInstance().init(activity, Const.APP_HOME, Const.APP_NAME,
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        String authinfo;
                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        Logger.e(authinfo);
                    }

                    public void initSuccess() {
                        Logger.e("百度导航引擎初始化成功");
                    }

                    public void initStart() {
                        Logger.e("百度导航引擎初始化开始");
                    }

                    public void initFailed() {
                        Logger.e("百度导航引擎初始化失败");
                    }
                }, null /*mTTSCallback*/);
    }
}
