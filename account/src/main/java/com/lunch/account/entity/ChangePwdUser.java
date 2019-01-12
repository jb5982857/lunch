package com.lunch.account.entity;

import com.lunch.support.entity.BaseUser;

/**
 * 修改密码的传的参数
 * 此时的password为老密码
 */
public class ChangePwdUser extends BaseUser {
    //图形验证码
    private String code;
    //新密码
    private String newPwd;
    //设备id
    private String deviceId;

    @Override
    public boolean isEmpty(String... excepts) {
        return super.isEmpty(excepts) || code == null || newPwd == null || deviceId == null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
