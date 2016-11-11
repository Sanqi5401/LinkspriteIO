package com.linksprite.io.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/8/5.
 */
public class BaseRespone {
    @Expose
    public String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
