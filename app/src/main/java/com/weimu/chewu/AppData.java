package com.weimu.chewu;

import android.content.Context;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.weimu.chewu.backend.bean.PositionB;
import com.weimu.chewu.origin.center.UmengCenter;
import com.weimu.chewu.utils.SoundUtil;
import com.weimu.universalib.OriginAppData;
import com.weimu.universalib.utils.FileUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Author:你需要一台永动机
 * Date:2018/3/8 11:00
 * Description:
 */

public class AppData extends OriginAppData {

    public static String token;
    public static PositionB currentPosition = new PositionB("我的位置");
    public static SoundUtil soundUtil;


    @Override
    public void onCreate() {
        super.onCreate();
        //创建文件夹
        FileUtils.makeDirs(Const.FILE_APP_HOME);
        //推送
        initJpush();
        //声音
        soundUtil = SoundUtil.getInstance();
        //友盟分析
        UmengCenter.INSTANCE.init(this, "chewu");


    }



    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    private void initJpush() {
        JPushInterface.init(this);
    }


    public static PositionB getCurrentPosition() {
        return currentPosition;
    }

}
