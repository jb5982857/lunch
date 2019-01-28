package com.lunch.account.result;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.result.BaseResult;

public class AccountResult extends BaseResult {
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

    public AccountResult(int code, String desc, String session, String username, String uid, String token, String nickname) {
        super(code, desc);
        this.session = session;
        this.username = username;
        this.uid = uid;
        this.token = token;
        this.nickname = nickname;
    }

    //成功
    public AccountResult(String session, String username, String uid, String token, String nickname) {
        this(Code.SUCCESS, S.SUCCESS, session, username, uid, token, nickname);
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
}
