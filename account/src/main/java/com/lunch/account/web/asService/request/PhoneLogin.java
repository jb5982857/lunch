package com.lunch.account.web.asService.request;

import com.lunch.support.entity.BaseUser;
import com.lunch.support.tool.StringUtils;

public class PhoneLogin extends BaseUser {
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public boolean isEmpty(String... excepts) {
        return StringUtils.isEmpty(getUsername()) || code == null;
    }

    @Override
    public String toString() {
        return getUsername() + "|" + code;
    }
}
