package com.weimu.chewu.backend.bean;

import com.weimu.chewu.backend.bean.base.BaseB;


/**
 * Created by huangjinfu on 18/5/11.
 */

public class WithdrawRecordB extends BaseB{
    private Integer id;
    private Integer customer_id;
    private String type;
    private String balance;
    private String remain_balance;
    private String created_at;
    private String updated_at;
    private String backup;

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRemain_balance() {
        return remain_balance;
    }

    public void setRemain_balance(String remain_balance) {
        this.remain_balance = remain_balance;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}