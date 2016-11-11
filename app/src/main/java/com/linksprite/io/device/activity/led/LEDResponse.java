package com.linksprite.io.device.activity.led;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/11/11.
 */
public class LEDResponse {


    /**
     * num : 0012
     * time : 100
     * count : 30
     * mode : 4
     * color : 0
     */

    @Expose
    private String num;
    @Expose
    private String time;
    @Expose
    private String count;
    private String mode;
    @Expose
    private String color;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
