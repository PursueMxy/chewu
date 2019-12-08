package com.weimu.chewu.backend.bean;

import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.weimu.chewu.backend.bean.base.BaseB;

import java.util.List;

/**
 * Author:你需要一台永动机
 * Date:2018/5/1 12:51
 * Description:
 */
public class StationListResultB extends BaseB {


    /**
     * current_page : 1
     * data : [{"id":1,"province":"福建省","city":"厦门市","county":"翔安区","address":"世纪大道1号","name":"1号测试站","backup":"这里是备注","created_at":"2018-04-20 10:52:11","updated_at":"2018-04-20 10:52:11"},{"id":2,"province":"福建省","city":"厦门市","county":"集美区","address":"世纪大道1号","name":"1号测试站","backup":"这里是备注","created_at":"2018-04-20 10:52:11","updated_at":"2018-04-20 10:52:11"}]
     * first_page_url : http://chewuapp.test/api/stations?page=1
     * from : 1
     * last_page : 30
     * last_page_url : http://chewuapp.test/api/stations?page=30
     * next_page_url : http://chewuapp.test/api/stations?page=2
     * path : http://chewuapp.test/api/stations
     * per_page : 2
     * prev_page_url : null
     * to : 2
     * total : 60
     */

    private int current_page;
    private String first_page_url;
    private int from;
    private int last_page;
    private String last_page_url;
    private String next_page_url;
    private String path;
    private int per_page;
    private Object prev_page_url;
    private int to;
    private int total;
    private List<StationB> data;

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

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
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

    public List<StationB> getData() {
        return data;
    }

    public void setData(List<StationB> data) {
        this.data = data;
    }

    public static class StationB extends BaseB{
        /**
         * id : 1
         * province : 福建省
         * city : 厦门市
         * county : 翔安区
         * address : 世纪大道1号
         * name : 1号测试站
         * backup : 这里是备注
         * created_at : 2018-04-20 10:52:11
         * updated_at : 2018-04-20 10:52:11
         */

        private int id;
        private String province;
        private String city;
        private String county;
        private String address;
        private String name;
        private String backup;
        private String created_at;
        private String updated_at;
        private String location;


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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBackup() {
            return backup;
        }

        public void setBackup(String backup) {
            this.backup = backup;
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


        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
