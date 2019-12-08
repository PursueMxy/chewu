package com.weimu.chewu.backend.bean.wallet;

import java.io.Serializable;

/**
 * Created by huangjinfu on 18/4/30.
 */

public class BankInfo implements Serializable{
//            "id": 1,
//                    "customer_id": 1,
//                    "cardholder": "张三三",
//                    "card_no": "1354185413641658",
//                    "bank": "建设银行",
//                    "created_at": null,
//                    "updated_at": null

    private Integer id;
    private Integer customer_id;
    private String cardholder;
    private String card_no;
    private String bank;
    private String created_at;
    private String updated_at;

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

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
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
