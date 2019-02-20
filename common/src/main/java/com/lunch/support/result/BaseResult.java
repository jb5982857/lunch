package com.lunch.support.result;

import com.alibaba.fastjson.JSON;
import com.lunch.support.constants.Code;
import com.lunch.support.constants.S;

/**
 * 返回有客户端的基础参数
 */
public class BaseResult<T> {
    //0、成功    1、失败
    private int code;
    //描述
    private String desc;

    private T data;

    public BaseResult() {

    }

    public BaseResult(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static <T> BaseResult obtain(T t) {
        BaseResult<T> result = new BaseResult<>(Code.SUCCESS, S.SUCCESS);
        result.setData(t);
        return result;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
