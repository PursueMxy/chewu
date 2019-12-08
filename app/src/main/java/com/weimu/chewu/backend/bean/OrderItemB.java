package com.weimu.chewu.backend.bean;

import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.weimu.chewu.backend.bean.base.BaseB;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/4/29 16:43
 * Description:
 */
public class OrderItemB extends BaseB {
    /**
     * id : 2
     * name : Miss Bernadette Bartoletti
     * phone : 461.790.9590 x195
     * car_no : 4358041
     * user_id : 1
     * order_id : null
     * station_id : null
     * address : 五缘湾乐都汇购物广场
     * is_illegal : 1
     * price : 272
     * driver_price : 285
     * source : backstage
     * project : VIP客户
     * status : pending
     * release_at : 2018-04-20 10:52:11
     * finished_at : null
     * customer_id : 1
     * is_return : 0
     * checkout_result : pending
     * failed_reason_img : null
     * electron_sign_img : null
     * car_images : null
     * backup : null
     * province : 福建省
     * city : 厦门市
     * county : 思明区
     * created_at : 2018-04-20 10:52:11
     * updated_at : 2018-04-20 10:52:11
     * customer : {"id":1,"phone":"13811112222","avatar":"fuck.jpg","name":"反反复复付","passport":"350321199321325646","passport_images":["xxx.jpg","xxxx.jpg"],"driver_license_images":["xxx.jpg","xxxx.jpg"],"driving_license_images":["xxx.jpg","xxxx.jpg"],"status":"approve","province":"福建省","city":"厦门市","county":"思明区","balance":"9969","frozen_balance":"0","created_at":"2018-04-20 10:59:56","updated_at":"2018-04-20 17:24:26"}
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
    private List<String> failed_reason_img;
    private String electron_sign_img;
    private List<String> car_images;
    private List<OrderDetailB.BackupBean> backup;
    private String province;
    private String city;
    private String county;
    private String created_at;
    private String updated_at;
    private String location;
    private String payment_status;//付款状态
    private String payment_url;//付款的URL

    private CustomerBean customer;
    private StationListResultB.StationB station;
    private String accepted_at;//接单时间

    public String getAccepted_at() {
        return accepted_at;
    }

    public void setAccepted_at(String accepted_at) {
        this.accepted_at = accepted_at;
    }

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


    public StationListResultB.StationB getStation() {
        return station;
    }

    public void setStation(StationListResultB.StationB station) {
        this.station = station;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public String getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(String finished_at) {
        this.finished_at = finished_at;
    }

    public void setFailed_reason_img(List<String> failed_reason_img) {
        this.failed_reason_img = failed_reason_img;
    }

    public String getElectron_sign_img() {
        return electron_sign_img;
    }

    public void setElectron_sign_img(String electron_sign_img) {
        this.electron_sign_img = electron_sign_img;
    }


    public List<OrderDetailB.BackupBean> getBackup() {
        return backup;
    }

    public void setBackup(List<OrderDetailB.BackupBean> backup) {
        this.backup = backup;
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

    public List<String> getFailed_reason_img() {
        return failed_reason_img;
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

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public List<String> getCar_images() {
        return car_images;
    }

    public void setCar_images(List<String> car_images) {
        this.car_images = car_images;
    }

    public static class CustomerBean extends BaseB {
        /**
         * id : 1
         * phone : 13811112222
         * avatar : fuck.jpg
         * name : 反反复复付
         * passport : 350321199321325646
         * passport_images : ["xxx.jpg","xxxx.jpg"]
         * driver_license_images : ["xxx.jpg","xxxx.jpg"]
         * driving_license_images : ["xxx.jpg","xxxx.jpg"]
         * status : approve
         * province : 福建省
         * city : 厦门市
         * county : 思明区
         * balance : 9969
         * frozen_balance : 0
         * created_at : 2018-04-20 10:59:56
         * updated_at : 2018-04-20 17:24:26
         */

        private int id;
        private String phone;
        private String avatar;
        private String name;
        private String passport;
        private String status;
        private String province;
        private String city;
        private String county;
        private String balance;
        private String frozen_balance;
        private String created_at;
        private String updated_at;
        private List<String> passport_images;
        private List<String> driver_license_images;
        private List<String> driving_license_images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassport() {
            return passport;
        }

        public void setPassport(String passport) {
            this.passport = passport;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getFrozen_balance() {
            return frozen_balance;
        }

        public void setFrozen_balance(String frozen_balance) {
            this.frozen_balance = frozen_balance;
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

        public List<String> getPassport_images() {
            return passport_images;
        }

        public void setPassport_images(List<String> passport_images) {
            this.passport_images = passport_images;
        }

        public List<String> getDriver_license_images() {
            return driver_license_images;
        }

        public void setDriver_license_images(List<String> driver_license_images) {
            this.driver_license_images = driver_license_images;
        }

        public List<String> getDriving_license_images() {
            return driving_license_images;
        }

        public void setDriving_license_images(List<String> driving_license_images) {
            this.driving_license_images = driving_license_images;
        }
    }


}
