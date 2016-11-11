package com.linksprite.io.network.model;

/**
 * Created by Administrator on 2016/3/25.
 */


public class LoginResponse extends BaseRespone {
    private String jwt;
    private User user;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public class User {
        private String _id;
        private String apikey;
        private String createdAt;
        private String email;
        private int iat;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getApikey() {
            return apikey;
        }

        public void setApikey(String apikey) {
            this.apikey = apikey;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getIat() {
            return iat;
        }

        public void setIat(int iat) {
            this.iat = iat;
        }

        @Override
        public String toString() {
            return "User{" +
                    "_id='" + _id + '\'' +
                    ", apikey='" + apikey + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", email='" + email + '\'' +
                    ", iat=" + iat +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "jwt='" + jwt + '\'' +
                ", user=" + user +
                ", error='" + getError() + '\'' +
                '}';
    }
}
