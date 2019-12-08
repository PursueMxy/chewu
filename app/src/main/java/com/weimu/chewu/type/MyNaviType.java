package com.weimu.chewu.type;

import java.io.Serializable;

/**
 * Author:你需要一台永动机
 * Date:2018/5/5 17:14
 * Description:
 */
public enum MyNaviType implements Serializable {
    DRIVE("驾车"),
    BUS("公交"),
    RIDE("骑行"),
    WALK("步行");

    public String name;//名称

    MyNaviType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
