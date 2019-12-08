package com.weimu.chewu.backend.http.core;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

/**
 * Author:你需要一台永动机
 * Date:2018/3/12 17:19
 * Description:
 */

public class ErrorHandler {

    private Throwable e;

    public ErrorHandler(Throwable e) {
        this.e = e;
    }

    public Throwable getThrowable() {
        return e;
    }

    //todo 需要国际化
    public String getErrorMessage() {
        if (e instanceof IllegalArgumentException) {
            return e.getMessage();
        } else if (e instanceof ConnectException) {
            return "网络连接异常";
        } else if (e instanceof SSLHandshakeException) {
            return "证书有问题,请检查证书";
        } else if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            return "网络超时";
        }
        return "网络错误";
    }


}
