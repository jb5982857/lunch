package com.lunch.support.entity;

import com.lunch.support.constants.Code;
import com.lunch.support.tool.MD5Utils;
import com.lunch.support.tool.StringUtils;

/**
 * 通过认证了的账号
 */
public class AccessUser extends BaseUser {

    private String uid;

    //会话，过期时间7天
    private Session session = new Session("", 0);

    private String phone;

    //token过期时间，存放在redis中
    private Token token = new Token("", 0);

    //头像地址
    private String avatar;

    public AccessUser() {
    }

    public AccessUser(BaseUser baseUser) {
        this.setUsername(baseUser.getUsername());
        this.setPassword(baseUser.getPassword());
    }

    public boolean hasToken() {
        return !StringUtils.isEmpty(token.getResult());
    }

    public boolean hasSession() {
        return !StringUtils.isEmpty(session.getResult());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return super.toString()+"|"+"AccessUser{" +
                "uid='" + uid + '\'' +
                ", session=" + session +
                ", phone='" + phone + '\'' +
                ", token=" + token +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
