package com.linksprite.io.network.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/6/2.
 */
public class LoginRequest {

    /**
     * email : qi.yao@linksprite.com
     * password : yaoq04
     */

    private String email;
    private String password;
    @Expose
    private String response;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest(String email, String password, String response) {
        this.email = email;
        this.password = password;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
