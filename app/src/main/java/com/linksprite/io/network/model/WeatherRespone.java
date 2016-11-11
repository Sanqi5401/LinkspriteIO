package com.linksprite.io.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/8/5.
 */
public class WeatherRespone {


    /**
     * temperature : 44
     * humidity : 20
     * pressure : 1233
     */
    @Expose
    private String temperature;
    @Expose
    private String humidity;
    @Expose
    private String pressure;

    public WeatherRespone(String temperature, String humidity, String pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }


}
