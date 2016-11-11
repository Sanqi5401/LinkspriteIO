package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/11/4.
 */
public class ResetPasswordRequest {


    /**
     * oldPassword : old password
     * newPassword : new password
     * confirmPassword : confirm password
     */

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public ResetPasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
