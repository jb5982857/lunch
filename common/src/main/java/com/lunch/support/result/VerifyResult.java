package com.lunch.support.result;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;

public class VerifyResult {
    private long userId;

    public VerifyResult() {
    }

    public VerifyResult(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
