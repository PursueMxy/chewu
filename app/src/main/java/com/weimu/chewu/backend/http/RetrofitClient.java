package com.weimu.chewu.backend.http;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.weimu.chewu.backend.http.core.OKHttpsUtils;
import com.weimu.chewu.backend.http.jsonConverter.WMGsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.weimu.chewu.BuildConfig.HOST;


public class RetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getDefault() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .client(OKHttpsUtils.getSessionOKHttpClient())
                            .addConverterFactory(WMGsonConverterFactory.create())//Gson进行转化
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava进行一处处理
                            .baseUrl(HOST)
                            .build();
                }
            }
        }
        return retrofit;
    }


}
