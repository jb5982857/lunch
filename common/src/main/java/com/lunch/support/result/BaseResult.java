package com.lunch.support.result;

import com.alibaba.fastjson.JSON;

/**
 * 返回有客户端的基础参数
 */
public class BaseResult {
    //0、成功    1、失败
    private int code;
    //描述
    private String desc;

    public BaseResult(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
