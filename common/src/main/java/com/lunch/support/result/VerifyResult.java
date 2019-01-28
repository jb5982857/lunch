package com.lunch.support.result;

import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;

public class VerifyResult extends BaseResult {
    private long userId;

    public VerifyResult(){
        super(Code.SUCCESS, S.SUCCESS);
    }

    public VerifyResult(int code, String desc, int userId) {
        super(code, desc);
        this.userId = userId;
    }

    public VerifyResult(long userId) {
        super(Code.SUCCESS, S.SUCCESS);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
