package com.weimu.chewu.backend.bean;

import com.weimu.chewu.backend.bean.base.BaseB;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 16:56
 * Description:
 */
public class OrderListResultB extends BaseB {


    /**
     * current_page : 1
     * data : [{"id":2,"name":"Miss Bernadette Bartoletti","phone":"461.790.9590 x195","car_no":"4358041","user_id":1,"order_id":null,"station_id":null,"address":"五缘湾乐都汇购物广场","is_illegal":1,"price":272,"driver_price":285,"source":"backstage","project":"VIP客户","status":"pending","release_at":"2018-04-20 10:52:11","finished_at":null,"customer_id":1,"is_return":0,"checkout_result":"pending","failed_reason_img":null,"electron_sign_img":null,"car_images":null,"backup":null,"province":"福建省","city":"厦门市","county":"思明区","created_at":"2018-04-20 10:52:11","updated_at":"2018-04-20 10:52:11","customer":{"id":1,"phone":"13811112222","avatar":"fuck.jpg","name":"反反复复付","passport":"350321199321325646","passport_images":["xxx.jpg","xxxx.jpg"],"driver_license_images":["xxx.jpg","xxxx.jpg"],"driving_license_images":["xxx.jpg","xxxx.jpg"],"status":"approve","province":"福建省","city":"厦门市","county":"思明区","balance":"9969","frozen_balance":"0","created_at":"2018-04-20 10:59:56","updated_at":"2018-04-20 17:24:26"}}]
     * first_page_url : http://chewuapp.test/api/customer/orders?status=pending&page=1
     * from : 1
     * last_page : 1
     * last_page_url : http://chewuapp.test/api/customer/orders?status=pending&page=1
     * next_page_url : null
     * path : http://chewuapp.test/api/customer/orders
     * per_page : 10
     * prev_page_url : null
     * to : 1
     * total : 1
     */

    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private Object next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<OrderItemB> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<OrderItemB> getData() {
        return data;
    }

    public void setData(List<OrderItemB> data) {
        this.data = data;
    }


}
