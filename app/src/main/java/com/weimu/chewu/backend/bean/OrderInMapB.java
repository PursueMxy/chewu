package com.weimu.chewu.backend.bean;

import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.weimu.chewu.backend.bean.base.BaseB;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/4/23 23:47
 * Description:在地图上的订单
 */
public class OrderInMapB extends BaseB {


    /**
     * id : 1
     * name : Tyler McGlynn
     * phone : +1-917-576-2746
     * car_no : 862923
     * user_id : 1
     * order_id : null
     * station_id : 1
     * address : 五缘湾乐都汇购物广场
     * is_illegal : 1
     * price : 618
     * driver_price : 110
     * source : backstage
     * project : 上线年检
     * status : succeed
     * release_at : 2018-04-20 10:52:11
     * finished_at : 2018-04-20 14:42:40
     * customer_id : 1
     * is_return : 0
     * checkout_result : failed
     * failed_reason_img : ["xxxx.jpg","xxxx.jpg"]
     * electron_sign_img : xxxxx.jpg
     * car_images : ["xxxxx.jpg","xxxx.jpg"]
     * backup : [{"created_at":"2018-04-20 14:43:39","content":"fuckyou"},{"created_at":"2018-04-20 14:43:41","content":"fuckyou1"},{"created_at":"2018-04-20 14:43:43","content":"fuckyou12"}]
     * province : 福建省
     * city : 厦门市
     * county : 思明区
     * created_at : 2018-04-20 10:52:11
     * updated_at : 2018-04-20 14:43:43
     */

    private int id;
    private String name;
    private String phone;
    private String car_no;
    private int user_id;
    private Object order_id;
    private int station_id;
    private String address;
    private int is_illegal;
    private int price;
    private int driver_price;
    private String source;
    private String project;
    private String status;
    private String release_at;
    private String finished_at;
    private int customer_id;
    private int is_return;
    private String checkout_result;
    private String electron_sign_img;
    private String province;
    private String city;
    private String county;
    private String created_at;
    private String updated_at;
    private List<String> failed_reason_img;
    private List<String> car_images;
    private List<BackupBean> backup;
    private String location;//定位  "118.165123,24.744308"

    //获取坐标
    public LatLng getLatlng() {
        if (TextUtils.isEmpty(location)) {
            return null;
        }
        String[] split = location.split(",");
        double latitude = Double.parseDouble(split[1]);
        double longitude = Double.parseDouble(split[0]);
        return new LatLng(latitude, longitude);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Object getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Object order_id) {
        this.order_id = order_id;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIs_illegal() {
        return is_illegal;
    }

    public void setIs_illegal(int is_illegal) {
        this.is_illegal = is_illegal;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDriver_price() {
        return driver_price;
    }

    public void setDriver_price(int driver_price) {
        this.driver_price = driver_price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelease_at() {
        return release_at;
    }

    public void setRelease_at(String release_at) {
        this.release_at = release_at;
    }

    public String getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(String finished_at) {
        this.finished_at = finished_at;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getIs_return() {
        return is_return;
    }

    public void setIs_return(int is_return) {
        this.is_return = is_return;
    }

    public String getCheckout_result() {
        return checkout_result;
    }

    public void setCheckout_result(String checkout_result) {
        this.checkout_result = checkout_result;
    }

    public String getElectron_sign_img() {
        return electron_sign_img;
    }

    public void setElectron_sign_img(String electron_sign_img) {
        this.electron_sign_img = electron_sign_img;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public List<String> getFailed_reason_img() {
        return failed_reason_img;
    }

    public void setFailed_reason_img(List<String> failed_reason_img) {
        this.failed_reason_img = failed_reason_img;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getCar_images() {
        return car_images;
    }

    public void setCar_images(List<String> car_images) {
        this.car_images = car_images;
    }

    public List<BackupBean> getBackup() {
        return backup;
    }

    public void setBackup(List<BackupBean> backup) {
        this.backup = backup;
    }

    public static class BackupBean {
        /**
         * created_at : 2018-04-20 14:43:39
         * content : fuckyou
         */

        private String created_at;
        private String content;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
