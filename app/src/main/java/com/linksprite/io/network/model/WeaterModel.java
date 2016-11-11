package com.linksprite.io.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/8/3.
 */
public class WeaterModel extends BaseRespone {

    /**
     * _id : 576913f05418982f5f0ff5d6
     * name : WS-1
     * type : 00
     * deviceid : 0000000058
     * apikey : f300225b-5e33-49a6-9ee2-cb44f59e4805
     * __v : 0
     * params : {"humidity":"0","temperature":"0","Pressure":"0"}
     * online : false
     * createdAt : 2016-06-21T10:16:16.601Z
     * group : Weather station
     */

    private String _id;
    private String name;
    private String type;
    private String deviceid;
    private String apikey;
    private int __v;
    /**
     * humidity : 0
     * temperature : 0
     * Pressure : 0
     */

    private ParamsBean params;
    private boolean online;
    private String createdAt;
    private String group;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public static class ParamsBean {
        @Expose
        private String humidity;
        @Expose
        private String temperature;
        @Expose
        private String pressure;

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String Pressure) {
            this.pressure = Pressure;
        }

        @Override
        public String toString() {
            return "ParamsBean{" +
                    "humidity='" + humidity + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", Pressure='" + pressure + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WeaterModel{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", apikey='" + apikey + '\'' +
                ", __v=" + __v +
                ", params=" + params +
                ", online=" + online +
                ", createdAt='" + createdAt + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
