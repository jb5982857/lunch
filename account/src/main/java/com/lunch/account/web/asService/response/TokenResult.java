package com.lunch.account.web.asService.response;

public class TokenResult  {
    private String uid;

    public TokenResult(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
