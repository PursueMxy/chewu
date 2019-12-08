package com.weimu.chewu.backend.bean;

import com.weimu.chewu.backend.bean.base.BaseB;

import java.io.Serializable;

/**
 * Author:你需要一台永动机
 * Date:2018/5/2 00:02
 * Description:
 */
public class ContactServiceB extends BaseB implements Serializable{

    /**
     * id : 1
     * title : Mrs.
     * content : Quia nesciunt aut quis magnam omnis. Sit quo aspernatur officiis ut est ipsum. Ullam minima fuga sint incidunt omnis repudiandae.
     * created_at : 2018-04-20 17:32:29
     * updated_at : 2018-04-20 17:32:29
     */

    private int id;
    private String title;
    private String content;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
