package com.lunch.account.web.asService.request;

import com.lunch.support.entity.BaseUser;
import com.lunch.support.tool.StringUtils;

public class ForgetPwd extends BaseUser {
    private Integer code;
    private String newPwd;

    public ForgetPwd() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    @Override
    public boolean isEmpty(String... excepts) {
        return StringUtils.isEmpty(getUsername()) || code == null || StringUtils.isEmpty(newPwd);
    }

    @Override
    public String toString() {
        return getUsername() + "|" + getCode() + "|" + getNewPwd();
    }
}
