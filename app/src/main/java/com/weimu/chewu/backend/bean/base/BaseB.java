package com.weimu.chewu.backend.bean.base;

import com.google.gson.Gson;

import java.io.Serializable;

public class BaseB implements Serializable {


    public String toJson() {
        return new Gson().toJson(this);
    }


}
