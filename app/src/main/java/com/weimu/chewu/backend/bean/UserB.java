package com.weimu.chewu.backend.bean;

import com.weimu.chewu.backend.bean.base.BaseB;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/4/1 23:09
 * Description:
 */
public class UserB extends BaseB {


    /**
     * token : LrcLw8dMwnuBRkUFWSdfNwkjpC1vh0Ar
     * customer : {"id":1,"phone":"13215089276","avatar":null,"name":"test name","passport":"350321199321325646","passport_images":["xxx.jpg","xxxx.jpg"],"driver_license_images":["xxx.jpg","xxxx.jpg"],"driving_license_images":["xxx.jpg","xxxx.jpg"],"status":"approve","province":"福建省","city":"厦门市","county":"思明区","balance":"0","frozen_balance":"0","created_at":"2018-04-20 10:59:56","updated_at":"2018-04-20 10:59:56"}
     */

    private String token;
    private CustomerBean customer;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public static class CustomerBean {
        /**
         * id : 1
         * phone : 13215089276
         * avatar : null
         * name : test name
         * passport : 350321199321325646
         * passport_images : ["xxx.jpg","xxxx.jpg"]
         * driver_license_images : ["xxx.jpg","xxxx.jpg"]
         * driving_license_images : ["xxx.jpg","xxxx.jpg"]
         * status : approve
         * province : 福建省
         * city : 厦门市
         * county : 思明区
         * balance : 0
         * frozen_balance : 0
         * created_at : 2018-04-20 10:59:56
         * updated_at : 2018-04-20 10:59:56
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
