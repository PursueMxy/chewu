package com.weimu.chewu.backend.http.core;


import android.util.Log;


import com.weimu.chewu.AppData;
import com.weimu.chewu.backend.http.core.cookie.CookieCenter;
import com.weimu.chewu.backend.http.core.cookie.PersistentCookieStore;

import java.io.File;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by 艹羊 on 2017/4/19.
 */
public class OKHttpsUtils {
    private static final int DEFAULT_TIMEOUT = 10000;
    private static final int CACHE_SIZE = 50 * 1024 * 1024;

    //glide所需要的okhttpClient  不需要长链接
    public synchronized static OkHttpClient getGlideOKHttpClient() {
        return getClient(false);
    }

    //普通的OKHttp客户端 自带长连接
    public synchronized static OkHttpClient getSessionOKHttpClient() {
        return getClient(true);
    }


    /**
     * 备注：由于4.4以及以下的手机不支持TLS协议，所以原生的OKHTTP并不能请求https协议的接口
     */
    private synchronized static OkHttpClient getClient(boolean needSession) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        try {
            // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
            final X509TrustManager trustAllCert =
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    };
            final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
            builder.sslSocketFactory(sslSocketFactory, trustAllCert)
                    .cache(new Cache(new File(AppData.getContext().getCacheDir(), "okHttp"), CACHE_SIZE))
                    .addInterceptor(getLog())
                    .addInterceptor(new TokenInterceptor())
                    .retryOnConnectionFailure(true)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            if (needSession) {
                builder.cookieJar(getCookiesManager());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    }


    /**
     * 获取日志
     *
     * @return
     */
    private static HttpLoggingInterceptor getLog() {
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("weimu", message);
            }
        });
        //日志显示级别
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    /**
     * 保持session
     */
    private static CookieJar getCookiesManager() {
        final PersistentCookieStore cookieStore = CookieCenter.getInstance().getCookieStore();
        return new CookieJar() {


            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("HTTP保存 cookies数=" + cookies.size() + "\n");
                    for (Cookie item : cookies) {
                        cookieStore.add(url, item);
                        builder.append(item.name()).append("=").append(item.value()).append("\n");
                    }
                    //Logger.d(builder.toString());
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                StringBuilder builder = new StringBuilder();
                builder.append("HTTP读取 cookies数=" + cookies.size() + "\n");
                for (Cookie item : cookies) {
                    builder.append(item.name()).append("=").append(item.value()).append("\n");
                }
                //Logger.d(builder.toString());
                return cookies;
            }
        };
    }

}
