package com.lunch.account.web.asService.response;

public class AccountResult {
    //会话
    private String session;
    //用户名称
    private String username;
    //uid
    private String uid;
    //token
    private String token;
    //昵称、针对第三方登录
    private String nickname;
    //头像
    private String avatar;

    public AccountResult(String session, String username, String uid, String token, String nickname, String avatar) {
        this.session = session;
        this.username = username;
        this.uid = uid;
        this.token = token;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
