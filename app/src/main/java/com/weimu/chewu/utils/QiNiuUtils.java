package com.weimu.chewu.utils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by huangjinfu on 18/4/23.
 * 七牛上传工具类
 */

public class QiNiuUtils {
    private static QiNiuUtils qiNiuUtils;
    private static UploadManager uploadManager;
    //取消上传标记
    private volatile boolean isCancelled = false;

    public static QiNiuUtils getInstance() {
        if (qiNiuUtils == null) {
            qiNiuUtils = new QiNiuUtils();
            return qiNiuUtils;
        }
        return qiNiuUtils;
    }

    //参数配置
    public static QiNiuUtils initConfig() {
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
        return qiNiuUtils;
    }

    //上传操作
    public void upLoad(File file, String token, final OnCompleteListener onCompleteListener) {
        uploadManager.put(file, getStringRandom(6), token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                onCompleteListener.OnCompleteCallBack(key, info, response);
            }

        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                onCompleteListener.OnProgress(key, percent);
            }

        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return isCancelled;
            }
        }));
    }


    // 点击取消按钮，让UpCancellationSignal##isCancelled()方法返回true，以停止上传
    public void cancell() {
        isCancelled = true;
    }

    public interface OnCompleteListener {
        void OnCompleteCallBack(String key, ResponseInfo info, JSONObject response);

        void OnProgress(String key, double percent);
    }

    //生成随机数字和字母,
    public String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        Long timeMillis = System.currentTimeMillis();
//        return timeMillis + val;
        return timeMillis+"";
    }
}
