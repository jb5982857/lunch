package com.lunch.account.result;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;
import com.lunch.support.result.BaseResult;

public class TokenResult extends BaseResult {
    private String uid;

    public TokenResult(String uid) {
        super(Code.SUCCESS, S.SUCCESS);
        this.uid = uid;
    }

    public TokenResult(int code, String desc, String uid) {
        super(code, desc);
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
