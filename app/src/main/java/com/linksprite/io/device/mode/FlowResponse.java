package com.linksprite.io.device.mode;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/12/19.
 */

public class FlowResponse extends CustomResponse {

    /**
     * water : 0
     * Soil_Mois : Dry
     * humidity : 50
     * updateAt : 2016-12-10 12:06:37
     * temperature : 16
     * light(%) : 0
     */
    @Expose
    public String water;
    @Expose
    public String Soil_Mois;
    @Expose
    public String humidity;
    @Expose
    public String updateAt;
    @Expose
    public String temperature;
    @Expose
    public String light;

    @Override
    public String toString() {
        return "FlowResponse{" +
                "water='" + water + '\'' +
                ", Soil_Mois='" + Soil_Mois + '\'' +
                ", humidity='" + humidity + '\'' +
                ", updateAt='" + updateAt + '\'' +
                ", temperature='" + temperature + '\'' +
                ", light='" + light + '\'' +
                '}';
    }
}
