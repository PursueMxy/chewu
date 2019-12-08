package com.weimu.chewu.backend.bean;

import com.weimu.chewu.backend.bean.base.BaseB;

import java.util.Date;

/**
 * Created by huangjinfu on 18/5/13.
 */

public class MessageCenterB extends BaseB{
    private String id;
    private String type;
    private String notifiable_type;
    private String notifiable_id;
    private CenterData data;
    private String created_at;
    private String read_at;

    public String getRead_at() {
        return read_at;
    }

    public void setRead_at(String read_at) {
        this.read_at = read_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotifiable_type() {
        return notifiable_type;
    }

    public void setNotifiable_type(String notifiable_type) {
        this.notifiable_type = notifiable_type;
    }

    public String getNotifiable_id() {
        return notifiable_id;
    }

    public void setNotifiable_id(String notifiable_id) {
        this.notifiable_id = notifiable_id;
    }

    public CenterData getData() {
        return data;
    }

    public void setData(CenterData data) {
        this.data = data;
    }

    public static class CenterData{
        private String content;
        private Extras extras;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Extras getExtras() {
            return extras;
        }

        public void setExtras(Extras extras) {
            this.extras = extras;
        }

        public static class Extras{
            private String type;
            private String data_id;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getData_id() {
                return data_id;
            }

            public void setData_id(String data_id) {
                this.data_id = data_id;
            }
        }
    }
}

//            "id": "ddd248b3-0ab8-44d3-a3ed-675f1d385150",
//                    "type": "App\\Notifications\\Push",
//                    "notifiable_type": "App\\Entities\\Customer",
//                    "notifiable_id": 1,
//                    "data": {
//        "content": "恭喜您, 于 2018-05-12 20:28:44 成功提现 55 元",
//                "extras": {
//            "type": "withdraw",
//                    "data_id": 1
//        }
//}
