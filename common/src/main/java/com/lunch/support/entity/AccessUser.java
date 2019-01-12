package com.lunch.support.entity;

import com.lunch.support.tool.MD5Utils;

/**
 * 通过认证了的账号
 */
public class AccessUser extends BaseUser {

    private String uid;

    //session暂时不用，后面再想想
    private String session;

    private String phone;

    //token过期时间，存放在redis中
    private Token token;

    public AccessUser() {
    }

    //密码做md5
    public AccessUser(BaseUser baseUser) {
        this.setUsername(baseUser.getUsername());
        this.setPassword(MD5Utils.toMD5(baseUser.getPassword()));
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "AccessUser{" + "username=" + getUsername() + "\'" + "password=" + getPassword() + "\'" +
                "uid='" + uid + '\'' +
                ", session='" + session + '\'' +
                ", token=" + token +
                '}';
    }
}
