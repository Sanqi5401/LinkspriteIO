package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/8/5.
 */
public class DataType {

    private String type;
    private String deviceID;


    public DataType(String type, String deviceID) {
        this.type = type;
        this.deviceID = deviceID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
