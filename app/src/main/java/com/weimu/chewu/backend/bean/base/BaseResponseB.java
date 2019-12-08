package com.weimu.chewu.backend.bean.base;

/**
 * Created by 艹羊 on 2017/5/25.
 */

public class BaseResponseB extends BaseB {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
