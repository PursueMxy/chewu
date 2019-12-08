package com.weimu.chewu;

import android.os.Environment;

/**
 * Author:你需要一台永动机
 * Date:2018/4/1 23:28
 * Description:
 */
public class Const {

    public static final int REQUEST_SUCCESS = 200;
    public static final int REQUEST_NEED_LOGIN = 403;
    public static final String FilePreoviderAuthorities = "com.weimu.chewu.fileprovider";//类名+fileprovider


    //item
    public static final int INTENT_TO_ADD_REMARK = 0x001;//添加备注
    public static final int INTENT_TO_STATION = 0x002;//进入监测站
    public static final int INTENT_TO_UPDATE_CHECK = 0x003;//上传检测
    public static final int INTENT_TO_SIGNATURE = 0x004;//上传签名
    public static final int INTENT_TO_ADD_ARRIVAL_PICTURE = 0x005;//上传到达照片
    public static final int INTENT_TO_CHOOSE_CITY = 0x006;//选择城市

    //file
    public static final String FILE_APP_HOME = Environment.getExternalStorageDirectory().toString() + "/" + BuildConfig.APPNAME + "/";

}
