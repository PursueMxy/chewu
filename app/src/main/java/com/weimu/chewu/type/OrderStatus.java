package com.weimu.chewu.type;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 16:50
 * Description:
 */
public enum OrderStatus {
    PENDING("pending", "等待客服沟通"),
    RELEASING("releasing", "等待发布"),
    RELEASED("released", "已经发布,等待接单"),
    ACCEPTING("accepting", "已经被接单,等待联系客户"),
    ACCEPTED("accepted", "正式接单"),
    SUCCEED("succeed", "已经完成");


    private String name;
    private String tip;

    OrderStatus(String name, String tip) {
        this.name = name;
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
