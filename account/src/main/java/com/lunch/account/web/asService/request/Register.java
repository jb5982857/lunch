package com.lunch.account.web.asService.request;

import com.lunch.support.entity.BaseUser;

public class Register extends BaseUser {
    private Integer code;

    @Override
    public boolean isEmpty(String... excepts) {
        return super.isEmpty(excepts) || code == null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
