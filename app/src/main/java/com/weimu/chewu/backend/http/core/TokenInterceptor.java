package com.weimu.chewu.backend.http.core;

import com.orhanobut.logger.Logger;
import com.weimu.chewu.origin.center.SharePreferenceCenter;
import com.weimu.chewu.origin.center.UserCenter;
import com.weimu.chewu.utils.SharedDataTool;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 艹羊 on 2017/5/25.
 */
public class TokenInterceptor implements Interceptor {

    enum CONTENT_TYPE {
        URL_ENCODED("application/x-www-form-urlencoded; charset=UTF-8"),
        JSON("application/application/json; charset=UTF-8");

        CONTENT_TYPE(String type) {
            this.type = type;
        }

        String type;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Headers headers = chain.request().headers();

        Request.Builder builder = chain.request()
                .newBuilder()
                //.addHeader("Content-Type", CONTENT_TYPE.URL_ENCODED.type)
                .addHeader("Accept", CONTENT_TYPE.JSON.type);

        //用户登录的情况下 加token头部
        if (UserCenter.getInstance().isUserLogin())
            builder.addHeader("Authorization", "Bearer " + UserCenter.getInstance().getUserShareP().getToken());


        return chain.proceed(builder.build());
    }
}