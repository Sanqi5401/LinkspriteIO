package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/8/30.
 */
public class UpdateRespone extends BaseRespone {


    /**
     * deviceid : 01ad0253f2
     * apikey : 123e4567-e89b-12d3-a456-426655440000
     */

    private String deviceid;
    private String apikey;

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

    @Override
    public String toString() {
        return "UpdateRespone{" +
                "deviceid='" + deviceid + '\'' +
                ", apikey='" + apikey + '\'' +
                '}';
    }
}
