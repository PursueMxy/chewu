package com.weimu.chewu.backend.bean;

import com.weimu.chewu.backend.bean.base.BaseB;

/**
 * Author:你需要一台永动机
 * Date:2018/4/1 23:13
 * Description:
 */
public class AppSharePreB extends BaseB {
    private int orderId = -1;
    private boolean showWhiteListSetting = false;//是否第一次设置 白名单
    private PositionB uploadPosition = new PositionB(0, 0);


    public PositionB getUploadPosition() {
        return uploadPosition;
    }

    public void setUploadPosition(PositionB uploadPosition) {
        if (uploadPosition == null)
            this.uploadPosition = new PositionB(0, 0);
        else
            this.uploadPosition = uploadPosition;
    }

    public boolean isShowWhiteListSetting() {
        return showWhiteListSetting;
    }

    public void setShowWhiteListSetting(boolean showWhiteListSetting) {
        this.showWhiteListSetting = showWhiteListSetting;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
