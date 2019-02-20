package com.lunch.account.web.asClient.response;

import com.lunch.support.tool.LogUtils;

public class MobResponse {
    private static final String SUCCESS = "200";
    private String status;

    public MobResponse() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        LogUtils.info("MobResponse :" + status);
        return SUCCESS.equals(status);
    }
}
