package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/8/30.
 */
public class BaseUpdateRequest<T>{

    /**
     * action : update
     * deviceid : 01ad0253f2
     * apikey : 123e4567-e89b-12d3-a456-426655440000
     * params : {"powerswitch":"on"}
     */

    private String action;
    private String deviceid;
    private String apikey;
    /**
     * powerswitch : on
     */

    private T params;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

}
