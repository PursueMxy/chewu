package com.weimu.chewu.backend.bean.base;

/**
 * author：leo on 2016/6/1 10:00
 * email： leocheung4ever@gmail.com
 * description: base response class
 * what & why is modified:
 */
public class NormalResponseB<T> extends BaseB {


    private int code;
    private String message;
    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


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
