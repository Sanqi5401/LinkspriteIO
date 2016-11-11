package com.linksprite.io.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/8/30.
 */
public class LedUpdateRequest {

    /**
     * mode : 0A
     * time : time
     * count : count
     * color : color
     * number : number
     */

    private String mode;
    @Expose
    private String time;
    @Expose
    private String count;
    @Expose
    private String color;
    @Expose
    private String num;

    public LedUpdateRequest() {
    }

    public LedUpdateRequest(String mode) {
        this.mode = mode;
    }

    public LedUpdateRequest(String mode, String color) {
        this.mode = mode;
        this.color = color;
    }

    public LedUpdateRequest(String mode, String time, String count, String color) {
        this.mode = mode;
        this.time = time;
        this.count = count;
        this.color = color;
    }

    public LedUpdateRequest(String mode, String time, String color) {
        this.mode = mode;
        this.time = time;
        this.color = color;
    }

    public LedUpdateRequest(String mode, String time, String count, String color, String num) {
        this.mode = mode;
        this.time = time;
        this.count = count;
        this.color = color;
        this.num = num;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String number) {
        this.num = number;
    }
}
