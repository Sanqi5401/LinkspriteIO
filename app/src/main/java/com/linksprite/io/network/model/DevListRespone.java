package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/8/1.
 */
public class DevListRespone {

    /**
     * _id : 56e26d35b37380cc19e2f6df
     * name : Light
     * type : 02
     * deviceid : 0200000002
     * apikey : f300225b-5e33-49a6-9ee2-cb44f59e4805
     * __v : 0
     * params : {"light":"off"}
     * online : true
     * createdAt : 2016-03-11T07:01:09.268Z
     * group : LinkNode
     */


    private String _id;
    private String name;
    private String type;
    private String deviceid;
    private String apikey;
    private int __v;
    private boolean online;
    private String createdAt;
    private String group;

    public DevListRespone() {
    }




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

    @Override
    public String toString() {
        return "Device{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", apikey='" + apikey + '\'' +
                ", __v=" + __v +
                ", online=" + online +
                ", createdAt='" + createdAt + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
