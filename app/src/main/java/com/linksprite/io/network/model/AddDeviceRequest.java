package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/11/7.
 */
public class AddDeviceRequest {

    /**
     * name : Lamp
     * group : Indie
     * deviceid : 0280000001
     * apikey : f44eeb0b-8a9e-4454-ad51-89beb93b072e
     * type : 01
     */

    private String name;
    private String group;
    private String deviceid;
    private String apikey;

    public AddDeviceRequest(String name, String group, String deviceid, String apikey) {
        this.name = name;
        this.group = group;
        this.deviceid = deviceid;
        this.apikey = apikey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

}
