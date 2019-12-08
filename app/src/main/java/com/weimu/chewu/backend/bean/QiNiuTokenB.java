package com.weimu.chewu.backend.bean;

import com.weimu.chewu.backend.bean.base.BaseB;

/**
 * Created by huangjinfu on 18/4/23.
 */

public class QiNiuTokenB extends BaseB {
    private String base_url;
    private String token;

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
